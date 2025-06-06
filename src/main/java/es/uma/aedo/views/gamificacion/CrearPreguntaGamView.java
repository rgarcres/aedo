package es.uma.aedo.views.gamificacion;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.services.GamificacionService;
import es.uma.aedo.views.utilidades.LayoutConfig;

@PageTitle("Crear pregunta gamificación")
@Route("gamificacion/crear-pregunta")
public class CrearPreguntaGamView extends Div{
    private final GamificacionService gamificacionService;

    public CrearPreguntaGamView(GamificacionService gService){
        this.gamificacionService = gService;

        setWidthFull();
        VerticalLayout layout = new VerticalLayout(
            LayoutConfig.crearTituloLayout("Crear Pregunta de Gamificación", "gamificacion"),
            GestionGamificacion.crearCamposLayout(gamificacionService, null)
        );

        layout.setAlignItems(Alignment.CENTER);
        layout.setPadding(true);
        layout.setSpacing(true);
        add(layout);
    }
}
