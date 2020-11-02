package iiitd.nrl.evalapp;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unchecked")
public class WelcomePageLauncher extends Application {

    @FXML
    protected Button about, back, proceed;

    @FXML
    protected TextField email, locn;

    @FXML
    protected Label alert;

    @FXML
    protected CheckBox amazon_cb, flipkart_cb, youtube_cb, hotstar_cb, linkedin_cb, facebook_cb, googlenews_cb, dailyhunt_cb, amazonpay_cb, paytm_cb, mobikwik_cb;
    protected HashMap<String, Boolean> apps_to_run = new HashMap<>();

    protected static String studentEmailId, studentLocation;

    public void initialize() {
        apps_to_run.put("amazon", true);
        apps_to_run.put("flipkart", true);
        apps_to_run.put("youtube", true);
        apps_to_run.put("hotstar", true);
        apps_to_run.put("linkedin", true);
        apps_to_run.put("facebook", true);
        apps_to_run.put("googlenews", true);
        apps_to_run.put("dailyhunt", true);
        apps_to_run.put("amazonpay", true);
        apps_to_run.put("paytm", true);
        apps_to_run.put("mobikwik", true);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("WelcomePage.fxml"));
        AnchorPane root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.setHeight(700);
        primaryStage.setWidth(570);

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
            if (youtube_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.YouTubeTests"));
                MyDatabase.totalTests += 2;
            }

            if (hotstar_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.HotstarTests"));
                MyDatabase.totalTests += 3;
            }
            if (googlenews_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.GooglenewsTests"));
                MyDatabase.totalTests += 2;
            }
            if (dailyhunt_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.DailyhuntTests"));
                MyDatabase.totalTests += 2;
            }
            if (linkedin_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.LinkedInTests"));
                MyDatabase.totalTests += 4;
            }
            if (facebook_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.FacebookTests"));
                MyDatabase.totalTests += 3;
            }
            if (flipkart_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.FlipkartTests"));
                MyDatabase.totalTests += 2;
            }
            if (amazon_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.AmazonTests"));
                MyDatabase.totalTests += 2;
            }

            // payment apps
            if (amazonpay_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.AmazonPayTest"));
                MyDatabase.totalTests += 1;
            }
            if (paytm_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.PaytmTests"));
                MyDatabase.totalTests += 1;
            }
            if (mobikwik_cb.isSelected()) {
                classes.add(new XmlClass("iiitd.nrl.evalapp.MobikwikTests"));
                MyDatabase.totalTests += 1;
            }
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


    public static void main(String[] args) {
        MyDatabase.setUpDatabase();
        launch(args);
    }
}
