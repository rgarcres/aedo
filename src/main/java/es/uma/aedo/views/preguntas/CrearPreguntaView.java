package es.uma.aedo.views.preguntas;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.services.PreguntaService;

@PageTitle("Crear Pregunta")
@Route("preguntas/crear-pregunta")
public class CrearPreguntaView extends Div{
    
    private final PreguntaService preguntaService;

    public CrearPreguntaView(PreguntaService service){
        this.preguntaService = service;
        addClassNames("crear-regiones-view");

        VerticalLayout layout = new VerticalLayout(
            GestionPregunta.crearCamposRellenar(preguntaService)
        );
        layout.setAlignItems(Alignment.CENTER);
        layout.setPadding(true);
        layout.setSpacing(true);
        add(layout);
    }
}
