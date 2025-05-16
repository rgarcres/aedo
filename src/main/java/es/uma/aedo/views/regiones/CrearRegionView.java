package es.uma.aedo.views.regiones;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.services.RegionService;

@PageTitle("Crear Region")
@Route("regiones/crear-region")
public class CrearRegionView extends Div{
    
    private final RegionService regionService;

    public CrearRegionView(RegionService service) {
        this.regionService = service;
        setSizeFull();
        addClassNames("crear-regiones-view");

        VerticalLayout layout = new VerticalLayout(GestionRegion.crearCamposLayout(null, regionService));
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

}
