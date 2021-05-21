package iiitd.nrl.evalapp;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

@SuppressWarnings("unchecked")
public class HotstarTests_trending {
    AndroidDriver<MobileElement> driver;
    String appName = "Hotstar_trending";
    String testName = "NA";
    String testStatusReason = "NA";
    String commandsCompleted = "";

    @BeforeMethod
    public void launchCap() {
        DesiredCapabilities cap=new DesiredCapabilities();
        cap.setCapability("appPackage", "in.startv.hotstar");
        cap.setCapability("appActivity", "in.startv.hotstar.rocky.launch.splash.SplashActivity");
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
        String jsonString = driver.getEvents().getJsonData();

        MyDatabase.setCurrentApp(appName);
        MyDatabase.setCommands(commandsCompleted);
        MyDatabase.setAppJsonCommands(jsonString);
        MyDatabase.setTestStatus(testResult.isSuccess());
        MyDatabase.setTestStatusReason(testStatusReason);
        MyDatabase.setConnType(getConnectionType());

        testStatusReason = "NA";
        MyDatabase.addTestResult();
        driver.quit();
    }


    @Test
    public void trendingTest(){

        testName = "Trending Test";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
        try {

            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Open navigation drawer"))).click();
            commandsCompleted += "openDrawer:";
            /* load trending videos page time measurement starts*/
            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"Trending\")"))).click();
            commandsCompleted += "clickTrending:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("in.startv.hotstar:id/frame_player")));
            commandsCompleted += "checkVideoPlayer:";
            commandsCompleted += "P";
            /* load trending videos page time measurement stops*/
        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
//		JSON COMMANDS
    }
//	{
//		"commands": [
//		{
//			"cmd": "findElement",
//				"startTime": 1615909220427,
//				"endTime": 1615909221646
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615909221655,
//				"endTime": 1615909222685
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615909222693,
//				"endTime": 1615909222907
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615909222923,
//				"endTime": 1615909225549
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615909225567,
//				"endTime": 1615909226079
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615909226082,
//				"endTime": 1615909228087
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1615909228103,
//				"endTime": 1615909228103
//		}
//  ]
//	}
}
