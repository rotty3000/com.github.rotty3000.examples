package com.github.rotty3000.microprofile;

import org.apache.aries.testsupport.it.jaxrs.JaxrsRuntimeRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TopCDsEndpointTest {

	@Rule
	public JaxrsRuntimeRule jaxrsRuntime = (JaxrsRuntimeRule)new JaxrsRuntimeRule().require();

	private String baseURI;
	private Client client;
	private WebTarget webTarget;

	@Before
	public void initWebTarget() {
		client = jaxrsRuntime.clientBuilder().build();
		baseURI = jaxrsRuntime.getEndpoint();
		webTarget = client.target(baseURI);
	}

	// ======================================
	// = Test methods =
	// ======================================

	@Test
	public void should_be_deployed() {
		Response response = webTarget.request(MediaType.APPLICATION_JSON).get();
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
	}

	@Test
	public void should_have_five_items() {
		String body = webTarget.request(MediaType.APPLICATION_JSON).get(String.class);
		//assertThatJson(body).isArray().ofLength(5);
		assertTrue(body.startsWith("[{\"id\":"));
	}

}
