package es.uma.aedo.views.bloques;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import es.uma.aedo.data.entidades.Bloque;
import es.uma.aedo.services.BloqueService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;

public class CrearEditarBloque {
        
    public static VerticalLayout crearCamposLayout(Bloque bloque, BloqueService bloqueService){
        //------------Atributos------------
        VerticalLayout mainLayout = new VerticalLayout();
        FormLayout camposLayout = new FormLayout();
        TextField idField = new TextField("ID*");
        TextField nombreField = new TextField("Nombre*");
        TextField descripcionField = new TextField("Descripcion*");
        HorizontalLayout botonesLayout = new HorizontalLayout();
        Button crearButton;
        if(bloque != null){
            crearButton = BotonesConfig.crearBotonPrincipal("Editar bloque");
        } else {
            crearButton = BotonesConfig.crearBotonPrincipal("Crear bloque");
        }
        Button cancelarButton = BotonesConfig.crearBotonSecundario("Cancelar", "bloques");

        //------------Instanciar valores de los TextField------------
        if(bloque != null){
            idField.setValue(bloque.getId());
            nombreField.setValue(bloque.getNombre());
            descripcionField.setValue(bloque.getDescripcion());
        }

        //------------Comportamiento boton------------
        crearButton.addClickListener(e -> {
            String id = idField.getValue();
            String nombre = nombreField.getValue();
            String descripcion = descripcionField.getValue();
            boolean exito = false;

            if(bloque == null){
                exito = crearBloque(bloqueService, id, nombre, descripcion);
            } else {
                exito = editarBloque(bloque, bloqueService, id, nombre, descripcion);
            }

            if(exito){
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
    private static boolean crearBloque(BloqueService service, String id, String nombre, String descripcion){
        //Comprobar que ninguno de los campos está vacío
        if(comprobarVacios(id, nombre, descripcion)){
            NotificacionesConfig.crearNotificacionError("Campos vacíos", "Ninguno de los campos puede estar vacío");
            return false;
        //Comprobar que si el ID ha sido cambiado, no está ya en la base de datos
        } else if(comprobarId(id, service)){
            NotificacionesConfig.crearNotificacionError("El ID ya existe","Introduzca un ID nuevo que sea único");
            return false;
        //Editar el bloque
        } else {
            Bloque nuevoBloque = new Bloque();
            nuevoBloque.setId(id);
            nuevoBloque.setNombre(nombre);
            nuevoBloque.setDescripcion(descripcion);
            service.save(nuevoBloque);

            NotificacionesConfig.crearNotificacionExito("¡Bloque creado!", "El bloque se ha creado con éxito.\nNuevo bloque: "+nuevoBloque);
            return true;
        }
    }
    /*
     * Método que edita un bloque y devuelve true si se ha editado con éxito
     */
    private static boolean editarBloque(Bloque bloque, BloqueService service, String id, String nombre, String descripcion ){
        //Comprobar que ninguno de los campos está vacío
        if(comprobarVacios(id, nombre, descripcion)){
            NotificacionesConfig.crearNotificacionError("Campos vacíos", "Ninguno de los campos puede estar vacío");
            return false;
        //Comprobar que si el ID ha sido cambiado, no está ya en la base de datos
        } else if(comprobarId(id, service) && !id.equals(bloque.getId())){
            NotificacionesConfig.crearNotificacionError("El ID ya existe","Introduzca un ID nuevo que sea único");
            return false;
        //Editar el bloque
        } else {
            bloque.setId(id);
            bloque.setNombre(nombre);
            bloque.setDescripcion(descripcion);
            service.save(bloque);

            NotificacionesConfig.crearNotificacionExito("¡Bloque editado!", "El bloque se ha editado con éxito.\nNuevo bloque editado: "+bloque);
            return true;
        }
    }

    /*
     * Devuelve TRUE si alguno de los campos está vacío
     */
    private static boolean comprobarVacios(String id, String nombre, String descripcion){
        return id.isBlank() || nombre.isBlank() || descripcion.isBlank();
    }

    /*
    * Devuelve TRUE si el bloque ya existe en la base de datos
    */
    private static boolean comprobarId(String id, BloqueService service){
        return service.get(id).isPresent();
    }
}
