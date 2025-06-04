package es.uma.aedo.security;

import java.util.Optional;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class AdminSessionStore implements SessionStore{
    
    @Override
    public Optional<String> getSessionId(WebContext context, boolean createSession) {
        HttpServletRequest request = (HttpServletRequest) ((AdminWebContext) context).getRequest();
        HttpSession session = request.getSession(createSession);
        return Optional.ofNullable(session != null ? session.getId() : null);
    }

    @Override
    public Optional<Object> get(WebContext context, String key) {
        HttpSession session = getSession(((AdminWebContext) context).getRequest(), false);
        return Optional.ofNullable(session != null ? session.getAttribute(key) : null);
    }

    @Override
    public void set(WebContext context, String key, Object value) {
        HttpSession session = getSession(((AdminWebContext) context).getRequest(), true);
        if (session != null) {
            session.setAttribute(key, value);
        }
    }

    @Override
    public boolean destroySession(WebContext context) {
        HttpSession session = getSession(((AdminWebContext) context).getRequest(), false);
        if (session != null) {
            session.invalidate();
            return true;
        }
        return false;
    }

    @Override
    public Optional<Object> getTrackableSession(WebContext context) {
        return Optional.ofNullable(getSession(((AdminWebContext) context).getRequest(), false));
    }

    @Override
    public Optional<SessionStore> buildFromTrackableSession(WebContext context, Object trackableSession) {
        return Optional.of(this);
    }

    @Override
    public boolean renewSession(WebContext context) {
        HttpServletRequest request = ((AdminWebContext) context).getRequest();
        HttpSession session = getSession(request, false);
        if (session != null) {
            session.invalidate();
            request.getSession(true);
            return true;
        }
        return false;
    }

    private HttpSession getSession(HttpServletRequest request, boolean create) {
        return request.getSession(create);
    }
    
}
