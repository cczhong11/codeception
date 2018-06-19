package com.walmart.app.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

import org.json.JSONArray;
import org.json.JSONObject;
import org.xnio.IoUtils;

public class GetService extends HttpServlet {

  /**
   *
   */
  private DocumentClient client;

  private static Gson gson = new Gson();
  private static String collectionLink = "";

  public GetService() {

    client = new DocumentClient(AccountCredentials.HOST, AccountCredentials.MASTER_KEY, null, null);
    RequestOptions options = new RequestOptions();
    options.setOfferThroughput(400);
    collectionLink = String.format("/dbs/%s/colls/%s", AccountCredentials.databaseId, AccountCredentials.collectionId);

  }

  @Override
  protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException {
    // TODO:
    Record r = retrievePostData(request.getReader());
    JSONObject obj = getResult(r);
    response.setContentType("application/json;charset=UTF-8");
    PrintWriter writer = response.getWriter();
    writer.write(obj.toString());
    writer.close();
  }

  private Record retrievePostData(BufferedReader r) {
    String data = r.lines().collect(Collectors.joining(System.lineSeparator()));
    JSONObject obj = new JSONObject(data);
    Instant instant = Instant.now();
    float x = obj.getFloat("x");
    float y = obj.getFloat("y");

    return new Record(x, y, "", instant.toString(), "");
  }

  private JSONObject getResult(Record r) {
    JSONObject obj = new JSONObject();
    //x lan y lon
    String SQLquery = MessageFormat.format("SELECT * FROM root dest WHERE (dest.y between ({1}-50/abs(cos(radians({0}))*69)) and ({1}+50/abs(cos(radians({0}))*69))) and (dest.x between ({0}-(50/69)) and ({0}+(50/69))) and 3956 * 2 * ASIN(SQRT( POWER(SIN(({0} -dest.x) * pi()/180 / 2), 2) +COS({0}* pi()/180) * COS(dest.x * pi()/180) *POWER(SIN(({1} -dest.y) * pi()/180 / 2), 2))) < 50",r.x,r.y);
    List<Document> documentList = client.queryDocuments(collectionLink, SQLquery, null)
        .getQueryIterable().toList();

    if (documentList.size() > 0) {
      JSONArray arr = new JSONArray();
      List<JSONObject> l = new ArrayList<JSONObject>();
      for (int i = 0; i < documentList.size(); i++) {
        JSONObject object = new JSONObject(documentList.get(i).toJson());
        l.add(object);
      }
      obj.put("data", l);
    } else {
      System.out.println("nothing");

    }
    return obj;
  }
}
