package es.uma.aedo.views.usuarios;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.data.entidades.Usuario;
import es.uma.aedo.services.UsuarioService;
import es.uma.aedo.views.utilidades.LayoutConfig;
import es.uma.aedo.views.utilidades.OtrasConfig;

@PageTitle("Editar usuario")
@Route("usuarios/editar-usuario")
public class EditarUsuarioView extends Div implements HasUrlParameter<String>{

    private final UsuarioService usuarioService;
    private Usuario usuario;

    public EditarUsuarioView(UsuarioService uService){
        this.usuarioService = uService;
    }

    @Override
    public void setParameter(BeforeEvent event, String id) {
        usuario = (Usuario) OtrasConfig.getEntidadPorParametro(id, usuarioService);
        if(usuario != null){
            setWidthFull();
            VerticalLayout layout = new VerticalLayout(
                LayoutConfig.createTituloLayout("Editar usuario: "+usuario.getAlias(), "usuarios"),
                GestionUsuario.crearCamposLayout(usuarioService, usuario)
            );
            
            layout.setAlignItems(Alignment.CENTER);
            add(layout);
        } else {
            add(LayoutConfig.createNotFoundLayout());
        }
    }
    
}
