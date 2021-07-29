package iiitd.nrl.evalapp;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class MyDatabase {
    public static String packet_sizes_before;
    public static String packet_sizes_after;
    protected static float version = 8.0f;
    protected static int testTimeLimit = 15;
    protected static int count = 0;
    protected static int totalTests = 0;
    public static MongoClient mongoClient;
    public static MongoDatabase database;
    public static MongoCollection<Document> student_collection;

    public static String appJsonCommands;
    public static String commands;
    public static String currentApp;
    public static String testStatusReason;
    public static boolean testStatus;
    public static String connType;


    public static void setCommands(String commands) {
        MyDatabase.commands = commands;
    }

    public static void setAppJsonCommands(String appJsonCommands) {
        MyDatabase.appJsonCommands = appJsonCommands;
    }

    public static void setCurrentApp(String currentApp) {
        MyDatabase.currentApp = currentApp;
    }

    public static void setTestStatus(boolean testStatus) {
        MyDatabase.testStatus = testStatus;
    }

    public static void setTestStatusReason(String testStatusReason) {
        MyDatabase.testStatusReason = testStatusReason;
    }

    public static void setConnType(String connType) {
        MyDatabase.connType = connType;
    }

    public static void setPacket_sizes_before(String packet_sizes_before) {
        MyDatabase.packet_sizes_before = packet_sizes_before;
    }

    public static void setPacket_sizes_after(String packet_sizes_after) {
        MyDatabase.packet_sizes_after = packet_sizes_after;
    }

    public static void setUpDatabase()
    {
//        String uri = "mongodb+srv://admin17080:prince%40123@cluster0.ssjoc.gcp.mongodb.net/TestingApps?retryWrites=true&w=majority";
        String uri = "mongodb+srv://admin17080:test123@cluster0.ssjoc.gcp.mongodb.net/TestingApps?retryWrites=true&w=majority";
        mongoClient = MongoClients.create(uri);
        database = mongoClient.getDatabase("TestingApps");
    }

    public static void testsStarted()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String currentTime = dtf.format(now);
        student_collection = database.getCollection(MainLauncher.studentEmailId);

        Document document = new Document("Location", MainLauncher.studentLocation);
        document.append("Tests Started at", currentTime);
        document.append("App Tests Version", version);

        student_collection.insertOne(document);
    }

    public static long getTimeTaken(String jsonString, int startIndex, int endIndex) {

        JSONParser parser = new JSONParser();

        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONArray commands = (JSONArray) jsonObject.get("commands");

        if (startIndex < 0)
            startIndex = commands.size() + startIndex;

        if (endIndex < 0)
            endIndex = commands.size() + endIndex;

        JSONObject startEvent = (JSONObject) commands.get(startIndex);
        JSONObject endEvent = (JSONObject) commands.get(endIndex);

        long startTime = (long) startEvent.get("startTime");
        long endTime = (long) endEvent.get("endTime");
        long timeTaken = endTime - startTime; // time difference in mill seconds

        return timeTaken;
    }

    public static void addTestResult(String appName, String testName, HashMap<String, Long> main_events, String connType, boolean testStatus, String testStatusReason)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String currentTime = dtf.format(now);

        count++;
        System.out.println("Test No. " + count + "/" + totalTests + " Completed");
        System.out.println("App: " + appName);
        System.out.println("Test: " + testName);
        System.out.println("Status: " + testStatus + "\n");
        System.out.println("Reason: " + testStatusReason + "\n");
        System.out.println("Time Taken: " + main_events.toString());

        Document document = new Document("startedAt", currentTime);
        document.append("app", appName);
        document.append("test", testName);
        document.append("times", main_events);
        document.append("connType", connType);
        document.append("status", testStatus);
        document.append("reason", testStatusReason);

        student_collection.insertOne(document);
    }

    public static void addTestResult() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String currentTime = dtf.format(now);

        count++;
        System.out.println("Test No. " + count + "/" + totalTests + " Completed");
        System.out.println("App: " + MyDatabase.currentApp);
        System.out.println("Status: " + MyDatabase.testStatus + "\n");
        System.out.println("Reason: " + MyDatabase.testStatusReason + "\n");
        System.out.println("JSON: " + MyDatabase.appJsonCommands);

        Document document = new Document("startedAt", currentTime);
        document.append("app", MyDatabase.currentApp);
        document.append("json", MyDatabase.appJsonCommands);
        document.append("commands", MyDatabase.commands);
        document.append("connType", MyDatabase.connType);
        document.append("status", MyDatabase.testStatus);
        document.append("reason", MyDatabase.testStatusReason);
        document.append("data_before", packet_sizes_before);
        document.append("data_after", packet_sizes_after);

        MyDatabase.setPacket_sizes_before(MyDatabase.packet_sizes_after);

        student_collection.insertOne(document);

        MyDatabase.setCurrentApp("");
        MyDatabase.setAppJsonCommands("");
        MyDatabase.setTestStatus(false);
        MyDatabase.setTestStatusReason("");
        MyDatabase.setConnType("");
        MyDatabase.setCommands("");
    }

    public static void sendPINGLog() {
        List<String> ping_files = List.of("www.google.com.log", "www.amazon.com.log", "www.mobikwik.com.log");
//        List<String> ping_files = List.of("www.google.com.log");
        List<String> filenames = List.of("Google", "Amazon", "Mobikwik");
//        List<String> filenames = List.of("google");



        int i = 0;
        for (String filename:ping_files) {
            File file = new File(filename);
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
                String line;
                String pingLogFile = "";

//                Ping format:
//                Date:08-11-20 17:43:22
//                PING www.mobikwik.com.cdn.cloudflare.net (104.18.2.135) 56(84) bytes of data.
//                64 bytes from 104.18.2.135: icmp_seq=1 ttl=56 time=49.5 ms
//
//                        --- www.mobikwik.com.cdn.cloudflare.net ping statistics ---
//                        1 packets transmitted, 1 received, 0% packet loss, time 0ms
//                rtt min/avg/max/mdev = 49.572/49.572/49.572/0.000 ms

                String datetime = "";
                String ttl = "";
                String time = "";

                JSONArray jsonArray = new JSONArray();

                while ((line = br.readLine()) != null) {
                    if (line.contains("Date"))
                        datetime = line.substring(line.indexOf(":")+1);

                    while ((line = br.readLine()) != null) {
                        if (line.contains("icmp_seq=1")) {
                            ttl = line.substring(line.indexOf("ttl=")+4, line.indexOf("time=")-1);
                            time = line.substring(line.indexOf("time=")+5);
                        }
                        else if (line.contains("rtt min/avg/max/mdev"))
                            break;
                    }

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("t", datetime);
                    jsonObject.put("ttl", ttl);
                    jsonObject.put("rt", time);
                    jsonArray.add(jsonObject);

                }
                pingLogFile += jsonArray.toJSONString();

                System.out.println("Time uploaded: " + System.currentTimeMillis());
                Document pingDocument = new Document("Time Uploaded", System.currentTimeMillis());
                pingDocument.append("ping" + filenames.get(i++), pingLogFile);

                student_collection.insertOne(pingDocument);
            } catch (FileNotFoundException e) {
                Document pingDocument = new Document("Time Uploaded", System.currentTimeMillis());
                pingDocument.append("pingFailed", e.toString());
            } catch (IOException e) {
                Document pingDocument = new Document("Time Uploaded", System.currentTimeMillis());
                pingDocument.append("pingFailed", e.toString());
            }
        }
    }

    public static void uploadPacketsData() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String currentTime = dtf.format(now);

        Document document = new Document("uploadedAt", currentTime);
        document.append("data_before", packet_sizes_before);
        document.append("data_after", packet_sizes_after);
        student_collection.insertOne(document);
    }
}
