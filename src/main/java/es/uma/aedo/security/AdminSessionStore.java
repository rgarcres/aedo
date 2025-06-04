package es.uma.aedo.security;

import java.util.Optional;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class AdminSessionStore implements SessionStore{
    
    private HttpServletRequest getRequest(WebContext context) {
        if (context instanceof AdminWebContext adminContext) {
            return adminContext.getRequest();
        }
        throw new IllegalArgumentException("Expected AdminWebContext");
    }
    
    @Override
    public Optional<String> getSessionId(WebContext context, boolean createSession) {
        HttpSession session = getRequest(context).getSession(createSession);
        return session != null ? Optional.of(session.getId()) : Optional.empty();
    }

    @Override
    public Optional<Object> get(WebContext context, String key) {
        HttpSession session = getRequest(context).getSession(false);
        return session != null ? Optional.ofNullable(session.getAttribute(key)) : Optional.empty();
    }

    @Override
    public void set(WebContext context, String key, Object value) {
        HttpSession session = getRequest(context).getSession(true);
        session.setAttribute(key, value);
    }

    @Override
    public boolean destroySession(WebContext context) {
        HttpSession session = getRequest(context).getSession(false);
        if (session != null) {
            session.invalidate();
            return true;
        }
        return false;
    }

    @Override
    public Optional<Object> getTrackableSession(WebContext context) {
        return get(context, "TRACKABLE_SESSION");
    }

    @Override
    public Optional<SessionStore> buildFromTrackableSession(WebContext context, Object trackableSession) {
        return Optional.of(this);
    }

    @Override
    public boolean renewSession(WebContext context) {
        HttpServletRequest request = getRequest(context);
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            var attributes = oldSession.getAttributeNames();
            var values = new java.util.HashMap<String, Object>();
            while (attributes.hasMoreElements()) {
                String name = attributes.nextElement();
                values.put(name, oldSession.getAttribute(name));
            }
            oldSession.invalidate();
            HttpSession newSession = request.getSession(true);
            for (var entry : values.entrySet()) {
                newSession.setAttribute(entry.getKey(), entry.getValue());
            }
            return true;
        }
        return false;
    }
    
}
