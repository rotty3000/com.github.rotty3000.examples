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

import java.util.List;
import java.util.Map;

import javax.ws.rs.client.ClientBuilder;

import org.osgi.framework.dto.ServiceReferenceDTO;
import org.osgi.service.jaxrs.runtime.JaxrsServiceRuntime;
import org.osgi.service.jaxrs.runtime.dto.ApplicationDTO;
import org.osgi.service.jaxrs.runtime.dto.ExtensionDTO;
import org.osgi.service.jaxrs.runtime.dto.FailedApplicationDTO;
import org.osgi.service.jaxrs.runtime.dto.FailedExtensionDTO;
import org.osgi.service.jaxrs.runtime.dto.FailedResourceDTO;
import org.osgi.service.jaxrs.runtime.dto.ResourceDTO;
import org.osgi.service.jaxrs.runtime.dto.ResourceMethodInfoDTO;
import org.osgi.util.tracker.ServiceTracker;

public class JaxrsRuntimeRule extends RuntimeRule<JaxrsServiceRuntime> {

	private ServiceTracker<ClientBuilder, ClientBuilder> clientBuilderTracker;

	public JaxrsRuntimeRule() {
		super(JaxrsServiceRuntime.class);
	}

	public JaxrsRuntimeRule(long timeout) {
		super(JaxrsServiceRuntime.class, timeout);
	}

	public JaxrsRuntimeRule(String filter, long timeout) {
		super(JaxrsServiceRuntime.class, filter, timeout);
	}

	public JaxrsRuntimeRule(String filter) {
		super(JaxrsServiceRuntime.class, filter);
	}

	public ApplicationDTO getApplicationDTOByName(String name) {
		return awaitChangeCount(b -> getApplicationDTOByName0(name));
	}

	public ApplicationDTO[] getApplicationDTOs() {
		return awaitChangeCount(b -> getApplicationDTOs0());
	}

	public ApplicationDTO getDefaultApplicationDTO() {
		return awaitChangeCount(b -> getDefaultApplicationDTO0());
	}

	@Override
	public void after() {
		super.after();
		clientBuilderTracker.close();
	}

	@Override
	public void before() {
		super.before();
		clientBuilderTracker = new ServiceTracker<>(context(), ClientBuilder.class, null);
		clientBuilderTracker.open();
	}

	public ClientBuilder clientBuilder() {
		try {
			return clientBuilderTracker.waitForService(timeout());
		}
		catch (InterruptedException e) {
			throw new AssertionError("Service " + ClientBuilder.class.getName() + " didn't arrive by timeout: " + timeout());
		}
	}

	public String getEndpoint() {
		Map<String, Object> properties = getServiceDTO().properties;

		@SuppressWarnings("unchecked")
		List<String> endpoints = (List<String>)properties.get("osgi.jaxrs.endpoint");

		if (endpoints == null || endpoints.isEmpty()) {
			throw new AssertionError("'osgi.jaxrs.endpoint' not available");
		}

		return endpoints.get(0);
	}

	public ExtensionDTO getExtensionDTOByName(final String application, final String name) {
		return awaitChangeCount(b -> getExtensionDTOByName0(application, name));
	}

	public FailedApplicationDTO[] getFailedApplicationDTOs() {
		return awaitChangeCount(b -> getFailedApplicationDTOs0());
	}

	public FailedExtensionDTO[] getFailedExtensionDTOs() {
		return awaitChangeCount(b -> getFailedExtensionDTOs0());
	}

	public FailedResourceDTO[] getFailedResourceDTOs() {
		return awaitChangeCount(b -> getFailedResourceDTOs0());
	}

	public ResourceDTO getResourceDTOByName(final String application, final String name) {
		return awaitChangeCount(b -> getResourceDTOByName0(application, name));
	}

	public ResourceMethodInfoDTO[] getResourceMethodInfoDTOs(final String application, final String name) {
		return awaitChangeCount(b -> getResourceMethodInfoDTOs(application));
	}

	public ServiceReferenceDTO getServiceDTO() {
		return awaitChangeCount(b -> getServiceDTO0());
	}

	private ApplicationDTO getApplicationDTOByName0(String name) {
		if (".default".equals(name)) {
			return service().getRuntimeDTO().defaultApplication;
		}

		for (ApplicationDTO applicationDTO : getApplicationDTOs0()) {
			if (name.equals(applicationDTO.name)) {
				return applicationDTO;
			}
		}

		return null;
	}

	private ApplicationDTO[] getApplicationDTOs0() {
		return service().getRuntimeDTO().applicationDTOs;
	}

	private ApplicationDTO getDefaultApplicationDTO0() {
		return service().getRuntimeDTO().defaultApplication;
	}

	private ExtensionDTO getExtensionDTOByName0(String application, String name) {
		ApplicationDTO applicationDTO = getApplicationDTOByName0(application);

		if (applicationDTO == null) {
			return null;
		}

		for (ExtensionDTO extensionDTO : applicationDTO.extensionDTOs) {
			if (name.equals(extensionDTO.name)) {
				return extensionDTO;
			}
		}

		return null;
	}

	private FailedApplicationDTO[] getFailedApplicationDTOs0() {
		return service().getRuntimeDTO().failedApplicationDTOs;
	}

	private FailedExtensionDTO[] getFailedExtensionDTOs0() {
		return service().getRuntimeDTO().failedExtensionDTOs;
	}

	private FailedResourceDTO[] getFailedResourceDTOs0() {
		return service().getRuntimeDTO().failedResourceDTOs;
	}

	private ResourceDTO getResourceDTOByName0(String application, String name) {
		ApplicationDTO applicationDTO = getApplicationDTOByName0(application);

		if (applicationDTO == null) {
			return null;
		}

		for (ResourceDTO resourceDTO : applicationDTO.resourceDTOs) {
			if (name.equals(resourceDTO.name)) {
				return resourceDTO;
			}
		}

		return null;
	}

	private ResourceMethodInfoDTO[] getResourceMethodInfoDTOs(String application) {
		ApplicationDTO applicationDTO = getApplicationDTOByName0(application);

		if (applicationDTO == null) {
			return null;
		}

		return applicationDTO.resourceMethods;
	}

	private ServiceReferenceDTO getServiceDTO0() {
		return service().getRuntimeDTO().serviceDTO;
	}

}
