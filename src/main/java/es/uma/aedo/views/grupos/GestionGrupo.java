package es.uma.aedo.views.grupos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
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
import es.uma.aedo.services.GrupoService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class GestionGrupo {
    public static class Filters extends Div implements Specification<Grupo> {

        private final TextField nombre = new TextField("Nombre");

        public Filters(Runnable onSearch) {

            setWidthFull();
            addClassName("filter-layout");
            HorizontalLayout layout = new HorizontalLayout();

            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);

            List<HasValue<?, ?>> fields = new ArrayList<>();
            fields.add(nombre);

            Div actions = LayoutConfig.crearBotonesFiltros(onSearch, fields);

            layout.add(nombre, actions);
            add(layout);
        }

        @Override
        public Predicate toPredicate(Root<Grupo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if (!nombre.isEmpty()) {
                String dbColumn = "nombre";
                String nombreMinus = nombre.getValue().toLowerCase();
                Predicate p = criteriaBuilder.like(criteriaBuilder.lower(root.get(dbColumn)), "%" + nombreMinus + "%");
                predicates.add(p);
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }
    }

    public static VerticalLayout crearCamposRellenar(Grupo grupo, GrupoService service) {
        // ------------Layouts------------
        VerticalLayout layout = new VerticalLayout();
        FormLayout camposLayout = new FormLayout();
        HorizontalLayout botonesLayout = new HorizontalLayout();
        // ------------Campos------------
        TextField idField = new TextField("ID*");
        TextField nombreField = new TextField("Nombre*");
        TextField descripcionField = new TextField("Descripción");
        // ------------Botones------------
        Button crearEditar = BotonesConfig.crearBotonPrincipal("Crear grupo");
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar", "grupos");

        // ------------Rellenar campos (solo edición)------------
        if (grupo != null) {
            idField.setReadOnly(true);
            idField.setValue(grupo.getId());
            nombreField.setValue(grupo.getNombre());
            descripcionField.setValue(grupo.getDescripcion());
            crearEditar.setText("Editar grupo");
        }

        // ------------Comportamiento de botones------------
        crearEditar.addClickListener(e -> {
            String id = idField.getValue();
            String nombre = nombreField.getValue();
            String descripcion = descripcionField.getValue();

            boolean exito = crearGrupo(service, grupo, id, nombre, descripcion);

            if (exito) {
                crearEditar.getUI().ifPresent(ui -> ui.navigate("grupos"));
            }
        });

        // ------------Añadir al layout------------
        camposLayout.add(idField, nombreField, descripcionField);
        camposLayout.setColspan(descripcionField, 2);
        botonesLayout.add(crearEditar, cancelar);
        layout.setAlignItems(Alignment.CENTER);
        layout.add(camposLayout, botonesLayout);
        return layout;
    }

    public static Grid<Grupo> createGrid(GrupoService grupoService, Specification<Grupo> filters) {
        Grid<Grupo> grid = new Grid<>(Grupo.class, false);
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("nombre").setAutoWidth(true);
        grid.addColumn("descripcion").setAutoWidth(true);

        grid.setItems(query -> grupoService.list(VaadinSpringDataHelpers.toSpringPageRequest(query), filters)
                .stream());

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }

    private static boolean crearGrupo(GrupoService service, Grupo grupo, String id, String nombre, String descripcion) {
        if (id.isBlank() || nombre.isBlank()) {
            NotificacionesConfig.crearNotificacionError("Campos vacios",
                    "Los campos ID y Nombre no pueden estar vacíos.");
            return false;
        } else if (OtrasConfig.comprobarId(id, service, grupo)) {
            NotificacionesConfig.crearNotificacionError("El ID ya existe", "Introduzca un ID nuevo que sea único");
            return false;
        } else {
            if (grupo == null) {
                Grupo nuevoGrupo = new Grupo();
                nuevoGrupo.setId(id);
                nuevoGrupo.setNombre(nombre);
                nuevoGrupo.setDescripcion(descripcion);
                service.save(nuevoGrupo);
                NotificacionesConfig.crearNotificacionExito("¡Grupo creado!",
                        "El grupo se ha creado con éxito. Nuevo grupo: " + nuevoGrupo);
            } else {
                grupo.setNombre(nombre);
                grupo.setDescripcion(descripcion);
                service.save(grupo);
                NotificacionesConfig.crearNotificacionExito("¡Grupo editado!",
                        "El grupo se ha editado con éxito. Nuevo grupo: " + grupo);
            }
            return true;
        }
    }
}
