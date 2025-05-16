package es.uma.aedo.views.grupos;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.Grupo;
import es.uma.aedo.services.GrupoService;
import es.uma.aedo.views.utilidades.LayoutConfig;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Grupos")
@Route("grupos")
@Menu(order = 3, icon = LineAwesomeIconUrl.USERS_SOLID)
@Uses(Icon.class)
public class GruposView extends Div {

    private Grid<Grupo> grid;

    private Filters filters;
    private final GrupoService grupoService;
    private Grupo grupoSeleccionado;

    public GruposView(GrupoService service) {
        this.grupoService = service;
        setSizeFull();
        addClassNames("grupos-view");

        filters = new Filters(() -> refreshGrid());
        VerticalLayout layout = new VerticalLayout(
            LayoutConfig.createTituloLayout("Grupos", ""),
            LayoutConfig.createMobileFilters(filters),
            filters, 
            createGrid(),
            LayoutConfig.createButtons(
                () -> grupoSeleccionado, 
                "grupo", 
                "grupos", 
                grupoService, 
                grid
            )
        );

        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

    public static class Filters extends Div implements Specification<Grupo> {

        private final TextField nombre = new TextField("Nombre");

        public Filters(Runnable onSearch) {

            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);


            // Action buttons
            Button resetBtn = new Button("Reset");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                nombre.clear();
                onSearch.run();
            });
            Button searchBtn = new Button("Search");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> onSearch.run());

            Div actions = new Div(resetBtn, searchBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");

            add(nombre, actions);
        }

        @Override
        public Predicate toPredicate(Root<Grupo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if(!nombre.isEmpty()){
                String dbColumn = "nombre";
                String nombreMinus = nombre.getValue().toLowerCase();
                Predicate p = criteriaBuilder.like(criteriaBuilder.lower(root.get(dbColumn)), nombreMinus);
                predicates.add(p);
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }
    }

    private Component createGrid() {
        grid = new Grid<>(Grupo.class, false);
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("nombre").setAutoWidth(true);
        grid.addColumn("descripcion").setAutoWidth(true);

        grid.setItems(query -> grupoService.list(VaadinSpringDataHelpers.toSpringPageRequest(query), filters)
                .stream());
        grid.addItemClickListener(e -> {
            grupoSeleccionado = e.getItem();
        });
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

}
