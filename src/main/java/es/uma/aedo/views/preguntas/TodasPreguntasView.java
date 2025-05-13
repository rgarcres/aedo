package es.uma.aedo.views.preguntas;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.Bloque;
import es.uma.aedo.data.entidades.Pregunta;
import es.uma.aedo.services.PreguntaService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@PageTitle("Preguntas")
@Route("preguntas")
public class TodasPreguntasView extends Div {

    private Grid<Pregunta> grid;
    private final PreguntaService preguntaService;
    private Filters filters;
    private boolean crudPregunta = true;
    private Pregunta preguntaSeleccionada;
    private Bloque bloqueSeleccionado;

    public TodasPreguntasView(PreguntaService service) {
        this.preguntaService = service;
        this.bloqueSeleccionado = (Bloque) VaadinSession.getCurrent().getAttribute("bloquePregunta");
        addClassNames("preguntas-view");
        crudPregunta = (boolean) VaadinSession.getCurrent().getAttribute("crudPregunta");
        filters = new Filters(() -> refreshGrid());
        Button back;
        HorizontalLayout botonesLayout = new HorizontalLayout();

        if(crudPregunta){
            back = BotonesConfig.crearBotonSecundario("<", "bloques");
            botonesLayout = LayoutConfig.createButtons(() -> preguntaSeleccionada, "pregunta", "preguntas", preguntaService, grid);
        } else {
            back = BotonesConfig.crearBotonSecundario("<", "preguntas-bloque");
            botonesLayout = crearBotonesLayout();
        }
    
        // Crear layout
        VerticalLayout layout = new VerticalLayout(
                back,
                LayoutConfig.createMobileFilters(filters),
                filters,
                createGrid(),
                botonesLayout
        );
 
        // Comportamiento al hacer click en una fila del grid
        grid.addItemClickListener(e -> {
            preguntaSeleccionada = e.getItem();
        });

        layout.setAlignItems(Alignment.CENTER);
        layout.setPadding(true);
        layout.setSpacing(true);
        add(layout);
    }

    private HorizontalLayout crearBotonesLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        Button anadir = BotonesConfig.crearBotonPrincipal("Añadir");
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar", "preguntas-bloque");

        anadir.addClickListener(e -> {
            preguntaSeleccionada.setBloque(bloqueSeleccionado);
            preguntaService.save(preguntaSeleccionada);
            NotificacionesConfig.crearNotificacionExito("¡Se ha cambiado el bloque!", "El bloque de la pregunta "
                    + preguntaSeleccionada + " ha sido cambiado con éxito. " + bloqueSeleccionado);
            anadir.getUI().ifPresent(ui -> ui.navigate("preguntas-bloque"));
        });

        layout.add(anadir, cancelar);
        return layout;
    }

    public static class Filters extends Div implements Specification<Pregunta> {

        private final TextField enunciado = new TextField("Enunciado");
        private final CheckboxGroup<Integer> tipo = new CheckboxGroup<>("Tipo");
        private final MultiSelectComboBox<Bloque> bloque = new MultiSelectComboBox<>("Bloque");

        public Filters(Runnable onSearch) {

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

            if (!bloque.isEmpty()) {
                predicates.add(root.get("bloque").in(bloque.getValue()));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }
    }

    public Component createGrid() {
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

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }
}
