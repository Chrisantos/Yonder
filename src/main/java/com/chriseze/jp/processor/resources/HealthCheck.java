package com.chriseze.jp.processor.resources;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/")
public class HealthCheck {
	
	private static final Logger log = Logger.getLogger(HealthCheck.class.getName());
	
	@Context UriInfo uriInfo;
	
	@PostConstruct
	public void init() {
		log.info(new StringBuilder("request URI: ").append(uriInfo.getRequestUri()).toString());
	}

	@GET
	@POST
	@PermitAll
	@Path("/ping")
	@Produces("text/plain")
	public Response doGet() {
		return Response.ok("Services is Up!").build();
	}

}
