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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class LinkedInTests_profile {
    AndroidDriver<MobileElement> driver;
    String appName = "LinkedIn_profile";
    String testName = "NA";
    String testStatusReason = "NA";
    String commandsCompleted = "";
    ArrayList<Integer> txrx;
    String tx_bytes = "";
    String rx_bytes = "";

    @BeforeMethod
    public void launchCap() throws IOException {
        txrx = NetStats.getstats("10352");
        Integer rx_initial = txrx.get(0);
        Integer tx_initial = txrx.get(1);
        System.out.println(rx_initial + "  "+ tx_initial);
        tx_bytes += tx_initial+":";
        rx_bytes += rx_initial+":";
        DesiredCapabilities cap=new DesiredCapabilities();
        cap.setCapability("appPackage", "com.linkedin.android");
        cap.setCapability("appActivity", "com.linkedin.android.authenticator.LaunchActivity");
        cap.setCapability("noReset", "true");
        cap.setCapability("fullReset", "false");
        cap.setCapability("autoGrantPermissions", true);
        cap.setCapability("autoAcceptAlerts", true);
        cap.setCapability("uiautomator2ServerInstallTimeout", 60000);

        URL url;
        try {
            url = new URL("http://127.0.0.1:4723/wd/hub");
            driver= new AndroidDriver<MobileElement>(url, cap);
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
            return "Wifi 2";
        else if (connType == 4)
            return "MobileData 4";
        else if (connType == 6)
            return "Wifi & MobileData 6";
        return "Wifi " + connType;
    }

    @AfterMethod
    public void restart(ITestResult testResult) throws Exception {
        String jsonString = driver.getEvents().getJsonData();

        MyDatabase.setCurrentApp(appName);
        MyDatabase.setCommands(commandsCompleted);
        MyDatabase.setAppJsonCommands(jsonString);
        MyDatabase.setTestStatus(testResult.isSuccess());
        MyDatabase.setTestStatusReason(testStatusReason);
        MyDatabase.setConnType(getConnectionType());
        MyDatabase.set_TX_RX_Bytes(tx_bytes, rx_bytes);

        testStatusReason = "NA";
        driver.quit();
    }

    @Test
    public void viewProfile() throws InterruptedException, IOException {

        testName = "view profile";
        WebDriverWait wait = new WebDriverWait(driver,MyDatabase.testTimeLimit);

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/me_launcher"))).click();
            commandsCompleted += "myLauncher:";

            /* view profile time measurement starts*/
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/interests_panel_view_profile"))).click();
            commandsCompleted += "myProfile:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/profile_top_card_profile_picture")));
//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/profile_view_messob_top_card_profile_picture")));
            commandsCompleted += "myProfilePicture:";
            commandsCompleted += "P";
            /* view profile time measurement stops*/
            txrx = NetStats.getstats("10352");
            Integer rx_1 = txrx.get(0);
            Integer tx_1 = txrx.get(1);
            System.out.println(rx_1 + "  "+ tx_1);
            tx_bytes += tx_1;
            rx_bytes += rx_1;
        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
//	JSON Commands in the below comment
    }
//	{
//		"commands": [
//		{
//			"cmd": "findElement",
//				"startTime": 1615907140774,
//				"endTime": 1615907141861
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615907141871,
//				"endTime": 1615907144409
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615907144421,
//				"endTime": 1615907144536
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615907144542,
//				"endTime": 1615907146126
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615907146130,
//				"endTime": 1615907146153
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615907146171,
//				"endTime": 1615907146262
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615907147331,
//				"endTime": 1615907148413
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615907148416,
//				"endTime": 1615907148452
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1615907148468,
//				"endTime": 1615907148468
//		}
//	  ]
//	}
}
