package es.uma.aedo.views.grupos;

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
import es.uma.aedo.services.GrupoService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Grupos")
@Route("grupos")
@Menu(order = 3, icon = LineAwesomeIconUrl.USERS_SOLID)
@Uses(Icon.class)
public class GruposView extends Div {

    private Grid<Grupo> grid;

    private final GrupoService grupoService;
    private Grupo grupoSeleccionado;

    public GruposView(GrupoService service) {
        this.grupoService = service;
        setSizeFull();
        addClassNames("grupos-view");

        GestionGrupo.Filters filters = new GestionGrupo.Filters(() -> refreshGrid());
        grid = GestionGrupo.createGrid(service, filters);
        grid.addItemClickListener(e -> {
            grupoSeleccionado = e.getItem();
        });
        VerticalLayout layout = new VerticalLayout(
            LayoutConfig.crearTituloLayout("Grupos", ""),
            LayoutConfig.createMobileFilters(filters),
            filters,
            grid,
            LayoutConfig.createButtons(
                () -> grupoSeleccionado,
                "grupo",
                "grupos",
                grupoService,
                grid
            ),
            crearBotonesUsuarios()
        );

        layout.setAlignItems(Alignment.CENTER);
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

    private HorizontalLayout crearBotonesUsuarios() {
        HorizontalLayout layout = new HorizontalLayout();

        Button anadir = BotonesConfig.crearBotonExtra("Añadir usuarios al grupo");
        Button verTodos = BotonesConfig.crearBotonExtraSecundario("Ver todos los usuarios", "usuarios");

        anadir.addClickListener(e -> {
            if (grupoSeleccionado != null) {
                getUI().ifPresent(ui -> ui.navigate("usuarios/" + grupoSeleccionado.getId()));
            } else {
                NotificacionesConfig.crearNotificacionError("Selecciona un grupo", "No hay ningún grupo seleccionado");
            }
        });

        layout.setAlignItems(Alignment.CENTER);
        layout.add(anadir, verTodos);
        return layout;
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

}
