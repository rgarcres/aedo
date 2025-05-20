package es.uma.aedo.views.usuarios;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.Usuario;
import es.uma.aedo.services.UsuarioService;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;

@PageTitle("Crear Usuario")
@Route("usuarios/crear-usuario")
public class CrearUsuarioView extends Div implements HasUrlParameter<String>{
    private final UsuarioService usuarioService;
    private Usuario usuario;
    
    public CrearUsuarioView(UsuarioService service) {
        this.usuarioService = service;
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String id) {
        usuario = (Usuario) OtrasConfig.getEntidadPorParametro(id, usuarioService);
        setWidthFull();

        VerticalLayout layout = new VerticalLayout(
            LayoutConfig.createTituloLayout("Crear usuario", "usuarios"),
            GestionUsuario.crearCamposLayout(usuarioService, usuario, false)
        );
        
        layout.setAlignItems(Alignment.CENTER);
        add(layout);
    }
}
