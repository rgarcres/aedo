package es.uma.aedo.views.preguntas;

import org.vaadin.lineawesome.LineAwesomeIconUrl;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.Bloque;
import es.uma.aedo.data.entidades.Pregunta;
import es.uma.aedo.services.BloqueService;
import es.uma.aedo.services.PreguntaService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;

@PageTitle("Preguntas")
@Route("preguntas")
@Menu(order = 6, icon = LineAwesomeIconUrl.QUESTION_SOLID)
@Uses(Icon.class)
public class TodasPreguntasView extends Div implements HasUrlParameter<String>{

    private Grid<Pregunta> grid;
    private final PreguntaService preguntaService;
    private final BloqueService bloqueService;

    private Pregunta preguntaSeleccionada;
    private Bloque bloque;

    public TodasPreguntasView(PreguntaService pService, BloqueService bService) {
        // Instanciar atributos
        this.preguntaService = pService;
        this.bloqueService = bService;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String id) {
        bloque = (Bloque) OtrasConfig.getEntidadPorParametro(id, bloqueService);

        if (bloque != null) {
            add(crearLayout(true));
        } else {
            add(crearLayout(false));
        }
    }

    private VerticalLayout crearLayout(boolean bloqueUnico){
        VerticalLayout layout = new VerticalLayout();

        GestionPregunta.Filters filters = new GestionPregunta.Filters(() -> refreshGrid(), null, bloqueService);
        HorizontalLayout botonesLayout = new HorizontalLayout();
        HorizontalLayout tituloLayout = new HorizontalLayout();
        grid = GestionPregunta.createGrid(preguntaService, filters);
        grid.addItemClickListener(e -> {
            preguntaSeleccionada = e.getItem();
        });

        // Comprobar si hay que mostrar todos los bloques y los botones CRUD
        // o si solo hay que añadir y quitar del bloque
        if (!bloqueUnico) {
            tituloLayout = LayoutConfig.createTituloLayout("Preguntas", "bloques");
            botonesLayout = LayoutConfig.createButtons(() -> preguntaSeleccionada, "pregunta", "preguntas", preguntaService, grid);
        } else {
            tituloLayout = LayoutConfig.createTituloLayout("Preguntas del bloque: " + bloque,
                    "preguntas-bloque");
            botonesLayout = crearBotonesLayout();
        }

        // Crear layout
        layout.add(
            tituloLayout,
            filters,
            grid,
            botonesLayout
        );

        layout.setPadding(true);
        layout.setSpacing(true);
        return layout;
    }

    private HorizontalLayout crearBotonesLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        Button anadir = BotonesConfig.crearBotonPrincipal("Añadir");
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar", "preguntas-bloque");

        anadir.addClickListener(e -> {
            preguntaSeleccionada.setBloque(bloque);
            preguntaService.save(preguntaSeleccionada);
            NotificacionesConfig.crearNotificacionExito("¡Se ha cambiado el bloque!", "El bloque de la pregunta "
                    + preguntaSeleccionada + " ha sido cambiado con éxito. " + bloque);
            anadir.getUI().ifPresent(ui -> ui.navigate("preguntas-bloque/"+bloque.getId()));
        });

        layout.add(anadir, cancelar);
        return layout;
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

}
