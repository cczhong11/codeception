package hello;

import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.microsoft.azure.documentdb.Document;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.RequestOptions;

import org.json.JSONObject;

@RestController
public class HelloController {

    private static DocumentClient client;
  private static Gson gson = new Gson();
  private static String collectionLink = "";

    
    
  
    public HelloController(){
        client = new DocumentClient(AccountCredentials.HOST,AccountCredentials.MASTER_KEY,null,null);
        RequestOptions options = new RequestOptions();
        options.setOfferThroughput(400);
        collectionLink = String.format("/dbs/%s/colls/%s", AccountCredentials.databaseId, AccountCredentials.collectionId);
    
    }
    @ResponseBody
    @RequestMapping(value = {"/upload"}, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public void dopost(HttpServletRequest request,
    final HttpServletResponse response){
        Record r;
        try{
            r = retrievePostData(request.getReader());
        }catch(Exception e){
            
        }
        insertData(client,r);
        PrintWriter writer = response.getWriter();
        writer.write("success");
        writer.close();
    }
    private Record retrievePostData(BufferedReader r){
        String data = r.lines().collect(Collectors.joining(System.lineSeparator()));
        JSONObject obj = new JSONObject(data);
        Instant instant = Instant.now();
        float x = obj.getFloat("x");
        float y = obj.getFloat("y");
        String filelink = obj.getString("filelink");
        String username = obj.getString("username");
        return new Record(x,y, username, instant.toString(), filelink);
      }
      
      private void insertData(DocumentClient client, Record r){
        
          Instant instant = Instant.now();
              Document documentDefinition = new Document();
              documentDefinition.set("id",instant.toString());
              documentDefinition.set("x", r.x);
              documentDefinition.set("y", r.y);
              documentDefinition.set("time",r.time);
              documentDefinition.set("filelink",r.filelink);
              
              try{
                  client.createDocument(collectionLink, documentDefinition, null, true);
              }
              catch(Exception e){
                  e.printStackTrace();
              }
    
          }
}
