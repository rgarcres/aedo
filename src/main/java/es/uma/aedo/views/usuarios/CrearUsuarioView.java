package es.uma.aedo.views.usuarios;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.services.UsuarioService;
import es.uma.aedo.views.utilidades.LayoutConfig;

@PageTitle("Crear Usuario")
@Route("usuarios/crear-usuario")
public class CrearUsuarioView extends Div {
    private final UsuarioService usuarioService;

    public CrearUsuarioView(UsuarioService service) {
        this.usuarioService = service;
        setWidthFull();

        VerticalLayout layout = new VerticalLayout(
            LayoutConfig.createTituloLayout("Crear usuario", "usuarios"),
            GestionUsuario.crearCamposLayout(usuarioService, null)
        );
        
        layout.setAlignItems(Alignment.CENTER);
        add(layout);
    }
}
