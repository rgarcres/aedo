package es.uma.aedo.views.preguntas;

import org.springframework.data.jpa.domain.Specification;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.Bloque;
import es.uma.aedo.data.entidades.Pregunta;
import es.uma.aedo.services.BloqueService;
import es.uma.aedo.services.PreguntaService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;

public class GestionPregunta {
    /*
     * Crea el layout con los campos a rellenar a la hora de crear una pregunta
     */
    public static VerticalLayout crearCamposRellenar(PreguntaService preguntaService, BloqueService bloqueService, Pregunta pregunta) {
        VerticalLayout layout = new VerticalLayout();
        FormLayout camposLayout = new FormLayout();
        HorizontalLayout botonesLayout = new HorizontalLayout();

        TextField idField = new TextField("ID*");
        TextField enunciadoField = new TextField("Enunciado*");
        ComboBox<Bloque> bloqueComboBox = new ComboBox<>("Bloque*");

        Button siguiente;
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar", "preguntas");

        bloqueComboBox.setItems(bloqueService.getAll());

        if (pregunta == null) {
            siguiente = BotonesConfig.crearBotonPrincipal("Siguiente");
        } else {
            idField.setValue(pregunta.getId());
            enunciadoField.setValue(pregunta.getEnunciado());
            bloqueComboBox.setValue(pregunta.getBloque());
            siguiente = BotonesConfig.crearBotonPrincipal("Siguiente");
        }

        camposLayout.setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2));

        siguiente.addClickListener(e -> {
            boolean exito = false;
            String id = idField.getValue();
            Bloque bloque = bloqueComboBox.getValue();
            String enunciado = enunciadoField.getValue();

            if (pregunta == null) {
                exito = crearPregunta(preguntaService, bloque, id, enunciado);
            } else {
                exito = editarPregunta(pregunta, preguntaService, bloque, id, enunciado);
            }

            if(exito){
                siguiente.getUI().ifPresent(ui -> ui.navigate("preguntas/seleccionar-opciones/"+id));
            }
        });
        camposLayout.add(idField, enunciadoField, bloqueComboBox);
        botonesLayout.add(siguiente, cancelar);
        layout.add(camposLayout, botonesLayout);

        return layout;
    }

    /*
     * Crea el componente grid que contiene todas las preguntas
     */
    public static Grid<Pregunta> createGrid(PreguntaService preguntaService,
            Specification<Pregunta> filters) {
        Grid <Pregunta> grid = new Grid<>(Pregunta.class, false);
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("enunciado").setAutoWidth(true);
        grid.addColumn("tipo").setAutoWidth(true);
        grid.addColumn("bloque").setAutoWidth(true);

        grid.setItems(query -> preguntaService.list(VaadinSpringDataHelpers.toSpringPageRequest(query), filters)
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }

    private static boolean crearPregunta(PreguntaService preguntaService, Bloque bloque, String id, String enunciado) {
        if (id.isBlank() || enunciado.isBlank() || bloque == null) {
            NotificacionesConfig.crearNotificacionError("Campos vacíos", "Ninguno de los campos puede estar vacío");
            return false;
        } else if (preguntaService.get(id).isPresent()) {
            NotificacionesConfig.crearNotificacionError("El ID ya existe", "Introduzca un ID nuevo que sea único");
            return false;
        } else {
            Pregunta p = new Pregunta();
            p.setId(id);
            p.setEnunciado(enunciado);
            p.setBloque(bloque);
            //Tipo 0 indica que la pregunta está siendo creada
            p.setTipo(0);
            preguntaService.save(p);
            return true;
        }
    }

    private static boolean editarPregunta(Pregunta pregunta, PreguntaService preguntaService, Bloque bloque, String id, String enunciado) {
        if (id.isBlank() || enunciado.isBlank() || bloque == null) {
            NotificacionesConfig.crearNotificacionError("Campos vacíos", "Ninguno de los campos puede estar vacío");
            return false;
        } else if (preguntaService.get(id).isPresent() && !id.equals(pregunta.getId())) {
            NotificacionesConfig.crearNotificacionError("El ID ya existe", "Introduzca un ID nuevo que sea único");
            return false;
        } else {
            pregunta.setId(id);
            pregunta.setEnunciado(enunciado);
            pregunta.setBloque(bloque);
            preguntaService.save(pregunta);
            return true;
        }
    }
}
