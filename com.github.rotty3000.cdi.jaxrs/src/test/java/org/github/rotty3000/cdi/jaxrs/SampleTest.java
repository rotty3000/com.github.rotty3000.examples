/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.github.rotty3000.cdi.jaxrs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.runtime.HttpServiceRuntime;
import org.osgi.service.http.runtime.dto.ServletContextDTO;
import org.osgi.service.http.runtime.dto.ServletDTO;
import org.osgi.util.tracker.ServiceTracker;

public class SampleTest {

	ServiceTracker<HttpClientBuilderFactory, HttpClientBuilderFactory> hcbfTracker;
	ServiceTracker<HttpServiceRuntime, HttpServiceRuntime> hsrTracker;
	ServiceReference<HttpServiceRuntime> hsrReference;
	BundleContext bundleContext;

	@Before
	public void before() {
		bundleContext = FrameworkUtil.getBundle(getClass()).getBundleContext();
		hsrTracker = new ServiceTracker<>(bundleContext, HttpServiceRuntime.class, null);
		hsrTracker.open();
		hcbfTracker = new ServiceTracker<>(bundleContext, HttpClientBuilderFactory.class, null);
		hcbfTracker.open();
	}

	@After
	public void after() {
		hsrTracker.close();
		hcbfTracker.close();
	}

	@Test
	public void checkServletInit() throws Exception {
		ServletDTO servletDTO = getServletDTOByName("default", "cxf-servlet");
		assertNotNull(servletDTO);
	}

//	@Test
//	public void testResponse() throws Exception {
//		SimpleServlet.servletContext.getPromise().timeout(5000).getValue();
//		hsrTracker.waitForService(500);
//		hsrReference = hsrTracker.getServiceReference();
//
//		HttpClientBuilderFactory hcbf = hcbfTracker.waitForService(500);
//
//		HttpClientBuilder clientBuilder = hcbf.newBuilder();
//		CloseableHttpClient httpclient = clientBuilder.build();
//
//		CookieStore cookieStore = new BasicCookieStore();
//		HttpContext httpContext = new BasicHttpContext();
//		httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
//
//		URI uri = new URIBuilder(getEndpoint()).
//				setPath("/s/foo").
//				setParameter("name", "test").
//				build();
//
//		HttpGet httpget = new HttpGet(uri);
//
//		String expected =
//			"<h2>It Works!</h2>\n" +
//			"<p>URI was: /s/foo<br/>\n" +
//			"<p>Parameters were: <br/>\n" +
//			"name = [test]<br/>\n" +
//			"</p>\n";
//
//		try (CloseableHttpResponse response = httpclient.execute(httpget, httpContext)) {
//			HttpEntity entity = response.getEntity();
//
//			assertEquals(expected, read(entity));
//		}
//	}

	String read(HttpEntity entity) throws Exception {
		if (entity == null) {
			return null;
		}

		try (InputStream in = entity.getContent();
			java.util.Scanner s = new java.util.Scanner(in)) {

			s.useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		}
	}

	String getEndpoint() {
		String[] endpoints = (String[])hsrReference.getProperty("osgi.http.endpoint");

		if (endpoints == null || endpoints.length == 0) {
			String port = (String)hsrReference.getProperty("org.osgi.service.http.port");
			return "http://localhost:" + port;
		}

		return endpoints[0];
	}


	protected ServletDTO getServletDTOByName(String context, String name) {
		ServletContextDTO servletContextDTO = getServletContextDTOByName(context);

		if (servletContextDTO == null) {
			return null;
		}

		for (ServletDTO servletDTO : servletContextDTO.servletDTOs) {
			if (name.equals(servletDTO.name)) {
				return servletDTO;
			}
		}

		return null;
	}

	protected ServletContextDTO getServletContextDTOByName(String name) {
		for (ServletContextDTO servletContextDTO : getServletContextDTOs()) {
			if (name.equals(servletContextDTO.name)) {
				return servletContextDTO;
			}
		}

		return null;
	}

	protected ServletContextDTO[] getServletContextDTOs() {
		return getHttpServiceRuntime().getRuntimeDTO().servletContextDTOs;
	}

	protected HttpServiceRuntime getHttpServiceRuntime() {
		ServiceReference<HttpServiceRuntime> serviceReference =
				getBundleContext().getServiceReference(HttpServiceRuntime.class);

		assertNotNull(serviceReference);

		return getBundleContext().getService(serviceReference);
	}

	protected BundleContext getBundleContext() {
		return FrameworkUtil.getBundle(getClass()).getBundleContext();
	}

}
