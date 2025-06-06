package es.uma.aedo.views.bloques;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import es.uma.aedo.data.entidades.Bloque;
import es.uma.aedo.services.BloqueService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Bloques")
@Route("bloques")
@Menu(order = 5, icon = LineAwesomeIconUrl.CLIPBOARD_LIST_SOLID)
@Uses(Icon.class)
public class BloquesView extends Div {

    private Grid<Bloque> grid;

    private final BloqueService bloqueService;
    private Bloque bloqueSeleccionado;

    public BloquesView(BloqueService service) {
        this.bloqueService = service;
        setSizeFull();
        addClassNames("bloques-view");

        GestionBloque.Filters filters = new GestionBloque.Filters(() -> refreshGrid());

        grid = GestionBloque.crearGrid(bloqueService, filters);
        grid.addItemClickListener(e -> {
            bloqueSeleccionado = e.getItem();
        });
        
        VerticalLayout layout = new VerticalLayout(
            LayoutConfig.crearTituloLayout("Bloques", ""),
            filters, 
            grid, 
            LayoutConfig.createButtons(() -> bloqueSeleccionado, "Bloque", 
            "bloques", this.bloqueService, grid), 
            crearBotonesPregunta()
        );

        layout.setSizeFull();
        layout.setAlignItems(Alignment.CENTER);
        layout.setPadding(true);
        layout.setSpacing(false);
        add(layout);
    }



    /*
     * Layout que muestra los botones correspondientes a la navegación de las preguntas
     * de cada bloque
     */
    private HorizontalLayout crearBotonesPregunta(){
        HorizontalLayout layout = new HorizontalLayout();
        Button verPreguntas = BotonesConfig.crearBotonExtra("Ver preguntas");
        Button todasPreguntas = BotonesConfig.crearBotonExtraSecundario("Ver todas las preguntas");

        verPreguntas.addClickListener(e -> {
            if(bloqueSeleccionado != null){
                getUI().ifPresent(ui -> ui.navigate("preguntas-bloque/"+bloqueSeleccionado.getId()));
            } else {
                NotificacionesConfig.crearNotificacionError("Selecciona un bloque", "No hay ningún bloque seleccionado");
            }
        });

        todasPreguntas.addClickListener(e-> {
            /*
             * Atributo crudPregunta:
             *      false: NO activa los botones CRUD de la entidad pregunta
             *      true: activa los botones necesarios para CRUD de preguntas
             */
            VaadinSession.getCurrent().setAttribute("crudPregunta", true);
            todasPreguntas.getUI().ifPresent(ui -> ui.navigate("preguntas"));
        });
        
        layout.add(verPreguntas, todasPreguntas);

        return layout;
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

}
