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
import java.time.Instant;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
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
        String filename,filename2;
        try {
            // Raw file 
            File sourceFile = File.createTempFile(sha256hex, ".jpeg");
            filename = sourceFile.getName();
            Writer output = new BufferedWriter(new FileWriter(sourceFile));
            output.write(data);
            output.close();


            int start = data.indexOf(",");
            data = data.substring(start + 1);

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(javax.xml.bind.DatatypeConverter.parseBase64Binary(data)));
            BufferedImage thumbImg = Scalr.resize(image, Method.ULTRA_QUALITY,
                    Mode.AUTOMATIC, 200, 200);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbImg, "PNG", baos);
            byte[] encodeBase64 = Base64.encodeBase64(baos.toByteArray());

            String base64Encoded = new String(encodeBase64);
            base64Encoded = "data:image/jpeg;base64,"+base64Encoded;
            baos.close();

            // thumbnail file
            File sourceFile2 = File.createTempFile("thumbnail", ".jpeg");
            filename2 = sourceFile2.getName();
            Writer output2 = new BufferedWriter(new FileWriter(sourceFile2));
            output2.write(base64Encoded);
            output2.close();
            // Getting a blob reference
            CloudBlockBlob blob = container.getBlockBlobReference(sourceFile.getName());
            blob.uploadFromFile(sourceFile.getAbsolutePath());
            
            CloudBlockBlob blob2 = container.getBlockBlobReference(sourceFile2.getName());
            
            blob2.uploadFromFile(sourceFile2.getAbsolutePath());
            System.out.println("Uploading the sample file ");

        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }
        System.out.println(filename+"+"+filename2);
        return filename+"+"+filename2;
    }

}
