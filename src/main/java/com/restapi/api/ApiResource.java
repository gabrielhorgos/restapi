package com.restapi.api;

import com.restapi.api.service.ApiService;
import com.restapi.api.service.ApiServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@ManagedBean
@Path("/hello")
public class ApiResource {
	
	@Inject
	private ApiService apiService;
	
	@GET
	@Path("/{param}")
	@Operation(summary = "The API Resource", tags = { "Hello Api", },
			responses = {
					@ApiResponse(responseCode = "200", description = "Returns a hello message for given parameter",
							content = @Content(mediaType = "application/json",
									schema = @Schema(implementation = String.class)))})
	public Response sayHello(@PathParam("param") String param, @Context HttpServletRequest request) {
		String message = apiService.buildMessage(param);
		message += request.getContextPath();
		return Response.ok().entity(message).build();
	}
	
}
