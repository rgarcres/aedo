package es.uma.aedo.views.regiones;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.Region;
import es.uma.aedo.services.RegionService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Regiones")
@Route("regiones")
@Menu(order = 6, icon = LineAwesomeIconUrl.MAP_SOLID)
@Uses(Icon.class)
public class RegionesView extends Div {

    private Grid<Region> grid;

    private Filters filters;
    private final RegionService regionService;

    public RegionesView(RegionService service) {
        this.regionService = service;
        setSizeFull();
        addClassNames("regiones-view");

        filters = new Filters(() -> refreshGrid(), regionService);
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

    public static class Filters extends Div implements Specification<Region> {
        //------------Campos de filtros------------
        private final MultiSelectComboBox<String> localidad = new MultiSelectComboBox<>("Localidad");
        private final MultiSelectComboBox<String> provincia = new MultiSelectComboBox<>("Provincia");
        private final MultiSelectComboBox<String> comunidad = new MultiSelectComboBox<>("Comunidad Autonoma");

        public Filters(Runnable onSearch, RegionService regionService) {

            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);
            
            localidad.setItems(regionService.getAllLocalidades());
            provincia.setItems(regionService.getAllProvincias());
            comunidad.setItems(regionService.getAllComunidades());

            // Action buttons
            Button resetBtn = new Button("Reset");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                localidad.clear();
                provincia.clear();
                comunidad.clear();
                onSearch.run();
            });
            Button searchBtn = new Button("Search");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> onSearch.run());

            Div actions = new Div(resetBtn, searchBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");

            add(localidad, provincia, comunidad, actions);
        }

        @Override
        public Predicate toPredicate(Root<Region> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if(!localidad.isEmpty()){
                String dbColumn = "localidad";
                List<Predicate> localidadPredicates = new ArrayList<>();
                for(String l: localidad.getValue()){
                    localidadPredicates.add(criteriaBuilder.equal(criteriaBuilder.literal(l), root.get(dbColumn)));
                }
                predicates.add(criteriaBuilder.or(localidadPredicates.toArray(Predicate[]::new)));
            }
            if(!provincia.isEmpty()){
                String dbColumn = "provincia";
                List<Predicate> provinciaPredicates = new ArrayList<>();
                for(String p: provincia.getValue()){
                    provinciaPredicates.add(criteriaBuilder.equal(criteriaBuilder.literal(p), root.get(dbColumn)));
                }
                predicates.add(criteriaBuilder.or(provinciaPredicates.toArray(Predicate[]::new)));
            }
            if(!comunidad.isEmpty()){
                String dbColumn = "comunidadAutonoma";
                List<Predicate> comunidadPredicates = new ArrayList<>();
                for(String c: comunidad.getValue()){
                    comunidadPredicates.add(criteriaBuilder.equal(criteriaBuilder.literal(c), root.get(dbColumn)));
                }
                predicates.add(criteriaBuilder.or(comunidadPredicates.toArray(Predicate[]::new)));
            }        

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }
    }

    private Component createGrid() {
        grid = new Grid<>(Region.class, false);
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("localidad").setAutoWidth(true);
        grid.addColumn("provincia").setAutoWidth(true);
        grid.addColumn("comunidadAutonoma").setAutoWidth(true);

        grid.setItems(query -> regionService.list(VaadinSpringDataHelpers.toSpringPageRequest(query), filters)
            .stream()
        );
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }
}
