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

import java.util.Map;

import org.osgi.framework.dto.ServiceReferenceDTO;
import org.osgi.service.http.runtime.HttpServiceRuntime;
import org.osgi.service.http.runtime.dto.ErrorPageDTO;
import org.osgi.service.http.runtime.dto.FailedErrorPageDTO;
import org.osgi.service.http.runtime.dto.FailedFilterDTO;
import org.osgi.service.http.runtime.dto.FailedListenerDTO;
import org.osgi.service.http.runtime.dto.FailedResourceDTO;
import org.osgi.service.http.runtime.dto.FailedServletContextDTO;
import org.osgi.service.http.runtime.dto.FailedServletDTO;
import org.osgi.service.http.runtime.dto.FilterDTO;
import org.osgi.service.http.runtime.dto.ListenerDTO;
import org.osgi.service.http.runtime.dto.ResourceDTO;
import org.osgi.service.http.runtime.dto.ServletContextDTO;
import org.osgi.service.http.runtime.dto.ServletDTO;

public class HttpRuntimeRule extends RuntimeRule<HttpServiceRuntime> {

	public HttpRuntimeRule() {
		super(HttpServiceRuntime.class);
	}

	public HttpRuntimeRule(long timeout) {
		super(HttpServiceRuntime.class, timeout);
	}

	public HttpRuntimeRule(String filter, long timeout) {
		super(HttpServiceRuntime.class, filter, timeout);
	}

	public HttpRuntimeRule(String filter) {
		super(HttpServiceRuntime.class, filter);
	}

	public String getEndpoint() {
		ServiceReferenceDTO serviceDTO = getServiceDTO();

		Map<String, Object> properties = serviceDTO.properties;

		String[] endpoints = (String[])properties.get("osgi.http.endpoint");

		if (endpoints == null || endpoints.length == 0) {
			String port = (String)properties.get("org.osgi.service.http.port");
			return "http://localhost:" + port;
		}

		return endpoints[0];
	}

	public ErrorPageDTO getErrorPageDTOByName(String context, String name) {
		return awaitChangeCount(b -> getErrorPageDTOByName0(context, name));
	}

	public FailedErrorPageDTO[] getFailedErrorPageDTOs() {
		return awaitChangeCount(b -> getFailedErrorPageDTOs0());
	}

	public FailedFilterDTO[] getFailedFilterDTOs() {
		return awaitChangeCount(b -> getFailedFilterDTOs0());
	}

	public FailedListenerDTO[] getFailedListenerDTOs() {
		return awaitChangeCount(b -> getFailedListenerDTOs0());
	}

	public FailedResourceDTO[] getFailedResourceDTOs() {
		return awaitChangeCount(b -> getFailedResourceDTOs0());
	}

	public FailedServletContextDTO[] getFailedServletContextDTOs() {
		return awaitChangeCount(b -> getFailedServletContextDTOs0());
	}

	public FailedServletDTO[] getFailedServletDTOs() {
		return awaitChangeCount(b -> getFailedServletDTOs0());
	}

	public FilterDTO getFilterDTOByName(String context, String name) {
		return awaitChangeCount(b -> getFilterDTOByName0(context, name));
	}

	public ListenerDTO[] getListenerDTO(String context) {
		return awaitChangeCount(b -> getListenerDTO0(context));
	}

	public ResourceDTO[] getResourceDTO(String context) {
		return awaitChangeCount(b -> getResourceDTO0(context));
	}

	public ServletDTO getServletDTOByName(String context, String name) {
		return awaitChangeCount(b -> getServletDTOByName0(context, name));
	}

	public ServiceReferenceDTO getServiceDTO() {
		return awaitChangeCount(b -> getServiceDTO0());
	}

	private ErrorPageDTO getErrorPageDTOByName0(String context, String name) {
		ServletContextDTO servletContextDTO = getServletContextDTOByName0(context);

		if (servletContextDTO == null) {
			return null;
		}

		for (ErrorPageDTO filterDTO : servletContextDTO.errorPageDTOs) {
			if (name.equals(filterDTO.name)) {
				return filterDTO;
			}
		}

		return null;
	}

	private FailedErrorPageDTO[] getFailedErrorPageDTOs0() {
		return service().getRuntimeDTO().failedErrorPageDTOs;
	}

	private FailedFilterDTO[] getFailedFilterDTOs0() {
		return service().getRuntimeDTO().failedFilterDTOs;
	}

	private FailedListenerDTO[] getFailedListenerDTOs0() {
		return service().getRuntimeDTO().failedListenerDTOs;
	}

	private FailedResourceDTO[] getFailedResourceDTOs0() {
		return service().getRuntimeDTO().failedResourceDTOs;
	}

	private FailedServletContextDTO[] getFailedServletContextDTOs0() {
		return service().getRuntimeDTO().failedServletContextDTOs;
	}

	private FailedServletDTO[] getFailedServletDTOs0() {
		return service().getRuntimeDTO().failedServletDTOs;
	}

	private FilterDTO getFilterDTOByName0(String context, String name) {
		ServletContextDTO servletContextDTO = getServletContextDTOByName0(context);

		if (servletContextDTO == null) {
			return null;
		}

		for (FilterDTO filterDTO : servletContextDTO.filterDTOs) {
			if (name.equals(filterDTO.name)) {
				return filterDTO;
			}
		}

		return null;
	}

	private ListenerDTO[] getListenerDTO0(String context) {
		ServletContextDTO servletContextDTO = getServletContextDTOByName0(context);

		if (servletContextDTO == null) {
			return null;
		}

		return servletContextDTO.listenerDTOs;
	}

	private ResourceDTO[] getResourceDTO0(String context) {
		ServletContextDTO servletContextDTO = getServletContextDTOByName0(context);

		if (servletContextDTO == null) {
			return null;
		}

		return servletContextDTO.resourceDTOs;
	}

	private ServletContextDTO getServletContextDTOByName0(String name) {
		for (ServletContextDTO servletContextDTO : getServletContextDTOs0()) {
			if (name.equals(servletContextDTO.name)) {
				return servletContextDTO;
			}
		}

		return null;
	}

	private ServletContextDTO[] getServletContextDTOs0() {
		return service().getRuntimeDTO().servletContextDTOs;
	}

	private ServletDTO getServletDTOByName0(String context, String name) {
		ServletContextDTO servletContextDTO = getServletContextDTOByName0(context);

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

	private ServiceReferenceDTO getServiceDTO0() {
		return service().getRuntimeDTO().serviceDTO;
	}

}
