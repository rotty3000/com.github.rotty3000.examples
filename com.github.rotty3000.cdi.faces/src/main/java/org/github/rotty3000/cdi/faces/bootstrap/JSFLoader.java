package org.github.rotty3000.cdi.faces.bootstrap;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Enumeration;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.sun.el.ExpressionFactoryImpl;
import com.sun.faces.vendor.WebContainerInjectionProvider;

public class JSFLoader extends URLClassLoader {

	static final JSFLoader INSTANCE = new JSFLoader();

	JSFLoader() {
		super(new URL[0], JSFLoader.class.getClassLoader());

		_bundles = new Bundle[] {
			FrameworkUtil.getBundle(JSFLoader.class),
			FrameworkUtil.getBundle(ExpressionFactoryImpl.class),
			FrameworkUtil.getBundle(WebContainerInjectionProvider.class)
		};
	}

	@Override
	public URL findResource(String name) {
		for (Bundle bundle : _bundles) {
			URL url = bundle.getResource(name);

			if (url != null) {
				return url;
			}
		}

		return null;
	}

	@Override
	public Enumeration<URL> findResources(String name) {
		for (Bundle bundle : _bundles) {
			try {
				Enumeration<URL> enumeration = bundle.getResources(name);

				if ((enumeration != null) && enumeration.hasMoreElements()) {
					return enumeration;
				}
			}
			catch (IOException ioe) {
			}
		}

		return Collections.enumeration(Collections.<URL>emptyList());
	}

	public Bundle[] getBundles() {
		return _bundles;
	}

	@Override
	public URL getResource(String name) {
		return findResource(name);
	}

	@Override
	public Enumeration<URL> getResources(String name) {
		return findResources(name);
	}

	@Override
	protected Class<?> loadClass(String name, boolean resolve)
		throws ClassNotFoundException {

		Class<?> clazz = null;

		for (Bundle bundle : _bundles) {
			try {
				clazz = bundle.loadClass(name);

				break;
			}
			catch (ClassNotFoundException cnfe) {
			}
		}

		if (clazz == null) {
			return super.loadClass(name, resolve);
		}

		if (resolve) {
			resolveClass(clazz);
		}

		return clazz;
	}

	public static class WithLoader implements AutoCloseable {

		private final ClassLoader original;

		public WithLoader() {
			Thread currentThread = Thread.currentThread();
			original = currentThread.getContextClassLoader();
			currentThread.setContextClassLoader(INSTANCE);
		}

		@Override
		public void close() {
			Thread currentThread = Thread.currentThread();
			currentThread.setContextClassLoader(original);
		}

	}

	private final Bundle[] _bundles;

}
