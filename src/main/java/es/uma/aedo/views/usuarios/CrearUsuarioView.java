package es.uma.aedo.views.usuarios;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import es.uma.aedo.data.entidades.Usuario;
import es.uma.aedo.services.RegionService;
import es.uma.aedo.services.UsuarioService;
import es.uma.aedo.views.utilidades.LayoutConfig;

@PageTitle("Crear Usuario")
@Route("usuarios/crear-usuario")
public class CrearUsuarioView extends Div {
    private final UsuarioService usuarioService;
    private final RegionService regionService;
    private Usuario usuario;

    public CrearUsuarioView(UsuarioService service, RegionService rService) {
        this.usuarioService = service;
        this.regionService = rService;
        setWidthFull();

        if(VaadinSession.getCurrent().getAttribute("usuarioMedioCreado") != null){
            usuario = (Usuario) VaadinSession.getCurrent().getAttribute("usuarioMedioCreado");
        }

        VerticalLayout layout = new VerticalLayout(
            LayoutConfig.crearTituloLayout("Crear usuario", "usuarios"),
            GestionUsuario.crearCamposLayout(usuarioService, regionService, usuario, false)
        );

        layout.setAlignItems(Alignment.CENTER);
        add(layout);
    }

}
