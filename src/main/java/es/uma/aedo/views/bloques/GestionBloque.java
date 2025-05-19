package es.uma.aedo.views.bloques;

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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.Bloque;
import es.uma.aedo.services.BloqueService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class GestionBloque {

    public static class Filters extends Div implements Specification<Bloque> {

        private final TextField nombre = new TextField("Nombre*");

        public Filters(Runnable onSearch) {

            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);

            List<HasValue<?, ?>> fields = new ArrayList<>();
            fields.add(nombre);

            Div actions = LayoutConfig.crearBotonesFiltros(onSearch, fields);

            add(nombre, actions);
        }

        @Override
        public Predicate toPredicate(Root<Bloque> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if (!nombre.isEmpty()) {
                String dbColumn = "nombre";
                Predicate p = criteriaBuilder.like(criteriaBuilder.lower(root.get(dbColumn)),
                        "%" + nombre.getValue().toLowerCase() + "%");
                predicates.add(p);
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }
    }

    public static Grid<Bloque> crearGrid(BloqueService bloqueService, Specification<Bloque> filters) {
        Grid<Bloque> grid = new Grid<>(Bloque.class, false);
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("nombre").setAutoWidth(true);
        grid.addColumn("descripcion").setAutoWidth(true);

        grid.setItems(query -> bloqueService.list(VaadinSpringDataHelpers.toSpringPageRequest(query), filters)
                .stream());

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }

    public static VerticalLayout crearCamposLayout(Bloque bloque, BloqueService bloqueService) {
        // ------------Layouts------------
        VerticalLayout mainLayout = new VerticalLayout();
        FormLayout camposLayout = new FormLayout();
        HorizontalLayout botonesLayout = new HorizontalLayout();
        // ------------TextField------------
        TextField idField = new TextField("ID*");
        TextField nombreField = new TextField("Nombre*");
        TextField descripcionField = new TextField("Descripcion*");
        // ------------Botones------------
        Button crearButton = BotonesConfig.crearBotonPrincipal("Crear bloque");
        Button cancelarButton = BotonesConfig.crearBotonSecundario("Cancelar", "bloques");

        // ------------Instanciar valores de los TextField y botones------------
        if (bloque != null) {
            idField.setValue(bloque.getId());
            nombreField.setValue(bloque.getNombre());
            descripcionField.setValue(bloque.getDescripcion());
            crearButton.setText("Editar bloque");
        } 

        // ------------Comportamiento boton------------
        crearButton.addClickListener(e -> {
            String id = idField.getValue();
            String nombre = nombreField.getValue();
            String descripcion = descripcionField.getValue();
            boolean exito = crearBloque(bloqueService, bloque, id, nombre, descripcion);

            if(exito) {
                crearButton.getUI().ifPresent(ui -> ui.navigate("bloques"));
            }
        });

        botonesLayout.add(crearButton, cancelarButton);
        camposLayout.add(idField, nombreField, descripcionField, botonesLayout);
        mainLayout.add(camposLayout, botonesLayout);

        return mainLayout;
    }

    /*
     * Método que crea un bloque y devuelve true si se ha creado con éxito
     */
    private static boolean crearBloque(BloqueService service, Bloque bloque, String id, String nombre,
            String descripcion) {
        if (comprobarVacios(id, nombre, descripcion)) {
            NotificacionesConfig.crearNotificacionError("Campos vacíos", "Ninguno de los campos puede estar vacío");
            return false;
        }
        if (OtrasConfig.comprobarId(id, service, bloque)) {
            NotificacionesConfig.crearNotificacionError("El ID ya existe", "Introduzca un ID nuevo que sea único");
            return false;
        } else {
            if (bloque == null) {
                Bloque nuevoBloque = new Bloque();
                nuevoBloque.setId(id);
                nuevoBloque.setNombre(nombre);
                nuevoBloque.setDescripcion(descripcion);
                service.save(nuevoBloque);

                NotificacionesConfig.crearNotificacionExito("¡Bloque creado!",
                        "El bloque se ha creado con éxito.\nNuevo bloque: " + nuevoBloque);
            } else {
                bloque.setId(id);
                bloque.setNombre(nombre);
                bloque.setDescripcion(descripcion);
                service.save(bloque);

                NotificacionesConfig.crearNotificacionExito("¡Bloque editado!",
                        "El bloque se ha editado con éxito.\nNuevo bloque: " + bloque);                
            }
            return true;
        }
    }

    /*
     * Devuelve TRUE si alguno de los campos está vacío
     */
    private static boolean comprobarVacios(String id, String nombre, String descripcion) {
        return id.isBlank() || nombre.isBlank() || descripcion.isBlank();
    }
}
