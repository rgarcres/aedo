package es.uma.aedo.views.campañas;

import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.Campanya;
import es.uma.aedo.services.CampanyaService;
import es.uma.aedo.views.utilidades.LayoutConfig;

import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Campañas")
@Route("campañas")
@Menu(order = 1, icon = LineAwesomeIconUrl.CHART_BAR)
@Uses(Icon.class)
public class CampañasView extends Div {

    private Grid<Campanya> grid;

    private final CampanyaService campService;
    private Campanya campSeleccionada;

    public CampañasView(CampanyaService cService) {
        this.campService = cService;

        GestionCamp.Filters filters = new GestionCamp.Filters(() -> refreshGrid());
        grid = GestionCamp.crearGrid(campService, filters);
        grid.addItemClickListener(e -> {
            campSeleccionada = e.getItem();
        });

        VerticalLayout layout = new VerticalLayout(
            LayoutConfig.createTituloLayout("Campañas", ""),
            filters,
            grid,
            LayoutConfig.createButtons(
                () -> campSeleccionada, 
                "campaña", 
                "campañas", 
                campService, 
                grid
            )
        );

        layout.setAlignItems(Alignment.CENTER);
        add(layout);
    }


    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

}
