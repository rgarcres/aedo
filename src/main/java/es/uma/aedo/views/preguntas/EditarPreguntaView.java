package es.uma.aedo.views.preguntas;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import es.uma.aedo.data.entidades.Pregunta;
import es.uma.aedo.services.BloqueService;
import es.uma.aedo.services.PreguntaService;

@PageTitle("Editar pregunta")
@Route("preguntas/editar-pregunta")
public class EditarPreguntaView extends Div{
    
    private Pregunta preguntaEditar;
    private final PreguntaService preguntaService;
    private final BloqueService bloqueService;

    public EditarPreguntaView(PreguntaService service, BloqueService bService){
        this.preguntaEditar = (Pregunta) VaadinSession.getCurrent().getAttribute("preguntaEditar");
        this.preguntaService = service;
        this.bloqueService = bService;
        addClassNames("crear-regiones-view");

        VerticalLayout layout = new VerticalLayout(
            GestionPregunta.crearCamposRellenar(preguntaService, bloqueService, preguntaEditar)
        );
        layout.setAlignItems(Alignment.CENTER);
        layout.setPadding(true);
        layout.setSpacing(true);

        add(layout);
    }
}
