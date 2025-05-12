package es.uma.aedo.views.preguntas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.Pregunta;
import es.uma.aedo.services.PreguntaService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;

@PageTitle("Crear Pregunta")
@Route("preguntas/crear-pregunta")
public class CrearPreguntaView extends Div{
    
    private final PreguntaService preguntaService;
    private TextField idField;
    private TextField enunciadoField;
    private ComboBox<Integer> tipoComboBox;

    public CrearPreguntaView(PreguntaService service){
        this.preguntaService = service;
        addClassNames("crear-regiones-view");

        VerticalLayout layout = new VerticalLayout(crearCamposRellenar(), crearBotonesLayout());
        layout.setAlignItems(Alignment.CENTER);
        layout.setPadding(true);
        layout.setSpacing(true);
        add(layout);
    }

    private FormLayout crearCamposRellenar(){
        FormLayout layout = new FormLayout();
        HorizontalLayout botonesLayout = new HorizontalLayout();

        TextField id = new TextField("ID*");
        TextField enunciado = new TextField("Enunciado*");
        ComboBox<Integer> tipo = new ComboBox<>("Tipo*");

        tipo.setItems(1, 2, 3, 4);

        layout.add(id, enunciado, tipo, botonesLayout);

        layout.setResponsiveSteps(
            new ResponsiveStep("0", 1),
            new ResponsiveStep("500px", 2)
        );

        this.idField = id;
        this.enunciadoField = enunciado;
        this.tipoComboBox = tipo;

        return layout;
    }

    private HorizontalLayout crearBotonesLayout(){
        HorizontalLayout layout = new HorizontalLayout();
        Button crearButton = BotonesConfig.crearBotonPrincipal("Añadir");
        Button cancelarButton = BotonesConfig.crearBotonSecundario("Cancelar", "regiones");

        crearButton.addClickListener(e -> {
            crearPregunta(idField.getValue(), enunciadoField.getValue(), tipoComboBox.getValue());
        });

        layout.add(crearButton, cancelarButton);

        return layout;
    }

    private void crearPregunta(String id, String enunciado, Integer tipo){
        if(id.isBlank() || enunciado.isBlank() || tipo == null){
            NotificacionesConfig.crearNotificacionError("Campos vacíos", "Ninguno de los campos puede estar vacío");
        } else if (preguntaService.get(id).isPresent()){
            NotificacionesConfig.crearNotificacionError("El ID ya existe","Introduzca un ID nuevo que sea único");
        } else {
            Pregunta p = new Pregunta();
            p.setId(id);
            p.setEnunciado(enunciado);
            p.setTipo(tipo);
            NotificacionesConfig.crearNotificacionExito("¡Pregunta creada!", "La pregunta se ha creado con éxito");
            //preguntaService.save(p);
            getUI().ifPresent(ui -> ui.navigate("preguntas"));
        }
    }   
}
