package es.uma.aedo.security;

import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    private static final Config CONFIG;

    static {
        AdminFormClient adminClient = new AdminFormClient();
        CONFIG = new Config(new Clients(adminClient));
    }

    public static Config getConfig() {
        return CONFIG;
    }
}
