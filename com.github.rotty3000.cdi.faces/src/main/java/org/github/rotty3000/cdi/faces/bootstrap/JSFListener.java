package org.github.rotty3000.cdi.faces.bootstrap;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.aries.cdi.extra.propertytypes.HttpWhiteboardContextSelect;
import org.apache.aries.cdi.extra.propertytypes.HttpWhiteboardListener;
import org.github.rotty3000.cdi.faces.bootstrap.JSFLoader.WithLoader;
import org.osgi.framework.BundleContext;
import org.osgi.service.cdi.annotations.Service;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import com.sun.faces.RIConstants;
import com.sun.faces.config.ConfigureListener;

@HttpWhiteboardListener
@HttpWhiteboardContextSelect("(" + HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=JSF)")
@Service({
	ConfigureListener.class,
	ServletRequestListener.class,
	HttpSessionListener.class,
	ServletRequestAttributeListener.class,
	HttpSessionAttributeListener.class,
	ServletContextAttributeListener.class,
	ServletContextListener.class
})
public class JSFListener extends ConfigureListener {

	private final Set<Class<?>> managedBeans = new HashSet<>();

	@Inject
	public JSFListener(BundleContext bundleContext) {
		String managedBeansHeader = bundleContext.getBundle().getHeaders().get("Managed-Beans");

		if (managedBeansHeader != null) {
			for (String managedBean : managedBeansHeader.split("\\s*,\\s*")) {
				try {
					Class<?> clazz = bundleContext.getBundle().loadClass(managedBean);
					managedBeans.add(clazz);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		try (WithLoader withLoader = new WithLoader()) {
			super.attributeAdded(event);
		}
	}

	@Override
	public void attributeAdded(ServletContextAttributeEvent event) {
		try (WithLoader withLoader = new WithLoader()) {
			super.attributeAdded(event);
		}
	}

	@Override
	public void attributeAdded(ServletRequestAttributeEvent event) {
		try (WithLoader withLoader = new WithLoader()) {
			super.attributeAdded(event);
		}
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		try (WithLoader withLoader = new WithLoader()) {
			super.attributeRemoved(event);
		}
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent event) {
		try (WithLoader withLoader = new WithLoader()) {
			super.attributeRemoved(event);
		}
	}

	@Override
	public void attributeRemoved(ServletRequestAttributeEvent event) {
		try (WithLoader withLoader = new WithLoader()) {
			super.attributeRemoved(event);
		}
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		try (WithLoader withLoader = new WithLoader()) {
			super.attributeReplaced(event);
		}
	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent event) {
		try (WithLoader withLoader = new WithLoader()) {
			super.attributeReplaced(event);
		}
	}

	@Override
	public void attributeReplaced(ServletRequestAttributeEvent event) {
		try (WithLoader withLoader = new WithLoader()) {
			super.attributeReplaced(event);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try (WithLoader withLoader = new WithLoader()) {
			super.contextDestroyed(sce);
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		servletContext.setAttribute(RIConstants.ANNOTATED_CLASSES, managedBeans);
		servletContext.setAttribute(RIConstants.FACES_INITIALIZER_MAPPINGS_ADDED, Boolean.TRUE);

		try (WithLoader withLoader = new WithLoader()) {
			super.contextInitialized(sce);
		}
	}

	@Override
	public void requestDestroyed(ServletRequestEvent event) {
		try (WithLoader withLoader = new WithLoader()) {
			super.requestDestroyed(event);
		}
	}

	@Override
	public void requestInitialized(ServletRequestEvent event) {
		try (WithLoader withLoader = new WithLoader()) {
			super.requestInitialized(event);
		}
	}

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		try (WithLoader withLoader = new WithLoader()) {
			super.sessionCreated(event);
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		try (WithLoader withLoader = new WithLoader()) {
			super.sessionDestroyed(event);
		}
	}

}
