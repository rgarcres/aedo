package es.uma.aedo.views.usuarios;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.Region;
import es.uma.aedo.data.entidades.Usuario;
import es.uma.aedo.services.RegionService;
import es.uma.aedo.services.UsuarioService;
import es.uma.aedo.views.regiones.GestionRegion;
import es.uma.aedo.views.regiones.RegionesView;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;

@PageTitle("Seleccionar Region")
@Route("usuarios/seleccionar-region")
public class SeleccionarRegionView extends Div implements HasUrlParameter<String>{

    private Grid<Region> grid;

    private Usuario usuario;
    private Region regionSeleccionada;
    private final UsuarioService usuarioService;
    private final RegionService regionService;

    public SeleccionarRegionView(UsuarioService uService, RegionService rService){
        this.usuarioService = uService;
        this.regionService = rService;
    }

    @Override
    public void setParameter(BeforeEvent event, String id) {
        usuario = (Usuario) OtrasConfig.getEntidadPorParametro(id, usuarioService);
        if(usuario != null){
            setWidthFull();
            addClassName("usuarios-seleccionar-region-view");
            
            String route;
            if(usuario.getRegion() == null){
                route = "usuarios/crear-usuario";
            } else {
                route = "usuarios/editar-usuario/"+usuario.getId();
            }

            RegionesView.Filters filters = new RegionesView.Filters(() -> refreshGrid(), regionService);
            
            grid = GestionRegion.crearGrid(regionService, filters);
            grid.addItemClickListener(e -> {
                regionSeleccionada = e.getItem();
            });

            VerticalLayout layout = new VerticalLayout(
                LayoutConfig.createTituloLayout("Seleccionar region", route),
                filters,
                grid,
                crearBotonesLayout()
            );

            layout.setAlignItems(Alignment.CENTER);
            layout.setPadding(true);
            layout.setSpacing(true);
            add(layout);
        } else {
            add(LayoutConfig.createNotFoundLayout());
        }
    }

    private HorizontalLayout crearBotonesLayout(){
        HorizontalLayout layout = new HorizontalLayout();

        Button siguiente = BotonesConfig.crearBotonPrincipal("Siguiente");
        Button cancelar = BotonesConfig.crearBotonSecundario("Cancelar");

        siguiente.addClickListener(e -> {
            if(regionSeleccionada != null){
                usuario.setRegion(regionSeleccionada);
                usuarioService.save(usuario);
                siguiente.getUI().ifPresent(ui -> ui.navigate("usuarios/seleccionar-grupos/"+usuario.getId()));
            } else {
                NotificacionesConfig.crearNotificacionError("Selecciona una región", "No hay ninguna región seleccionada");
            }
        });

        cancelar.addClickListener(e -> {
            usuarioService.delete(usuario.getId());
            cancelar.getUI().ifPresent(ui -> ui.navigate("usuarios"));
        });

        layout.setAlignItems(Alignment.CENTER);
        layout.add(siguiente, cancelar);
        return layout;
    }
    
    private void refreshGrid(){
        grid.getDataProvider().refreshAll();
    }
}
