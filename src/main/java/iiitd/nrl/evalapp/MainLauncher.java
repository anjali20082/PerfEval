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
    static String studentEmailId = "controlled-new-os";
    static ArrayList<String> defaultApps = new ArrayList<>(Arrays.asList("ytp", "yts", "gn", "gm", "dh", "fl", "fb"));


    private static void addTestsToSuite(List<XmlSuite> suiteFiles, List<XmlClass> classes, String app) {
        XmlSuite suite = new XmlSuite();
        suite.setName(app + "Suite");

        XmlTest test = new XmlTest(suite);
        test.setName(app + "Test");

        test.setClasses(classes);

        suiteFiles.add(suite);
    }

    public static void main(String[] args) throws MalformedURLException {
//        DesiredCapabilities cap = new DesiredCapabilities();
//
//        cap.setCapability("noReset", "true");
//        cap.setCapability("fullReset", "false");
//        cap.setCapability("autoGrantPermissions", true);
//        cap.setCapability("autoAcceptAlerts", true);
//        cap.setCapability("uiautomator2ServerInstallTimeout", 60000);
//
//        URL url;
//        try {
//            url = new URL("http://127.0.0.1:4723/wd/hub");
//            driver = new AndroidDriver<MobileElement>(url, cap);
//        } catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        int versionId;
        ArrayList<String> appsToRun = new ArrayList<>();
        osId = Integer.parseInt(args[0]);
        versionId = Integer.parseInt(args[1]);
        String apps[] = args[2].split(" ");
        for (int i = 0; i < apps.length; ++i) {
            String app = apps[i];
            System.out.println("You entered: " + app);

            appsToRun.add(app);
        }

        studentEmailId = studentEmailId.concat(Integer.toString(osId)).concat("_v" + versionId);
        MyDatabase.setVersionSelected(versionId);
        MyDatabase.setUpDatabase();

        TestNG runner = new TestNG();

        // Create a list of String
        List<XmlSuite> suiteFiles = new ArrayList<XmlSuite>();

        if (appsToRun.contains("yts")) {
            List<XmlClass> yts_classes = new ArrayList<XmlClass>();
            yts_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            yts_classes.add(new XmlClass("iiitd.nrl.evalapp.YouTubeTest_SearchChannel"));
            yts_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, yts_classes, "Youtube_Search");

            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("ytp")) {
            List<XmlClass> ytp_classes = new ArrayList<XmlClass>();
            ytp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            ytp_classes.add(new XmlClass("iiitd.nrl.evalapp.YouTubeTest_PlayVideo"));
            ytp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, ytp_classes, "Youtube_PlayVideo");

            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("fbp")) {
            List<XmlClass> fp_classes = new ArrayList<XmlClass>();
            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.FacebookTestsP"));
            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, fp_classes, "Facebook_Post");

            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("fbs")) {
            List<XmlClass> fs_classes = new ArrayList<XmlClass>();
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.FacebookTestsP_Search"));
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, fs_classes, "Facebook_Search");

            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("fl")) {
            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.FlipkartTests"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "Flipkart");
            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("tga")) {
            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TelegramTestsA"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "TelegramA");
            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("tgp")) {
            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TelegramTestsP"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "TelegramP");
            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("gm")) {
            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.GoogleMapsTest"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "Goglemaps");
            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("gn")) {
            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.GooglenewsTests"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "Googlenews");
            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("dh")) {
            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.DailyhuntTests"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "Dh");
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
