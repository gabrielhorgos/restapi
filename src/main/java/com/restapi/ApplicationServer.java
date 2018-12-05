package com.restapi;

import io.swagger.v3.jaxrs2.integration.OpenApiServlet;
import io.swagger.v3.jaxrs2.integration.SwaggerServletInitializer;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jboss.weld.environment.servlet.Listener;

import javax.servlet.ServletException;

import static io.undertow.servlet.Servlets.listener;
import static io.undertow.servlet.Servlets.servlet;

/**
 * Server startup and api deployment.
 */
public class ApplicationServer {

	private static Undertow server;

	public static void main(String[] args)
			throws ServletException {
		
		startContainer(8080);
	}

	public static void startContainer(int port)
			throws ServletException {
		DeploymentInfo servletBuilder = Servlets.deployment();

		servletBuilder
				.setClassLoader(ApplicationServer.class.getClassLoader())
				.setContextPath("/restapi")
				.setResourceManager(new ClassPathResourceManager(ApplicationServer.class.getClassLoader()))
				.setDeploymentName("restapi.war")
				.addListeners(listener(Listener.class))
				.addServlets(servlet("jerseyServlet", ServletContainer.class).setLoadOnStartup(1)
						.addInitParam("javax.ws.rs.Application", ApplicationConfig.class.getName())
						.addMapping("/api/*"));

		DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
		manager.deploy();
		PathHandler path = Handlers.path(Handlers.redirect("/restapi")).addPrefixPath("/restapi", manager.start());
	
		server = Undertow.builder()
				.addHttpListener(port, "localhost").setHandler(path)
				.build();

		server.start();
	}

	public static void stopContainer() {
		server.stop();
	}

}
