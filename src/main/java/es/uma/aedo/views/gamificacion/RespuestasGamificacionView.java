package es.uma.aedo.views.gamificacion;

import java.util.ArrayList;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import es.uma.aedo.data.entidades.PreguntaGamificacion;
import es.uma.aedo.services.GamificacionService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;

@PageTitle("Respuestas")
@Route("gamificacion/respuestas")
public class RespuestasGamificacionView extends Div implements HasUrlParameter<String> {

    private Grid<String> grid;
    private TextField respuestaField = new TextField("Posible respuesta");
    private Text respuestaCorrecta = new Text("Respuesta correcta: ");

    private String respuestaSeleccionada;

    private PreguntaGamificacion pg;
    private final GamificacionService gamificacionService;

    public RespuestasGamificacionView(GamificacionService gService) {
        this.gamificacionService = gService;
    }

    @Override
    public void setParameter(BeforeEvent event, String id) {
        pg = (PreguntaGamificacion) OtrasConfig.getEntidadPorParametro(id, gamificacionService);
        if (pg != null) {
            setWidthFull();
            if(pg.getPosiblesRespuestas() == null){
                pg.setPosiblesRespuestas(new ArrayList<>());
            }

            if (pg.getRespuestaCorrecta() != null) {
                respuestaCorrecta.setText("Respuesta correcta: " + pg.getRespuestaCorrecta());
            }
            add(crearLayout());
        } else {
            add(LayoutConfig.crearNotFoundLayout());
        }
    }

    private VerticalLayout crearLayout() {
        VerticalLayout layout = new VerticalLayout();

        respuestaField.addKeyPressListener(Key.ENTER, e -> {
            if (!respuestaField.getValue().isBlank()) {
                pg.addRespuesta(respuestaField.getValue());
                refreshGrid();
            }
        });

        layout.add(
            LayoutConfig.crearTituloLayout("Respuestas " + pg.getId(), "gamificacion"),
            respuestaCorrecta,
            respuestaField,
            crearGrid(),
            crearBotonesLayout(),
            crearBotonesGuardarLayout()
        );

        layout.setAlignItems(Alignment.CENTER);
        layout.setPadding(true);
        layout.setSpacing(true);
        return layout;
    }

    private Component crearGrid() {
        grid = new Grid<>(String.class, false);
        grid.addColumn(ress -> ress).setHeader("Respuestas").setAutoWidth(true);
        grid.addComponentColumn(res -> {
            Button correcta = BotonesConfig.crearBotonSecundario("Respuesta correcta");
            correcta.addClickListener(e -> {
                pg.setRespuestaCorrecta(res);
                NotificacionesConfig.notificar("Respusta correcta: "+ res);
                respuestaCorrecta.setText("Respuesta correcta: " + res);
            });
            return correcta;
        }).setAutoWidth(true);
        grid.setItems(pg.getPosiblesRespuestas());

        grid.addItemClickListener(e -> {
            respuestaSeleccionada = e.getItem();
        });

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }

    private HorizontalLayout crearBotonesLayout() {
        HorizontalLayout layout = new HorizontalLayout();

        Button anadir = BotonesConfig.crearBotonExtra("Añadir respuesta");
        Button eliminar = BotonesConfig.crearBotonError("Eliminar respuesta");

        anadir.addClickListener(e -> {
            if (!respuestaField.getValue().isBlank()) {
                pg.addRespuesta(respuestaField.getValue());
                refreshGrid();
            }
        });

        eliminar.addClickListener(e -> {
            if (respuestaSeleccionada != null) {
                pg.removeRespuesta(respuestaSeleccionada);
                refreshGrid();
                if(respuestaSeleccionada.equals(pg.getRespuestaCorrecta())){
                    pg.setRespuestaCorrecta("");
                }
            }
        });

        layout.add(anadir, eliminar);
        return layout;
    }

    private HorizontalLayout crearBotonesGuardarLayout() {
        HorizontalLayout layout = new HorizontalLayout();

        Button aplicar = BotonesConfig.crearBotonPrincipal("Aplicar");
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar", "gamificacion");

        aplicar.addClickListener(e -> {
            gamificacionService.save(pg);
            getUI().ifPresent(ui -> ui.navigate("gamificacion"));
        });

        layout.add(aplicar, cancelar);
        return layout;
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }
}
