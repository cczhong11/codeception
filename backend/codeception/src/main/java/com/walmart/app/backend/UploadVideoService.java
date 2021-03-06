package com.walmart.app.backend;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.google.gson.Gson;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.RequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.json.JSONObject;
import org.xnio.IoUtils;

import io.undertow.security.idm.Account;


public class UploadVideoService extends HttpServlet {

    /**
     *
     */
    private static CloudStorageAccount storageAccount;
    private static CloudBlobClient blobClient = null;
    private static CloudBlobContainer container = null;
    public static final String storageConnectionString = "DefaultEndpointsProtocol=https;" + "AccountName=cynosure;"
            + "AccountKey=" + System.getenv("STORAGEKEY") + ";";

    public UploadVideoService() {
        try {
            storageAccount = CloudStorageAccount.parse(storageConnectionString);
            blobClient = storageAccount.createCloudBlobClient();
            container = blobClient.getContainerReference("cynosure");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

        String filelink = retrievePostData(request.getReader());
        //String filelink = retrievePostData2(request);

        PrintWriter writer = response.getWriter();
        //System.out.println(filelink);
        writer.write(filelink);
        writer.close();
    }

    /**
     *
     * args: request.getreader this function will change data to record
     */

    private String retrievePostData(BufferedReader r) {
        String data = r.lines().collect(Collectors.joining(System.lineSeparator()));
        String sha256hex = DigestUtils.sha256Hex(data);
        String filename;
        try {
            File sourceFile = File.createTempFile(sha256hex, ".mov");
            
            filename = sourceFile.getName();
            Writer output = new BufferedWriter(new FileWriter(sourceFile));
            output.write(data);
            output.close();
            CloudBlockBlob blob = container.getBlockBlobReference(sourceFile.getName());
            blob.getProperties().setContentType("video/quicktime");
            blob.uploadFromFile(sourceFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }
        System.out.println(filename);
        return filename;
    }
    
    private String retrievePostData2(HttpServletRequest r) {
       
       
        String data = r.getParameter("video_upload_file");
        
        String filename;
        try {
            Part p = r.getPart("video_upload_file");
            String sha256hex = DigestUtils.sha256Hex(Instant.now().toString());
            File sourceFile = File.createTempFile(sha256hex, ".mov");
            
            filename = sourceFile.getName();
            Writer output = new BufferedWriter(new FileWriter(sourceFile));
            java.io.InputStream input= p.getInputStream();
            java.nio.file.Files.copy(
      input, 
      sourceFile.toPath(), 
      StandardCopyOption.REPLACE_EXISTING);
 input.close();
            CloudBlockBlob blob = container.getBlockBlobReference(sourceFile.getName());
            blob.uploadFromFile(sourceFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }
        System.out.println(filename);
        return filename;
    }

}
