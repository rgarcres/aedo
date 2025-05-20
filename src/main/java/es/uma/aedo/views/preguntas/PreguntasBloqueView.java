package es.uma.aedo.views.preguntas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.Bloque;
import es.uma.aedo.data.entidades.Pregunta;
import es.uma.aedo.services.BloqueService;
import es.uma.aedo.services.PreguntaService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;

@PageTitle("Preguntas")
@Route("preguntas-bloque")
public class PreguntasBloqueView extends Div implements HasUrlParameter<String> {

    private Grid<Pregunta> grid;

    private Pregunta preguntaSeleccionada;
    private final PreguntaService preguntaService;
    private final BloqueService bloqueService;
    private Bloque bloqueSeleccionado;

    /*
     * Vista en la que se muestras todas las preguntas de un único bloque
     */
    public PreguntasBloqueView(PreguntaService pService, BloqueService bService) {
        this.preguntaService = pService;
        this.bloqueService = bService;
    }

    @Override
    public void setParameter(BeforeEvent event, String id) {
        bloqueSeleccionado = (Bloque) OtrasConfig.getEntidadPorParametro(id, bloqueService);
        if (bloqueSeleccionado != null) {
            setSizeFull();
            addClassNames("preguntas-bloque-view");

            GestionPregunta.Filters filters = new GestionPregunta.Filters(() -> refreshGrid(), bloqueSeleccionado, bloqueService);

            grid = GestionPregunta.createGrid(preguntaService, filters);
            grid.addItemClickListener(e -> {
                preguntaSeleccionada = e.getItem();
            });
            VerticalLayout layout = new VerticalLayout(
                LayoutConfig.createTituloLayout("Preguntas del " + bloqueSeleccionado.getNombre(), "bloques"),
                filters,
                grid,
                createButtons()
            );

            layout.setSizeFull();
            layout.setPadding(true);
            layout.setSpacing(true);
            add(layout);
        } else {
            add(LayoutConfig.createNotFoundLayout());
        }
    }

    private HorizontalLayout createButtons() {
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setWidthFull();
        buttonsLayout.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER,
                LumoUtility.AlignItems.CENTER);
        buttonsLayout.addClassName("buttons-layout");
        buttonsLayout.setAlignItems(Alignment.CENTER);

        Button anadir = BotonesConfig.crearBotonPrincipal("Añadir preguntas al bloque");
        Button editar = BotonesConfig.crearBotonSecundario("Editar pregunta");
        Button borrar = BotonesConfig.crearBotonError("Eliminar pregunta del bloque");

        anadir.addClickListener(e -> {
            anadir.getUI().ifPresent(ui -> ui.navigate("preguntas/"+bloqueSeleccionado));
        });

        editar.addClickListener(e -> {
            if (preguntaSeleccionada != null) {
                editar.getUI().ifPresent(ui -> ui.navigate("preguntas/editar-pregunta/"+preguntaSeleccionada.getId()));
            }
        });

        borrar.addClickListener(e -> {
            if (preguntaSeleccionada != null) {
                preguntaSeleccionada.setBloque(null);
                preguntaService.save(preguntaSeleccionada);
                NotificacionesConfig.crearNotificacionExito(
                        "Pregunta eliminada del " + bloqueSeleccionado.toString().toLowerCase(),
                        "La pregunta se ha eliminado correctamente de este bloque");
            } else {
                NotificacionesConfig.crearNotificacionError("Seleccione una pregunta",
                        "No hay ninguna pregunta seleccionada, seleccione una pregunta para poder borrarla");
            }
        });
        buttonsLayout.add(anadir, editar, borrar);
        return buttonsLayout;
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }
}
