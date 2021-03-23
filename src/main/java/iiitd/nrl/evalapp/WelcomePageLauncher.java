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
    protected CheckBox amazon_cb, flipkart_cb, youtube_cb, hotstar_cb, linkedin_cb, facebook_cb, googlenews_cb, dailyhunt_cb, amazonpay_cb, paytm_cb, mobikwik_cb, telegram_cb, whatsapp_cb, googlemaps_cb;

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
            XmlSuite suite = new XmlSuite();
            suite.setName("AppSuite");

            XmlTest test = new XmlTest(suite);
            test.setName("AppiumTests");
            List<XmlClass> classes = new ArrayList<XmlClass>();

            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesData"));

            if (youtube_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.YouTubeTests_PlayVideo"));
                classes.add(new XmlClass("iiitd.nrl.evalapp.YouTubeTests_SearchChannel"));
                MyDatabase.totalTests += 2;
            }

            if (hotstar_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.HotstarTests"));
                MyDatabase.totalTests += 2;
            }
            if (linkedin_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.LinkedInTests"));
                MyDatabase.totalTests += 3;
            }
            if (facebook_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.FacebookTests"));
                MyDatabase.totalTests += 2;
            }
            if (flipkart_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.FlipkartTests"));
                MyDatabase.totalTests += 1;
            }
            if (amazon_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.AmazonTests"));
                MyDatabase.totalTests += 1;
            }

            if (telegram_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.TelegramTests"));
                MyDatabase.totalTests += 1;
            }

            if (whatsapp_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.WhatsappTests"));
                MyDatabase.totalTests += 1;
            }

            if (googlemaps_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.GoogleMapsTest"));
                MyDatabase.totalTests += 1;
            }

            if (googlenews_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.GooglenewsTests"));
                MyDatabase.totalTests += 1;
            }
            if (dailyhunt_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.DailyhuntTests"));
                MyDatabase.totalTests += 1;
            }

            // payment apps
//            if (amazonpay_cb.isSelected()) {
//                classes.add(new XmlClass("iiitd.nrl.evalapp.AmazonPayTest"));
//                MyDatabase.totalTests += 1;
//            }
            if (paytm_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.PaytmTests"));
                MyDatabase.totalTests += 1;
            }
            if (mobikwik_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.MobikwikTests"));
                MyDatabase.totalTests += 1;
            }

            classes.add(new XmlClass("iiitd.nrl.evalapp.TrakBytesUpload"));

            test.setXmlClasses(classes) ;

            // Create a list of String
            List<XmlSuite> suitefiles=new ArrayList<XmlSuite>();

            // Add xml file which you have to execute
            suitefiles.add(suite);

            // now set xml file for execution
            runner.setXmlSuites(suitefiles);

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
