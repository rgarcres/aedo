package es.uma.aedo.views.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pac4j.core.config.Config;
import org.pac4j.core.context.WebContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.storedobject.vaadin.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.views.utilidades.BotonesConfig;

@PageTitle("Iniciar sesion")
@Route("")
public class LoginView extends Div{

    private final TextField usuarioField = new TextField("Usuario");
    private final TextField passField = new TextField("Contraseña");
    private final Button login = BotonesConfig.crearBotonPrincipal("Iniciar sesión");

    public LoginView(Config config, HttpServletRequest req, HttpServletResponse resp) {
        
        setWidthFull();
        VerticalLayout layout = new VerticalLayout();
        H2 titulo = new H2("Iniciar Sesión");

        login.addClickListener(e -> doLogin());

        layout.setAlignItems(Alignment.CENTER);
        layout.add(titulo, usuarioField, passField, login);
        add(layout);
    }

    private void doLogin() {
        WebContext context;
    }
}
