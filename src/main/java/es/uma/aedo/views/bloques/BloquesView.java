package es.uma.aedo.views.bloques;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.Bloque;
import es.uma.aedo.services.BloqueService;
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
        VerticalLayout layout = new VerticalLayout(createMobileFilters(), filters, createGrid(), createButtons());
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

    private HorizontalLayout createButtons(){
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setWidthFull();
        buttonsLayout.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER, LumoUtility.AlignItems.CENTER);
        buttonsLayout.addClassName("buttons-layout");
        buttonsLayout.setAlignItems(Alignment.CENTER);

        Button crearBloqueButton = new Button("Añadir Bloque");
        Button editarBloqueButton = new Button("Editar Bloque");
        Button borrarBloqueButton = new Button("Borrar Bloque");
        crearBloqueButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        editarBloqueButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        borrarBloqueButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        crearBloqueButton.addClickListener(e -> {
            crearBloqueButton.getUI().ifPresent(ui -> ui.navigate("preguntas/crear-pregunta"));
        });

        editarBloqueButton.addClickListener(e -> {
            if(bloqueSeleccionado != null){
                VaadinSession.getCurrent().setAttribute("preguntaEditar", bloqueSeleccionado);
                editarBloqueButton.getUI().ifPresent(ui -> ui.navigate("preguntas/editar-pregunta"));
            }
        });

        borrarBloqueButton.addClickListener(e -> {
            if(bloqueSeleccionado != null){
                
            } else {
                NotificacionesConfig.crearNotificacionError("Seleccione una pregunta", "No hay ninguna pregunta seleccionada, seleccione una pregunta para poder borrarla");
            }
        });
        buttonsLayout.add(crearBloqueButton, editarBloqueButton, borrarBloqueButton);
        return buttonsLayout;
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

    private Component createGrid() {
        grid = new Grid<>(Bloque.class, false);
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("nombre").setAutoWidth(true);
        grid.addColumn("descripcion").setAutoWidth(true);

        grid.setItems(query -> bloqueService.list(VaadinSpringDataHelpers.toSpringPageRequest(query), filters)
                 .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

}
