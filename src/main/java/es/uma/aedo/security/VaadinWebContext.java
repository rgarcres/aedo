package es.uma.aedo.security;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.pac4j.core.context.Cookie;
import org.pac4j.core.context.WebContext;

import com.vaadin.flow.server.VaadinSession;

public class VaadinWebContext implements WebContext {

    @Override
    public Optional<String> getRequestParameter(String name) {
        if ("username".equals(name)) {
            return Optional.ofNullable((String) VaadinSession.getCurrent().getAttribute("form-username"));
        } else if ("password".equals(name)) {
            return Optional.ofNullable((String) VaadinSession.getCurrent().getAttribute("form-password"));
        }
        return Optional.empty();
    }

    @Override
    public Map<String, String[]> getRequestParameters() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRequestParameters'");
    }

    @Override
    public Optional getRequestAttribute(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRequestAttribute'");
    }

    @Override
    public void setRequestAttribute(String name, Object value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setRequestAttribute'");
    }

    @Override
    public Optional<String> getRequestHeader(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRequestHeader'");
    }

    @Override
    public String getRequestMethod() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRequestMethod'");
    }

    @Override
    public String getRemoteAddr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRemoteAddr'");
    }

    @Override
    public void setResponseHeader(String name, String value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setResponseHeader'");
    }

    @Override
    public Optional<String> getResponseHeader(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getResponseHeader'");
    }

    @Override
    public void setResponseContentType(String content) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setResponseContentType'");
    }

    @Override
    public String getServerName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getServerName'");
    }

    @Override
    public int getServerPort() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getServerPort'");
    }

    @Override
    public String getScheme() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getScheme'");
    }

    @Override
    public boolean isSecure() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isSecure'");
    }

    @Override
    public String getFullRequestURL() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFullRequestURL'");
    }

    @Override
    public Collection<Cookie> getRequestCookies() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRequestCookies'");
    }

    @Override
    public void addResponseCookie(Cookie cookie) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addResponseCookie'");
    }

    @Override
    public String getPath() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPath'");
    }

}
