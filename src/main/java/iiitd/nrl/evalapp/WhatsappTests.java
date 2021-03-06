package iiitd.nrl.evalapp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import io.appium.java_client.MobileBy;
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
                main_events.put(event_name, timeTaken);
            }
        }
        MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);
        driver.quit();
    }

    @Test
    public void sendMessage() throws InterruptedException {
        testName = "send message";
        WebDriverWait wait = new WebDriverWait(driver, 300);

        try {

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.whatsapp:id/menuitem_search"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.whatsapp:id/search_input"))).sendKeys("automation testing");
            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Automation Testing"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.whatsapp:id/entry"))).sendKeys("Hi, this is an automated text");

            /* sending message time measurement starts */
            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Send"))).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Sent"))).isDisplayed();
            /* sending message time measurement stops */

        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
    }
}