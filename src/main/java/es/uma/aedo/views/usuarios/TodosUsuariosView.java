package es.uma.aedo.views.usuarios;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.Grupo;
import es.uma.aedo.data.entidades.Usuario;
import es.uma.aedo.services.GrupoService;
import es.uma.aedo.services.RegionService;
import es.uma.aedo.services.UsuarioService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Usuarios")
@Route("usuarios")
@Menu(order = 2, icon = LineAwesomeIconUrl.USER_CIRCLE)
@Uses(Icon.class)
public class TodosUsuariosView extends Div implements HasUrlParameter<String>{

    private Grid<Usuario> grid;

    private final UsuarioService usuarioService;
    private final RegionService regionService;
    private final GrupoService grupoService;
    private Usuario usuarioSeleccionado;
    private Grupo grupo;

    public TodosUsuariosView(UsuarioService service, RegionService rService, GrupoService gService) {
        this.usuarioService = service;
        this.regionService = rService;
        this.grupoService = gService;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String id) {
        if(id != null){
            setSizeFull();
            addClassNames("usuarios-view");
            grupo = grupoService.getConUsuarios(id).get();

            if(grupo != null){
                add(crearLayoutConGrupo());
            } else {
                add(LayoutConfig.crearNotFoundLayout());
            }
        } else {
            setSizeFull();
            addClassNames("usuarios-view");

            add(crearLayoutSinGrupo());
        }
    }

    private VerticalLayout crearLayoutConGrupo(){
        GestionUsuario.Filters filters = new GestionUsuario.Filters(() -> refreshGrid(), regionService);
        grid = GestionUsuario.crearGrid(usuarioService, filters);
        grid.setSelectionMode(SelectionMode.MULTI);

        if(!grupo.getUsuarios().isEmpty()){
            for(Usuario u: grupo.getUsuarios()){
                grid.select(u);
            }
        }

        VerticalLayout layout = new VerticalLayout(
            LayoutConfig.createTituloLayout("Seleccionar usuarios", "grupos"),
            filters,
            grid,
            crearBotonesConGrupoLayout()
        );
        
        return layout;
    }

    private HorizontalLayout crearBotonesConGrupoLayout(){
        HorizontalLayout layout = new HorizontalLayout();

        Button aplicar = BotonesConfig.crearBotonPrincipal("Aplicar cambios");
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar", "grupos");

        aplicar.addClickListener(e -> {
            List<Usuario> selected = new ArrayList<>(grid.getSelectedItems());
            for(Usuario u: selected){
                usuarioService.addUsuarioAGrupo(grupo.getId(), u.getId());
                usuarioService.save(u);
            }
            grupoService.save(grupo);
            NotificacionesConfig.crearNotificacionExito("Usuarios añadidos", "Los usuarios seleccionados se han añadido con éxito");
            getUI().ifPresent(ui -> ui.navigate("grupos"));
        });

        layout.add(aplicar, cancelar);
        return layout;
    }

    private VerticalLayout crearLayoutSinGrupo(){
            GestionUsuario.Filters filters = new GestionUsuario.Filters(() -> refreshGrid(), regionService);
            grid = GestionUsuario.crearGrid(usuarioService, filters);
            grid.addItemClickListener(e -> {
                usuarioSeleccionado = e.getItem();
            });
            
            VerticalLayout layout = new VerticalLayout(
                LayoutConfig.createTituloLayout("Usuarios", ""),
                LayoutConfig.createMobileFilters(filters),
                filters,
                grid,
                LayoutConfig.createButtons(
                    () -> usuarioSeleccionado,
                    "usuario",
                    "usuarios",
                    usuarioService,
                    grid
                ),
                crearBotonesSinGrupoLayout()
            );

            layout.setAlignItems(Alignment.CENTER);
            layout.setPadding(false);
            layout.setSpacing(false);

            return layout;
    }


    private HorizontalLayout crearBotonesSinGrupoLayout(){
        HorizontalLayout layout = new HorizontalLayout();

        Button grupos = BotonesConfig.crearBotonExtra("Ver grupos");

        grupos.addClickListener(e -> {
            if(usuarioSeleccionado != null){
                getUI().ifPresent(ui -> ui.navigate("usuarios/seleccionar-grupos/"+usuarioSeleccionado.getId()));
            }
        });
        layout.add(grupos);
        return layout;
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

}
