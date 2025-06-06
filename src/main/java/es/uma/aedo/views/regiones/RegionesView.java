package es.uma.aedo.views.regiones;

import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.Region;
import es.uma.aedo.services.RegionService;
import es.uma.aedo.views.utilidades.LayoutConfig;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Regiones")
@Route("regiones")
@Menu(order = 6, icon = LineAwesomeIconUrl.MAP_SOLID)
@Uses(Icon.class)
public class RegionesView extends Div {

    private Grid<Region> grid;

    private final RegionService regionService;
    private Region regionSeleccionada;

    public RegionesView(RegionService service) {
        this.regionService = service;
        setSizeFull();
        addClassNames("regiones-view");

        GestionRegion.Filters filters = new GestionRegion.Filters(() -> refreshGrid(), regionService);
        grid = GestionRegion.crearGrid(regionService, filters);
        grid.addItemClickListener(e -> {
            regionSeleccionada = e.getItem();
        });

        VerticalLayout layout = 
        new VerticalLayout(
            LayoutConfig.crearTituloLayout("Regiones", ""), 
            filters, 
            grid, 
            LayoutConfig.createButtons(
                () -> regionSeleccionada, 
                "Region", 
                "regiones", 
                this.regionService, 
                grid
            )
        );

        layout.setSizeFull();
        layout.setPadding(true);
        layout.setSpacing(true);
        add(layout);
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

}
