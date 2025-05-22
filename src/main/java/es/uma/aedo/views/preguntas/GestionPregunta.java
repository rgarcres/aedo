package es.uma.aedo.views.preguntas;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.Bloque;
import es.uma.aedo.data.entidades.Pregunta;
import es.uma.aedo.services.BloqueService;
import es.uma.aedo.services.PreguntaService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class GestionPregunta {
    public static class Filters extends Div implements Specification<Pregunta> {

        private final TextField enunciado = new TextField("Enunciado");
        private final CheckboxGroup<Integer> tipo = new CheckboxGroup<>("Tipo");
        private final MultiSelectComboBox<Bloque> bloque = new MultiSelectComboBox<>("Bloque");

        private final Bloque bloqueSeleccionado;

        public Filters(Runnable onSearch, Bloque b, BloqueService bloqueService) {
            this.bloqueSeleccionado = b;
            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);
            HorizontalLayout filtersLayout = new HorizontalLayout();

            tipo.setItems(1, 2, 3, 4);
            bloque.setItems(bloqueService.getAll());

            List<HasValue<?, ?>> fields = new ArrayList<>();
            fields.add(enunciado);
            fields.add(tipo);
            if (bloqueSeleccionado == null) {
                fields.add(bloque);
            }

            Div actions = LayoutConfig.crearBotonesFiltros(onSearch, fields);

            filtersLayout.add(enunciado, tipo);
            if (bloqueSeleccionado == null) {
                filtersLayout.add(bloque);
            }
            filtersLayout.setAlignItems(Alignment.CENTER);
            filtersLayout.setJustifyContentMode(JustifyContentMode.CENTER);
            filtersLayout.add(actions);

            add(filtersLayout);
        }

        @Override
        public Predicate toPredicate(Root<Pregunta> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if (!enunciado.isEmpty()) {
                String enunciadoLimpio = enunciado.getValue().toLowerCase();
                Predicate p = criteriaBuilder.like(criteriaBuilder.lower(root.get("enunciado")),
                        "%" + enunciadoLimpio + "%");
                predicates.add(p);
            }

            if (!tipo.isEmpty()) {
                predicates.add(root.get("tipo").in(tipo.getValue()));
            }

            if (bloqueSeleccionado == null) {
                if (!bloque.isEmpty()) {
                    predicates.add(root.get("bloque").in(bloque.getValue()));
                }
            } else {
                predicates.add(criteriaBuilder.equal(root.get("bloque"), bloqueSeleccionado));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }
    }

    /*
     * Crea el layout con los campos a rellenar a la hora de crear una pregunta
     */
    public static VerticalLayout crearCamposRellenar(PreguntaService preguntaService, BloqueService bloqueService,
            Pregunta pregunta) {
        VerticalLayout layout = new VerticalLayout();
        FormLayout camposLayout = new FormLayout();
        HorizontalLayout botonesLayout = new HorizontalLayout();

        TextField idField = new TextField("ID*");
        TextField enunciadoField = new TextField("Enunciado*");
        ComboBox<Bloque> bloqueBox = new ComboBox<>("Bloque*");

        Button aplicar = BotonesConfig.crearBotonPrincipal("Aplicar");
        
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar", "preguntas");

        bloqueBox.setItems(bloqueService.getAll());

        if (pregunta != null) {
            idField.setValue(pregunta.getId());
            enunciadoField.setValue(pregunta.getEnunciado());
            bloqueBox.setValue(pregunta.getBloque());
        }

        camposLayout.setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("500px", 2));

        aplicar.addClickListener(e -> {
            String id = idField.getValue();
            Bloque bloque = bloqueBox.getValue();
            String enunciado = enunciadoField.getValue();

            boolean exito = crearPregunta(pregunta, preguntaService, bloque, id, enunciado);

            if (exito) {
                aplicar.getUI().ifPresent(ui -> ui.navigate("preguntas"));
            }
        });

        camposLayout.add(idField, enunciadoField);
        botonesLayout.add(aplicar, cancelar);
        layout.setAlignItems(Alignment.CENTER);
        bloqueBox.setWidthFull();
        layout.add(camposLayout, bloqueBox, botonesLayout);

        return layout;
    }

    /*
     * Crea el componente grid que contiene todas las preguntas
     */
    public static Grid<Pregunta> createGrid(PreguntaService preguntaService,
            Specification<Pregunta> filters) {
        Grid<Pregunta> grid = new Grid<>(Pregunta.class, false);
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

    private static boolean crearPregunta(Pregunta pregunta, PreguntaService preguntaService, Bloque bloque, String id,
            String enunciado) {
        if (id.isBlank() || enunciado.isBlank() || bloque == null) {
            NotificacionesConfig.crearNotificacionError("Campos vacíos", "Ninguno de los campos puede estar vacío");
            return false;
        } else if (OtrasConfig.comprobarId(id, preguntaService, pregunta)) {
            NotificacionesConfig.crearNotificacionError("El ID ya existe", "Introduzca un ID nuevo que sea único");
            return false;
        } else {
            if (pregunta == null) {
                Pregunta p = new Pregunta();
                p.setId(id);
                p.setEnunciado(enunciado);
                p.setBloque(bloque);
                // Tipo 0 indica que la pregunta está siendo creada
                p.setTipo(0);
                preguntaService.save(p);
                NotificacionesConfig.crearNotificacionExito("¡Pregunta creada!", "La pregunta se ha creado con éxito");
            } else {
                pregunta.setId(id);
                pregunta.setEnunciado(enunciado);
                pregunta.setBloque(bloque);
                preguntaService.save(pregunta);
                NotificacionesConfig.crearNotificacionExito("¡Pregunta editada!", "La pregunta se ha editado con éxito");
            }

            return true;
        }
    }
}
