package es.uma.aedo.views.usuarios;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.Grupo;
import es.uma.aedo.data.entidades.Usuario;
import es.uma.aedo.services.GrupoService;
import es.uma.aedo.services.RegionService;
import es.uma.aedo.services.UsuarioService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Usuarios")
@Route("usuarios")
@Menu(order = 2, icon = LineAwesomeIconUrl.USER_CIRCLE)
@Uses(Icon.class)
public class TodosUsuariosView extends Div {

    private Grid<Usuario> grid;

    private final UsuarioService usuarioService;
    private final RegionService regionService;
    private Usuario usuarioSeleccionado;
    private Grupo grupo;

    public TodosUsuariosView(UsuarioService service, RegionService rService, GrupoService gService) {
        this.usuarioService = service;
        this.regionService = rService;
        setSizeFull();
        addClassNames("usuarios-view");

        GestionUsuario.Filters filters = new GestionUsuario.Filters(() -> refreshGrid(), regionService, grupo);
        grid = GestionUsuario.crearGrid(service, filters);
        grid.addItemClickListener(e -> {
            usuarioSeleccionado = e.getItem();
        });
        
        VerticalLayout layout = new VerticalLayout(
            LayoutConfig.createTituloLayout("Usuarios", ""),
            filters,
            grid,
            LayoutConfig.createButtons(
                () -> usuarioSeleccionado,
                "usuario",
                "usuarios",
                usuarioService,
                grid
            ),
            crearBotonesGrupoLayout()
        );

        layout.setAlignItems(Alignment.CENTER);
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

    private HorizontalLayout crearBotonesGrupoLayout(){
        HorizontalLayout layout = new HorizontalLayout();

        Button grupos = BotonesConfig.crearBotonPrincipal("Ver grupos");

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
