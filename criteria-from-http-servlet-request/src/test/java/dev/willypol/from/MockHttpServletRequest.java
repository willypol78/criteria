package dev.willypol.from;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConnection;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpUpgradeHandler;
import jakarta.servlet.http.Part;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MockHttpServletRequest implements HttpServletRequest {
	private final Map<String, String[]> parameters = new HashMap<>();
	private       String                method;
	private       String                requestURI;
	private       String                queryString;
	private       String                contentType;
	private       byte[]                content;

	@Override
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String getPathInfo() {
		return "";
	}

	@Override
	public String getPathTranslated() {
		return "";
	}

	@Override
	public String getRequestURI() {
		return requestURI;
	}

	public void setRequestURI(String requestURI) {
		if (requestURI.contains("?")) {
			String[] parts = requestURI.split("\\?");
			this.requestURI = parts[0];
			setQueryString(parts[1]);
		} else {
			this.requestURI = requestURI;
		}
	}

	@Override
	public StringBuffer getRequestURL() {
		return null;
	}

	@Override
	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
		// Parse query string into parameters
		if (queryString != null) {
			String[] pairs = queryString.split("&");
			for (String pair : pairs) {
				String[] keyValue = pair.split("=");
				String   key      = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
				String   value    = URLDecoder.decode(keyValue.length > 1 ? keyValue[1] : "", StandardCharsets.UTF_8);
				parameters.put(key, new String[]{value});
			}
		}
	}

	@Override
	public String getRemoteUser() {
		return "";
	}

	@Override
	public boolean isUserInRole(final String s) {
		return false;
	}

	@Override
	public Principal getUserPrincipal() {
		return null;
	}

	@Override
	public String getRequestedSessionId() {
		return "";
	}

	@Override
	public String getParameter(String name) {
		String[] values = parameters.get(name);
		return (values != null && values.length > 0) ? values[0] : null;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(parameters.keySet());
	}

	@Override
	public String[] getParameterValues(String name) {
		return parameters.get(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return Map.of();
	}

	// Implementa otros métodos de HttpServletRequest según sea necesario
	@Override
	public HttpSession getSession() {return null;} // Placeholder

	@Override
	public String changeSessionId() {
		return "";
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return false;
	}

	@Override
	public boolean authenticate(final HttpServletResponse httpServletResponse) throws IOException, ServletException {
		return false;
	}

	@Override
	public void login(final String s, final String s1) throws ServletException {
		// Not used but necessary to implement
	}

	@Override
	public void logout() throws ServletException {
		// Not used but necessary to implement
	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		return List.of();
	}

	@Override
	public Part getPart(final String s) throws IOException, ServletException {
		return null;
	}

	@Override
	public <T extends HttpUpgradeHandler> T upgrade(final Class<T> aClass) throws IOException, ServletException {
		return null;
	}

	@Override
	public Object getAttribute(String name) {return null;}

	@Override
	public Enumeration<String> getAttributeNames() {return null;}

	@Override
	public String getCharacterEncoding() {
		return "";
	}

	@Override
	public void setCharacterEncoding(final String s) throws UnsupportedEncodingException {
		// Not used but necessary to implement
	}

	@Override
	public int getContentLength() {
		return 0;
	}

	@Override
	public long getContentLengthLong() {
		return 0;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return null;
	}

	@Override
	public String getAuthType() {return null;}

	@Override
	public Cookie[] getCookies() {
		return new Cookie[0];
	}

	@Override
	public long getDateHeader(final String s) {
		return 0;
	}

	@Override
	public String getHeader(final String s) {
		return "";
	}

	@Override
	public Enumeration<String> getHeaders(final String s) {
		return null;
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		return null;
	}

	@Override
	public int getIntHeader(final String s) {
		return 0;
	}

	@Override
	public String getContextPath() {return null;}

	@Override
	public String getProtocol() {return null;}

	@Override
	public String getScheme() {
		return "";
	}

	@Override
	public String getServerName() {return null;}

	@Override
	public int getServerPort() {return 0;}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content), StandardCharsets.UTF_8));
	}

	@Override
	public String getRemoteAddr() {
		return "";
	}

	@Override
	public String getRemoteHost() {
		return "";
	}

	@Override
	public void setAttribute(final String s, final Object o) {
		// Not used but necessary to implement
	}

	@Override
	public void removeAttribute(final String s) {
		// Not used but necessary to implement
	}

	@Override
	public Locale getLocale() {
		return null;
	}

	@Override
	public Enumeration<Locale> getLocales() {
		return null;
	}

	@Override
	public boolean isSecure() {
		return false;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(final String s) {
		return null;
	}

	@Override
	public int getRemotePort() {
		return 0;
	}

	@Override
	public String getLocalName() {
		return "";
	}

	@Override
	public String getLocalAddr() {
		return "";
	}

	@Override
	public int getLocalPort() {
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		return null;
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		return null;
	}

	@Override
	public AsyncContext startAsync(final ServletRequest servletRequest, final ServletResponse servletResponse) throws IllegalStateException {
		return null;
	}

	@Override
	public boolean isAsyncStarted() {
		return false;
	}

	@Override
	public boolean isAsyncSupported() {
		return false;
	}

	@Override
	public AsyncContext getAsyncContext() {
		return null;
	}

	@Override
	public DispatcherType getDispatcherType() {
		return null;
	}

	@Override
	public String getRequestId() {
		return "";
	}

	@Override
	public String getProtocolRequestId() {
		return "";
	}

	@Override
	public ServletConnection getServletConnection() {
		return null;
	}

	@Override
	public String getServletPath() {return null;}

	@Override
	public HttpSession getSession(final boolean b) {
		return null;
	}

	public void setContent(final byte[] bytes) {
		this.content = bytes;
	}

}
