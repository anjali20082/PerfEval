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

//DAILYHUNT:454622:258550:0:0
//YOUTUBE:8591820:220743:2:1
//GOOGLE NEWS:0:0:0:0
//MAPS:435342:66301:0:0


public class MainLauncher {
    static AndroidDriver<MobileElement> driver;
    static int defaultVersionId = 5;
    static String studentEmailId = "final_os";
    static ArrayList<String> defaultApps = new ArrayList<>(Arrays.asList("ytp", "yts", "gn", "gm", "dh"));


    public static void main(String[] args) throws MalformedURLException {
        DesiredCapabilities cap = new DesiredCapabilities();

        cap.setCapability("noReset", "true");
        cap.setCapability("fullReset", "false");
        cap.setCapability("autoGrantPermissions", true);
        cap.setCapability("autoAcceptAlerts", true);
        cap.setCapability("uiautomator2ServerInstallTimeout", 60000);
        URL url;
        try {
            url = new URL("http://127.0.0.1:4723/wd/hub");
            driver = new AndroidDriver<MobileElement>(url, cap);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int versionId, osId;
        ArrayList<String> appsToRun = new ArrayList<>();
        osId = Integer.parseInt(args[0]);
        versionId = Integer.parseInt(args[1]);
        String apps[] = args[2].split(" ");
        for (int i = 0; i < apps.length; ++i) {
            String app = apps[i];
            System.out.println("You entered: " + app);

            appsToRun.add(app);
        }

        studentEmailId = studentEmailId.concat(Integer.toString(osId)).concat("_v" + Integer.toString(versionId));
        MyDatabase.setVersionSelected(versionId);
        MyDatabase.setUpDatabase();
////
        TestNG runner = new TestNG();
        XmlSuite suite = new XmlSuite();
        suite.setName("AppSuite");

        XmlTest test = new XmlTest(suite);
        test.setName("AppiumTests");
        List<XmlClass> classes = new ArrayList<XmlClass>();
//
        classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));

////
        if (appsToRun.contains("ytp")) {
            classes.add(new XmlClass("iiitd.nrl.evalapp.YouTubeTest_PlayVideo"));
            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("yts")) {
            classes.add(new XmlClass("iiitd.nrl.evalapp.YouTubeTest_SearchChannel"));
            MyDatabase.totalTests += 1;
        }
//
        if (appsToRun.contains("gm")) {
            classes.add(new XmlClass("iiitd.nrl.evalapp.GoogleMapsTest"));
            MyDatabase.totalTests += 1;
        }
//
        if (appsToRun.contains("gn")) {
            classes.add(new XmlClass("iiitd.nrl.evalapp.GooglenewsTests"));
            MyDatabase.totalTests += 1;
        }
        if (appsToRun.contains("dh")) {
            classes.add(new XmlClass("iiitd.nrl.evalapp.DailyhuntTests"));
            MyDatabase.totalTests += 1;
        }

        classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
//
//
        test.setXmlClasses(classes) ;

        // Create a list of String
        List<XmlSuite> suitefiles=new ArrayList<XmlSuite>();

        // Add xml file which you have to execute
        suitefiles.add(suite);

        // now set xml file for execution
        runner.setXmlSuites(suitefiles);

        runner.run();
    }
}
