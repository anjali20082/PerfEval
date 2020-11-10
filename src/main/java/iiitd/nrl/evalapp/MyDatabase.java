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
import java.util.List;

public class MyDatabase {
    protected static float version = 2.1f;
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
    }

    public static long getTimeTaken(String jsonString, int startOffset, int endOffset) {

        JSONParser parser = new JSONParser();

        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONArray commands = (JSONArray) jsonObject.get("commands");
        JSONObject startEvent = (JSONObject) commands.get(commands.size() + startOffset);
        JSONObject endEvent = (JSONObject) commands.get(commands.size() + endOffset);

        long startTime = (long) startEvent.get("startTime");
        long endTime = (long) endEvent.get("endTime");
        long timeTaken = endTime - startTime; // time difference in mill seconds

        return timeTaken;
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
        List<String> ping_files = List.of("www.google.com.log", "www.amazon.com.log", "www.mobikwik.com.log");
        List<String> filenames = List.of("google", "amazon", "mobikwik");

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
                    jsonObject.put("Timestamp", datetime);
                    jsonObject.put("TTL", ttl);
                    jsonObject.put("ResponseTime", time);

                    pingLogFile += jsonObject.toJSONString() + ",";
                }

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String currentTime = dtf.format(now);

                Document pingDocument = new Document("Time Uploaded", currentTime);
                pingDocument.append("ping-" + filenames.get(i++), pingLogFile);

//                student_collection = database.getCollection("prince17080");
                student_collection.insertOne(pingDocument);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
