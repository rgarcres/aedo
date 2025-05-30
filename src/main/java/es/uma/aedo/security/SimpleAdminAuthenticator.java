package es.uma.aedo.security;

import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.profile.CommonProfile;

import java.util.Optional;

import org.pac4j.core.context.CallContext;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.credentials.UsernamePasswordCredentials;

public class SimpleAdminAuthenticator implements Authenticator{

    @Override
    public Optional<Credentials> validate(CallContext ctx, Credentials rawCredentials) {
        UsernamePasswordCredentials credentials = (UsernamePasswordCredentials) rawCredentials;

        String username = credentials.getUsername();
        String password = credentials.getPassword();
        String admin = "admin";

        if(admin.equals(username) && admin.equals(password)){
            CommonProfile profile = new CommonProfile();
            profile.setId(username);
            profile.addAttribute("username", username);
            credentials.setUserProfile(profile);
            return Optional.of(credentials);
        } else {
            return Optional.empty();
        }
    }
    
}
