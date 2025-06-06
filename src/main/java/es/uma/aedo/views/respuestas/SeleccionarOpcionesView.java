package es.uma.aedo.views.respuestas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.Pregunta;
import es.uma.aedo.data.entidades.RespuestaTipo1.RespuestaSiNo;
import es.uma.aedo.data.entidades.RespuestaTipo2.RespuestaSiNoIntensidad;
import es.uma.aedo.services.PreguntaService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;

@PageTitle("Selccionar Opciones")
@Route("preguntas/seleccionar-opciones/")
/*
 * Vista para cuando se está creando una pregunta de tipo 3
 * Es necesario establecer las opciones que existen
 */
public class SeleccionarOpcionesView extends Div implements HasUrlParameter<String> {

    private final PreguntaService preguntaService;
    private Grid<String> grid;
    private String respuestaSeleccionada;
    private Pregunta pregunta;

    public SeleccionarOpcionesView(PreguntaService pService) {
        this.preguntaService = pService;
    }

    @Override
    public void setParameter(BeforeEvent event, String id) {
        pregunta = (Pregunta) OtrasConfig.getEntidadPorParametro(id, preguntaService);
        
        if(id != null && preguntaService.getConOpciones(id).isPresent()){
            pregunta = preguntaService.getConOpciones(id).get();
            if (pregunta != null) {
                VerticalLayout layout = new VerticalLayout(
                    LayoutConfig.createTituloLayout("Tipo y respuestas", "preguntas"),
                    crearLayout()
                );
                add(layout);
            } else {
                add(LayoutConfig.crearNotFoundLayout());
            }
        }

    }

    private VerticalLayout crearLayout() {
        // ------------COMPONENTES------------
        // ------------Layouts------------
        VerticalLayout layout = new VerticalLayout();
        VerticalLayout contenido = new VerticalLayout();
        HorizontalLayout botonesLayout = new HorizontalLayout();
        // ------------ComboBox------------
        ComboBox<Integer> tipoBox = new ComboBox<>("Tipo");
        // ------------Botones------------
        Button aplicar = BotonesConfig.crearBotonPrincipal("Aplicar");
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar");

        // ------------Comportamiento tipoBox------------
        tipoBox.setItems(1, 2, 3, 4);
        Integer tipo = pregunta.getTipo();
        if (tipo != 0) {
            tipoBox.setValue(pregunta.getTipo());
            if (tipo == 1 || tipo == 2) {
                contenido.add(crearContenidoTipo1Y2(tipo));
            } else if (tipo == 3 || tipo == 4) {
                contenido.add(crearContenidoTipo3Y4(tipo));
            }
        }

        tipoBox.addValueChangeListener(e -> {
            contenido.removeAll();

            Integer t = tipoBox.getValue();
            if (t == 1 || t == 2) {
                contenido.add(crearContenidoTipo1Y2(t));
            } else if (t == 3 || t == 4) {
                contenido.add(crearContenidoTipo3Y4(t));
            }
        });

        // ------------Comportamiento botones------------
        cancelar.addClickListener(e -> {
            cancelar();
        });

        aplicar.addClickListener(e -> {
            Integer t = tipoBox.getValue();
            crear(t);
        });
        // ------------Añadir componentes al layout------------
        botonesLayout.add(aplicar, cancelar);
        layout.setAlignItems(Alignment.CENTER);
        layout.add(tipoBox, contenido, botonesLayout);

        return layout;
    }

    private VerticalLayout crearContenidoTipo1Y2(Integer t) {
        VerticalLayout layout = new VerticalLayout();
        H3 h3 = new H3("Respuestas tipo " + t);
        Div div = new Div();
        if (t == 1) {
            div.setText(RespuestaSiNo.listToString());
        } else {
            div.setText(RespuestaSiNoIntensidad.listToString());
        }

        layout.setAlignItems(Alignment.CENTER);
        layout.add(h3, div);
        return layout;
    }

