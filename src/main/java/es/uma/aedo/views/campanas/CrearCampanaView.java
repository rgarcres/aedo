package es.uma.aedo.views.campanas;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.Campanya;
import es.uma.aedo.services.CampanyaService;
import es.uma.aedo.views.utilidades.LayoutConfig;

@PageTitle("Crear campaña")
@Route("campañas/crear-campaña")
public class CrearCampanaView extends Div{
    private final CampanyaService campService;
    private Campanya camp = null;

    public CrearCampanaView(CampanyaService cService){
        this.campService = cService;
        setWidthFull();
        
        VerticalLayout layout = new VerticalLayout(
            LayoutConfig.crearTituloLayout("Crear campaña", "campañas"),
            GestionCamp.crearCamposLayout(campService, camp)
        );

        layout.setAlignItems(Alignment.CENTER);
        add(layout);
    }

}
