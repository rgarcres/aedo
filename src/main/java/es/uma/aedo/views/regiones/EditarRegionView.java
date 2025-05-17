package es.uma.aedo.views.regiones;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.Region;
import es.uma.aedo.services.RegionService;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;

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
        regionEditar = (Region) OtrasConfig.getEntidadPorParametro(id, regionService);
        if(regionEditar != null){
            addClassName("editar-regiones-view");

            VerticalLayout layout = new VerticalLayout(
                LayoutConfig.createTituloLayout("Editar región: "+regionEditar.toString(), "regiones"),
                GestionRegion.crearCamposLayout(regionEditar, regionService)
            );

            layout.setAlignItems(Alignment.CENTER);
            layout.setPadding(true);
            layout.setSpacing(true);
            add(layout);
        } else {
            add(LayoutConfig.createNotFoundLayout());
        }
    }
}
