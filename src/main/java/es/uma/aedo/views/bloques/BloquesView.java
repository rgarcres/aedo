package es.uma.aedo.views.bloques;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.Bloque;
import es.uma.aedo.services.BloqueService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Bloques")
@Route("bloques")
@Menu(order = 5, icon = LineAwesomeIconUrl.CLIPBOARD_LIST_SOLID)
@Uses(Icon.class)
public class BloquesView extends Div {

    private Grid<Bloque> grid;

    private Filters filters;
    private final BloqueService bloqueService;
    private Bloque bloqueSeleccionado;

    public BloquesView(BloqueService service) {
        this.bloqueService = service;
        setSizeFull();
        addClassNames("bloques-view");

        filters = new Filters(() -> refreshGrid());
        VerticalLayout layout = new VerticalLayout(
            LayoutConfig.createMobileFilters(filters), 
            filters, 
            createGrid(), 
            LayoutConfig.createButtons(() -> bloqueSeleccionado, "Bloque", 
            "bloques", this.bloqueService, grid), 
            crearBotonesPregunta()
        );

        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setAlignItems(Alignment.CENTER);
        layout.setHorizontalComponentAlignment(Alignment.CENTER);
        add(layout);
    }

    public static class Filters extends Div implements Specification<Bloque> {

        private final TextField nombre = new TextField("Nombre*");
        
        public Filters(Runnable onSearch) {
            
            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);


            // Action buttons
            Button resetBtn = new Button("Reset");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {

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
        public Predicate toPredicate(Root<Bloque> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if(!nombre.isEmpty()){
                String dbColumn = "nombre";
                Predicate p = criteriaBuilder.like(criteriaBuilder.lower(root.get(dbColumn)), nombre.getValue().toLowerCase() + "%");
                predicates.add(p);
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }
    }

    /*
     * Layout que muestra los botones correspondientes a la navegación de las preguntas
     * de cada bloque
     */
    private HorizontalLayout crearBotonesPregunta(){
        HorizontalLayout layout = new HorizontalLayout();
        Button verPreguntas = BotonesConfig.crearBotonPrincipal("Ver preguntas");
        verPreguntas.addClickListener(e -> {
            if(bloqueSeleccionado != null){
                VaadinSession.getCurrent().setAttribute("bloquePregunta", bloqueSeleccionado);
                getUI().ifPresent(ui -> ui.navigate("preguntas"));
            } else {
                NotificacionesConfig.crearNotificacionError("Selecciona un bloque", "No hay ningún bloque seleccionado");
            }
        });
        verPreguntas.getStyle().set("background-color", "#94fa70");
        layout.add(verPreguntas);

        return layout;
    }

    private Component createGrid() {
        grid = new Grid<>(Bloque.class, false);
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("nombre").setAutoWidth(true);
        grid.addColumn("descripcion").setAutoWidth(true);

        grid.setItems(query -> bloqueService.list(VaadinSpringDataHelpers.toSpringPageRequest(query), filters)
                 .stream());
        grid.addItemClickListener(e -> {
            bloqueSeleccionado = e.getItem();
        });
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

}
