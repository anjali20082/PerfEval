package iiitd.nrl.evalapp;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import org.testng.TestNG;
import javafx.application.Platform;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainAppLauncher {
    protected TestNG runner;

    @FXML
    protected Button runTest;



    protected static int tests=0, total=14;
    @FXML
    void clickRun(ActionEvent e) throws InterruptedException {
//        System.out.println("Tests Started ...");

//        Thread.sleep(1000);

        Thread chkThread=new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        Process process = null;
                        try {
//                            System.out.println("command");
//                            String adbPath = "$ANDROID_HOME/platform-tools/adb";
//                            String[] aCommand = new String[] { adbPath,"devices"};
                            process = Runtime.getRuntime().exec(String.format("adb devices"));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Pattern pattern = Pattern.compile("^([a-zA-Z0-9\\-]+)(\\s+)(device)");

                                    Matcher matcher;
                                    boolean good_to_go=false;String line=null;
                                    while ((line = in.readLine()) != null) {
                                        if (line.matches(pattern.pattern())) {
                                            matcher = pattern.matcher(line);
                                            if (matcher.find()){
//                                                System.out.println(matcher.group(1));
                                                good_to_go=true;
                                                break;
                                            }
                                        }

                                    }
                                    if(good_to_go){
                                        // finally execute the runner using run method
                                        runTest.setText("Tests Started ...");
                                        runTest.setStyle("-fx-background-color: yellow; ");
                                        Thread testThread=new Thread(
                                                new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        runner.run();
                                                    }
                                                }
                                        );
                                        testThread.start();
                                        testThread.join();
                                        runTest.setText("Tests Completed");
                                        runTest.setStyle("-fx-background-color: lightgreen; ");

                                        MyDatabase.sendPINGLog();
                                    }
                                    else{
                                        Alert alert = new Alert(AlertType.ERROR);
                                        alert.setTitle("Alert");
                                        alert.setContentText("Mobile device not connected");
                                        alert.show();
                                    }
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
                                }
                            }
                        });
                    }
                }
        );




        chkThread.start();


//        runTest1.setVisible(false);

    }

    public void setTestNGRunner(TestNG r) {
        runner = r;
    }
}
