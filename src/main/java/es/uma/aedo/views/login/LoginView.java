package es.uma.aedo.views.login;

import java.util.Optional;

import org.pac4j.core.context.CallContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.profile.UserProfile;

import com.storedobject.vaadin.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;

import es.uma.aedo.security.AdminFormClient;
import es.uma.aedo.security.AdminSessionStore;
import es.uma.aedo.security.AdminWebContext;
import es.uma.aedo.views.utilidades.BotonesConfig;
import es.uma.aedo.views.utilidades.NotificacionesConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@PageTitle("Iniciar sesion")
@Route("iniciar-sesion")
public class LoginView extends Div {

    private final TextField usuarioField = new TextField("Usuario");
    private final PasswordField passField = new PasswordField("Contraseña");
    private final Button login = BotonesConfig.crearBotonPrincipal("Iniciar sesión");

    public LoginView() {

        setWidthFull();
        VerticalLayout layout = new VerticalLayout();
        H2 titulo = new H2("Iniciar Sesión");

        login.addClickListener(e -> {
            try {
                HttpServletRequest request = (HttpServletRequest) VaadinServletRequest.getCurrent().getHttpServletRequest();
                HttpServletResponse response = (HttpServletResponse) VaadinServletResponse.getCurrent().getHttpServletResponse();

                AdminWebContext context = new AdminWebContext(request, response);
                AdminSessionStore sessionStore = new AdminSessionStore();

                CallContext callContext = new CallContext(context, sessionStore);
                UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
                    usuarioField.getValue(), passField.getValue()
                );

                AdminFormClient client = new AdminFormClient();
                client.init();

                client.validateCredentials(callContext, credentials);
                Optional<UserProfile> profileOpt = client.getUserProfile(callContext, credentials);

                if (profileOpt.isPresent()) {
                    UserProfile profile = profileOpt.get();
                    sessionStore.set(context, UserProfile.class.getName(), profile);

                    NotificacionesConfig.notificar("Credenciales correctas!");
                    getUI().ifPresent(ui -> ui.navigate(""));
                } else {
                    NotificacionesConfig.notificar("Credenciales incorrectas");
                }
            } catch (Exception ex) {
                NotificacionesConfig.notificar("Credenciales incorrectas");
                System.out.println(ex.getMessage());
            }
        });

        layout.setAlignItems(Alignment.CENTER);
        layout.add(titulo, usuarioField, passField, login);
        add(layout);
    }
}
