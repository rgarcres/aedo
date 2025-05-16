package es.uma.aedo.views.preguntas;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.Bloque;
import es.uma.aedo.data.entidades.Pregunta;
import es.uma.aedo.services.PreguntaService;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

@PageTitle("Preguntas")
@Route("preguntas-bloque")
public class PreguntasBloqueView extends Div {

    private Grid<Pregunta> grid;

    private Pregunta preguntaSeleccionada;
    private Filters filters;
    private final PreguntaService preguntaService;
    private final Bloque bloqueSeleccionado;

    /*
     * Vista en la que se muestras todas las preguntas de un único bloque
     */
    public PreguntasBloqueView(PreguntaService service) {
        this.preguntaService = service;
        this.bloqueSeleccionado = (Bloque) VaadinSession.getCurrent().getAttribute("bloquePregunta");
        setSizeFull();
        addClassNames("preguntas-bloque-view");

        filters = new Filters(() -> refreshGrid(), bloqueSeleccionado);
        grid = GestionPregunta.createGrid(preguntaService, filters);
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
    }

    private HorizontalLayout createButtons() {
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setWidthFull();
        buttonsLayout.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER,
                LumoUtility.AlignItems.CENTER);
        buttonsLayout.addClassName("buttons-layout");
        buttonsLayout.setAlignItems(Alignment.CENTER);

        Button crearPreguntaButton = new Button("Añadir pregunta al bloque");
        Button editarPreguntaButton = new Button("Editar pregunta");
        Button borrarPreguntaButton = new Button("Eliminar pregunta del bloque");
        crearPreguntaButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        editarPreguntaButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        borrarPreguntaButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        crearPreguntaButton.addClickListener(e -> {
            /*
             * Atributo crudPregunta:
             * false: NO activa los botones CRUD de la entidad pregunta
             * true: activa los botones necesarios para CRUD de preguntas
             */
            VaadinSession.getCurrent().setAttribute("crudPregunta", false);
            crearPreguntaButton.getUI().ifPresent(ui -> ui.navigate("preguntas"));
        });

        editarPreguntaButton.addClickListener(e -> {
            if (preguntaSeleccionada != null) {
                VaadinSession.getCurrent().setAttribute("preguntaEditar", preguntaSeleccionada);
                editarPreguntaButton.getUI().ifPresent(ui -> ui.navigate("preguntas/editar-pregunta"));
            }
        });

        borrarPreguntaButton.addClickListener(e -> {
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
        buttonsLayout.add(crearPreguntaButton, editarPreguntaButton, borrarPreguntaButton);
        return buttonsLayout;
    }

    public static class Filters extends Div implements Specification<Pregunta> {

        private final TextField enunciado = new TextField("Enunciado");
        private final CheckboxGroup<Integer> tipo = new CheckboxGroup<>("Tipo");
        private final Bloque bloque;

        public Filters(Runnable onSearch, Bloque b) {
            this.bloque = b;
            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);

            tipo.setItems(1, 2, 3, 4);

            // Action buttons
            Button resetBtn = new Button("Reset");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                enunciado.clear();
                tipo.clear();
                onSearch.run();
            });
            Button searchBtn = new Button("Search");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> onSearch.run());

            Div actions = new Div(resetBtn, searchBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");
            
            add(enunciado, tipo, actions);
        }

        @Override
        public Predicate toPredicate(Root<Pregunta> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if (!enunciado.isEmpty()) {
                String enunciadoLimpio = enunciado.getValue().toLowerCase();
                Predicate p = criteriaBuilder.like(criteriaBuilder.lower(root.get("enunciado")), enunciadoLimpio + "%");
                predicates.add(p);
            }

            if (!tipo.isEmpty()) {
                predicates.add(root.get("tipo").in(tipo.getValue()));
            }

            predicates.add(criteriaBuilder.equal(root.get("bloque"), bloque));

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }

    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }
}
