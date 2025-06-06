package es.uma.aedo.views.gamificacion;

import org.vaadin.lineawesome.LineAwesomeIconUrl;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.PreguntaGamificacion;
import es.uma.aedo.services.GamificacionService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;

@PageTitle("Preguntas de Gamificación")
@Route("gamificacion")
@Menu(order = 7, icon = LineAwesomeIconUrl.QUESTION_CIRCLE)
@Uses(Icon.class)
public class GamificacionView extends Div {
    
    private Grid<PreguntaGamificacion> grid;

    private PreguntaGamificacion preguntaSeleccionada;

    private final GamificacionService gamificacionService;

    public GamificacionView(GamificacionService gService){
        this.gamificacionService = gService;
        setWidthFull();

        //------------Filtros y grid------------   
        GestionGamificacion.Filters filters = new GestionGamificacion.Filters(() -> refreshGrid());
        grid = GestionGamificacion.crearGrid(gamificacionService, filters);
        grid.addItemClickListener(e -> {
            preguntaSeleccionada = e.getItem();
        });

        //------------Botón para ver las respuestas------------   
        Button respuestas = BotonesConfig.crearBotonExtra("Ver respuestas");
        respuestas.addClickListener(e -> {
            if(preguntaSeleccionada != null){
                getUI().ifPresent(ui -> ui.navigate("gamificacion/respuestas/"+preguntaSeleccionada.getId()));
            }
        });

        VerticalLayout layout = new VerticalLayout(
            LayoutConfig.crearTituloLayout("Preguntas de Gamificación", ""),
            filters,
            grid,
            LayoutConfig.createButtons(
                () -> preguntaSeleccionada,
                "pregunta", 
                "gamificacion", 
                gamificacionService, 
                grid
            ),
            respuestas
        );

        layout.setAlignItems(Alignment.CENTER);
        layout.setPadding(true);
        layout.setSpacing(true);
        add(layout);
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }


}
