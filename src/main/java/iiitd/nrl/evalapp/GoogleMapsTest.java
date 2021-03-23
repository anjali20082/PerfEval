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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
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
    Long start_time , end_time;
    int versionId;

    @BeforeClass
    public void setUp() throws IOException, InterruptedException {
        versionId = MyDatabase.getVersionSelected();
//        versionId = 2;
        System.out.println("APP: " + appName + " Version ID: " + versionId);

    }

    @BeforeMethod
    public void launchCap() throws IOException {
        driver = MainLauncher.driver;
        driver.startActivity(new Activity("com.google.android.apps.maps","com.google.android.maps.MapsActivity"));
//        DesiredCapabilities cap=new DesiredCapabilities();
//        cap.setCapability("appPackage", "com.google.android.apps.maps");
//        cap.setCapability("appActivity", "com.google.android.maps.MapsActivity");
//        cap.setCapability("noReset", "true");
//        cap.setCapability("fullReset", "false");
//        cap.setCapability("autoGrantPermissions", true);
//        cap.setCapability("autoAcceptAlerts", true);
//        cap.setCapability("uiautomator2ServerInstallTimeout", 60000);
//
//        URL url;
//        try {
//            url = new URL("http://127.0.0.1:4723/wd/hub");
//            driver=new AndroidDriver<MobileElement>(url,cap);
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
//        String jsonString = driver.getEvents().getJsonData();
//        System.out.println(jsonString);
        long timeTaken = 0;

        HashMap<String, Long> main_events = new HashMap<>();

        if (testResult.isSuccess()) {
            if (testResult.getName() == "searchPlace") {
                timeTaken = end_time - start_time;
                main_events.put("searchPlace",timeTaken);
            }
        }

//		System.out.println("testStatusReason:" + testStatusReason);
		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);

//        driver.quit();
    }

    @Test
    public void searchPlace() throws InterruptedException {
        testName = "search place";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
        try {

            // Version 9.36
            if (versionId == 1) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/tutorial_side_menu_got_it"))).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[3]/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.EditText/android.widget.TextView"))).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/search_omnibox_edit_text"))).sendKeys("india gate");

                start_time =System.currentTimeMillis();
                ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/expandingscrollview_container")));
                while(!driver.findElements(By.id("com.google.android.apps.maps:id/scalebar_widget")).isEmpty());
                end_time = System.currentTimeMillis();
            }

            // Version 9.67
            else if (versionId == 2) {
                //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[3]/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.EditText/android.widget.TextView"))).click();
                wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textMatches(\"(?i)Search here(?-i)\");"))).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/search_omnibox_edit_text"))).sendKeys("india gate");
                start_time =System.currentTimeMillis();
                ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/expandingscrollview_container")));
                while(!driver.findElements(By.id("com.google.android.apps.maps:id/scalebar_widget")).isEmpty());
                end_time = System.currentTimeMillis();
            }

            // Version 10.2
            else if (versionId == 3) {
                //wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[4]/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.EditText/android.widget.TextView"))).click();
                wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textMatches(\"(?i)Search here(?-i)\");"))).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/search_omnibox_edit_text"))).sendKeys("india gate");
                start_time =System.currentTimeMillis();
                ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/expandingscrollview_container")));
                while(!driver.findElements(By.id("com.google.android.apps.maps:id/scalebar_widget")).isEmpty());
                end_time = System.currentTimeMillis();
            }

            // Version 10.8
            else if (versionId == 4) {
//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/androidx.drawerlayout.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[3]/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.EditText/android.widget.TextView"))).click();
                wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textMatches(\"(?i)Search here(?-i)\");"))).click();

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/search_omnibox_edit_text"))).sendKeys("india gate");
                start_time =System.currentTimeMillis();
                ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/expandingscrollview_container")));
                while(!driver.findElements(By.id("com.google.android.apps.maps:id/scalebar_widget")).isEmpty());
                end_time = System.currentTimeMillis();
            }

            // Version latest
            else {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/androidx.drawerlayout.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[3]/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.EditText/android.widget.TextView"))).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/search_omnibox_edit_text"))).sendKeys("india gate");
                start_time =System.currentTimeMillis();
                ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/street_view_thumbnail")));
                while(!driver.findElements(By.id("com.google.android.apps.maps:id/scalebar_widget")).isEmpty());
                end_time = System.currentTimeMillis();
            }

        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
    }

}