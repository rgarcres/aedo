package es.uma.aedo.views.regiones;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.Region;
import es.uma.aedo.services.RegionService;
import es.uma.aedo.views.utilidades.LayoutConfig;

@PageTitle("Editar Región")
@Route("regiones/editar-region/")
public class EditarRegionView extends Div implements HasUrlParameter<String> {

    private final RegionService regionService;
    private Region regionEditar;

    public EditarRegionView(RegionService service) {
        this.regionService = service;
    }

    @Override
    public void setParameter(BeforeEvent event, String id) {
        if (id != null) {
            if (regionService.get(id).isPresent()) {
                regionEditar = regionService.get(id).get();
                setSizeFull();
                addClassNames("editar-regiones-view");

                VerticalLayout layout = new VerticalLayout(
                        CrearEditarRegion.crearCamposLayout(regionEditar, regionService));
                layout.setSizeFull();
                layout.setPadding(false);
                layout.setSpacing(false);
                add(layout);
            } else {
                add(LayoutConfig.createNotFoundLayout());
            }
        } else {
            add(LayoutConfig.createNotFoundLayout());
        }
    }
}
