package es.uma.aedo.views.grupos;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextField;

import es.uma.aedo.data.entidades.Grupo;
import es.uma.aedo.services.GrupoService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;

public class GestionGrupo {
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
            idField.setValue(grupo.getId());
            nombreField.setValue(grupo.getNombre());
            descripcionField.setValue(grupo.getDescripcion());
            crearEditar.setText("Editar grupo");
            // ------------Comportamiento de botones------------
            crearEditar.addClickListener(e -> {
                if (editar(service, grupo, idField.getValue(), nombreField.getValue(), descripcionField.getValue())) {
                    crearEditar.getUI().ifPresent(ui -> ui.navigate("grupos"));
                }
            });
        } else {
            crearEditar.addClickListener(e -> {
                if (crear(service, idField.getValue(), nombreField.getValue(), descripcionField.getValue())) {
                    crearEditar.getUI().ifPresent(ui -> ui.navigate("grupos"));
                }
            });
        }

        // ------------Añadir al layout------------
        camposLayout.add(idField, nombreField, descripcionField);
        camposLayout.setColspan(descripcionField, 2);
        botonesLayout.add(crearEditar, cancelar);
        layout.setAlignItems(Alignment.CENTER);
        layout.add(camposLayout, botonesLayout);
        return layout;
    }

    private static boolean crear(GrupoService service, String id, String nombre, String descripcion) {
        if (id.isBlank() || nombre.isBlank()) {
            NotificacionesConfig.crearNotificacionError("Campos vacios",
                    "Los campos ID y Nombre no pueden estar vacíos.");
            return false;
        } else if (service.get(id).isPresent()) {
            NotificacionesConfig.crearNotificacionError("El ID ya existe", "Introduzca un ID nuevo que sea único");
            return false;
        } else {
            Grupo g = new Grupo();
            g.setId(id);
            g.setNombre(nombre);
            g.setDescripcion(descripcion);
            service.save(g);
            NotificacionesConfig.crearNotificacionExito("¡Grupo creado!",
                    "El grupo se ha creado con éxito. Nuevo grupo: " + g);
            return true;
        }
    }

    private static boolean editar(GrupoService service, Grupo grupo, String id, String nombre, String descripcion) {
        if (id.isBlank() || nombre.isBlank()) {
            NotificacionesConfig.crearNotificacionError("Campos vacios",
                    "Los campos ID y Nombre no pueden estar vacíos.");
            return false;
        } else if (service.get(id).isPresent() && !id.equals(grupo.getId())) {
            NotificacionesConfig.crearNotificacionError("El ID ya existe", "Introduzca un ID nuevo que sea único");
            return false;
        } else {
            grupo.setId(id);
            grupo.setNombre(nombre);
            grupo.setDescripcion(descripcion);
            service.save(grupo);
            NotificacionesConfig.crearNotificacionExito("¡Grupo editado!",
                    "El grupo se ha editado con éxito. Nuevo grupo: " + grupo);
            return true;
        }
    }
}
