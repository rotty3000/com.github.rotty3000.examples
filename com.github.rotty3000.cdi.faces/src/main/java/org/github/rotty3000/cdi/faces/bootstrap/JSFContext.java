package org.github.rotty3000.cdi.faces.bootstrap;

import java.net.URL;

import javax.inject.Inject;

import org.apache.aries.cdi.extra.propertytypes.HttpWhiteboardContext;
import org.osgi.framework.BundleContext;
import org.osgi.service.cdi.annotations.Service;
import org.osgi.service.http.context.ServletContextHelper;

@HttpWhiteboardContext(name = "JSF", path = "/")
@Service(ServletContextHelper.class)
public class JSFContext extends ServletContextHelper {

	@Inject
	public JSFContext(BundleContext bundleContext) {
		super(bundleContext.getBundle());
	}

	@Override
	public URL getResource(String name) {
		if (!name.startsWith("/")) {
			name = "/" + name;
		}
		if (name.endsWith(".xhtml")) {
			return super.getResource("META-INF/resources" + name);
		}
		return super.getResource(name);
	}

}
