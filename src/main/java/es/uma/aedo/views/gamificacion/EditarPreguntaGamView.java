package es.uma.aedo.views.gamificacion;

import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import es.uma.aedo.data.entidades.PreguntaGamificacion;
import es.uma.aedo.services.GamificacionService;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;

public class EditarPreguntaGamView extends Div implements HasUrlParameter<String> {

    private final GamificacionService gamificacionService;
    private PreguntaGamificacion pg;

    public EditarPreguntaGamView(GamificacionService gService) {
        this.gamificacionService = gService;
    }

    @Override
    public void setParameter(BeforeEvent event, String id) {
        pg = (PreguntaGamificacion) OtrasConfig.getEntidadPorParametro(id, gamificacionService);

        if (pg != null) {
            setWidthFull();
            VerticalLayout layout = new VerticalLayout(
                    LayoutConfig.createTituloLayout("Crear Pregunta de Gamificación", "gamificacion"),
                    GestionGamificacion.crearCamposLayout(gamificacionService, pg));

            layout.setAlignItems(Alignment.CENTER);
            layout.setPadding(true);
            layout.setSpacing(true);
            add(layout);
        } else {
            add(LayoutConfig.crearNotFoundLayout());
        }
    }
}
