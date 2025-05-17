package es.uma.aedo.views.regiones;

import org.springframework.data.jpa.domain.Specification;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.Region;
import es.uma.aedo.services.RegionService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;

public class GestionRegion {
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
        Button crearButton;
        Button cancelarButton = BotonesConfig.crearBotonSecundario("Cancelar", "regiones");

        // ------------Instanciar valores de los TextField y botones------------
        if (region != null) {
            idField.setValue(region.getId());
            localidadField.setValue(region.getLocalidad());
            provinciaField.setValue(region.getProvincia());
            comunidadField.setValue(region.getComunidadAutonoma());
            crearButton = BotonesConfig.crearBotonPrincipal("Editar region");
        } else {
            crearButton = BotonesConfig.crearBotonPrincipal("Crear Region");
        }

        crearButton.addClickListener(e -> {
            String id = idField.getValue();
            String localidad = localidadField.getValue();
            String provincia = provinciaField.getValue();
            String comunidad = comunidadField.getValue();

            // Comprobar que ninguno de los campos está vacío
            if (comprobarVacios(id, localidad, provincia, comunidad)) {
                NotificacionesConfig.crearNotificacionError("Campos vacíos", "Ninguno de los campos puede estar vacío");
                // Comprobar que si el ID ha sido cambiado, no está ya en la base de datos
            } else {
                if (region == null) {
                    if (comprobarId(id, service)) {
                        NotificacionesConfig.crearNotificacionError("El ID ya existe",
                                "Introduzca un ID nuevo que sea único");
                    } else {
                        crearRegion(service, id, localidad, provincia, comunidad);
                        crearButton.getUI().ifPresent(ui -> ui.navigate("regiones"));
                    }
                } else {
                    if (comprobarId(id, service) && !id.equals(region.getId())) {
                        NotificacionesConfig.crearNotificacionError("El ID ya existe",
                                "Introduzca un ID nuevo que sea único");
                    } else {
                        editarRegion(region, service, id, localidad, provincia, comunidad);
                        crearButton.getUI().ifPresent(ui -> ui.navigate("regiones"));
                    }
                }
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

    private static void crearRegion(RegionService service, String id, String localidad, String provincia,
            String comunidad) {
        Region nuevaRegion = new Region();
        nuevaRegion.setId(id);
        nuevaRegion.setLocalidad(localidad);
        nuevaRegion.setProvincia(provincia);
        nuevaRegion.setComunidadAutonoma(comunidad);

        service.save(nuevaRegion);
        NotificacionesConfig.crearNotificacionExito("¡Región creada!",
                "La región se ha creado con éxito.\nNueva región: " + nuevaRegion);
    }

    private static void editarRegion(Region region, RegionService service, String id, String localidad,
            String provincia, String comunidad) {
        region.setId(id);
        region.setLocalidad(localidad);
        region.setProvincia(provincia);
        region.setComunidadAutonoma(comunidad);

        service.save(region);
        NotificacionesConfig.crearNotificacionExito("¡Región editada!",
                "La región se ha editado con éxito.\nNueva región editada: " + region);
    }

    /*
     * Devuelve TRUE si alguno de los campos está vacío
     */
    private static boolean comprobarVacios(String id, String localidad, String provincia, String comunidad) {
        return id.isBlank() || localidad.isBlank() || provincia.isBlank() || comunidad.isBlank();
    }

    /*
     * Devuelve TRUE si el bloque ya existe en la base de datos
     */
    private static boolean comprobarId(String id, RegionService service) {
        return service.get(id).isPresent();
    }
}
