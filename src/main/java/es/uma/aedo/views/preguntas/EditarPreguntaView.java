package es.uma.aedo.views.preguntas;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.Pregunta;
import es.uma.aedo.services.BloqueService;
import es.uma.aedo.services.PreguntaService;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;

@PageTitle("Editar pregunta")
@Route("preguntas/editar-pregunta/")
public class EditarPreguntaView extends Div implements HasUrlParameter<String> {

    private Pregunta preguntaEditar;
    private final PreguntaService preguntaService;
    private final BloqueService bloqueService;

    public EditarPreguntaView(PreguntaService service, BloqueService bService) {
        this.preguntaService = service;
        this.bloqueService = bService;
    }

    @Override
    public void setParameter(BeforeEvent event, String id) {
        preguntaEditar = (Pregunta) OtrasConfig.getEntidadPorParametro(id, preguntaService);
        if(preguntaEditar != null){
            addClassName("editar-regiones-view");

            VerticalLayout layout = new VerticalLayout(
                LayoutConfig.createTituloLayout("Editar pregunta "+preguntaEditar.getId(), "preguntas"),
                GestionPregunta.crearCamposRellenar(preguntaService, bloqueService, preguntaEditar)
            );

            layout.setAlignItems(Alignment.CENTER);
            layout.setPadding(true);
            layout.setSpacing(true);
            add(layout);
        } else {
            add(LayoutConfig.crearNotFoundLayout());
        }
    }
}
