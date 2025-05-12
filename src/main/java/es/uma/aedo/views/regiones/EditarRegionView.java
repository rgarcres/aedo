package es.uma.aedo.views.regiones;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import es.uma.aedo.data.entidades.Region;
import es.uma.aedo.services.RegionService;

@PageTitle("Editar Región")
@Route("regiones/editar-region")
public class EditarRegionView extends Div{
    
    private final RegionService regionService;
    private final Region regionEditar;

    public EditarRegionView(RegionService service){
        this.regionService = service;
        this.regionEditar = (Region) VaadinSession.getCurrent().getAttribute("regionEditar");
        setSizeFull();
        addClassNames("editar-regiones-view");

        VerticalLayout layout = new VerticalLayout(CrearEditarRegion.crearCamposLayout(regionEditar, regionService));
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }
}
