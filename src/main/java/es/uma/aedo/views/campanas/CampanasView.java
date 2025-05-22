package es.uma.aedo.views.campanas;

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

import es.uma.aedo.data.entidades.Campanya;
import es.uma.aedo.services.CampanyaService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;

import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Campañas")
@Route("campañas")
@Menu(order = 1, icon = LineAwesomeIconUrl.CHART_BAR)
@Uses(Icon.class)
public class CampanasView extends Div {

    private Grid<Campanya> grid;

    private final CampanyaService campService;
    private Campanya campSeleccionada;

    public CampanasView(CampanyaService cService) {
        this.campService = cService;

        GestionCamp.Filters filters = new GestionCamp.Filters(() -> refreshGrid());
        grid = GestionCamp.crearGrid(campService, filters);
        grid.addItemClickListener(e -> {
            campSeleccionada = e.getItem();
        });

        VerticalLayout layout = new VerticalLayout(
            LayoutConfig.createTituloLayout("Campañas", ""),
            filters,
            grid,
            LayoutConfig.createButtons(
                () -> campSeleccionada, 
                "campaña", 
                "campañas", 
                campService, 
                grid
            ),
            crearBotonesGruposBloques()
        );

        layout.setAlignItems(Alignment.CENTER);
        add(layout);
    }

    private HorizontalLayout crearBotonesGruposBloques(){
        HorizontalLayout layout = new HorizontalLayout();

        Button grupos = BotonesConfig.crearBotonExtra("Ver grupos");
        Button bloques = BotonesConfig.crearBotonExtraSecundario("Ver bloques");

        grupos.addClickListener(e -> {
            if(campSeleccionada != null){
                getUI().ifPresent(ui -> ui.navigate("campañas/seleccionar-grupos/"+campSeleccionada.getId()));
            } else {
                NotificacionesConfig.crearNotificacionError("Selecciona una campaña", "No hay nignuna campaña seleccionada");
            }
        });

        bloques.addClickListener(e -> {
            if(campSeleccionada != null){
                getUI().ifPresent(ui -> ui.navigate("campañas/seleccionar-bloques/"+campSeleccionada.getId()));
            } else {
                NotificacionesConfig.crearNotificacionError("Selecciona una campaña", "No hay nignuna campaña seleccionada");
            }
        });

        layout.add(grupos, bloques);
        return layout;
    }


    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }
}
