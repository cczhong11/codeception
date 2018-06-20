package com.walmart.app.backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
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
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import io.undertow.security.idm.Account;

public class UploadImageService extends HttpServlet {

    /**
     *
     */
    private static CloudStorageAccount storageAccount;
    private static CloudBlobClient blobClient = null;
    private static CloudBlobContainer container = null;
    public static final String storageConnectionString = "DefaultEndpointsProtocol=https;" + "AccountName=cynosure;"
            + "AccountKey=" + System.getenv("STORAGEKEY") + ";";

    public UploadImageService() {
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
            File sourceFile = File.createTempFile(sha256hex, ".jpeg");
            filename = sourceFile.getName();
            Writer output = new BufferedWriter(new FileWriter(sourceFile));
            output.write(data);
            output.close();

            // Getting a blob reference
            CloudBlockBlob blob = container.getBlockBlobReference(sourceFile.getName());

            // Creating blob and uploading file to it
            System.out.println("Uploading the sample file ");
            blob.uploadFromFile(sourceFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }
        return filename;
    }

}
