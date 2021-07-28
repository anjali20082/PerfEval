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
    String commands = "";

    @BeforeClass
    public void setUp() throws IOException, InterruptedException {
        versionId = MyDatabase.getVersionSelected();
        System.out.println("APP: " + appName + " Version ID: " + versionId);
    }

    @BeforeMethod
    public void launchCap() {
//        driver = MainLauncher.driver;
//        Activity activity = new Activity("com.google.android.youtube","com.google.android.youtube.HomeActivity");
//        activity.setAppWaitActivity("com.google.android.apps.youtube.app.WatchWhileActivity");
//        driver.startActivity(activity);
        DesiredCapabilities cap=new DesiredCapabilities();
        cap.setCapability("appPackage", "com.google.android.youtube");
        cap.setCapability("appActivity", "com.google.android.youtube.HomeActivity");
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
//            MyDatabase.addTestResult(appName, testName, null, "NA", false, "App Not Installed");
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
        System.out.println(commands);

        MyDatabase.setCurrentApp(appName);
        MyDatabase.setCommands(commands);
        MyDatabase.setAppJsonCommands(jsonString);
        MyDatabase.setTestStatus(testResult.isSuccess());
        MyDatabase.setTestStatusReason(testStatusReason);
        MyDatabase.setConnType(getConnectionType());
    }



    @Test
    public void playTest() throws InterruptedException, IOException {
        testName = "play test";
        WebDriverWait wait = new WebDriverWait(driver, 15);
        Process process = null;
        try {

            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Search"))).click();
            commands += "search:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.youtube:id/search_edit_text"))).sendKeys("manikarnika");;
            commands += "enterName:";

//            process = Runtime.getRuntime().exec(String.format("adb shell screenrecord --bugreport sdcard/yt_play.mp4"));
            /* searching video time measurement starts */
//            start_time_1 =System.currentTimeMillis();

            ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
            commands += "pressEnter:";

            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
                    "new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
                            + "new UiSelector().descriptionContains(\"Official Trailer\"));"))).isDisplayed();
            commands += "checkTrailer:";
//            end_time_1 = System.currentTimeMillis();

            /* searching video time measurement starts */
//            start_time_2 =System.currentTimeMillis();

            driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Official Trailer\")")).click();
            commands += "clickTrailer:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"com.google.android.youtube:id/title\")")));
            commands += "checkVideoPage:";
            commands += "P";
//            end_time_2 = System.currentTimeMillis();
//            process.destroy();
        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
    }
}
