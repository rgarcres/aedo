package es.uma.aedo.views.utilidades;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class NotificacionesConfig {
    public static void crearNotificacionError(String titulo, String text){
        Notification errorNoti = new Notification();
        H3 msg = new H3(titulo);
        Div texto = new Div(text);
        Button cerrar = new Button("Cerrar");
        VerticalLayout layout = new VerticalLayout();
        
        layout.setAlignItems(Alignment.CENTER);
        errorNoti.addThemeVariants(NotificationVariant.LUMO_ERROR);
        cerrar.getStyle().set("background-color", "#fe6a6a");
        cerrar.addClickListener(e -> {
            errorNoti.close();
        });
        layout.add(msg, texto, cerrar);
        errorNoti.add(layout);
        errorNoti.setPosition(Notification.Position.MIDDLE);
        errorNoti.open();
    }

    public static void crearNotificacionExito(String titulo, String text){
        Notification noti = new Notification();
        H3 msg = new H3(titulo);
        Div texto = new Div(text);
        Button cerrar = new Button("Cerrar");
        VerticalLayout layout = new VerticalLayout();
        
        layout.setAlignItems(Alignment.CENTER);
        noti.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        cerrar.getStyle().set("background-color", "#91ff9f");
        cerrar.addClickListener(e -> {
            noti.close();
        });
        layout.add(msg, texto, cerrar);
        noti.add(layout);
        noti.setPosition(Notification.Position.MIDDLE);
        noti.open();
    }

    // public static void crearNotificiacionConfirmacion(String titulo, String text){
    //     Notification noti = new Notification();
    //     H3 tit = new H3(titulo);
    //     Div texto = new Div(text);
    //     Button confirmar = new Button("Confirmar");
    //     Button cancelar = new Button("Cancelar");
    //     VerticalLayout layout = new VerticalLayout();
        
    //     layout.setAlignItems(Alignment.CENTER);
    //     noti.addThemeVariants(NotificationVariant.LUMO_WARNING);
    //     confirmar.getStyle().set("background-color", "#fff799");

    // }
}
