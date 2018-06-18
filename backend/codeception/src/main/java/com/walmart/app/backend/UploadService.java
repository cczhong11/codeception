package com.walmart.app.backend;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.RequestOptions;

import org.json.JSONObject;

import io.undertow.security.idm.Account;

public class UploadService extends HttpServlet {

  /**
   *
   */
  private static DocumentClient client;
  public UploadService() {}

  @Override
  protected void doPost(final HttpServletRequest request,
                        final HttpServletResponse response)
      throws ServletException, IOException {
    // TODO: read from post data
    //
    RequestOptions options = new RequestOptions();
        options.setOfferThroughput(400);
        String collectionLink = String.format("/dbs/%s/colls/%s", AccountCredentials.databaseId, AccountCredentials.collectionId);
        try{
          String collectionResourceId = client.readCollection(collectionLink, options).getResource().getResourceId();
          
        }catch(Exception e){
          e.printStackTrace();
        }
        
    PrintWriter writer = response.getWriter();
    writer.write("hello world");
    writer.close();
  }
}
