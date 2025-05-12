package es.uma.aedo.views.utilidades;

import java.util.function.Supplier;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.AbstractEntity;
import es.uma.aedo.services.IService;

public class LayoutConfig {

    public static HorizontalLayout createMobileFilters(Div filters) {
        // Mobile version
        HorizontalLayout mobileFilters = new HorizontalLayout();
        mobileFilters.setWidthFull();
        mobileFilters.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER,
                LumoUtility.AlignItems.CENTER);
        mobileFilters.addClassName("mobile-filters");

        Icon mobileIcon = new Icon("lumo", "plus");
        Span filtersHeading = new Span("Filters");
        mobileFilters.add(mobileIcon, filtersHeading);
        mobileFilters.setFlexGrow(1, filtersHeading);
        mobileFilters.addClickListener(e -> {
            if (filters.getClassNames().contains("visible")) {
                filters.removeClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:plus");
            } else {
                filters.addClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:minus");
            }
        });
        return mobileFilters;
    }

    public static <T> HorizontalLayout createButtons(Supplier<? extends AbstractEntity> entitySupplier, String entityName, 
                                                     String mainRoute, IService<T> service, Grid<T> grid)
    {
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setWidthFull();
        buttonsLayout.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER, LumoUtility.AlignItems.CENTER);
        buttonsLayout.addClassName("buttons-layout");
        buttonsLayout.setAlignItems(Alignment.CENTER);
        String name = entityName.toLowerCase();

        Button crearButton = new Button("Añadir "+name);
        Button editarButton = new Button("Editar "+name);
        Button borrarButton = new Button("Borrar "+name);
        crearButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        editarButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        borrarButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        crearButton.addClickListener(e -> {
            crearButton.getUI().ifPresent(ui -> ui.navigate(mainRoute + "/crear-"+name));
        });

        editarButton.addClickListener(e -> {
            AbstractEntity entity = entitySupplier.get();
            if(entity != null){
                VaadinSession.getCurrent().setAttribute(name+"Editar", entity);
                editarButton.getUI().ifPresent(ui -> ui.navigate(mainRoute + "/editar-"+name));
            }
        });

        borrarButton.addClickListener(e -> {
            AbstractEntity entity = entitySupplier.get();
            if(entity != null){
                borrarEntidad(service, entity, grid);
            } else {
                NotificacionesConfig.crearNotificacionError("Seleccione una " + name, "No hay ninguna " 
                + name + " seleccionada, seleccione una " + name + " para poder borrarla");
            }
        });
        buttonsLayout.add(crearButton, editarButton, borrarButton);
        return buttonsLayout;
    }

    private static <T> void borrarEntidad(IService<T> service, AbstractEntity entity, Grid<T> grid){
        Notification noti = new Notification();
        H3 tit = new H3("Cofirme transacción");
        Div texto = new Div("¿Está seguro de querer borrar esta entidad?");
        Button confirmar = new Button("Confirmar");
        Button cancelar = new Button("Cancelar");
        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout botonesLayout = new HorizontalLayout();
                
        layout.setAlignItems(Alignment.CENTER);
        noti.addThemeVariants(NotificationVariant.LUMO_WARNING);
        confirmar.getStyle().set("background-color", "#fff799");

        confirmar.addClickListener(e -> {
            service.delete(entity.getId());
            grid.getDataProvider().refreshAll();
            NotificacionesConfig.crearNotificacionExito("¡Entidad eliminada!", 
            "La entidad" + entity.toString() + " ha sido eliminada con éxito");
            noti.close();
        });
        cancelar.addClickListener(e -> {
            noti.close();
        });
        botonesLayout.add(confirmar, cancelar);
        layout.add(tit, texto, botonesLayout);
        noti.add(layout);
        noti.setPosition(Notification.Position.MIDDLE);
        noti.open();
    }


}
