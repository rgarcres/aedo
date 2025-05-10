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

import es.uma.aedo.data.entidades.Region;
import es.uma.aedo.services.RegionService;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;

@PageTitle("Crear Region")
@Route("regiones/crear-region")
public class CrearRegionView extends Div{
    
    private final RegionService regionService;

    public CrearRegionView(RegionService service) {
        this.regionService = service;
        setSizeFull();
        addClassNames("crear-regiones-view");

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

        Button crearButton = BotonesConfig.crearBotonPrincipal("Añadir");
        Button cancelarButton = BotonesConfig.crearBotonSecundario("Cancelar", "regiones");

        crearButton.addClickListener(e -> {
            System.out.println("Boton pulsado");
            crearNuevaRegion(id.getValue(), localidad.getValue(), 
                             provincia.getValue(), comunidad.getValue());
        });

        layout.add(id, localidad, provincia, comunidad, crearButton, cancelarButton);

        layout.setResponsiveSteps(
            new ResponsiveStep("0", 1),
            new ResponsiveStep("500px", 2)
        );
        return layout;
    }

    /*
     * Método para crear una nueva región comprobando que están todos los campos llenos,
     * que el ID no esté ya en la BD y que la localidad no esté ya introducida
    */
    private void crearNuevaRegion(String id, String localidad, String provincia, String comunidad){
        List<Region> regiones = regionService.getAll();
        
        //Ninguno de los campos están vacíos
        if(id.isBlank() || localidad.isBlank() || provincia.isBlank() || comunidad.isBlank()){
            NotificacionesConfig.crearNotificacionError("Campos vacíos", 
                                "Ninguno de los campos puede estar vacío");
        } else if(regionService.get(id).isPresent()){
            NotificacionesConfig.crearNotificacionError("El ID ya existe","Introduzca un ID nuevo que sea único");
        } else if (!comprobarRegion(regiones, localidad, provincia, comunidad)){
            NotificacionesConfig.crearNotificacionError("Localidad ya existe", 
            "La localidad que está intentado introducir ya existe en la base de datos");
        } else {
            Region nuevaRegion = new Region();
            nuevaRegion.setId(id);
            nuevaRegion.setLocalidad(localidad);
            nuevaRegion.setProvincia(provincia);
            nuevaRegion.setComunidadAutonoma(comunidad);

            regionService.save(nuevaRegion);
            NotificacionesConfig.crearNotificacionExito("¡Región creada!", "Región "+nuevaRegion+" creada con éxito");
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
