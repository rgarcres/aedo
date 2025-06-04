package es.uma.aedo.security;

import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.springframework.context.annotation.Bean;
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
    
    @Bean
    public AdminFormClient adminFormClient() {
        AdminFormClient client = new AdminFormClient();
        client.init(); // Aquí sí se llama una vez para inicializar correctamente
        return client;
    }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http
    //     .authorizeHttpRequests(auth -> auth
    //         .requestMatchers("/iniciar-sesion", "/css/**", "/js/**").permitAll()
    //         .anyRequest().authenticated()
    //     )
    //     .formLogin(form -> form
    //         .loginPage("/iniciar-sesion") // <- Tu vista de login personalizada
    //         .permitAll()
    //     )
    //     .logout(logout -> logout
    //         .logoutSuccessUrl("/iniciar-sesion?logout").permitAll()
    //     );

    //     return http.build();
    // }
}
