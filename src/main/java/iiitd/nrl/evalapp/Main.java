package iiitd.nrl.evalapp;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class Main extends Application {

    protected static float version = 1.5f;

    protected static int count = 0;

    protected static int totalTests = 23;

    @FXML
    protected Button about, back, proceed;

    @FXML
    protected Button runTest, runTest1, runTest11;

    @FXML
    protected TextField email, locn;

    @FXML
    protected Label alert;

    protected static String studentEmailId, studentLocation;

//    public void initialize() throws IOException {
//        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
//
//        Process process = Runtime.getRuntime()
//                .exec(String.format("appium"));
//    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("WelcomePage.fxml"));
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
            Parent testScreen=FXMLLoader.load(getClass().getClassLoader().getResource("MainApp.fxml"));
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

    @FXML
    void clickRun(ActionEvent e) throws IOException, InterruptedException {
        System.out.println("Tests Started ...");
        runTest.setVisible(false);
        Thread.sleep(1000);

        TestNG runner=new TestNG();
        XmlSuite suite = new XmlSuite();
        suite.setName("AppSuite");

        XmlTest test = new XmlTest(suite);
        test.setName("AppiumTests");
        List<XmlClass> classes = new ArrayList<XmlClass>();
        classes.add(new XmlClass("iiitd.nrl.evalapp.YouTubeTests"));
        classes.add(new XmlClass("iiitd.nrl.evalapp.HotstarTests"));
        classes.add(new XmlClass("iiitd.nrl.evalapp.GooglenewsTests"));
        classes.add(new XmlClass("iiitd.nrl.evalapp.DailyhuntTests"));
        classes.add(new XmlClass("iiitd.nrl.evalapp.LinkedInTests"));
        classes.add(new XmlClass("iiitd.nrl.evalapp.FacebookTests"));
        classes.add(new XmlClass("iiitd.nrl.evalapp.FlipkartTests"));
        classes.add(new XmlClass("iiitd.nrl.evalapp.AmazonTests"));

        // payment apps
        classes.add(new XmlClass("iiitd.nrl.evalapp.AmazonPayTest"));
        classes.add(new XmlClass("iiitd.nrl.evalapp.PaytmTests"));
        classes.add(new XmlClass("iiitd.nrl.evalapp.MobikwikTests"));
        test.setXmlClasses(classes) ;

		// Create a list of String
        List<XmlSuite> suitefiles=new ArrayList<XmlSuite>();

		// Add xml file which you have to execute
        suitefiles.add(suite);

        // now set xml file for execution
        runner.setXmlSuites(suitefiles);

        // finally execute the runner using run method
        runner.run();
        runTest1.setVisible(false);
    }

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        MyDatabase.setUpDatabase();
        launch(args);
    }
}
