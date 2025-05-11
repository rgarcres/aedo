package es.uma.aedo.views.preguntas;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.Bloque;
import es.uma.aedo.data.entidades.Pregunta;
import es.uma.aedo.services.PreguntaService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Preguntas")
@Route("preguntas")
@Menu(order = 4, icon = LineAwesomeIconUrl.QUESTION_SOLID)
@Uses(Icon.class)
public class PreguntasView extends Div {

    private Grid<Pregunta> grid;

    private Filters filters;
    private final PreguntaService preguntaService;

    public PreguntasView(PreguntaService service) {
        this.preguntaService = service;
        setSizeFull();
        addClassNames("preguntas-view");

        filters = new Filters(() -> refreshGrid());
        VerticalLayout layout = new VerticalLayout(createMobileFilters(), filters, createGrid());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

    private HorizontalLayout createMobileFilters() {
        // Mobile version
        HorizontalLayout mobileFilters = new HorizontalLayout();
        mobileFilters.setWidthFull();
        mobileFilters.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER,
                LumoUtility.AlignItems.CENTER);
        mobileFilters.addClassName("mobile-filters");

        Icon mobileIcon = new Icon("lumo", "plus");
        Span filtersHeading = new Span("Filters");
        mobileFilters.add(mobileIcon, filtersHeading);
        mobileFilters.setFlexGrow(1, filtersHeading);
        mobileFilters.addClickListener(e -> {
            if (filters.getClassNames().contains("visible")) {
                filters.removeClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:plus");
            } else {
                filters.addClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:minus");
            }
        });
        return mobileFilters;
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


            tipo.setItems(1, 2 , 3, 4);

            // Action buttons
            Button resetBtn = new Button("Reset");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                enunciado.clear();
                tipo.clear();
                bloque.clear();
                onSearch.run();
            });
            Button searchBtn = new Button("Search");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> onSearch.run());

            Div actions = new Div(resetBtn, searchBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");

            add(enunciado, tipo, bloque, actions);
        }

        @Override
        public Predicate toPredicate(Root<Pregunta> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if(!enunciado.isEmpty()){
                String enunciadoLimpio = enunciado.getValue().toLowerCase();
                Predicate p = criteriaBuilder.like(criteriaBuilder.lower(root.get("enunciado")), enunciadoLimpio + "%");
                predicates.add(p);
            }

            if(!tipo.isEmpty()){
                predicates.add(root.get("tipo").in(tipo.getValue()));
            }

            if(!bloque.isEmpty()){
                predicates.add(root.get("bloque").in(bloque.getValue()));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }

    }

    private Component createGrid() {
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
