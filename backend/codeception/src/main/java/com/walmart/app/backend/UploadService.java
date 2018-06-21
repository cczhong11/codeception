package com.walmart.app.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.microsoft.azure.documentdb.Document;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.RequestOptions;

import org.json.JSONObject;

import io.undertow.security.idm.Account;

public class UploadService extends HttpServlet {

  /**
   *
   */
  private static DocumentClient client;
  private static Gson gson = new Gson();
  private static String collectionLink = "";
  
  public UploadService() {
    
    client = new DocumentClient(AccountCredentials.HOST,AccountCredentials.MASTER_KEY,null,null);
    RequestOptions options = new RequestOptions();
    options.setOfferThroughput(400);
    collectionLink = String.format("/dbs/%s/colls/%s", AccountCredentials.databaseId, AccountCredentials.collectionId);

  }

  @Override
  protected void doPost(final HttpServletRequest request,
                        final HttpServletResponse response)
      throws ServletException, IOException {
  
    Record r = retrievePostData(request.getReader());
    insertData(client,r);
    PrintWriter writer = response.getWriter();
    writer.write("success");
    writer.close();
  }
  /**
   *
   * args: request.getreader
   *  this function will change data to record 
   */
   
  private Record retrievePostData(BufferedReader r){
    String data = r.lines().collect(Collectors.joining(System.lineSeparator()));
    JSONObject obj = new JSONObject(data);
    Instant instant = Instant.now();
    float x = obj.getFloat("x");
    float y = obj.getFloat("y");
    String filelink = obj.getString("filelink");
    String[] f = filelink.split("\\+");
    filelink = f[0];
    String filelink2 = f[1];
    String username = obj.getString("username");
    Record newRecord;
    try{
      String description = obj.getString("description");
      newRecord = new Record(x,y, username, instant.toString(), filelink,filelink2,description);
    }catch(Exception e){
      newRecord=new Record(x,y, username, instant.toString(), filelink,filelink2,"");
    }
    return newRecord;
  }
  
  private void insertData(DocumentClient client, Record r){
    
      Instant instant = Instant.now();
          Document documentDefinition = new Document();
          documentDefinition.set("id",instant.toString());
          documentDefinition.set("x", r.x);
          documentDefinition.set("y", r.y);
          documentDefinition.set("time",r.time);
          documentDefinition.set("filelink",r.filelink);
          documentDefinition.set("Thunmblink",r.Thumbfilelink);
          
          try{
              client.createDocument(collectionLink, documentDefinition, null, true);
          }
          catch(Exception e){
              e.printStackTrace();
          }

      }
}
