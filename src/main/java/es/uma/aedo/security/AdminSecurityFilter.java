package es.uma.aedo.security;


import org.pac4j.jee.filter.SecurityFilter;

import jakarta.servlet.annotation.WebFilter;

@WebFilter(urlPatterns = {"/*"})
public class AdminSecurityFilter extends SecurityFilter {

    public AdminSecurityFilter(){
        super(SecurityConfig.getConfig(), "adminFormClient");
    }
    
}
