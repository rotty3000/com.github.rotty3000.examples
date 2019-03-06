package org.github.rotty3000.cdi.faces.bootstrap;

import java.io.IOException;

import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.aries.cdi.extra.propertytypes.HttpWhiteboardContextSelect;
import org.github.rotty3000.cdi.faces.bootstrap.JSFLoader.WithLoader;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

@HttpWhiteboardContextSelect("(" + HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=JSF)")
@WebServlet(name = "Faces Servlet", urlPatterns = {"/faces/*", "*.jsf", "*.faces", "*.xhtml"}, asyncSupported = true)
public class JSFServlet extends HttpServlet {

	private static final long serialVersionUID = 1801380820576021288L;

	private FacesServlet facesServlet;

	public JSFServlet() {
		facesServlet = new FacesServlet();
	}

	@Override
	public ServletConfig getServletConfig() {
		return facesServlet.getServletConfig();
	}

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		try (WithLoader withLoader = new WithLoader()) {
			facesServlet.init(servletConfig);
		}
	}

	@Override
	public void destroy() {
		try (WithLoader withLoader = new WithLoader()) {
			facesServlet.destroy();
		}
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		try (WithLoader withLoader = new WithLoader()) {
			facesServlet.service(request, response);
		}
	}

}