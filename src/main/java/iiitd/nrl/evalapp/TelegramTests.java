package iiitd.nrl.evalapp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.rules.ExpectedException;
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
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

@SuppressWarnings("unchecked")
public class TelegramTests  {
    AndroidDriver<MobileElement> driver;
    String appName = "Telegram";
    String testName = "NA";
    String testStatusReason = "NA";

    @AfterClass
    public void update() {

    }

    @BeforeMethod
    public void launchCap() {
        DesiredCapabilities cap=new DesiredCapabilities();
        cap.setCapability("appPackage", "org.telegram.messenger");
        cap.setCapability("appActivity", "org.telegram.ui.LaunchActivity");
        cap.setCapability("noReset", "true");
        cap.setCapability("fullReset", "false");
        cap.setCapability("autoGrantPermissions", true);
        cap.setCapability("autoAcceptAlerts", true);
        cap.setCapability("uiautomator2ServerInstallTimeout", 60000);

        URL url;
        try {
            url = new URL("http://127.0.0.1:4723/wd/hub");
            driver=new AndroidDriver<MobileElement>(url,cap);
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

        HashMap<String, Long> main_events = new HashMap<>();

        if (testResult.isSuccess()) {
            if (testResult.getName() == "sendMessage") {
                timeTaken = MyDatabase.getTimeTaken(jsonString, 12, -2);
                main_events.put(testResult.getName(), timeTaken);
            }
        }

        MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);

        driver.quit();
    }

    @Test
    public void sendMessage() throws Exception {
        testName = "send message";
        WebDriverWait wait = new WebDriverWait(driver, 300);

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Search"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("Automation Testing EvalApp");
            List<MobileElement> results= (List<MobileElement>) driver.findElementsByClassName("android.view.ViewGroup");
            results.get(0).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("Hi, this is an automated text");

            // calculate time of below 2 commands
            int before_length = driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Sent\");")).size();

            /* sending message time measurement starts */
            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Send"))).click();

            Calendar calendar = Calendar.getInstance();
            long startTime = calendar.getTimeInMillis();
            long currentTime = startTime;
            long limitTime = startTime + 300000;

            int after_length = driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Sent\");")).size();
            while (currentTime < limitTime && after_length <= before_length) {
                after_length = driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Sent\");")).size();

                calendar = Calendar.getInstance();
                currentTime = calendar.getTimeInMillis();
            }

            if (currentTime >= limitTime) {
                throw new Exception("Message not sent");
            }

            /* sending message time measurement stops */

        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }

//        and we meet here as well Sent at 4:59 PM, Seen
    }
}