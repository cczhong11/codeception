package com.walmart.app.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.microsoft.azure.documentdb.Database;
import com.microsoft.azure.documentdb.Document;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.azure.documentdb.DocumentCollection;
import org.json.JSONObject;

public class GetService extends HttpServlet {

  /**
   *
   */
  private DocumentClient client;
  public GetService() {
    client = new DocumentClient(AccountCredentials.HOST,AccountCredentials.MASTER_KEY,null,null);
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

  }
}
