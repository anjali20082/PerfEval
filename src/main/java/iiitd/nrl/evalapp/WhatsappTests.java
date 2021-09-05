package iiitd.nrl.evalapp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.Activity;
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
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

@SuppressWarnings("unchecked")
public class WhatsappTests {
    AndroidDriver<MobileElement> driver;
    String appName = "Whatsapp";
    String testName = "NA";
    String testStatusReason = "NA";
    String commandsCompleted = "";

    @BeforeMethod
    public void launchCap() {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("appPackage", "com.whatsapp");
        cap.setCapability("appActivity", "com.whatsapp.Main");
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
            return "Wifi 2";
        else if (connType == 4)
            return "MobileData 4";
        else if (connType == 6)
            return "Wifi & MobileData 6";
        return "Wifi " + connType;
    }
    public void upload_stats()throws Exception{
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
        Activity activity = new Activity("com.hawk.trakbytes", "com.hawk.trakbytes.MainActivity");
        driver.startActivity(activity);
        String packetData = "NA";
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.hawk.trakbytes:id/upload_stats"))).click();
            Thread.sleep(3000);
            System.out.println("Upload Stats clicked");
            packetData = driver.findElement(By.id("com.hawk.trakbytes:id/stats_text")).getText();

            System.out.println(packetData);
        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
        MyDatabase.setPacket_sizes_after(packetData);
        MyDatabase.addTestResult();
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

        testStatusReason = "NA";
//        upload_stats();
        driver.quit();
    }

    @Test
    public void sendMessage() throws InterruptedException {
        testName = "send message";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

        Random rand = new Random();
        int rand_int = rand.nextInt(1000);
        String rand_str = Integer.toString(rand_int);
        String message = "Hi, this is an automated text:" + rand_str;
        String ui;
        try {

            ui = "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().text(\"EvalApp Group\"));";
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui))).click();
            commandsCompleted += "openGroup:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.whatsapp:id/entry"))).sendKeys(message);
            commandsCompleted += "enterMsg:";

            /* sending message time measurement starts */
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Send"))).click();
            commandsCompleted += "send:";

            wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Sent")), ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Delivered"))));
            commandsCompleted += "checkIfMessageSent:";
            commandsCompleted += "P";

            /* sending message time measurement stops */

        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
//        json commands
    }
//    {
//        "commands": [
//        {
//            "cmd": "findElement",
//                "startTime": 1615993897958,
//                "endTime": 1615993917441
//        },
//        {
//            "cmd": "click",
//                "startTime": 1615993917454,
//                "endTime": 1615993918388
//        },
//        {
//            "cmd": "findElement",
//                "startTime": 1615993918402,
//                "endTime": 1615993918468
//        },
//        {
//            "cmd": "elementDisplayed",
//                "startTime": 1615993918472,
//                "endTime": 1615993918507
//        },
//        {
//            "cmd": "setValue",
//                "startTime": 1615993918527,
//                "endTime": 1615993919147
//        },
//        {
//            "cmd": "findElement",
//                "startTime": 1615993919153,
//                "endTime": 1615993919829
//        },
//        {
//            "cmd": "click",
//                "startTime": 1615993919841,
//                "endTime": 1615993921369
//        },
//        {
//            "cmd": "findElement",
//                "startTime": 1615993921379,
//                "endTime": 1615993921451
//        },
//        {
//            "cmd": "getLogEvents",
//                "startTime": 1615993921471,
//                "endTime": 1615993921471
//        }
//  ]
//    }
}