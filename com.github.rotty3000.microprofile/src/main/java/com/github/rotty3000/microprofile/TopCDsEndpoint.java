package com.github.rotty3000.microprofile;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.osgi.service.cdi.ServiceScope;
import org.osgi.service.cdi.annotations.Service;
import org.osgi.service.cdi.annotations.ServiceInstance;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsResource;
import org.osgi.service.log.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Path("/")
@Service
@ServiceInstance(ServiceScope.PROTOTYPE)
@JaxrsResource
public class TopCDsEndpoint {

	@Inject
	private Logger logger;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getTopCDs() {

		final JsonArrayBuilder array = Json.createArrayBuilder();
		final List<Integer> randomCDs = getRandomNumbers();
		for (final Integer randomCD : randomCDs) {
			array.add(Json.createObjectBuilder().add("id", randomCD));
		}
		return array.build().toString();
	}

	private List<Integer> getRandomNumbers() {
		final List<Integer> randomCDs = new ArrayList<>();
		final Random r = new Random();
		randomCDs.add(r.nextInt(100) + 1101);
		randomCDs.add(r.nextInt(100) + 1101);
		randomCDs.add(r.nextInt(100) + 1101);
		randomCDs.add(r.nextInt(100) + 1101);
		randomCDs.add(r.nextInt(100) + 1101);

		logger.info("Top CDs are " + randomCDs);

		return randomCDs;
	}
}
