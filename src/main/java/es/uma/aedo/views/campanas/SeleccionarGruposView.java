package es.uma.aedo.views.campanas;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.Campanya;
import es.uma.aedo.data.entidades.Grupo;
import es.uma.aedo.services.CampanyaService;
import es.uma.aedo.services.GrupoService;
import es.uma.aedo.views.grupos.GestionGrupo;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;

@PageTitle("Seleccionar grupos")
@Route("campañas/seleccionar-grupos")
public class SeleccionarGruposView extends Div implements HasUrlParameter<String>{
    
    private Grid<Grupo> grid;
    private Campanya camp;
    private String ruta;

    private final GrupoService grupoService;
    private final CampanyaService campService;

    public SeleccionarGruposView(CampanyaService cService, GrupoService gService){
        this.campService = cService;
        this.grupoService = gService;
    }

    @Override
    public void setParameter(BeforeEvent event, String id) {
        if(id != null){
            camp = campService.getConGrupo(id).get();
            if(camp != null){
                setWidthFull();

                GestionGrupo.Filters filters = new GestionGrupo.Filters(() -> refreshGrid());
                grid = GestionGrupo.createGrid(grupoService, filters);
                grid.setSelectionMode(SelectionMode.MULTI);

                if(camp != null && camp.getGrupos() != null){
                    for(Grupo g: camp.getGrupos()){
                        grid.select(g);
                    }
                }

                VerticalLayout layout = new VerticalLayout(
                    LayoutConfig.createTituloLayout("Seleccionar grupos", ruta),
                    filters,
                    grid,
                    crearBotonesLayout()
                );

                layout.setAlignItems(Alignment.CENTER);
                add(layout);
            } else {
                add(LayoutConfig.crearNotFoundLayout());
            }
        } else {
           add(LayoutConfig.crearNotFoundLayout());
        }

    }

    private HorizontalLayout crearBotonesLayout(){
        HorizontalLayout layout = new HorizontalLayout();

        Button aplicar = BotonesConfig.crearBotonPrincipal("Aplicar");
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar", "campañas");
        
        aplicar.addClickListener(e -> {
            if(!grid.getSelectedItems().isEmpty()){
                List<Grupo> grupos = new ArrayList<>(grid.getSelectedItems());
                camp.setGrupos(grupos);
                campService.save(camp);

                getUI().ifPresent(ui -> ui.navigate("campañas"));
            } else {
                NotificacionesConfig.crearNotificacionError("Selecciona un grupo", "No hay ningún grupo seleccionado");
            }
        });

        layout.add(aplicar, cancelar);
        return layout;
    }

    private void refreshGrid(){
        grid.getDataProvider().refreshAll();
    }
}
