package iiitd.nrl.evalapp;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

//DAILYHUNT:454622:258550:0:0
//YOUTUBE:8591820:220743:2:1
//GOOGLE NEWS:0:0:0:0
//MAPS:435342:66301:0:0


public class MainLauncher {
    static String studentEmailId = "arani@iiitd.ac.in";
    static String studentLocation = "110020";
    static String defaultApps = "yt gn gm dh tg fb hs li pt am fk wa";


    private static void addTestsToSuite(List<XmlSuite> suiteFiles, List<XmlClass> classes, String app) {
        XmlSuite suite = new XmlSuite();
        suite.setName(app + "Suite");

        XmlTest test = new XmlTest(suite);
        test.setName(app + "Test");

        test.setClasses(classes);

        suiteFiles.add(suite);
    }

    public static void main(String[] args) throws MalformedURLException {

        ArrayList<String> appsToRun = new ArrayList<>();
        System.out.println(args[0]);

        args[0] = args[0].trim();
        String apps[];
        if (args[0].equals("all")) {
            System.out.println("Running tests for all apps");
            apps = defaultApps.split(" ");
        }
        else {
            apps = args[0].split(" ");
        }

        for (int i = 0; i < apps.length; ++i) {
            String app = apps[i];
            System.out.println("You entered: " + app);

            appsToRun.add(app);
        }

        MyDatabase.setUpDatabase();
        MyDatabase.testsStarted();

        TestNG runner = new TestNG();

        // Create a list of String
        List<XmlSuite> suiteFiles = new ArrayList<XmlSuite>();

        List<XmlClass> tb_classes = new ArrayList<XmlClass>();
        tb_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
        addTestsToSuite(suiteFiles, tb_classes, "TB");


        if (appsToRun.contains("yt")) {
            System.out.println("Running YouTube tests");
            List<XmlClass> yts_classes = new ArrayList<XmlClass>();
            yts_classes.add(new XmlClass("iiitd.nrl.evalapp.YouTubeTests_SearchChannel"));
            yts_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, yts_classes, "Yts");

            List<XmlClass> ytp_classes = new ArrayList<XmlClass>();
            ytp_classes.add(new XmlClass("iiitd.nrl.evalapp.YouTubeTests_PlayVideo"));
            ytp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, ytp_classes, "Ytp");

            MyDatabase.totalTests += 2;
        }

        if (appsToRun.contains("hs")) {
            System.out.println("Running HotStar tests");

            List<XmlClass> hs_classes = new ArrayList<XmlClass>();
            hs_classes.add(new XmlClass("iiitd.nrl.evalapp.HotstarTests_search"));
            hs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, hs_classes, "Hs");


            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.HotstarTests_trending"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "Ht");


            MyDatabase.totalTests += 2;
        }

        if (appsToRun.contains("li")) {
            System.out.println("Running LinkedIn tests");

            List<XmlClass> lp_classes = new ArrayList<XmlClass>();
            lp_classes.add(new XmlClass("iiitd.nrl.evalapp.LinkedInTests_profile"));
            lp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, lp_classes, "Lp");

            List<XmlClass> lc_classes = new ArrayList<XmlClass>();
            lc_classes.add(new XmlClass("iiitd.nrl.evalapp.LinkedInTests_myConnections"));
            lc_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, lc_classes, "Lc");

            List<XmlClass> ls_classes = new ArrayList<XmlClass>();
            ls_classes.add(new XmlClass("iiitd.nrl.evalapp.LinkedInTests_search"));
            ls_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, ls_classes, "Ls");

            MyDatabase.totalTests += 3;
        }

        if (appsToRun.contains("fb")) {
            System.out.println("Running Facebook tests");

            List<XmlClass> fp_classes = new ArrayList<XmlClass>();
            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.FacebookTests_post"));
            fp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, fp_classes, "Fp");

            List<XmlClass> fs_classes = new ArrayList<XmlClass>();
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.FacebookTests_search"));
            fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, fs_classes, "Fs");

            MyDatabase.totalTests += 2;
        }

        if (appsToRun.contains("fk")) {
            System.out.println("Running Flipkart tests");

            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.FlipkartTests"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "Fk");
            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("am")) {
            System.out.println("Running Amazon tests");

            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.AmazonTests"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "Am");
            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("tg")) {
            System.out.println("Running Telegram tests");

            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.TelegramTests"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "Tg");
            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("wa")) {
            System.out.println("Running WhatsApp tests");

            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.WhatsappTests"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "Wa");
            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("gm")) {
            System.out.println("Running Google Maps tests");

            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.GoogleMapsTest"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "Gm");
            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("gn")) {
            System.out.println("Running Google News tests");

            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.GooglenewsTests"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "Gn");
            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("dh")) {
            System.out.println("Running DailyHunt tests");

            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.DailyhuntTests"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "Dh");
            MyDatabase.totalTests += 1;
        }

        if (appsToRun.contains("pt")) {
            System.out.println("Running Paytm tests");

            List<XmlClass> classes = new ArrayList<XmlClass>();
            classes.add(new XmlClass("iiitd.nrl.evalapp.PaytmTests"));
            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
            addTestsToSuite(suiteFiles, classes, "Pt");
            MyDatabase.totalTests += 1;
        }

        // now set xml file for execution
        runner.setXmlSuites(suiteFiles);

        runner.run();
    }
}
