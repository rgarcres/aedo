package es.uma.aedo.security;

import org.pac4j.core.client.DirectClient;

public class AdminFormClient extends DirectClient{

    @Override
    protected void internalInit(boolean forceReinit) {
        setCredentialsExtractor(new AdminFormExtractor());
        setAuthenticator(new SimpleAdminAuthenticator());
        setName("adminFormClient");
    }
}
