package es.uma.aedo.views.respuestas;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.Pregunta;
import es.uma.aedo.services.PreguntaService;
import es.uma.aedo.services.RespuestaService;

@PageTitle("Selccionar Opciones")
@Route("seleccionar-opciones")
/*
 * Vista para cuando se está creando una pregunta de tipo 3
 * Es necesario establecer las opciones que existen
 */
public class SeleccionarOpcionesView extends Div{
    
    private final RespuestaService respuestaService;
    private final PreguntaService preguntaService;
    private Pregunta pregunta;
    
    public SeleccionarOpcionesView(RespuestaService rService, PreguntaService pService){
        this.respuestaService = rService;
        this.preguntaService = pService;

        VerticalLayout layout = new VerticalLayout();
        
        add(layout);
    }
}
