package com.walmart.app.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.microsoft.azure.documentdb.Database;
import com.microsoft.azure.documentdb.Document;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.azure.documentdb.DocumentCollection;
import com.microsoft.azure.documentdb.RequestOptions;

import org.json.JSONObject;

public class GetService extends HttpServlet {

  /**
   *
   */
  private DocumentClient client;
  
  private static Gson gson = new Gson();
  private static String collectionLink = "";
  public GetService() {
    
    client = new DocumentClient(AccountCredentials.HOST,AccountCredentials.MASTER_KEY,null,null);
    RequestOptions options = new RequestOptions();
    options.setOfferThroughput(400);
    collectionLink = String.format("/dbs/%s/colls/%s", AccountCredentials.databaseId, AccountCredentials.collectionId);
        
  }

  @Override
  protected void doPost(final HttpServletRequest request,
                        final HttpServletResponse response)
      throws ServletException, IOException {
    // TODO:
    
    Record r = retrievePostData(request.getReader());
    insertData(client,r);
    PrintWriter writer = response.getWriter();
    writer.write("success");
    writer.close();
  }
  private Record retrievePostData(BufferedReader r){
    return null;
  }
  
  private void insertData(DocumentClient client, Record r){
    Document newRecord = new Document(gson.toJson(r));

  }
}
