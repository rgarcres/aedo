package es.uma.aedo.views.campanas;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import es.uma.aedo.data.entidades.Campanya;
import es.uma.aedo.data.entidades.Grupo;
import es.uma.aedo.services.GrupoService;
import es.uma.aedo.views.grupos.GestionGrupo;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;

@PageTitle("Seleccionar grupos")
@Route("campañas/seleccionar-grupos")
public class SeleccionarGruposView extends Div{
    
    private Grid<Grupo> grid;
    private Campanya camp;
    private String ruta;
    private boolean editar;

    private final GrupoService grupoService;

    public SeleccionarGruposView(GrupoService gService){
        this.grupoService = gService;
        setWidthFull();

        if(VaadinSession.getCurrent().getAttribute("campMedioCreada") != null){
            camp = (Campanya) VaadinSession.getCurrent().getAttribute("campMedioCreada");
            ruta = "campañas/crear-campaña";
            editar = false;
        } else if(VaadinSession.getCurrent().getAttribute("campMedioEditada") != null){
            camp = (Campanya) VaadinSession.getCurrent().getAttribute("campMedioEditada");
            ruta = "campañas/editar-campaña";
            editar = true;
        }

        if(camp != null){
            if(camp.getGrupos() != null){
                for(Grupo g: camp.getGrupos()){
                    grid.select(g);
                }
            }
        }

        GestionGrupo.Filters filters = new GestionGrupo.Filters(() -> refreshGrid());
        grid = GestionGrupo.createGrid(grupoService, filters);
        grid.setSelectionMode(SelectionMode.MULTI);

        VerticalLayout layout = new VerticalLayout(
            LayoutConfig.createTituloLayout("Selccionar grupos", ruta),
            filters,
            grid,
            crearBotonesLayout()
        );

        add(layout);
    }

    private HorizontalLayout crearBotonesLayout(){
        HorizontalLayout layout = new HorizontalLayout();

        Button siguiente = BotonesConfig.crearBotonPrincipal("Siguiente");
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar");
        
        siguiente.addClickListener(e -> {
            if(!grid.getSelectedItems().isEmpty()){
                List<Grupo> grupos = new ArrayList<>(grid.getSelectedItems());
                camp.setGrupos(grupos);

                if(editar){  
                    VaadinSession.getCurrent().setAttribute("campMedioEditada", camp);
                } else {
                    VaadinSession.getCurrent().setAttribute("campMedioCreada", camp);
                }
            } else {
                NotificacionesConfig.crearNotificacionError("Selecciona un grupo", "No hay ningún grupo seleccionado");
            }
        });

        cancelar.addClickListener(e -> {
            if(VaadinSession.getCurrent().getAttribute("campMedioCreada") != null){
                VaadinSession.getCurrent().setAttribute("campMedioCreada", null);
            }
            if(VaadinSession.getCurrent().getAttribute("campMedioEditada") != null){
                VaadinSession.getCurrent().setAttribute("campMedioEditada", null);
            }

            cancelar.getUI().ifPresent(ui -> ui.navigate("campañas"));
        });

        layout.add(siguiente, cancelar);
        return layout;
    }

    private void refreshGrid(){
        grid.getDataProvider().refreshAll();
    }
}
