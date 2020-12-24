package iiitd.nrl.evalapp;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class GoogleMapsTest {
    AndroidDriver<MobileElement> driver;
    String appName = "GoogleMaps";
    String testName = "NA";
    String testStatusReason = "NA";

    @BeforeMethod
    public void launchCap() throws IOException {
        DesiredCapabilities cap=new DesiredCapabilities();
        cap.setCapability("appPackage", "com.google.android.apps.maps");
        cap.setCapability("appActivity", "com.google.android.maps.MapsActivity");
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
        System.out.println(jsonString);
        long timeTaken = 0;

        HashMap<String, Long> main_events = new HashMap<>();

        if (testResult.isSuccess()) {
            if (testResult.getName() == "searchPlace") {
//
//                timeTaken = MyDatabase.getTimeTaken(jsonString, 7, 7) + MyDatabase.getTimeTaken(jsonString, 12, 14);
//                main_events.put(testResult.getName(), timeTaken);
//
//                timeTaken = MyDatabase.getTimeTaken(jsonString, 18, 20);
//                main_events.put("addToCart", timeTaken);
//
                timeTaken = MyDatabase.getTimeTaken(jsonString, -3, -2);
                main_events.put("searchPlace", timeTaken);
            }
        }

//		System.out.println("testStatusReason:" + testStatusReason);
//		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);

        driver.quit();
    }

    @Test
    public void searchPlace() throws InterruptedException {
        testName = "search place";
        WebDriverWait wait = new WebDriverWait(driver, 300);

        try {
            wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textMatches(\"(?i)Search here(?-i)\");")))).click();
            wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textMatches(\"(?i)Search here(?-i)\");")))).sendKeys("india gate");
            ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/street_view_thumbnail")));
//            wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textMatches(\"(?i)Directions(?-i)\");"))));
        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
    }
}



