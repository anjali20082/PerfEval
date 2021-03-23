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

    @AfterClass
    public void update() {
    }

    @BeforeMethod
    public void launchCap() {
        driver = MainLauncher.driver;
        driver.startActivity(new Activity("com.whatsapp","com.whatsapp.Main"));
//        DesiredCapabilities cap = new DesiredCapabilities();
//        cap.setCapability("appPackage", "com.whatsapp");
//        cap.setCapability("appActivity", "com.whatsapp.Main");
//        cap.setCapability("noReset", "true");
//        cap.setCapability("fullReset", "false");
//        cap.setCapability("autoGrantPermissions", true);
//        cap.setCapability("autoAcceptAlerts", true);
//        cap.setCapability("uiautomator2ServerInstallTimeout", 60000);
//
//        URL url;
//        try {
//            url = new URL("http://127.0.0.1:4723/wd/hub");
//            driver = new AndroidDriver<MobileElement>(url, cap);
//        } catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (WebDriverException e) {
//            MyDatabase.addTestResult(appName, testName, null, "NA" , false, "App Not Installed");
//        }
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
        String jsonString = driver.getEvents().getJsonData();
//        System.out.println(jsonString);
        long timeTaken = 0;

        String event_name = "";
        HashMap<String, Long> main_events = new HashMap<>();

        if (testResult.isSuccess()) {
            if (testResult.getName() == "sendMessage") {
                timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -2);
                main_events.put(testResult.getName(), timeTaken);
            }
        }
        MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);
//        driver.quit();
    }

    @Test
    public void sendMessage() throws InterruptedException {
        testName = "send message";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

        Random rand = new Random();
        int rand_int = rand.nextInt(1000);
        String rand_str = Integer.toString(rand_int);
        String message = "Hi, this is an automated text:" + rand_str;

        try {

//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.whatsapp:id/menuitem_search"))).click();
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.whatsapp:id/search_input"))).click();
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.whatsapp:id/search_input"))).sendKeys("EvalApp");

            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
                    + "new UiSelector().text(\"EvalApp Group\"));"))).click();
//                    MobileBy.AccessibilityId("EvalApp Group"))).click();
//            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.whatsapp:id/message_btn"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.whatsapp:id/entry"))).sendKeys(message);

            /* sending message time measurement starts */
            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Send"))).click();
            wait.until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Sent")), ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Delivered"))));
            /* sending message time measurement stops */

        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
    }
}