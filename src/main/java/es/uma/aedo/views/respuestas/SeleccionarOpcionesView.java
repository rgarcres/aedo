package es.uma.aedo.views.respuestas;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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

import es.uma.aedo.data.entidades.Pregunta;
import es.uma.aedo.data.entidades.RespuestaTipo1.RespuestaSiNo;
import es.uma.aedo.data.entidades.RespuestaTipo2.RespuestaSiNoIntensidad;
import es.uma.aedo.services.PreguntaService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;

@PageTitle("Selccionar Opciones")
@Route("preguntas/seleccionar-opciones/")
/*
 * Vista para cuando se está creando una pregunta de tipo 3
 * Es necesario establecer las opciones que existen
 */
public class SeleccionarOpcionesView extends Div implements HasUrlParameter<String> {

    private final PreguntaService preguntaService;
    private final List<String> opciones = new ArrayList<>();
    private Pregunta pregunta;

    public SeleccionarOpcionesView(PreguntaService pService) {
        this.preguntaService = pService;
    }

    @Override
    public void setParameter(BeforeEvent event, String id) {
        if (id != null) {
            if (preguntaService.get(id).isPresent()) {
                pregunta = preguntaService.get(id).get();

                add(crearLayout());
            } else {
                add(LayoutConfig.createNotFoundLayout());
            }
        } else {
            add(LayoutConfig.createNotFoundLayout());
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
        Button crear = BotonesConfig.crearBotonPrincipal("Crear pregunta");
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar");

        // ------------Comportamiento tipoBox------------
        tipoBox.setItems(1, 2, 3, 4);
        Integer tipo = pregunta.getTipo();
        if(tipo != 0){
            tipoBox.setValue(pregunta.getTipo());
            if(tipo == 1 || tipo == 2){
                contenido.add(crearContenidoTipo1Y2(tipo));
            } else if(tipo == 3 || tipo == 4){
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

        crear.addClickListener(e -> {
            Integer t = tipoBox.getValue();
            crear(t);
        });
        // ------------Añadir componentes al layout------------
        botonesLayout.add(crear, cancelar);
        layout.setAlignItems(Alignment.CENTER);
        layout.add(tipoBox, contenido, botonesLayout);

        return layout;
    }

    private VerticalLayout crearContenidoTipo1Y2(Integer t) {
        VerticalLayout layout = new VerticalLayout();
        H3 h3 = new H3("Respuestas tipo " + t);
        Div div = new Div();
        if (t == 1) {
            div.setText(RespuestaSiNo.values().toString());
        } else {
            div.setText(RespuestaSiNoIntensidad.values().toString());
        }

        layout.setAlignItems(Alignment.CENTER);
        layout.add(h3, div);
        return layout;
    }

    private VerticalLayout crearContenidoTipo3Y4(Integer t) {
        VerticalLayout layout = new VerticalLayout();
        H3 h3 = new H3("Respuestas tipo " + t);
        TextField respuestasField = new TextField("Posible respuesta");
        Div respuestasText = new Div();
        Button anadir = BotonesConfig.crearBotonPrincipal("Añadir posible respuesta");
        respuestasField.setPlaceholder("Introduzca una posible respuesta...");

        respuestasText.setText(opciones.toString());

        anadir.addClickListener(e -> {
            String res = respuestasField.getValue();
            if (!res.isBlank()) {
                opciones.add(res);
                respuestasText.removeAll();
                respuestasText.setText(opciones.toString());
                NotificacionesConfig.notificar("Respuesta: " + res + " añadida");
            } else {
                NotificacionesConfig.crearNotificacionError("Respuesta vacía",
                        "No puede añadir una respuesta vacía. Introduzca una respuesta válida");
            }
        });

        layout.setAlignItems(Alignment.CENTER);
        layout.add(h3, respuestasField, anadir, respuestasText);
        return layout;
    }

    /*
     * Método para cancelar la transacción
     *      Si está creando: se borra la pregunta a medio crear de la base de datos
     *      Si está editando: se obtiene el valor de la pregunta antes de que fuera editada y 
     *                          se vuelve a guarda en la BD
     */     
    private void cancelar() {
        //Tipo = 0 -> pregunta a medio crear
        if (pregunta.getTipo() == 0) {
            preguntaService.delete(pregunta.getId());
            NotificacionesConfig.notificar("La pregunta: " + pregunta + " no se ha creado.");
        //Comprobar que el atributo de sesión de la pregunta original antes de editar no esté a null
        } else if (VaadinSession.getCurrent().getAttribute("preguntaSinEditar") != null) {
            Pregunta p = (Pregunta) VaadinSession.getCurrent().getAttribute("preguntaSinEditar");
            pregunta.setId(p.getId());
            pregunta.setBloque(p.getBloque());
            pregunta.setEnunciado(p.getEnunciado());
            preguntaService.save(pregunta);
            NotificacionesConfig.notificar("La pregunta: " + pregunta + " no se ha editado.");
        }
        //Navegar
        getUI().ifPresent(ui -> ui.navigate("preguntas"));
    }

    /*
     * Método para crear/editar la pregunta
     */
    private void crear(Integer t) {
        if (t != null) {
            //Establecer el tipo y, si fuese necesario, las opciones a elegir 
            pregunta.setTipo(t);
            if (t == 3 || t == 4) {
                pregunta.setOpciones(opciones);
            }
            //Guardar la pregunta en la BD
            preguntaService.save(pregunta);
            //Notificar y navegar
            NotificacionesConfig.crearNotificacionExito("¡Pregunta creada!", "La pregunta se ha creado con éxito");
            getUI().ifPresent(ui -> ui.navigate("preguntas"));
        } else {
            //Notificar error
            NotificacionesConfig.crearNotificacionError("Selecciona un tipo",
                    "No hay ningún tipo seleccionado, por favor seleccione un tipo.");
        }
    }
}
