package iiitd.nrl.evalapp;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.StartsActivity;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.reporters.jq.Main;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.HashMap;
import java.util.Random;

public class pcapdroid {
    AndroidDriver<MobileElement> driver;
    String appName = "PcapDroid";
    String testName = "NA";
    String testStatusReason = "NA";
    String packetData = "NA";
    Timestamp t1;


    @BeforeMethod
    public void launchCap() {
//        driver = MainLauncher.driver;
//        driver.startActivity(new Activity("com.hawk.trakbytes","com.hawk.trakbytes.MainActivity"));
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("appPackage", "com.emanuelef.remote_capture");
        cap.setCapability("appActivity", "com.emanuelef.remote_capture.activities.MainActivity");
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
        } catch (WebDriverException e) {
            MyDatabase.addTestResult(appName, testName, null, "NA" , false, "App Not Installed");
        }
    }

    public String getConnectionType() {
        Long connType = driver.getConnection().getBitMask();
        if (connType == 2)
            return "Wifi";
        else if (connType == 4)
            return "MobileData";
        else if (connType == 6)
            return "Wifi & MobileData";
        return "Wifi";
    }

    @AfterMethod
    public void restart(ITestResult testResult) {
//        MyDatabase.setPacket_sizes_before(packetData);
//        driver.quit();
    }

    @Test
    public void copyTrakBytes() throws InterruptedException {
        testName = "pcap droid started";
        WebDriverWait wait = new WebDriverWait(driver, 20);

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.emanuelef.remote_capture:id/action_start"))).click();
            System.out.println("Start button clicked");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("android:id/button1"))).click();
            System.out.println("here");
//            System.out.println("Pcap capture started");
            t1 = new Timestamp(System.currentTimeMillis());
            System.out.println("packet app started at : " + t1);

//            ((AndroidDriver)driver).runAppInBackground( Duration.ofSeconds(100));
//            ((AndroidDriver)driver).runAppInBackground(Duration.ofSeconds(20));


            Thread.sleep(3000);

//            ((StartsActivity)driver).currentActivity();
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("android:id/button1"))).click();

//            packetData = driver.findElement(By.id("com.hawk.trakbytes:id/stats_text")).getText();
//
//            System.out.println(packetData);
        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
    }
}