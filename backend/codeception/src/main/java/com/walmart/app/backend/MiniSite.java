package com.walmart.app.backend;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

import javax.servlet.ServletException;

import static io.undertow.servlet.Servlets.defaultContainer;
import static io.undertow.servlet.Servlets.deployment;
import static io.undertow.servlet.Servlets.servlet;
import io.undertow.Handlers;


public class MiniSite {
  public MiniSite() throws Exception {}

  public static final String PATH = "/";

  public static void main(String[] args) throws Exception {
    try {
      DeploymentInfo servletBuilder =
          deployment()
              .setClassLoader(MiniSite.class.getClassLoader())
              .setContextPath(PATH)
              .setDeploymentName("handler.war")
              .addServlets(
                  servlet("UploadService", UploadService.class)
                      .addMapping("/upload"),
                servlet("UploadImageService", UploadImageService.class)
                      .addMapping("/uploadImage"),
                servlet("UploadVideoService", UploadVideoService.class)
                      .addMapping("/uploadVideo"),
                  servlet("GetService", GetService.class).addMapping("/get"),
                  servlet("HealthCheckServlet", HealthCheckServlet.class)
                      .addMapping("/"));

      DeploymentManager manager =
          defaultContainer().addDeployment(servletBuilder);
      manager.deploy();

      HttpHandler servletHandler = manager.start();
      PathHandler path = Handlers.path(Handlers.redirect(PATH))
                             .addPrefixPath(PATH, servletHandler);

      Undertow server = Undertow.builder()
                            .addHttpListener(80, "0.0.0.0")
                            .setHandler(path)
                            .build();

      server.start();
    } catch (ServletException e) {
      throw new RuntimeException(e);
    }
  }
}
