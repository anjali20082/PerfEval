package iiitd.nrl.evalapp;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public class WelcomePageLauncher extends Application {

    @FXML
    protected Button about, back, proceed;

    @FXML
    protected TextField email, locn;

    @FXML
    protected Label alert;

    @FXML
    protected CheckBox amazon_cb, flipkart_cb, youtube_cb, hotstar_cb, linkedin_cb, facebook_cb, googlenews_cb, dailyhunt_cb, paytm_cb, telegram_cb, whatsapp_cb, googlemaps_cb;

    protected static String studentEmailId, studentLocation;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("WelcomePage.fxml"));
        AnchorPane root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.setHeight(700);
        primaryStage.setWidth(570);

        primaryStage.setTitle("EvalApp:" + MyDatabase.version);
        primaryStage.show();
    }

    @FXML
    void clickAbout(ActionEvent e) throws IOException {
        Parent aboutScreen=FXMLLoader.load(getClass().getClassLoader().getResource("AboutPage.fxml"));
        Stage stage = (Stage) about.getScene().getWindow();
        stage.setScene(new Scene(aboutScreen));
        stage.show();
    }

    private void addTestsToSuite(List<XmlSuite> suiteFiles, List<XmlClass> classes, String app) {
        XmlSuite suite = new XmlSuite();
        suite.setName(app + "Suite");

        XmlTest test = new XmlTest(suite);
        test.setName(app + "Test");

        test.setClasses(classes);

        suiteFiles.add(suite);
    }

    @FXML
    void clickProceed(ActionEvent e) throws IOException {
        if( (email.getText()==null || email.getText().trim().isEmpty() ) || (locn.getText()==null || locn.getText().trim().isEmpty() ) )
            alert.setText("Please enter your details");
        else {
            studentEmailId = email.getText();
            studentLocation = locn.getText();
            MyDatabase.testsStarted();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("MainApp.fxml"));
            AnchorPane testScreen = loader.load();
            MainAppLauncher controller = loader.getController();

            TestNG runner = new TestNG();

            // Create a list of String
            List<XmlSuite> suiteFiles = new ArrayList<XmlSuite>();

//            List<XmlClass> tb_classes = new ArrayList<XmlClass>();
//            tb_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));
//            addTestsToSuite(suiteFiles, tb_classes, "Yts");


            if (youtube_cb.isSelected()) {
                List<XmlClass> yts_classes = new ArrayList<XmlClass>();
                yts_classes.add(new XmlClass("iiitd.nrl.evalapp.YouTubeTests_SearchChannel"));
                yts_classes.add(new XmlClass("iiitd.nrl.evalapp.YouTubeTests_SearchChannel"));
//                yts_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
                addTestsToSuite(suiteFiles, yts_classes, "Yts");

                List<XmlClass> ytp_classes = new ArrayList<XmlClass>();
                ytp_classes.add(new XmlClass("iiitd.nrl.evalapp.YouTubeTests_PlayVideo"));
                ytp_classes.add(new XmlClass("iiitd.nrl.evalapp.YouTubeTests_PlayVideo"));
//                ytp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
                addTestsToSuite(suiteFiles, ytp_classes, "Ytp");


                MyDatabase.totalTests += 2;
            }

            if (hotstar_cb.isSelected()) {
                List<XmlClass> hs_classes = new ArrayList<XmlClass>();
                hs_classes.add(new XmlClass("iiitd.nrl.evalapp.HotstarTests_search"));
                hs_classes.add(new XmlClass("iiitd.nrl.evalapp.HotstarTests_search"));
//                hs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
                addTestsToSuite(suiteFiles, hs_classes, "Hs");


                List<XmlClass> classes = new ArrayList<XmlClass>();
                classes.add(new XmlClass("iiitd.nrl.evalapp.HotstarTests_trending"));
                classes.add(new XmlClass("iiitd.nrl.evalapp.HotstarTests_trending"));
//                classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
                addTestsToSuite(suiteFiles, classes, "Ht");


                MyDatabase.totalTests += 2;
            }

            if (linkedin_cb.isSelected()) {
                List<XmlClass> lp_classes = new ArrayList<XmlClass>();
                lp_classes.add(new XmlClass("iiitd.nrl.evalapp.LinkedInTests_profile"));
                lp_classes.add(new XmlClass("iiitd.nrl.evalapp.LinkedInTests_profile"));
//                lp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
                addTestsToSuite(suiteFiles, lp_classes, "Lp");

                List<XmlClass> lc_classes = new ArrayList<XmlClass>();
                lc_classes.add(new XmlClass("iiitd.nrl.evalapp.LinkedInTests_myConnections"));
                lc_classes.add(new XmlClass("iiitd.nrl.evalapp.LinkedInTests_myConnections"));
//                lc_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
                addTestsToSuite(suiteFiles, lc_classes, "Lc");

                List<XmlClass> ls_classes = new ArrayList<XmlClass>();
                ls_classes.add(new XmlClass("iiitd.nrl.evalapp.LinkedInTests_search"));
                ls_classes.add(new XmlClass("iiitd.nrl.evalapp.LinkedInTests_search"));
//                ls_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
                addTestsToSuite(suiteFiles, ls_classes, "Ls");

                MyDatabase.totalTests += 3;
            }

            if (facebook_cb.isSelected()) {
                List<XmlClass> fp_classes = new ArrayList<XmlClass>();
                fp_classes.add(new XmlClass("iiitd.nrl.evalapp.FacebookTests_post"));
                fp_classes.add(new XmlClass("iiitd.nrl.evalapp.FacebookTests_post"));
//                fp_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
                addTestsToSuite(suiteFiles, fp_classes, "Fp");

                List<XmlClass> fs_classes = new ArrayList<XmlClass>();
                fs_classes.add(new XmlClass("iiitd.nrl.evalapp.FacebookTests_search"));
                fs_classes.add(new XmlClass("iiitd.nrl.evalapp.FacebookTests_search"));
//                fs_classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
                addTestsToSuite(suiteFiles, fs_classes, "Fs");

                MyDatabase.totalTests += 2;
            }

            if (flipkart_cb.isSelected()) {
                List<XmlClass> classes = new ArrayList<XmlClass>();
                classes.add(new XmlClass("iiitd.nrl.evalapp.FlipkartTests"));
                classes.add(new XmlClass("iiitd.nrl.evalapp.FlipkartTests"));
//                classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
                addTestsToSuite(suiteFiles, classes, "Fl");
                MyDatabase.totalTests += 1;
            }

            if (amazon_cb.isSelected()) {
                List<XmlClass> classes = new ArrayList<XmlClass>();
                classes.add(new XmlClass("iiitd.nrl.evalapp.AmazonTests"));
                classes.add(new XmlClass("iiitd.nrl.evalapp.AmazonTests"));
//                classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
                addTestsToSuite(suiteFiles, classes, "Am");
                MyDatabase.totalTests += 1;
            }

            if (telegram_cb.isSelected()) {
                List<XmlClass> classes = new ArrayList<XmlClass>();
                classes.add(new XmlClass("iiitd.nrl.evalapp.TelegramTests"));
                classes.add(new XmlClass("iiitd.nrl.evalapp.TelegramTests"));
//                classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
                addTestsToSuite(suiteFiles, classes, "Tg");
                MyDatabase.totalTests += 1;
            }

            if (whatsapp_cb.isSelected()) {
                List<XmlClass> classes = new ArrayList<XmlClass>();
                classes.add(new XmlClass("iiitd.nrl.evalapp.WhatsappTests"));
                classes.add(new XmlClass("iiitd.nrl.evalapp.WhatsappTests"));
//                classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
                addTestsToSuite(suiteFiles, classes, "Wh");
                MyDatabase.totalTests += 1;
            }

            if (googlemaps_cb.isSelected()) {
                List<XmlClass> classes = new ArrayList<XmlClass>();
                classes.add(new XmlClass("iiitd.nrl.evalapp.GoogleMapsTest"));
                classes.add(new XmlClass("iiitd.nrl.evalapp.GoogleMapsTest"));
//                classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
                addTestsToSuite(suiteFiles, classes, "Gm");
                MyDatabase.totalTests += 1;
            }

            if (googlenews_cb.isSelected()) {
                List<XmlClass> classes = new ArrayList<XmlClass>();
                classes.add(new XmlClass("iiitd.nrl.evalapp.GooglenewsTests"));
                classes.add(new XmlClass("iiitd.nrl.evalapp.GooglenewsTests"));
//                classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
                addTestsToSuite(suiteFiles, classes, "Gn");
                MyDatabase.totalTests += 1;
            }

            if (dailyhunt_cb.isSelected()) {
                List<XmlClass> classes = new ArrayList<XmlClass>();
                classes.add(new XmlClass("iiitd.nrl.evalapp.DailyhuntTests"));
                classes.add(new XmlClass("iiitd.nrl.evalapp.DailyhuntTests"));
//                classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
                addTestsToSuite(suiteFiles, classes, "Dh");
                MyDatabase.totalTests += 1;
            }

            if (paytm_cb.isSelected()) {
                List<XmlClass> classes = new ArrayList<XmlClass>();
                classes.add(new XmlClass("iiitd.nrl.evalapp.PaytmTests"));
                classes.add(new XmlClass("iiitd.nrl.evalapp.PaytmTests"));
//                classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
                addTestsToSuite(suiteFiles, classes, "Pt");
                MyDatabase.totalTests += 1;
            }
//            if (mobikwik_cb.isSelected()) {
//                List<XmlClass> classes = new ArrayList<XmlClass>();
//                classes.add(new XmlClass("iiitd.nrl.evalapp.MobikwikTests"));
////                classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));
//                addTestsToSuite(suiteFiles, classes, "Mk");
//                MyDatabase.totalTests += 1;
//            }

            // now set xml file for execution
            runner.setXmlSuites(suiteFiles);

            controller.setTestNGRunner(runner);
            Stage stage = (Stage) proceed.getScene().getWindow();
            stage.setScene(new Scene(testScreen));
            stage.show();
        }
    }

    @FXML
    void clickBack(ActionEvent e) throws IOException {
        Parent welcomeScreen=FXMLLoader.load(getClass().getClassLoader().getResource("WelcomePage.fxml"));
        Stage stage = (Stage) back.getScene().getWindow();
        stage.setScene(new Scene(welcomeScreen));
        stage.show();
    }


    public static void main(String[] args) throws IOException {
        MyDatabase.setUpDatabase();
        launch(args);
    }
}
