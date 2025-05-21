package es.uma.aedo.views.campañas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.Campanya;
import es.uma.aedo.services.CampanyaService;
import es.uma.aedo.views.utilidades.LayoutConfig;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class GestionCamp {

    public static class Filters extends Div implements Specification<Campanya> {
        // ------------Componentes------------
        private final TextField nombreField = new TextField("Nombre");
        private final DatePicker inicioPicker = new DatePicker("Inicio");
        private final DatePicker finPicker = new DatePicker("Fin");

        public Filters(Runnable onSearch) {
            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);
            getStyle().set("display", "flex");
            getStyle().set("gap", "1rem"); // Opcional: separa los componentes

            inicioPicker.setAriaLabel("Inicio desde...");
            finPicker.setAriaLabel("Fin hasta...");
            inicioPicker.addValueChangeListener(e -> {
                finPicker.setMin(inicioPicker.getValue().plusDays(1));
            });
            List<HasValue<?, ?>> fields = new ArrayList<>();
            fields.add(nombreField);
            fields.add(inicioPicker);
            fields.add(finPicker);

            Div actions = LayoutConfig.crearBotonesFiltros(onSearch, fields);

            add(nombreField, LayoutConfig.crearRangoFechas(inicioPicker, finPicker), actions);
        }

        @Override
        public Predicate toPredicate(Root<Campanya> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if (!nombreField.isEmpty()) {
                String nombreMinus = nombreField.getValue().toLowerCase();
                Predicate p = criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")),
                        "%" + nombreMinus + "%");
                predicates.add(p);
            }

            if (!inicioPicker.isEmpty()) {
                LocalDate inicio = inicioPicker.getValue();
                Predicate p = criteriaBuilder.greaterThanOrEqualTo(
                        root.get("inicio"), criteriaBuilder.literal(inicio));

                predicates.add(p);
            }

            if (!finPicker.isEmpty()) {
                LocalDate fin = finPicker.getValue();

                Predicate p = criteriaBuilder.greaterThanOrEqualTo(
                        criteriaBuilder.literal(fin), root.get("inicio"));

                predicates.add(p);
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }

    }

    public static Grid<Campanya> crearGrid(CampanyaService service, Specification<Campanya> filters) {
        Grid<Campanya> grid = new Grid<>(Campanya.class, false);
        grid.addColumn("nombre").setAutoWidth(true);
        grid.addColumn("inicio").setAutoWidth(true);
        grid.addColumn("fin").setAutoWidth(true);
        grid.addColumn("objetivo").setAutoWidth(true);
        grid.addColumn("demografia").setAutoWidth(true);
        grid.addColumn("grupos").setAutoWidth(true);

        grid.setItems(query -> service.list(VaadinSpringDataHelpers.toSpringPageRequest(query), filters)
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }
}
