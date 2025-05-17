package es.uma.aedo.views.usuarios;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.Grupo;
import es.uma.aedo.data.entidades.Region;
import es.uma.aedo.data.entidades.Usuario;
import es.uma.aedo.data.enumerados.EGenero;
import es.uma.aedo.data.enumerados.ENivelEstudios;
import es.uma.aedo.data.enumerados.ESituacionLaboral;
import es.uma.aedo.data.enumerados.ESituacionPersonal;
import es.uma.aedo.services.GrupoService;
import es.uma.aedo.services.RegionService;
import es.uma.aedo.services.UsuarioService;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Usuarios")
@Route("usuarios")
@Menu(order = 2, icon = LineAwesomeIconUrl.USER_CIRCLE)
@Uses(Icon.class)
public class TodosUsuariosView extends Div implements HasUrlParameter<String> {

    private Grid<Usuario> grid;

    private Filters filters;
    private final UsuarioService usuarioService;
    private final RegionService regionService;
    private final GrupoService grupoService;
    private Usuario usuarioSeleccionado;
    private Grupo grupo;

    public TodosUsuariosView(UsuarioService service, RegionService rService, GrupoService gService) {
        this.usuarioService = service;
        this.regionService = rService;
        this.grupoService = gService;
        setSizeFull();
        addClassNames("usuarios-view");

        filters = new Filters(() -> refreshGrid(), regionService, grupo);
        VerticalLayout layout = new VerticalLayout(
                LayoutConfig.createTituloLayout("Usuarios", ""),
                filters,
                createGrid(),
                LayoutConfig.createButtons(
                        () -> usuarioSeleccionado,
                        "usuario",
                        "usuarios",
                        usuarioService,
                        grid));

        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String id) {
        grupo = (Grupo) OtrasConfig.getEntidadPorParametro(id, grupoService);
    }

    public static class Filters extends Div implements Specification<Usuario> {
        // ------------Componentes------------
        private final TextField aliasField = new TextField("Alias");
        private final DatePicker desdePicker = new DatePicker("Desde");
        private final DatePicker hastaPicker = new DatePicker("Hasta");
        private final MultiSelectComboBox<EGenero> generoBox = new MultiSelectComboBox<>("Género");
        private final MultiSelectComboBox<ENivelEstudios> estudiosBox = new MultiSelectComboBox<>("Nivel de Estudios");
        private final MultiSelectComboBox<ESituacionLaboral> laboralBox = new MultiSelectComboBox<>(
                "Situación Laboral");
        private final MultiSelectComboBox<ESituacionPersonal> personalBox = new MultiSelectComboBox<>(
                "Situación Personal");
        private final MultiSelectComboBox<Region> regionBox = new MultiSelectComboBox<>("Region");
        private final Grupo grupoFiltro;

        // ------------Constructor------------
        public Filters(Runnable onSearch, RegionService rService, Grupo g) {
            this.grupoFiltro = g;
            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);

            // ------------Inicializar------------
            generoBox.setItems(EGenero.values());
            estudiosBox.setItems(ENivelEstudios.values());
            laboralBox.setItems(ESituacionLaboral.values());
            personalBox.setItems(ESituacionPersonal.values());
            regionBox.setItems(rService.getAll());

            desdePicker.addValueChangeListener(e -> {
                hastaPicker.setMin(desdePicker.getValue().plusDays(1));
            });

            // Action buttons
            Button resetBtn = new Button("Reset");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                aliasField.clear();
                desdePicker.clear();
                hastaPicker.clear();
                generoBox.clear();
                estudiosBox.clear();
                laboralBox.clear();
                personalBox.clear();
                regionBox.clear();
                onSearch.run();
            });
            Button searchBtn = new Button("Search");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> onSearch.run());

            Div actions = new Div(resetBtn, searchBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");

            add (
                aliasField,
                LayoutConfig.crearRangoFechas(desdePicker, hastaPicker),
                generoBox,
                estudiosBox,
                laboralBox,
                personalBox,
                regionBox,
                actions
            );
        }

        @Override
        public Predicate toPredicate(Root<Usuario> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if (!aliasField.isEmpty()) {
                String dbColumn = "alias";
                String aliasMinus = aliasField.getValue().toLowerCase();
                Predicate p = criteriaBuilder.like(criteriaBuilder.lower(root.get(dbColumn)), aliasMinus);
                predicates.add(p);
            }

            if (!desdePicker.isEmpty()) {
                LocalDate desde = desdePicker.getValue();
                Predicate p = criteriaBuilder.greaterThanOrEqualTo(root.get("fechaNacimiento"),
                        criteriaBuilder.literal(desde));
                predicates.add(p);
            }

            if (!hastaPicker.isEmpty()) {
                LocalDate hasta = hastaPicker.getValue();
                Predicate p = criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.literal(hasta),
                        root.get("fechaNacimiento"));
                predicates.add(p);
            }

            if (!generoBox.isEmpty()) {
                predicates.add(root.get("genero").in(generoBox.getValue()));
            }

            if (!estudiosBox.isEmpty()) {
                predicates.add(root.get("nivelEstudios").in(estudiosBox.getValue()));
            }

            if (!laboralBox.isEmpty()) {
                predicates.add(root.get("situacionLaboral").in(laboralBox.getValue()));
            }

            if (!personalBox.isEmpty()) {
                predicates.add(root.get("situacionPersonal").in(laboralBox.getValue()));
            }

            if (!regionBox.isEmpty()) {
                predicates.add(root.get("region").in(regionBox.getValue()));
            }

            if (grupoFiltro != null){
                predicates.add(criteriaBuilder.literal(grupoFiltro).in(root.get("grupos")));
            }
            

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }
    }

    private Component createGrid() {
        grid = new Grid<>(Usuario.class, false);
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("alias").setAutoWidth(true);
        grid.addColumn("genero").setAutoWidth(true);
        grid.addColumn("nivelEstudios").setAutoWidth(true);
        grid.addColumn("situacionLaboral").setAutoWidth(true);
        grid.addColumn("situacionPersonal").setAutoWidth(true);
        grid.addColumn("region").setAutoWidth(true);

        grid.setItems(query -> usuarioService.list(VaadinSpringDataHelpers.toSpringPageRequest(query), filters)
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }
}
