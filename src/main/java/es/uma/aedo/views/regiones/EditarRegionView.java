package es.uma.aedo.views.regiones;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import es.uma.aedo.data.entidades.Region;
import es.uma.aedo.services.RegionService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;

@PageTitle("Editar Región")
@Route("regiones/editar-region")
public class EditarRegionView extends Div{
    
    private final RegionService regionService;
    private final Region regionEditar;

    public EditarRegionView(RegionService service){
        this.regionService = service;
        this.regionEditar = (Region) VaadinSession.getCurrent().getAttribute("regionEditar");
        setSizeFull();
        addClassNames("editar-regiones-view");

        VerticalLayout layout = new VerticalLayout(crearCamposRellenar());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

    private FormLayout crearCamposRellenar(){
        FormLayout layout = new FormLayout();
        
        TextField id = new TextField("ID*");
        TextField localidad = new TextField("Localidad*");
        TextField provincia = new TextField("Provincia*");
        TextField comunidad = new TextField("Comunidad Autonoma*");

        id.setValue(regionEditar.getId());
        localidad.setValue(regionEditar.getLocalidad());
        provincia.setValue(regionEditar.getProvincia());
        comunidad.setValue(regionEditar.getComunidadAutonoma());

        Button editarButton = BotonesConfig.crearBotonPrincipal("Confirmar Cambios");
        Button cancelarButton = BotonesConfig.crearBotonSecundario("Cancelar", "regiones");

        editarButton.addClickListener(e -> {
            editarRegion(regionService.getAll(), id.getValue(), localidad.getValue(), provincia.getValue(), comunidad.getValue());
        });

        layout.add(id, localidad, provincia, comunidad, editarButton, cancelarButton);

        layout.setResponsiveSteps(
            new ResponsiveStep("0", 1),
            new ResponsiveStep("500px", 2)
        );
        return layout;
    }

    private void editarRegion(List<Region> regiones, String id, String localidad, String provincia, String comunidad){
        //Si se ha puesto por error el ID de otra región
        if(regionService.get(id).isPresent() && !id.equals(regionEditar.getId())){
            NotificacionesConfig.crearNotificacionError("El ID ya existe","Introduzca un ID nuevo que sea único");
        } else if (!comprobarRegion(regiones, localidad, provincia, comunidad)){
            NotificacionesConfig.crearNotificacionError("La región ya existe", 
            "La región que está intentado introducir ya existe en la base de datos");
        } else {
            regionEditar.setId(id);
            regionEditar.setLocalidad(localidad);
            regionEditar.setProvincia(provincia);
            regionEditar.setComunidadAutonoma(comunidad);

            regionService.save(regionEditar);
            NotificacionesConfig.crearNotificacionExito("¡Región editada!", "Región editada con éxito. Nueva región: "+regionEditar);
            getUI().ifPresent(ui -> ui.navigate("regiones"));
        }
    }

    /*
     * Devuelve true si los atributos son válidos
    */
    private boolean comprobarRegion(List<Region> regiones, String localidad, String provincia, String comunidad){
        for(Region r: regiones){
            if(localidad.equalsIgnoreCase(r.getLocalidad()) && provincia.equalsIgnoreCase(r.getProvincia()) &&
                comunidad.equalsIgnoreCase(r.getComunidadAutonoma())){

                return false;
            }
        }
        return true;
    }
}
