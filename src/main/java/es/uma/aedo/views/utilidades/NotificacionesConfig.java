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
        Button close = new Button("Cerrar");
        VerticalLayout layout = new VerticalLayout();
        
        layout.setAlignItems(Alignment.CENTER);
        errorNoti.addThemeVariants(NotificationVariant.LUMO_ERROR);
        close.getStyle().set("background-color", "#fe6a6a");
        close.addClickListener(e -> {
            errorNoti.close();
        });
        layout.add(msg, texto, close);
        errorNoti.add(layout);
        errorNoti.setPosition(Notification.Position.MIDDLE);
        errorNoti.open();
    }

    public static void crearNotificacionExito(String titulo, String text){
        Notification noti = new Notification();
        H3 msg = new H3(titulo);
        Div texto = new Div(text);
        Button close = new Button("Cerrar");
        VerticalLayout layout = new VerticalLayout();
        
        layout.setAlignItems(Alignment.CENTER);
        noti.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        close.getStyle().set("background-color", "#91ff9f");
        close.addClickListener(e -> {
            noti.close();
        });
        layout.add(msg, texto, close);
        noti.add(layout);
        noti.setPosition(Notification.Position.MIDDLE);
        noti.open();
    }
}
