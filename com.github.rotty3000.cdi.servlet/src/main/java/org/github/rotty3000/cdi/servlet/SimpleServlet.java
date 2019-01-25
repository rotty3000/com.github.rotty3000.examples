package org.github.rotty3000.cdi.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.log.Logger;
import org.osgi.util.promise.Deferred;

@WebServlet(name = "SimpleServlet", urlPatterns = "/s/*")
public class SimpleServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final Logger logger;
	static final Deferred<ServletContext> servletContext = new Deferred<>();

	@Inject
	public SimpleServlet(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		servletContext.resolve(config.getServletContext());
	}

	@PostConstruct
	void done() {
		logger.debug("Ready after {}", ManagementFactory.getRuntimeMXBean().getUptime());
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();

		writer.println("<h2>It Works!</h2>");
		writer.format("<p>URI was: %s<br/>%n", request.getRequestURI());
		writer.println("<p>Parameters were: <br/>");
		request.getParameterMap().forEach((k, v) -> writer.format("%s = %s<br/>%n", k, Arrays.toString(v)));
		writer.println("</p>");
	}
}
