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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Rule;
import org.junit.Test;
import org.osgi.service.jaxrs.runtime.dto.ResourceDTO;

public class SampleTest {

	@Rule
	public final JaxrsRuntimeRule jsr = new JaxrsRuntimeRule();

	@Test
	public void checkResourceDTO() throws Exception {
		ResourceDTO resourceDTO = jsr.getResourceDTOByName(".default", "simpleEndpoint");
		assertNotNull(resourceDTO);
	}

	@Test
	public void testResponse() throws Exception {
		ClientBuilder clientBuilder = jsr.clientBuilder();

		Client client = clientBuilder.build();

		WebTarget webTarget = client.target(jsr.getEndpoint()).path("/foo");

		String expected =  "Hello foo";

		try (Response response = webTarget.request(MediaType.TEXT_PLAIN).get()) {
			String result = response.readEntity(String.class);

			assertEquals(expected, result);
		}
	}

}
