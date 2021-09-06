package iiitd.nrl.evalapp;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
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
import java.util.HashMap;
import java.util.Random;

public class TrakBytesData {
    AndroidDriver<MobileElement> driver;
    String appName = "TrakBytes";
    String testName = "NA";
    String testStatusReason = "NA";
    String packetData = "NA";


    @BeforeMethod
    public void launchCap() {
//        driver = MainLauncher.driver;
//        driver.startActivity(new Activity("com.hawk.trakbytes","com.hawk.trakbytes.MainActivity"));
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("appPackage", "com.hawk.trakbytes");
        cap.setCapability("appActivity", "com.hawk.trakbytes.MainActivity");
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
        MyDatabase.setPacket_sizes_before(packetData);
        driver.quit();
    }

    @Test
    public void copyTrakBytes() throws InterruptedException {
        testName = "trak bytes started";
        WebDriverWait wait = new WebDriverWait(driver, 20);

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.hawk.trakbytes:id/start_button"))).click();
            System.out.println("Start Stats clicked");
            Thread.sleep(3000);

            packetData = driver.findElement(By.id("com.hawk.trakbytes:id/stats_text")).getText();

            System.out.println(packetData);
        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
    }
}
