package com.walmart.app.backend;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class GetService extends HttpServlet {

  /**
   *
   */
  public GetService() {}

  @Override
  protected void doPost(final HttpServletRequest request,
                        final HttpServletResponse response)
      throws ServletException, IOException {
    /* TODO:
    1. read data from request and save data to a record class
    2. get data from cosmosdb
        1. connect cosmosdb with java sdk
        2. retrieve data with SQL from Li
    */
    PrintWriter writer = response.getWriter();
    writer.write("hello world");
    writer.close();
  }
}
