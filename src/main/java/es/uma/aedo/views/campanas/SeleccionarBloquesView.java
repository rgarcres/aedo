package es.uma.aedo.views.campanas;


import java.time.LocalDateTime;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.Bloque;
import es.uma.aedo.data.entidades.BloqueProgramado;
import es.uma.aedo.data.entidades.Campanya;
import es.uma.aedo.services.BloqueService;
import es.uma.aedo.services.CampanyaService;
import es.uma.aedo.views.bloques.GestionBloque;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;

@PageTitle("Seleccionar bloques")
@Route("campañas/seleccionar-bloques")
public class SeleccionarBloquesView extends Div implements HasUrlParameter<String>{

    private final CampanyaService campService;
    private final BloqueService bloqueService;
    
    private Bloque bloqueSeleccionado; 
    private BloqueProgramado bpSeleccionado;
    private Campanya camp;

    private Grid<Bloque> grid;
    private Grid<BloqueProgramado> gridSeleccionados;


    public SeleccionarBloquesView(CampanyaService cService, BloqueService bService){
        this.campService = cService;
        this.bloqueService = bService;
    }

    @Override
    public void setParameter(BeforeEvent event, String id) {
        if(id != null){
            camp = campService.getConBloque(id).get();
            if(camp != null){
                setWidthFull();

                GestionBloque.Filters filters = new GestionBloque.Filters(() -> refreshGrid());

                grid = GestionBloque.crearGrid(bloqueService, filters);
                grid.addItemClickListener(e -> {
                    bloqueSeleccionado = e.getItem();
                });
                
                H3 subtitulo = new H3("Bloques seleccionados");

                VerticalLayout layout = new VerticalLayout(
                    LayoutConfig.createTituloLayout("Seleccionar bloques", "campañas"),
                    subtitulo,
                    crearGridSeleccionados(),
                    filters,
                    grid,
                    crearBotonesLayout()
                );

                layout.setAlignItems(Alignment.CENTER);
                add(layout);
            } else {
                add(LayoutConfig.createNotFoundLayout());
            }
        } else {
            add(LayoutConfig.createNotFoundLayout());
        }
    }

    private Component crearGridSeleccionados(){
        gridSeleccionados = new Grid<>(BloqueProgramado.class, false);
        gridSeleccionados.addColumn("bloque").setAutoWidth(true);
        gridSeleccionados.addColumn("fechaHora").setAutoWidth(true);

        gridSeleccionados.setItems(camp.getBloques());

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        gridSeleccionados.addItemClickListener(e -> {
            bpSeleccionado = e.getItem();
        });

        return gridSeleccionados;
    }

    private HorizontalLayout crearBotonesLayout(){
        HorizontalLayout layout = new HorizontalLayout();

        Button aplicar = BotonesConfig.crearBotonPrincipal("Aplicar");
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar", "campañas");
        Button anadir = BotonesConfig.crearBotonExtra("Añadir bloque");
        Button eliminar = BotonesConfig.crearBotonError("Eliminar bloque");

        aplicar.addClickListener(e -> {
            campService.save(camp);
            NotificacionesConfig.crearNotificacionExito("¡Bloque programados!", "Los bloques se han añadido a la campaña y han sido programados con éxito");
            aplicar.getUI().ifPresent(ui -> ui.navigate("campañas"));
        });

        anadir.addClickListener(e -> {
            if(bloqueSeleccionado != null){
                agregarTiempo(bloqueSeleccionado);
            }
        });

        eliminar.addClickListener(e -> {
            if(bpSeleccionado != null){
                camp.removeBloque(bpSeleccionado);
                gridSeleccionados.getDataProvider().refreshAll();
            }
        });


        layout.add(aplicar, cancelar, anadir, eliminar);
        return layout;
    }

    private void agregarTiempo(Bloque bloque){
        BloqueProgramado bp = new BloqueProgramado();

        Notification noti = new Notification();
        VerticalLayout mainLayout = new VerticalLayout();
        HorizontalLayout botonesLayout = new HorizontalLayout();

        H3 titulo = new H3("Seleccionar fecha y hora");
        DateTimePicker fechaHoraPicker = new DateTimePicker("Fecha y hora");
        fechaHoraPicker.setMin(LocalDateTime.now());
        fechaHoraPicker.addValueChangeListener(e -> {
            bp.setFechaHora(fechaHoraPicker.getValue());
        });

        Button aplicar = BotonesConfig.crearBotonPrincipal("Aplicar");
        Button cerrar = BotonesConfig.crearBotonError("Cerrar");

        aplicar.addClickListener(e -> {
            if(bp.getFechaHora() == null ){
                NotificacionesConfig.crearNotificacionError("Selecciona una fecha y hora", "No hay ninguna fecha y hora seleccionada");
            } else if(!comprobarBloque(bp)){
                NotificacionesConfig.crearNotificacionError("Bloque repetido", "No puede introducir dos veces el mismo bloque en la misma campaña");
            } else {
                bp.setId(bloque.getId() + camp.getId());
                bp.setBloque(bloque);
                camp.addBloque(bp);
                gridSeleccionados.getDataProvider().refreshAll();
                noti.close();
            }
        });

        cerrar.addClickListener(e -> {
            noti.close();
        });

        mainLayout.add(titulo, fechaHoraPicker, botonesLayout);
        botonesLayout.add(aplicar,cerrar);
        mainLayout.setAlignItems(Alignment.CENTER);
        noti.add(mainLayout);
        noti.setPosition(Position.MIDDLE);
        noti.open();

    }

    private boolean comprobarBloque(BloqueProgramado bp){
        for(BloqueProgramado b: camp.getBloques()){
            if(b.getBloque().equals(bp.getBloque())){
                return false;
            }
        }
        return true;
    }
    private void refreshGrid(){
        grid.getDataProvider().refreshAll();
    }
}
