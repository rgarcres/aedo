package es.uma.aedo.views.preguntas;

import org.springframework.data.jpa.domain.Specification;

import com.vaadin.flow.component.Component;
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
        ComboBox<Integer> tipoComboBox = new ComboBox<>("Tipo*");
        ComboBox<Bloque> bloqueComboBox = new ComboBox<>("Bloque*");

        Button crearEditar;
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar", "preguntas");

        tipoComboBox.setItems(1, 2, 3, 4);
        bloqueComboBox.setItems(bloqueService.getAll());

        if (pregunta == null) {
            crearEditar = BotonesConfig.crearBotonPrincipal("Crear pregunta");
        } else {
            idField.setValue(pregunta.getId());
            enunciadoField.setValue(pregunta.getEnunciado());
            tipoComboBox.setValue(pregunta.getTipo());
            bloqueComboBox.setValue(pregunta.getBloque());
            crearEditar = BotonesConfig.crearBotonPrincipal("Editar pregunta");
        }

        camposLayout.setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2));

        crearEditar.addClickListener(e -> {
            boolean exito = false;

            if (pregunta == null) {
                exito = crearPregunta(preguntaService, bloqueComboBox.getValue(), idField.getValue(),
                        enunciadoField.getValue(), tipoComboBox.getValue());
            } else {
                exito = editarPregunta(pregunta, preguntaService, bloqueComboBox.getValue(), idField.getValue(),
                        enunciadoField.getValue(), tipoComboBox.getValue());
            }

            if(exito){
                crearEditar.getUI().ifPresent(ui -> ui.navigate("preguntas"));
            }
        });
        camposLayout.add(idField, enunciadoField, tipoComboBox, bloqueComboBox);
        botonesLayout.add(crearEditar, cancelar);
        layout.add(camposLayout, botonesLayout);

        return layout;
    }

    /*
     * Crea el componente grid que contiene todas las preguntas
     */
    public static Component createGrid(Grid<Pregunta> grid, PreguntaService preguntaService,
            Specification<Pregunta> filters) {
        grid = new Grid<>(Pregunta.class, false);
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

    private static boolean crearPregunta(PreguntaService preguntaService, Bloque bloque, String id, String enunciado,
            Integer tipo) {
        if (id.isBlank() || enunciado.isBlank() || tipo == null) {
            NotificacionesConfig.crearNotificacionError("Campos vacíos", "Ninguno de los campos puede estar vacío");
            return false;
        } else if (preguntaService.get(id).isPresent()) {
            NotificacionesConfig.crearNotificacionError("El ID ya existe", "Introduzca un ID nuevo que sea único");
            return false;
        } else {
            Pregunta p = new Pregunta();
            p.setId(id);
            p.setEnunciado(enunciado);
            p.setTipo(tipo);
            p.setBloque(bloque);
            NotificacionesConfig.crearNotificacionExito("¡Pregunta creada!", "La pregunta se ha creado con éxito");
            preguntaService.save(p);
            return true;
        }
    }

    private static boolean editarPregunta(Pregunta pregunta, PreguntaService preguntaService, Bloque bloque, String id, String enunciado,
            Integer tipo) {
        if (id.isBlank() || enunciado.isBlank() || tipo == null) {
            NotificacionesConfig.crearNotificacionError("Campos vacíos", "Ninguno de los campos puede estar vacío");
            return false;
        } else if (preguntaService.get(id).isPresent() && !id.equals(pregunta.getId())) {
            NotificacionesConfig.crearNotificacionError("El ID ya existe", "Introduzca un ID nuevo que sea único");
            return false;
        } else {
            pregunta.setId(id);
            pregunta.setEnunciado(enunciado);
            pregunta.setTipo(tipo);
            pregunta.setBloque(bloque);
            NotificacionesConfig.crearNotificacionExito("¡Pregunta editada!", "La pregunta se ha editado con éxito");
            preguntaService.save(pregunta);
            return true;
        }
    }
}
