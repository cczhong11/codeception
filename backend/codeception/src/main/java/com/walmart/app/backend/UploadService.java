package com.walmart.app.backend;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.sql.Timestamp;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microsoft.azure.documentdb.DataType;
import com.microsoft.azure.documentdb.Database;
import com.microsoft.azure.documentdb.Document;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.azure.documentdb.DocumentCollection;
import com.microsoft.azure.documentdb.FeedResponse;
import com.microsoft.azure.documentdb.IncludedPath;
import com.microsoft.azure.documentdb.Index;
import com.microsoft.azure.documentdb.IndexingPolicy;
import com.microsoft.azure.documentdb.Offer;
import com.microsoft.azure.documentdb.PartitionKey;
import com.microsoft.azure.documentdb.PartitionKeyDefinition;
import com.microsoft.azure.documentdb.RequestOptions;
import com.microsoft.azure.documentdb.ResourceResponse;

import org.json.JSONObject;

public class UploadService extends HttpServlet {

	private static final String Host = "https://codeception.documents.azure.com:443/";
	private static final String Key = "dgKTSZkHL1ARoljOMAmYejVyjUu7gvVTDnUsovcjCgP3Hh1QlmnvQ7Nn2SGOIIeOjy8amQzd6oOMZBZWiquu6A==";
	private static final String database = "db";
	private static final String collection = "codeception";
	private static Gson gson = new Gson();
	private DocumentClient client;
	
  public UploadService() {
	 // client = new DocumentClient(AccountCredentials.HOST, AccountCredentials.MASTER_KEY, null, null);
	  client = new DocumentClient(Host, Key, null, null);
  }

  @Override
  protected void doPost(final HttpServletRequest request,
                        final HttpServletResponse response)
      throws ServletException, IOException {
	  // TODO:
	  StringBuilder sb = new StringBuilder();
	  String line = null; 
	  try {
		  BufferedReader reader = request.getReader();
		  while ((line = reader.readLine()) != null) {
			  sb.append(line);
		  }
		  reader.close();
	  } catch (IOException ex) {
		  System.out.println(ex);
	  } 
	  
	  JSONObject obj = new JSONObject(sb.toString());
	  String fileLink = obj.getString("filelink");
	  float X = obj.getFloat("x");
	  float Y = obj.getFloat("y");
	  String id = obj.getString("id");
	  String time = obj.getString("time");
	  try {
		  insertDB(fileLink, X, Y);
	  } catch(DocumentClientException ex) {
		  System.out.println(ex);
	  }
 
	  PrintWriter writer = response.getWriter();  
	  writer.write("uploaded");
	  writer.close();
  }
  
  public void insertDB(String fileLink, float X, float Y) throws DocumentClientException {
      String collectionLink = String.format("/dbs/%s/colls/%s", database, collection);
      Document documentDefinition = new Document();
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      documentDefinition.set("id", String.valueOf(timestamp.getTime()));
      documentDefinition.set("filelink", fileLink);
      documentDefinition.set("x", X);
      documentDefinition.set("y", Y);
   //   documentDefinition.set("time", new Timestamp(System.currentTimeMillis()));
      documentDefinition.set("time", String.valueOf(timestamp));
      client.createDocument(collectionLink, documentDefinition, null, true);
  }
}
