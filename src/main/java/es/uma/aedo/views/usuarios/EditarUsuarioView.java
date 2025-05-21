package es.uma.aedo.views.usuarios;

import java.util.ArrayList;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import es.uma.aedo.data.entidades.Usuario;
import es.uma.aedo.services.RegionService;
import es.uma.aedo.services.UsuarioService;
import es.uma.aedo.views.utilidades.LayoutConfig;

@PageTitle("Editar usuario")
@Route("usuarios/editar-usuario")
public class EditarUsuarioView extends Div implements HasUrlParameter<String> {

    private final UsuarioService usuarioService;
    private final RegionService regionService;
    private Usuario usuario;

    public EditarUsuarioView(UsuarioService uService, RegionService rService) {
        this.usuarioService = uService;
        this.regionService = rService;
    }

    @Override
    public void setParameter(BeforeEvent event, String id) {
        // usuario = (Usuario) OtrasConfig.getEntidadPorParametro(id, usuarioService); 
        //Cambio el método get, por getConGrupo, que hace FETCH con el grupo del usuario
        if (id != null) {
            usuario = usuarioService.getConGrupo(id).get();
            if (usuario != null) {
                setWidthFull();
                Usuario aux = clonarUsuario(usuario);
                VaadinSession.getCurrent().setAttribute("usuarioEditar", aux);

                VerticalLayout layout = new VerticalLayout(
                    LayoutConfig.createTituloLayout("Editar usuario: " + usuario.getAlias(), "usuarios"),
                    GestionUsuario.crearCamposLayout(usuarioService, regionService, aux, true)
                );

                layout.setAlignItems(Alignment.CENTER);
                add(layout);
            } else {
                add(LayoutConfig.createNotFoundLayout());
            }
        } else {
            add(LayoutConfig.createNotFoundLayout());
        }

    }

    private Usuario clonarUsuario(Usuario original) {
        Usuario copia = new Usuario();
        copia.setId(original.getId());
        copia.setAlias(original.getAlias());
        copia.setFechaNacimiento(original.getFechaNacimiento());
        copia.setGenero(original.getGenero());
        copia.setNivelEstudios(original.getNivelEstudios());
        copia.setSituacionLaboral(original.getSituacionLaboral());
        copia.setSituacionPersonal(original.getSituacionPersonal());
        copia.setRegion(original.getRegion());
        copia.setGrupo(new ArrayList<>(original.getGrupo()));

        return copia;
    }

}
