package es.uma.aedo.security;

import java.util.Optional;

import org.pac4j.core.context.CallContext;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.extractor.CredentialsExtractor;

public class AdminFormExtractor implements CredentialsExtractor{

    @Override
    public Optional<Credentials> extract(CallContext ctx) {
        String username = ctx.webContext().getRequestParameter("username").orElse(null);
        String password = ctx.webContext().getRequestParameter("password").orElse(null);;

        if(username != null && password != null){
            return Optional.of(new UsernamePasswordCredentials(username, password));
        } else {
            return Optional.empty();
        }
    }
}