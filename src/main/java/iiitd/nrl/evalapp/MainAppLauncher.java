package iiitd.nrl.evalapp;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.testng.TestNG;

import java.io.IOException;

public class MainAppLauncher {
    protected TestNG runner;

    @FXML
    protected Button runTest, runTest1, runTest11;

    @FXML
    void clickRun(ActionEvent e) throws IOException, InterruptedException {
        System.out.println("Tests Started ...");
        runTest.setVisible(false);
        Thread.sleep(1000);
        // finally execute the runner using run method
        runner.run();
        runTest1.setVisible(false);

        MyDatabase.sendPINGLog();
    }

    public void setTestNGRunner(TestNG r) {
        runner = r;
    }
}
