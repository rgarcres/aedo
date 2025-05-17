package es.uma.aedo.views.usuarios;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.Usuario;
import es.uma.aedo.services.RegionService;
import es.uma.aedo.services.UsuarioService;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;

@PageTitle("Seleccionar Region")
@Route("usuarios/seleccionar-region")
public class SeleccionarRegionView extends Div implements HasUrlParameter<String>{

    private Usuario usuario;
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
            add("Seleccionar region");
        } else {
            add(LayoutConfig.createNotFoundLayout());
        }
    }
    
}
