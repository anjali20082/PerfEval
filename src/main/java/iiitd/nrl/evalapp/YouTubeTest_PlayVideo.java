package iiitd.nrl.evalapp;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.service.local.AppiumDriverLocalService;
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

public class YouTubeTest_PlayVideo {
    AndroidDriver<MobileElement> driver;

    AppiumDriverLocalService service;
    String appName = "Youtube_playVideo";
    String testName = "NA";
    String testStatusReason = "NA";
    Long start_time_1, start_time_2 , end_time_1, end_time_2;
    int versionId;

    @BeforeClass
    public void setUp() throws IOException, InterruptedException {
        versionId = MyDatabase.getVersionSelected();
//        versionId = 3;
        System.out.println("APP: " + appName + " Version ID: " + versionId);
    }

    @BeforeMethod
    public void launchCap() {
        driver = MainLauncher.driver;
        Activity activity = new Activity("com.google.android.youtube","com.google.android.youtube.HomeActivity");
        activity.setAppWaitActivity("com.google.android.apps.youtube.app.WatchWhileActivity");
        driver.startActivity(activity);
//        DesiredCapabilities cap=new DesiredCapabilities();
//        cap.setCapability("appPackage", "com.google.android.youtube");
//        cap.setCapability("appActivity", "com.google.android.youtube.HomeActivity");
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
//            MyDatabase.addTestResult(appName, testName, null, "NA", false, "App Not Installed");
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
        long timeTaken_1 = 0, timeTaken_2 = 0;

        HashMap<String, Long> main_events = new HashMap<>();

        if (testResult.isSuccess()) {
            if (testResult.getName() == "playTest") {
                timeTaken_1 = end_time_1 - start_time_1;
                main_events.put("searchVideo",timeTaken_1);

                timeTaken_2 = end_time_2 - start_time_2;
                main_events.put("playVideo",timeTaken_2);
            }

        }

        MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);

//        driver.quit();
    }



    @Test
    public void playTest() throws InterruptedException {
        testName = "play test";
        WebDriverWait wait = new WebDriverWait(driver, 15);
        try {

            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Search"))).click();
//            driver.findElement(By.id("com.google.android.youtube:id/search_edit_text")).sendKeys("manikarnika");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.youtube:id/search_edit_text"))).sendKeys("manikarnika");;

            /* searching video time measurement starts */
            start_time_1 =System.currentTimeMillis();
            ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));

            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
                    "new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
                            + "new UiSelector().descriptionContains(\"Official Trailer\"));"))).isDisplayed();
            end_time_1 = System.currentTimeMillis();

            /* searching video time measurement starts */
            start_time_2 =System.currentTimeMillis();

            driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Official Trailer\")")).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"com.google.android.youtube:id/title\")")));
            end_time_2 = System.currentTimeMillis();

            // YT version 14
//            if (versionId < 3) {
//                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[1]/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.View"))).isDisplayed();
//            }
//            else if (versionId == 3) {
//                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[2]/android.view.ViewGroup/android.view.ViewGroup/android.widget.FrameLayout[2]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.View"))).isDisplayed();
//            }
//            // YT version: 15 & 16
//            else if (versionId >= 4) {
//                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.widget.FrameLayout[@content-desc=\"Video player\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.View"))).isDisplayed();
//            }
        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
    }
}
