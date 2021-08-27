package iiitd.nrl.evalapp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

@SuppressWarnings("unchecked")
public class DH_test2  {
    AndroidDriver<MobileElement> driver;
    AndroidDriver<MobileElement> driver1;
    AndroidDriver<MobileElement> driver2;
    String appName = "Dailyhunt";
    String testName = "NA";
    String testStatusReason = "NA";
    String commandsCompleted = "";
    DesiredCapabilities cap=new DesiredCapabilities();

    @BeforeMethod
    public void launchCap() {
        cap=new DesiredCapabilities();
        cap.setCapability("appPackage", "com.eterno");
        cap.setCapability("appActivity", "com.newshunt.appview.common.ui.activity.HomeActivity");
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
            return "Wifi 2";
        else if (connType == 4)
            return "MobileData 4";
        else if (connType == 6)
            return "Wifi & MobileData 6";
        return "Wifi " + connType;
    }

    @AfterMethod
    public void restart(ITestResult testResult) {
        MyDatabase.setTestStatus(testResult.isSuccess());
//        driver.quit();
    }

    @Test(priority=0)
    public void searchNews() throws InterruptedException {
        testName = "search news";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

        try {

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/global_search"))).click();
            commandsCompleted += "clickSearch:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/search_box"))).sendKeys("sports");
            commandsCompleted += "enterName:";

            ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
            commandsCompleted += "pressEnter:";

            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"News\")"))).click();
            commandsCompleted += "clickNews:";

            /* Search news time measurement starts*/
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/follow_button"))).isDisplayed();
            commandsCompleted += "checkNews:";
            System.out.println("Follow found!");
            /* Search news time measurement stops*/
            commandsCompleted += "P";
        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
        String jsonString = driver.getEvents().getJsonData();

        MyDatabase.setCurrentApp(appName);
        MyDatabase.setCommands(commandsCompleted);
        MyDatabase.setAppJsonCommands(jsonString);
        MyDatabase.setTestStatusReason(testStatusReason);
        MyDatabase.setConnType(getConnectionType());
        System.out.println("Follow found!");
        testStatusReason = "NA";

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

        driver.pressKey(new KeyEvent(AndroidKey.APP_SWITCH));
        Thread.sleep(1000) ;
        System.out.println("APP_SWITCH clicked");
//        String ui = "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().AccessibilityId(\"Dailyhunt\"));";
//        wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui))).click();
        int  y = driver.manage().window().getSize().height / 2;
        int start_x = (int) (driver.manage().window().getSize().width * 0.2);
        int end_x = (int) (driver.manage().window().getSize().width * 0.8);
        TouchAction dragNDrop = new TouchAction(driver)
                .press(PointOption.point(start_x, y)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(500)))
                .moveTo(PointOption.point(end_x, y))
                .release();
        dragNDrop.perform();
        driver.findElement(MobileBy.AccessibilityId("Dailyhunt")).click();
//        driver.quit();
//        driver1.quit();
//        driver.runAppInBackground(Duration.ofSeconds(30));
//		driver.quit();
//		JSON COMMMANDS
    }
//    @Test(priority=1)
//    public void copyTrakBytes() throws InterruptedException {
//        String appName = "TrakBytes";
//        String testName = "NA";
//        String testStatusReason = "NA";
//        String packetData = "NA";
//        DesiredCapabilities cap1 = new DesiredCapabilities();
//        cap1.setCapability("appPackage", "com.hawk.trakbytes");
//        cap1.setCapability("appActivity", "com.hawk.trakbytes.MainActivity");
//        cap1.setCapability("noReset", "true");
//        cap1.setCapability("fullReset", "false");
//        cap1.setCapability("autoGrantPermissions", true);
//        cap1.setCapability("autoAcceptAlerts", true);
//        cap1.setCapability("uiautomator2ServerInstallTimeout", 60000);
//
//        URL url;
//        try {
//            url = new URL("http://127.0.0.1:4723/wd/hub");
//            driver1 = new AndroidDriver<MobileElement>(url, cap1);
//        } catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (WebDriverException e) {
//            MyDatabase.addTestResult(appName, testName, null, "NA" , false, "App Not Installed");
//        }
//        testName = "trak bytes upload";
//        WebDriverWait wait = new WebDriverWait(driver1, 20);
//
//        try {
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.hawk.trakbytes:id/upload_stats"))).click();
//            Thread.sleep(3000);
//            System.out.println("Upload Stats clicked");
//            packetData = driver1.findElement(By.id("com.hawk.trakbytes:id/stats_text")).getText();
//
//            System.out.println(packetData);
//        } catch (Exception e) {
//            testStatusReason = e.toString();
//            throw e;
//        }
//        MyDatabase.setPacket_sizes_after(packetData);
//        MyDatabase.addTestResult();
////        driver.quit();
//        driver1.quit();
//
//    }
//    @Test(priority=2)
//    public void searchNewsT(){
//        System.out.println("here");
//        driver.currentActivity();
//        testName = "search news2";
//        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
//
//        try {
//
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/global_search"))).click();
//            commandsCompleted += "clickSearch:";
//
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/search_box"))).sendKeys("sports");
//            commandsCompleted += "enterName:";
//
//            ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
//            commandsCompleted += "pressEnter:";
//
//            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"News\")"))).click();
//
//            /* Search news time measurement starts*/
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/follow_button"))).isDisplayed();
//            commandsCompleted += "checkNews:";
//            /* Search news time measurement stops*/
//            commandsCompleted += "P";
//        } catch (Exception e) {
//            testStatusReason = e.toString();
//            throw e;
//        }
//        String jsonString = driver.getEvents().getJsonData();
//        MyDatabase.setCurrentApp(appName);
//        MyDatabase.setCommands(commandsCompleted);
//        MyDatabase.setAppJsonCommands(jsonString);
//        MyDatabase.setTestStatusReason(testStatusReason);
//        MyDatabase.setConnType(getConnectionType());
//
//        testStatusReason = "NA";
////		driver.quit();
////		JSON COMMMANDS
//    }
//	{
//		"commands": [
//		{
//			"cmd": "findElement",
//				"startTime": 1615991691553,
//				"endTime": 1615991692387
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615991692404,
//				"endTime": 1615991693419
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615991693428,
//				"endTime": 1615991694589
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615991695256,
//				"endTime": 1615991695797
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615991695800,
//				"endTime": 1615991695830
//		},
//		{
//			"cmd": "setValue",
//				"startTime": 1615991695841,
//				"endTime": 1615991696648
//		},
//		{
//			"cmd": "pressKeyCode",
//				"startTime": 1615991696674,
//				"endTime": 1615991697763
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615991697782,
//				"endTime": 1615991699835
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615991699853,
//				"endTime": 1615991702045
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615991702049,
//				"endTime": 1615991702628
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615991702631,
//				"endTime": 1615991702650
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615991702653,
//				"endTime": 1615991702672
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1615991702683,
//				"endTime": 1615991702683
//		}
//  ]
//	}

    //	@Test
    public void livetvTest() throws InterruptedException{

        testName = "live tv";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

        try {

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/scrollable_bottom_container")));

            List<MobileElement> bottomBar = (List<MobileElement>) driver.findElementsById("com.eterno:id/navbar_appsection_icon");
            bottomBar.get(1).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/root_view")));
            List<MobileElement> news = (List<MobileElement>) driver.findElementsById("com.eterno:id/root_view");

            /* live tv time measurement starts*/
            news.get(0).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/constraint_lyt")));
            /* live tv time measurement stops*/
        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
    }

}