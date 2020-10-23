package iiitd.nrl.evalapp;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyDatabase {
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
        student_collection = database.getCollection(Main.studentEmailId);

        Document document = new Document("Location", Main.studentLocation);
        document.append("Tests Started at", currentTime);
        document.append("App Tests Version", Main.version);

        student_collection.insertOne(document);
    }

    public static void addTestResult(String appName, String testName, long time, String connType, boolean testStatus)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String currentTime = dtf.format(now);

        Main.count++;
        System.out.println("Test No. " + Main.count + "/" + Main.totalTests + " Completed");
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
}
