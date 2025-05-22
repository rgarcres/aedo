package es.uma.aedo.views.usuarios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.Grupo;
import es.uma.aedo.data.entidades.Region;
import es.uma.aedo.data.entidades.Usuario;
import es.uma.aedo.data.enumerados.EGenero;
import es.uma.aedo.data.enumerados.ENivelEstudios;
import es.uma.aedo.data.enumerados.ESituacionLaboral;
import es.uma.aedo.data.enumerados.ESituacionPersonal;
import es.uma.aedo.services.RegionService;
import es.uma.aedo.services.UsuarioService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class GestionUsuario {
    // ------------------------------------MÉTODOS PÚBLICOS------------------------------------
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

        // ------------Constructor------------
        public Filters(Runnable onSearch, RegionService rService) {
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
            List<HasValue<?, ?>> fields = new ArrayList<>();
            fields.add(aliasField);
            fields.add(desdePicker);
            fields.add(hastaPicker);
            fields.add(generoBox);
            fields.add(estudiosBox);
            fields.add(laboralBox);
            fields.add(personalBox);
            fields.add(regionBox);

            Div actions = LayoutConfig.crearBotonesFiltros(onSearch, fields);

            add(
                    aliasField,
                    LayoutConfig.crearRangoFechas(desdePicker, hastaPicker),
                    generoBox,
                    estudiosBox,
                    laboralBox,
                    personalBox,
                    regionBox,
                    actions);
        }

        @Override
        public Predicate toPredicate(Root<Usuario> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if (!aliasField.isEmpty()) {
                String dbColumn = "alias";
                String aliasMinus = aliasField.getValue().toLowerCase();
                Predicate p = criteriaBuilder.like(criteriaBuilder.lower(root.get(dbColumn)), "%" + aliasMinus + "%");
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

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }
    }

    public static VerticalLayout crearCamposLayout(UsuarioService usuarioService, RegionService regionService, Usuario usuario, boolean editar) {
        // ------------Layouts------------
        VerticalLayout layout = new VerticalLayout();
        FormLayout camposLayout = new FormLayout();
        HorizontalLayout botonesLayout = new HorizontalLayout();

        // ------------Componentes------------
        TextField idField = new TextField("ID");
        TextField aliasField = new TextField("Nombre");
        DatePicker nacimientoPicker = new DatePicker("Fecha Nacimiento");
        ComboBox<EGenero> generoBox = new ComboBox<>("Género");
        ComboBox<ENivelEstudios> estudiosBox = new ComboBox<>("Nivel de Estudios");
        ComboBox<ESituacionLaboral> laboralBox = new ComboBox<>("Situación Laboral");
        ComboBox<ESituacionPersonal> personalBox = new ComboBox<>("Situación Personal");
        ComboBox<Region> regionBox = new ComboBox<>("Region");

        // ------------Botones------------
        Button guardar = BotonesConfig.crearBotonPrincipal("Guardar");
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar", "usuarios");

        // ------------Instanciar comboBox------------
        generoBox.setItems(EGenero.values());
        estudiosBox.setItems(ENivelEstudios.values());
        laboralBox.setItems(ESituacionLaboral.values());
        personalBox.setItems(ESituacionPersonal.values());
        regionBox.setItems(regionService.getAll());

        // ------------Instanciar valores si hay que editar o si se ha vuelto atrás------------
        if (usuario != null) {
            idField.setValue(usuario.getId());
            aliasField.setValue(usuario.getAlias());
            nacimientoPicker.setValue(usuario.getFechaNacimiento());
            generoBox.setValue(usuario.getGenero());
            estudiosBox.setValue(usuario.getNivelEstudios());
            laboralBox.setValue(usuario.getSituacionLaboral());
            personalBox.setValue(usuario.getSituacionPersonal());
            regionBox.setValue(usuario.getRegion());
        }

        // ------------Comportamiento de botones------------
        guardar.addClickListener(e -> {
            String id = idField.getValue();
            String alias = aliasField.getValue();
            LocalDate nacimiento = nacimientoPicker.getValue();
            EGenero genero = generoBox.getValue();
            ENivelEstudios estudios = estudiosBox.getValue();
            ESituacionLaboral laboral = laboralBox.getValue();
            ESituacionPersonal personal = personalBox.getValue();
            Region region = regionBox.getValue();

            Boolean exito = crearUsuario(usuario, usuarioService, id, alias, nacimiento, genero, estudios, laboral,
                    personal, region, editar);
            if (exito) {
                guardar.getUI().ifPresent(ui -> ui.navigate("usuarios"));
            }
        });

        // ------------Añadir componentes al layout------------
        camposLayout.add(idField, aliasField, nacimientoPicker, generoBox, estudiosBox, laboralBox, personalBox, regionBox);
        botonesLayout.add(guardar, cancelar);
        layout.setAlignItems(Alignment.CENTER);
        layout.add(camposLayout, botonesLayout);
        return layout;
    }

    public static Grid<Usuario> crearGrid(UsuarioService usuarioService, Specification<Usuario> filters) {
        Grid<Usuario> grid = new Grid<>(Usuario.class, false);
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("alias").setAutoWidth(true);
        grid.addColumn("genero").setAutoWidth(true);
        grid.addColumn("nivelEstudios").setAutoWidth(true);
        grid.addColumn("situacionLaboral").setAutoWidth(true);
        grid.addColumn("situacionPersonal").setAutoWidth(true);
        grid.addColumn("region").setAutoWidth(true);
        grid.addColumn(u -> u.getGrupo().stream().map(Grupo::toString).reduce((g1, g2) -> g1 + ", " + g2).orElse("")).setHeader("Grupos").setAutoWidth(true);

        grid.setItems(query -> usuarioService.list(VaadinSpringDataHelpers.toSpringPageRequest(query), filters)
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }

    // ------------------------------------MÉTODOS PRIVADOS------------------------------------
    private static boolean crearUsuario(Usuario usuario, UsuarioService usuarioService, String id, String alias,
            LocalDate nacimiento, EGenero genero, ENivelEstudios estudios, ESituacionLaboral laboral,
            ESituacionPersonal personal, Region region, Boolean editar) {

        if (camposVacios(id, alias, nacimiento, genero, estudios, laboral, personal, region)) {
            NotificacionesConfig.crearNotificacionError("Campos vacíos", "Los campos no pueden estar vacíos");
            return false;
        } else if (!OtrasConfig.comprobarFecha(nacimiento)) {
            NotificacionesConfig.crearNotificacionError("Fecha incorecta", "La fecha introducida no es válida");
            return false;
        } else if (OtrasConfig.comprobarId(id, usuarioService, usuario)) {
            NotificacionesConfig.crearNotificacionError("El ID ya existe", "Introduzca un ID nuevo que sea único");
            return false;
        } else {
            if(usuario == null){
                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setId(id);
                nuevoUsuario.setAlias(alias);
                nuevoUsuario.setFechaNacimiento(nacimiento);
                nuevoUsuario.setGenero(genero);
                nuevoUsuario.setNivelEstudios(estudios);
                nuevoUsuario.setSituacionLaboral(laboral);
                nuevoUsuario.setSituacionPersonal(personal);
                nuevoUsuario.setRegion(region);
                usuarioService.save(nuevoUsuario);
                NotificacionesConfig.crearNotificacionExito("¡Usuario creado!", "El usuario: "+id+" se ha creado con éxito");
            } else {
                usuario.setId(id);
                usuario.setAlias(alias);
                usuario.setFechaNacimiento(nacimiento);
                usuario.setGenero(genero);
                usuario.setNivelEstudios(estudios);
                usuario.setSituacionLaboral(laboral);
                usuario.setSituacionPersonal(personal);
                usuario.setRegion(region);
                usuarioService.save(usuario);
                NotificacionesConfig.crearNotificacionExito("¡Usuario editado!", "El usuario: "+id+" se ha editado con éxito");
            }
            return true;
        }
    }

    private static boolean camposVacios(String id, String alias, LocalDate nacimiento,
            EGenero genero, ENivelEstudios estudios, ESituacionLaboral laboral, ESituacionPersonal personal, Region region) {
        return id.isBlank() || alias.isBlank() || nacimiento == null || genero == null || estudios == null
                || laboral == null
                || personal == null || region == null;
    }
}
