package iiitd.nrl.evalapp;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyDatabase {
    protected static float version = 1.7f;
    protected static int count = 0;
    protected static int totalTests = 0;
    public static MongoClient mongoClient;
    public static MongoDatabase database;
    public static MongoCollection<Document> student_collection;

    public static void setUpDatabase()
    {
        String uri = "mongodb+srv://admin17080:Shadow%40ps99@cluster0.ssjoc.gcp.mongodb.net/TestingApps?retryWrites=true&w=majority";
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("TestingApps");
        System.out.println("Database setup done");
    }

    public static void testsStarted()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        String currentTime = dtf.format(now);
        student_collection = database.getCollection(WelcomePageLauncher.studentEmailId);

        Document document = new Document("Location", WelcomePageLauncher.studentLocation);
        document.append("Tests Started at", currentTime);
        document.append("App Tests Version", version);

        student_collection.insertOne(document);
//        MyDatabase.sendPINGLog();

    }

    public static void addTestResult(String appName, String testName, long time, String connType, boolean testStatus)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String currentTime = dtf.format(now);

        count++;
        System.out.println("Test No. " + count + "/" + totalTests + " Completed");
        System.out.println("App: " + appName);
        System.out.println("Test: " + testName);
        System.out.println("Status: " + testStatus + "\n");

        Document document = new Document("Test Started at", currentTime);
        document.append("App Name", appName);
        document.append("Test Name", testName);
        document.append("Time Taken", time);
        document.append("ConnectionType", connType);
        document.append("Test Passed", testStatus);

        student_collection.insertOne(document);
    }

    public static void sendPINGLog() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String currentTime = dtf.format(now);

        File file = new File("ping_log.log");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            String pingLogFile = "";

            while ((line = br.readLine()) != null) {
                pingLogFile += line + "\n";
            }

            Document pingDocument = new Document("Time Uploaded", currentTime);
            pingDocument.append("Ping file", pingLogFile);

            student_collection.insertOne(pingDocument);
//            GridFSBucket bucket = GridFSBuckets.create(database);
//            InputStream inputStream = new FileInputStream(file);
//            GridFSUploadOptions uploadOptions = new GridFSUploadOptions().chunkSizeBytes(1024).metadata(new Document("type", "text").append("upload_date", currentTime).append("content_type", "text"));
//            ObjectId fileId = bucket.uploadFromStream("ping_log.log", inputStream, uploadOptions);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
