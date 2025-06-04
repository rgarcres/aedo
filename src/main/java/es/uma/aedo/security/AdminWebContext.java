package es.uma.aedo.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.pac4j.core.context.Cookie;
import org.pac4j.core.context.WebContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AdminWebContext implements WebContext {
    private final HttpServletRequest httpRequest;
    private final HttpServletResponse httpResponse;

    public AdminWebContext(HttpServletRequest request, HttpServletResponse response) {
        this.httpRequest = request;
        this.httpResponse = response;
    }

    public HttpServletRequest getRequest(){
        return httpRequest;
    }

    public HttpServletResponse getResponse() {
        return httpResponse;
    }

    @Override
    public Optional<String> getRequestParameter(String name) {
        return Optional.ofNullable(httpRequest.getParameter(name));
    }

    @Override
    public Map<String, String[]> getRequestParameters() {
        return httpRequest.getParameterMap();
    }

    @Override
    public Optional<Object> getRequestAttribute(String name) {
        return Optional.ofNullable(httpRequest.getAttribute(name));
    }

    @Override
    public void setRequestAttribute(String name, Object value) {
        httpRequest.setAttribute(name, value);
    }

    @Override
    public Optional<String> getRequestHeader(String name) {
        return Optional.ofNullable(httpRequest.getHeader(name));
    }

    @Override
    public String getRequestMethod() {
        return httpRequest.getMethod();
    }

    @Override
    public String getRemoteAddr() {
        return httpRequest.getRemoteAddr();
    }

    @Override
    public void setResponseHeader(String name, String value) {
        httpResponse.setHeader(name, value);
    }

    @Override
    public Optional<String> getResponseHeader(String name) {
        return Optional.ofNullable(httpResponse.getHeader(name));
    }

    @Override
    public void setResponseContentType(String content) {
        httpResponse.setContentType(content);
    }

    @Override
    public String getServerName() {
        return httpRequest.getServerName();
    }

    @Override
    public int getServerPort() {
        return httpRequest.getServerPort();
    }

    @Override
    public String getScheme() {
        return httpRequest.getScheme();
    }

    @Override
    public boolean isSecure() {
        return httpRequest.isSecure();
    }

    @Override
    public String getFullRequestURL() {
        StringBuffer url = httpRequest.getRequestURL();
        String queryString = httpRequest.getQueryString();
        if (queryString != null) {
            url.append("?").append(queryString);
        }
        return url.toString();
    }

    @Override
    public Collection<Cookie> getRequestCookies() {
        jakarta.servlet.http.Cookie[] servletCookies = httpRequest.getCookies();
        if (servletCookies == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(servletCookies)
                .map(c -> new Cookie(c.getName(), c.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public void addResponseCookie(Cookie cookie) {
        jakarta.servlet.http.Cookie servletCookie = new jakarta.servlet.http.Cookie(cookie.getName(),
                cookie.getValue());
        servletCookie.setMaxAge(cookie.getMaxAge());
        servletCookie.setPath(cookie.getPath());
        servletCookie.setSecure(cookie.isSecure());
        servletCookie.setHttpOnly(cookie.isHttpOnly());
        if (cookie.getDomain() != null) {
            servletCookie.setDomain(cookie.getDomain());
        }
        httpResponse.addCookie(servletCookie);
    }

    @Override
    public String getPath() {
        return httpRequest.getRequestURI();
    }
}
