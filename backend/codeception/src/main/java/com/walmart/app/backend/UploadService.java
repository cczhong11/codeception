package com.walmart.app.backend;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class UploadService extends HttpServlet {

  /**
   *
   */
  public UploadService() {}

  @Override
  protected void doPost(final HttpServletRequest request,
                        final HttpServletResponse response)
      throws ServletException, IOException {
    /* TODO:
    1. read data from request and save data to a record class
    2. insert data to cosmosdb
        1. connect cosmosdb with java sdk
        2. insert data in a document
    */
    PrintWriter writer = response.getWriter();
    writer.write("hello world");
    writer.close();
  }
}
