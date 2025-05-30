package es.uma.aedo.security;

import org.pac4j.core.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public Config pac4jConfig(){
        AdminFormClient formClient = new AdminFormClient();
        formClient.setName("adminForm");
        
        return new Config(formClient);
    }
}
