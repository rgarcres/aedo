package es.uma.aedo.views.utilidades;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

public class BotonesConfig {
    public static Button crearBotonPrincipal(String nombre, String ruta){
        Button boton = new Button(nombre);
        boton.setWidth("min-content");
        boton.getStyle().set("cursor", "pointer");
        boton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        boton.getStyle().set("background-color", "#6654ff");
        boton.addClickListener(e -> boton.getUI().ifPresent(
            ui -> ui.navigate(ruta)   
           ));

        return boton;
    }
    public static Button crearBotonPrincipal(String nombre){
        Button boton = new Button(nombre);
        boton.setWidth("min-content");
        boton.getStyle().set("cursor", "pointer");
        boton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        boton.getStyle().set("background-color", "#6654ff");
        return boton;
    }

    public static Button crearBotonSecundario(String nombre, String ruta){
        Button boton = new Button(nombre);
        boton.setWidth("min-content");
        boton.getStyle().set("cursor", "pointer");
        boton.addClickListener(e -> boton.getUI().ifPresent(
            ui -> ui.navigate(ruta)   
           ));

        boton.getStyle().set("color", "#6654ff");
        return boton;
    }
    public static Button crearBotonSecundario(String nombre){
        Button boton = new Button(nombre);
        boton.setWidth("min-content");
        boton.getStyle().set("cursor", "pointer");

        boton.getStyle().set("color", "#6654ff");
        return boton;
    }

    public static Button crearBotonError(String nombre){
        Button boton = new Button(nombre);
        boton.setWidth("min-content");
        boton.getStyle().set("cursor", "pointer");

        boton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        return boton;
    }
}
