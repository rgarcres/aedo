package es.uma.aedo.views.campanas;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import es.uma.aedo.data.entidades.Campanya;
import es.uma.aedo.services.CampanyaService;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;

@PageTitle("Editar campaña")
@Route("campañas/editar-campaña")
public class EditarCampanaView extends Div implements HasUrlParameter<String>{

    private Campanya camp;
    private final CampanyaService campService;

    public EditarCampanaView(CampanyaService cService){
        this.campService = cService;
    }

    @Override
    public void setParameter(BeforeEvent event, String id) {
        camp = (Campanya) OtrasConfig.getEntidadPorParametro(id, campService);

        if(camp != null){
            setWidthFull();
            VaadinSession.getCurrent().setAttribute("campSinEditar", camp);
            
            VerticalLayout layout = new VerticalLayout(
                LayoutConfig.crearTituloLayout("Editar campaña", "campañas"),
                GestionCamp.crearCamposLayout(campService, camp)
            );

            layout.setAlignItems(Alignment.CENTER);
            add(layout);
        } else {
            add(LayoutConfig.crearNotFoundLayout());
        }
    }
}
