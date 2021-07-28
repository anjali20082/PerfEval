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
    String commands = "";

    @BeforeClass
    public void setUp() throws IOException, InterruptedException {
        versionId = MyDatabase.getVersionSelected();
        System.out.println("APP: " + appName + " Version ID: " + versionId);
    }

    @BeforeMethod
    public void launchCap() throws IOException {
//        driver = MainLauncher.driver;
//        driver.startActivity(new Activity("com.google.android.apps.maps","com.google.android.maps.MapsActivity"));
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
        System.out.println(commands);

        MyDatabase.setCurrentApp(appName);
        MyDatabase.setCommands(commands);
        MyDatabase.setAppJsonCommands(jsonString);
        MyDatabase.setTestStatus(testResult.isSuccess());
        MyDatabase.setTestStatusReason(testStatusReason);
        MyDatabase.setConnType(getConnectionType());

//        driver.quit();
    }

    @Test
    public void searchPlace() throws InterruptedException, IOException {
        testName = "search place";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
        Process process = null;
        try {

            // Version 9.36
            if (versionId == 0) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/tutorial_side_menu_got_it"))).click();
                commands += "gotIt:";

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[3]/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.EditText/android.widget.TextView"))).click();
                commands += "search:";

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/search_omnibox_edit_text"))).sendKeys("india gate");
                commands += "enterName:";

                ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
                commands += "pressEnter:";

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/expandingscrollview_container")));
                commands += "checkMap:";

                while(!driver.findElements(By.id("com.google.android.apps.maps:id/scalebar_widget")).isEmpty());
                commands += "checkScalebar:";
            }

            // Version 9.67
            else if (versionId == 1) {

                wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textMatches(\"(?i)Search here(?-i)\");"))).click();
                commands += "search:";

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/search_omnibox_edit_text"))).sendKeys("india gate");
                commands += "enterName:";


                ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
                commands += "pressEnter:";

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/expandingscrollview_container")));
                commands += "checkMap:";

                while(!driver.findElements(By.id("com.google.android.apps.maps:id/scalebar_widget")).isEmpty());
                commands += "checkScalebar:";
            }

            // Version 10.2
            else if (versionId == 2) {
                wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textMatches(\"(?i)Search here(?-i)\");"))).click();
                commands += "search:";

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/search_omnibox_edit_text"))).sendKeys("india gate");
                commands += "enterName:";

                ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
                commands += "pressEnter:";

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/expandingscrollview_container")));
                commands += "checkMap:";

                while(!driver.findElements(By.id("com.google.android.apps.maps:id/scalebar_widget")).isEmpty());
                commands += "checkScalebar:";
            }

            // Version 10.8
            else if (versionId == 3) {
                wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textMatches(\"(?i)Search here(?-i)\");"))).click();
                commands += "search:";

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/search_omnibox_edit_text"))).sendKeys("india gate");
                commands += "enterName:";

                ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
                commands += "pressEnter:";

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/expandingscrollview_container")));
                commands += "checkMap:";

                while(!driver.findElements(By.id("com.google.android.apps.maps:id/scalebar_widget")).isEmpty());
                commands += "checkScalebar:";
            }

            commands += "P";
            // Version latest
//            else {
//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/androidx.drawerlayout.widget.DrawerLayout/android.widget.LinearLayout/android.view.ViewGroup/android.widget.FrameLayout[3]/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.EditText/android.widget.TextView"))).click();
//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/search_omnibox_edit_text"))).sendKeys("india gate");
//                process = Runtime.getRuntime().exec(String.format("adb shell screenrecord --bugreport sdcard/gmaps.mp4"));
//                start_time =System.currentTimeMillis();
//                ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/street_view_thumbnail")));
//                while(!driver.findElements(By.id("com.google.android.apps.maps:id/scalebar_widget")).isEmpty());
//                end_time = System.currentTimeMillis();
//            }
//            process.destroy();

        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
    }

}

//                process = Runtime.getRuntime().exec(String.format("adb shell screenrecord --bugreport sdcard/gmaps.mp4"));
//                start_time =System.currentTimeMillis();

//                end_time = System.currentTimeMillis();
