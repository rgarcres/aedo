package es.uma.aedo.views.regiones;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.Region;
import es.uma.aedo.services.RegionService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class GestionRegion {

    public static class Filters extends Div implements Specification<Region> {
        // ------------Campos de filtros------------
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

            List<HasValue<?, ?>> fields = new ArrayList<>();
            fields.add(localidad);
            fields.add(provincia);
            fields.add(comunidad);

            Div actions = LayoutConfig.crearBotonesFiltros(onSearch, fields);

            add(localidad, provincia, comunidad, actions);
        }

        @Override
        public Predicate toPredicate(Root<Region> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if (!localidad.isEmpty()) {
                String dbColumn = "localidad";
                List<Predicate> localidadPredicates = new ArrayList<>();
                for (String l : localidad.getValue()) {
                    localidadPredicates.add(criteriaBuilder.equal(criteriaBuilder.literal(l), root.get(dbColumn)));
                }
                predicates.add(criteriaBuilder.or(localidadPredicates.toArray(Predicate[]::new)));
            }
            if (!provincia.isEmpty()) {
                String dbColumn = "provincia";
                List<Predicate> provinciaPredicates = new ArrayList<>();
                for (String p : provincia.getValue()) {
                    provinciaPredicates.add(criteriaBuilder.equal(criteriaBuilder.literal(p), root.get(dbColumn)));
                }
                predicates.add(criteriaBuilder.or(provinciaPredicates.toArray(Predicate[]::new)));
            }
            if (!comunidad.isEmpty()) {
                String dbColumn = "comunidadAutonoma";
                List<Predicate> comunidadPredicates = new ArrayList<>();
                for (String c : comunidad.getValue()) {
                    comunidadPredicates.add(criteriaBuilder.equal(criteriaBuilder.literal(c), root.get(dbColumn)));
                }
                predicates.add(criteriaBuilder.or(comunidadPredicates.toArray(Predicate[]::new)));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }
    }

    public static VerticalLayout crearCamposLayout(Region region, RegionService service) {
        // ------------Layouts------------
        VerticalLayout mainLayout = new VerticalLayout();
        FormLayout camposLayout = new FormLayout();
        HorizontalLayout botonesLayout = new HorizontalLayout();
        // ------------TextField------------
        TextField idField = new TextField("ID*");
        TextField localidadField = new TextField("Localidad*");
        TextField provinciaField = new TextField("Provincia*");
        TextField comunidadField = new TextField("Comunidad Autonoma*");
        // ------------Botones------------
        Button crearButton = BotonesConfig.crearBotonPrincipal("Crear región");
        Button cancelarButton = BotonesConfig.crearBotonSecundario("Cancelar", "regiones");

        // ------------Instanciar valores de los TextField y botones------------
        if (region != null) {
            idField.setValue(region.getId());
            localidadField.setValue(region.getLocalidad());
            provinciaField.setValue(region.getProvincia());
            comunidadField.setValue(region.getComunidadAutonoma());
            crearButton.setText("Editar región");
        }

        crearButton.addClickListener(e -> {
            String id = idField.getValue();
            String localidad = localidadField.getValue();
            String provincia = provinciaField.getValue();
            String comunidad = comunidadField.getValue();

            boolean exito = crearRegion(service, region, id, localidad, provincia, comunidad);
            if (exito) {
                crearButton.getUI().ifPresent(ui -> ui.navigate("regiones"));
            }
        });

        camposLayout.add(idField, localidadField, provinciaField, comunidadField);
        botonesLayout.add(crearButton, cancelarButton);
        mainLayout.add(camposLayout, botonesLayout);
        return mainLayout;
    }

    public static Grid<Region> crearGrid(RegionService regionService, Specification<Region> filters) {
        Grid<Region> grid = new Grid<>(Region.class, false);
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("localidad").setAutoWidth(true);
        grid.addColumn("provincia").setAutoWidth(true);
        grid.addColumn("comunidadAutonoma").setAutoWidth(true);

        grid.setItems(query -> regionService.list(VaadinSpringDataHelpers.toSpringPageRequest(query), filters)
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);
        return grid;
    }

    private static boolean crearRegion(RegionService service, Region region, String id, String localidad,
            String provincia, String comunidad) {
        // Comprobar que ninguno de los campos está vacío
        if (comprobarVacios(id, localidad, provincia, comunidad)) {
            NotificacionesConfig.crearNotificacionError("Campos vacíos", "Ninguno de los campos puede estar vacío");
            return false;
            // Comprobar que si el ID ha sido cambiado, no está ya en la base de datos
        } else if (OtrasConfig.comprobarId(id, service, region)) {
            NotificacionesConfig.crearNotificacionError("El ID ya existe",
                    "Introduzca un ID nuevo que sea único");
            return false;
        } else {
            if (region == null) {
                Region nuevaRegion = new Region();
                nuevaRegion.setId(id);
                nuevaRegion.setLocalidad(localidad);
                nuevaRegion.setProvincia(provincia);
                nuevaRegion.setComunidadAutonoma(comunidad);
                service.save(nuevaRegion);
                NotificacionesConfig.crearNotificacionExito("¡Región creada!",
                        "La región se ha creado con éxito.\nNueva región: " + nuevaRegion);
            } else {
                region.setId(id);
                region.setLocalidad(localidad);
                region.setProvincia(provincia);
                region.setComunidadAutonoma(comunidad);
                service.save(region);
                NotificacionesConfig.crearNotificacionExito("¡Región editada!",
                        "La región se ha editado con éxito.\nNueva región: " + region);               
            }

            return true;
        }
    }

    /*
     * Devuelve TRUE si alguno de los campos está vacío
     */
    private static boolean comprobarVacios(String id, String localidad, String provincia, String comunidad) {
        return id.isBlank() || localidad.isBlank() || provincia.isBlank() || comunidad.isBlank();
    }
}