    /*
     * Si el tipo es 3 o 4 se carga un layout donde se pueden añadir y quitar
     * respuestas
     * de la lista de posibles respuestas que tiene esa pregunta
     */
    private VerticalLayout crearContenidoTipo3Y4(Integer t) {
        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout botonesLayout = new HorizontalLayout();

        H3 h3 = new H3("Respuestas tipo " + t);
        TextField respuestasField = new TextField("Posible respuesta");

        Button anadir = BotonesConfig.crearBotonPrincipal("Añadir posible respuesta");
        Button eliminar = BotonesConfig.crearBotonError("Eliminar");

        respuestasField.setPlaceholder("Introduzca una posible respuesta...");
        respuestasField.setWidthFull();
        crearGrid();

        anadir.addClickListener(e -> {
            String res = respuestasField.getValue();
            if (!res.isBlank()) {
                pregunta.getOpciones().add(res);
                NotificacionesConfig.notificar("Respuesta: " + res + " añadida");
                refreshGrid();
            } else {
                NotificacionesConfig.crearNotificacionError("Respuesta vacía",
                        "No puede añadir una respuesta vacía. Introduzca una respuesta válida");
            }
        });
        eliminar.addClickListener(e -> {
            if (respuestaSeleccionada != null) {
                pregunta.getOpciones().remove(respuestaSeleccionada);
                refreshGrid();
                NotificacionesConfig.notificar("Respuesta: " + respuestaSeleccionada + " eliminada");
            } else {
                NotificacionesConfig.crearNotificacionError("Selecciona una respuesta",
                        "No ha seleccionado ninguna respuesta");
            }
        });

        layout.setAlignItems(Alignment.CENTER);
        botonesLayout.add(anadir, eliminar);
        layout.add(h3, respuestasField, grid, botonesLayout);
        return layout;
    }

    /*
     * Método para cancelar la transacción
     * Si está creando: se borra la pregunta a medio crear de la base de datos
     * Si está editando: se obtiene el valor de la pregunta antes de que fuera
     * editada y
     * se vuelve a guarda en la BD
     */
    private void cancelar() {
        // Tipo = 0 -> pregunta a medio crear
        if (pregunta.getTipo() == 0) {
            preguntaService.delete(pregunta.getId());
            NotificacionesConfig.notificar("La pregunta: " + pregunta + " no se ha creado.");
            // Comprobar que el atributo de sesión de la pregunta original antes de editar
            // no esté a null
        } else if (VaadinSession.getCurrent().getAttribute("preguntaSinEditar") != null) {
            Pregunta p = (Pregunta) VaadinSession.getCurrent().getAttribute("preguntaSinEditar");
            pregunta.setId(p.getId());
            pregunta.setBloque(p.getBloque());
            pregunta.setEnunciado(p.getEnunciado());
            preguntaService.save(pregunta);
            NotificacionesConfig.notificar("La pregunta: " + pregunta + " no se ha editado.");
        }
        // Navegar
        getUI().ifPresent(ui -> ui.navigate("preguntas"));
    }

    /*
     * Método para crear/editar la pregunta
     */
    private void crear(Integer t) {
        if (t != null) {
            // Establecer el tipo y, si fuese necesario, las opciones a elegir
            pregunta.setTipo(t);
            // Guardar la pregunta en la BD
            preguntaService.save(pregunta);
            // Notificar y navegar
            NotificacionesConfig.crearNotificacionExito("¡Respuestas asignadas!", "Las respuestas se han asignado con éxito");
            getUI().ifPresent(ui -> ui.navigate("preguntas"));
        } else {
            // Notificar error
            NotificacionesConfig.crearNotificacionError("Selecciona un tipo",
                    "No hay ningún tipo seleccionado, por favor seleccione un tipo.");
        }
    }

    private void crearGrid() {
        grid = new Grid<>(String.class, false);
        grid.addColumn(s -> s).setHeader("Respuesta").setAutoWidth(true);
        grid.setItems(pregunta.getOpciones());

        grid.addItemClickListener(e -> {
            respuestaSeleccionada = e.getItem();
        });
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);
    }

    private void refreshGrid() {
        grid.setItems(pregunta.getOpciones());
    }
}
