package es.uma.aedo.views.campanas;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

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

        if(VaadinSession.getCurrent().getAttribute("campMedioCreada") != null){
            camp = (Campanya) VaadinSession.getCurrent().getAttribute("campMedioCreada");
        }
        
        VerticalLayout layout = new VerticalLayout(
            LayoutConfig.createTituloLayout("Crear campaña", "campañas"),
            GestionCamp.crearCamposLayout(campService, camp, false)
        );

        layout.setAlignItems(Alignment.CENTER);
        add(layout);
    }

}
