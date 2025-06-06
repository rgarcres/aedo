package es.uma.aedo.views.usuarios;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.Grupo;
import es.uma.aedo.data.entidades.Usuario;
import es.uma.aedo.services.GrupoService;
import es.uma.aedo.services.UsuarioService;
import es.uma.aedo.views.grupos.GestionGrupo;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;

@PageTitle("Seleccionar Grupo")
@Route("usuarios/seleccionar-grupos")
public class SeleccionarGrupoView extends Div implements HasUrlParameter<String>{

    private Grid<Grupo> grid;
    private List<Grupo> gruposSeleccionados;

    private Usuario usuario;
    private final UsuarioService usuarioService;
    private final GrupoService grupoService;

    public SeleccionarGrupoView(UsuarioService uService, GrupoService gService) {
        this.usuarioService = uService;
        this.grupoService = gService;
    }

    @Override
    public void setParameter(BeforeEvent event, String id) {
        if(id != null){
            usuario = usuarioService.getConGrupo(id).get();
            if (usuario != null) {
                setWidthFull();

                GestionGrupo.Filters filters = new GestionGrupo.Filters(() -> refreshGrid());
                grid = GestionGrupo.createGrid(grupoService, filters);
                grid.setSelectionMode(SelectionMode.MULTI);

                if(usuario.getGrupos() != null){
                    for(Grupo g: usuario.getGrupos()){
                        grid.select(g);
                    }
                }

                VerticalLayout layout = new VerticalLayout(
                    LayoutConfig.crearTituloLayout("Seleccionar grupo", "usuarios"),
                    filters,
                    grid,
                    crearBotonesLayout()
                );

                layout.setAlignItems(Alignment.CENTER);
                layout.setPadding(true);
                layout.setSpacing(true);
                add(layout);
            } else {
                add(LayoutConfig.crearNotFoundLayout());
            }
        } else {
            add(LayoutConfig.crearNotFoundLayout());
        }
    }


    private HorizontalLayout crearBotonesLayout() {
        HorizontalLayout layout = new HorizontalLayout();

        Button aplicar = BotonesConfig.crearBotonPrincipal("Aplicar");
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar");

        aplicar.addClickListener(e -> {
            gruposSeleccionados = new ArrayList<>(grid.getSelectedItems());

            if (!gruposSeleccionados.isEmpty()) {
                usuario.setGrupos(gruposSeleccionados);
                usuarioService.save(usuario);
                NotificacionesConfig.crearNotificacionExito("¡Grupos asignados!", "Los cambios se han aplicado con éxito");
                getUI().ifPresent(ui -> ui.navigate("usuarios"));
            } else {
                NotificacionesConfig.crearNotificacionError("Selecciona un grupo", "No hay ningún grupo seleccionado");
            }
        });

        cancelar.addClickListener(e -> {
            NotificacionesConfig.notificar("No se han aplicado los cambios");
            getUI().ifPresent(ui -> ui.navigate("usuarios"));
        });

        layout.add(aplicar, cancelar);
        return layout;
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }
}
