package iiitd.nrl.evalapp;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainLauncher {
    static AndroidDriver<MobileElement> driver;
    static int defaultVersionId = 1, osId;
    static String studentEmailId = "test";
    static ArrayList<String> appsToRun = new ArrayList<>(Arrays.asList("amz1","amz2","amz3","amz4","amz5","dht",
            "fbp","fbs1","fbs2","flp1","flp2","flp3","flp4","flp5","gmp",
            "gnw","hts1","hts2","htt","lnc","lnp","lns","tgm","wht","ytp1","ytp2","yts"));


    private static void addTestsToSuite(List<XmlSuite> suiteFiles, List<XmlClass> classes, String app) {
        XmlSuite suite = new XmlSuite();
        suite.setName(app + "Suite");

        XmlTest test = new XmlTest(suite);
        test.setName(app + "Test");

        test.setClasses(classes);

        suiteFiles.add(suite);
    }

    public static void main(String[] args) throws MalformedURLException {


//        int versionId;
//        ArrayList<String> appsToRun = new ArrayList<>();
//        osId = Integer.parseInt(args[0]);
//        versionId = Integer.parseInt(args[1]);
//        String apps[] = args[2].split(" ");
//        for (int i = 0; i < apps.length; ++i) {
//            String app = apps[i];
//            System.out.println("You entered: " + app);
//
//            appsToRun.add(app);
//        }
//
//        studentEmailId = studentEmailId.concat(Integer.toString(osId)).concat("_v" + versionId);
//        MyDatabase.setVersionSelected(versionId);
        MyDatabase.setUpDatabase();

        TestNG runner = new TestNG();

        // Create a list of String
        List<XmlSuite> suiteFiles = new ArrayList<XmlSuite>();


        if (appsToRun.contains("amz1")) {
            List<XmlClass> fp_classes = new ArrayList<XmlClass>();
            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.Amazon_search"));
//            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, fp_classes, "Amazon_Test1");

            MyDatabase.totalTests += 1;
        }
        if (appsToRun.contains("amz2")) {
            List<XmlClass> fp_classes = new ArrayList<XmlClass>();
            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.Amazon_Profile"));
//            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, fp_classes, "Amazon_Test2");

            MyDatabase.totalTests += 1;
        }
        if (appsToRun.contains("amz3")) {
            List<XmlClass> fp_classes = new ArrayList<XmlClass>();
            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.Amazon_Add"));
//            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, fp_classes, "Amazon_Test3");

            MyDatabase.totalTests += 1;
        }
        if (appsToRun.contains("amz4")) {
            List<XmlClass> fp_classes = new ArrayList<XmlClass>();
            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.Amazon_GoCart"));
//            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, fp_classes, "Amazon_Test4");

            MyDatabase.totalTests += 1;
        }
        if (appsToRun.contains("amz5")) {
            List<XmlClass> fp_classes = new ArrayList<XmlClass>();
            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.Amazon_Remove"));
//            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, fp_classes, "Amazon_Test5");

            MyDatabase.totalTests += 1;
        }
        if (appsToRun.contains("dht")) {
            List<XmlClass> fs_classes = new ArrayList<XmlClass>();
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.DailyhuntTests"));
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, fs_classes, "Dailyhunt_Test");

            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("flp1")) {
            List<XmlClass> fs_classes = new ArrayList<XmlClass>();
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.Flipkart_action1"));
//            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, fs_classes, "Flipkart_Test1");

            MyDatabase.totalTests += 1;
        }
        if (appsToRun.contains("flp2")) {
            List<XmlClass> fs_classes = new ArrayList<XmlClass>();
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.Flipkart_action2"));
//            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, fs_classes, "Flipkart_Test2");

            MyDatabase.totalTests += 1;
        }
        if (appsToRun.contains("flp3")) {
            List<XmlClass> fs_classes = new ArrayList<XmlClass>();
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.Flipkart_action3"));
//            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, fs_classes, "Flipkart_Test3");

            MyDatabase.totalTests += 1;
        }
        if (appsToRun.contains("flp4")) {
            List<XmlClass> fs_classes = new ArrayList<XmlClass>();
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.Flipkart_action4"));
//            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, fs_classes, "Flipkart_Test4");

            MyDatabase.totalTests += 1;
        }
        if (appsToRun.contains("flp5")) {
            List<XmlClass> fs_classes = new ArrayList<XmlClass>();
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.Flipkart_action5"));
//            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, fs_classes, "Flipkart_Test5");

            MyDatabase.totalTests += 1;
        }
        if (appsToRun.contains("gmp")) {
            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.GoogleMapsTest"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "GoogleMaps");
            MyDatabase.totalTests += 1;
        }
        if (appsToRun.contains("gnw")) {
            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.GooglenewsTests"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "GooglenewsTests");
            MyDatabase.totalTests += 1;
        }
        if (appsToRun.contains("hts1")) {
            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.Hotstar_SearchVid"));
//            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "HotstarTests_search1");
            MyDatabase.totalTests += 1;
        }
        if (appsToRun.contains("hts2")) {
            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.Hotstar_PlayVid"));
//            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "HotstarTests_search2");
            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("htt")) {
            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.HotstarTests_trending"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "HotstarTests_trending");
            MyDatabase.totalTests += 1;
        }
        if (appsToRun.contains("lnc")) {
            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.LinkedInTests_myConnections"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "LinkedInTests_myConnections");
            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("lnp")) {
            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.LinkedInTests_profile"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "LinkedInTests_profile");
            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("lns")) {
            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.LinkedInTests_search"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "LinkedInTests_search");
            MyDatabase.totalTests += 1;
        }

//        if (appsToRun.contains("ptm")) {
//            List<XmlClass> classes = new ArrayList<XmlClass>();
//            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
//            classes.add(new XmlClass("iiitd.nrl.evalapp.PaytmTests"));
//            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
//            addTestsToSuite(suiteFiles, classes, "PaytmTests");
//            MyDatabase.totalTests += 1;
//        }

        if (appsToRun.contains("tgm")) {
            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TelegramTests"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "TelegramTests");
            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("wht")) {
            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.WhatsappTests"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "WhatsappTests");
            MyDatabase.totalTests += 1;
        }
        if (appsToRun.contains("yts")) {
            List<XmlClass> yts_classes = new ArrayList<XmlClass>();
            yts_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            yts_classes.add(new XmlClass("iiitd.nrl.evalapp.YouTubeTest_SearchChannel"));
            yts_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, yts_classes, "Youtube_Search");

            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("ytp1")) {
            List<XmlClass> ytp_classes = new ArrayList<XmlClass>();
            ytp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            ytp_classes.add(new XmlClass("iiitd.nrl.evalapp.Youtube_SearchVid"));
//            ytp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, ytp_classes, "Youtube_PlayVideo1");

            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("ytp2")) {
            List<XmlClass> ytp_classes = new ArrayList<XmlClass>();
            ytp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            ytp_classes.add(new XmlClass("iiitd.nrl.evalapp.Youtube_PlayVid"));
//            ytp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, ytp_classes, "Youtube_PlayVideo2");

            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("fbp")) {
            List<XmlClass> fs_classes = new ArrayList<XmlClass>();
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.FacebookTestsP"));
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, fs_classes, "FacebookTests_post");

            MyDatabase.totalTests += 1;
        }
        if (appsToRun.contains("fbs1")) {
            List<XmlClass> fs_classes = new ArrayList<XmlClass>();
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.Facebook_Search"));
//            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, fs_classes, "FacebookTests_search1");

            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("fbs2")) {
            List<XmlClass> fs_classes = new ArrayList<XmlClass>();
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.Facebook_PersonProfile"));
//            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, fs_classes, "FacebookTests_search2");

            MyDatabase.totalTests += 1;
        }

        if (appsToRun.size() > 0) {
            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.PingUpload"));
            addTestsToSuite(suiteFiles, classes, "Ping");
        }

        // now set xml file for execution
        runner.setXmlSuites(suiteFiles);
        runner.run();
    }
}
