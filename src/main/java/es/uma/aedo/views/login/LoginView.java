package es.uma.aedo.views.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pac4j.core.context.CallContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.jee.context.JEEContext;
import org.pac4j.jee.context.session.JEESessionStoreFactory;

import com.storedobject.vaadin.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uma.aedo.security.AdminFormClient;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;

@PageTitle("Iniciar sesion")
@Route("iniciar-sesion")
public class LoginView extends Div {

    private final TextField usuarioField = new TextField("Usuario");
    private final PasswordField passField = new PasswordField("Contraseña");
    private final Button login = BotonesConfig.crearBotonPrincipal("Iniciar sesión");

    public LoginView(HttpServletRequest request, HttpServletResponse response) {
        
        setWidthFull();
        VerticalLayout layout = new VerticalLayout();
        H2 titulo = new H2("Iniciar Sesión");

        login.addClickListener(e -> {
            var context = new JEEContext(request, response);
            SessionStore sessionStore = new JEESessionStoreFactory().newSessionStore();

            CallContext callContext = new CallContext(context, sessionStore);
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
                usuarioField.getValue(), passField.getValue()
            );
            
            try {
                AdminFormClient client = new AdminFormClient();
                client.init();

                client.validateCredentials(callContext, credentials);
                var profile = client.getUserProfile(callContext, credentials);

                sessionStore.set(context, UserProfile.class.getName(), profile);
                getUI().ifPresent(ui -> ui.navigate("home"));
            } catch(Exception ex){
                NotificacionesConfig.notificar("Credenciales incorrectas");
            }
    

        });

        layout.setAlignItems(Alignment.CENTER);
        layout.add(titulo, usuarioField, passField, login);
        add(layout);
    }
}
