package iiitd.nrl.evalapp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import io.appium.java_client.android.Activity;
import io.appium.java_client.service.local.AppiumDriverLocalService;
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
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

@SuppressWarnings("unchecked")
public class Youtube_SearchVid {
    AndroidDriver<MobileElement> driver;

    AppiumDriverLocalService service;
    String appName = "Youtube_playVideo";
    String testName = "NA";
    String testStatusReason = "NA";
    String commandsCompleted = "";


    @BeforeMethod
    public void launchCap() {
        DesiredCapabilities cap = new DesiredCapabilities();
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
            driver = new AndroidDriver<MobileElement>(url, cap);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (WebDriverException e) {
            MyDatabase.addTestResult(appName, testName, null, "NA", false, "App Not Installed");
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
    public void restart(ITestResult testResult) throws Exception {
        String jsonString = driver.getEvents().getJsonData();

        MyDatabase.setCurrentApp(appName);
        MyDatabase.setCommands(commandsCompleted);
        MyDatabase.setAppJsonCommands(jsonString);
        MyDatabase.setTestStatus(testResult.isSuccess());
        MyDatabase.setTestStatusReason(testStatusReason);
        MyDatabase.setConnType(getConnectionType());

        testStatusReason = "NA";
        upload_stats();
        driver.quit();
    }

    public void upload_stats()throws Exception{
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
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
    }

    @Test
    public void playTest() throws InterruptedException {
        testName = "search video";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

        try {

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.youtube:id/menu_item_1"))).click();
            commandsCompleted += "clickSearch:";

            driver.findElement(By.id("com.google.android.youtube:id/search_edit_text")).sendKeys("manikarnika");
            commandsCompleted += "enterName:";

            /* searching video time measurement starts */
            ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
            commandsCompleted += "pressEnter:";

            String ui = "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().descriptionContains(\"Official Trailer\"));";
            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(ui))).isDisplayed();
            commandsCompleted += "searchResult:";
            /* searching video time measurement starts */

//            ui = "new UiSelector().descriptionContains(\"Official Trailer\")";
//            driver.findElement(MobileBy.AndroidUIAutomator(ui)).click();
//            commandsCompleted += "clickSearchResult:";
//
//            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Video player"))).isDisplayed();
//            commandsCompleted += "checkVideoPlayer:";
//
//            commandsCompleted += "P";

        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
//		json commands
    }

//	JSON COMMANDS

//	{
//		"commands": [
//		{
//			"cmd": "findElement",
//				"startTime": 1616826739150,
//				"endTime": 1616826740636
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616826740705,
//				"endTime": 1616826743654
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616826743687,
//				"endTime": 1616826743908
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826743931,
//				"endTime": 1616826744945
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826747095,
//				"endTime": 1616826747196
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616826747214,
//				"endTime": 1616826747252
//		},
//		{
//			"cmd": "setValue",
//				"startTime": 1616826747286,
//				"endTime": 1616826748216
//		},
//		{
//			"cmd": "pressKeyCode",
//				"startTime": 1616826748235,
//				"endTime": 1616826749741
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826752825,
//				"endTime": 1616826753214
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616826753243,
//				"endTime": 1616826753365
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826753384,
//				"endTime": 1616826761146
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826761159,
//				"endTime": 1616826761297
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616826761306,
//				"endTime": 1616826762722
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826764368,
//				"endTime": 1616826764663
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616826764675,
//				"endTime": 1616826764778
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826764789,
//				"endTime": 1616826764847
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616826764857,
//				"endTime": 1616826765428
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826765444,
//				"endTime": 1616826765501
//		},
//		{
//			"cmd": "proxyReqRes",
//				"startTime": 1616826765519,
//				"endTime": 1616826765604
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826765632,
//				"endTime": 1616826797339
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616826797355,
//				"endTime": 1616826827987
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826827998,
//				"endTime": 1616826858520
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616826858538,
//				"endTime": 1616826882022
//		},
//		{
//			"cmd": "proxyReqRes",
//				"startTime": 1616826882039,
//				"endTime": 1616826882084
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826882095,
//				"endTime": 1616826882161
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616826882175,
//				"endTime": 1616826882641
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826882666,
//				"endTime": 1616826902915
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616826902933,
//				"endTime": 1616826924876
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826924894,
//				"endTime": 1616826926142
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616826926149,
//				"endTime": 1616826926755
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826926770,
//				"endTime": 1616826926909
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616826926931,
//				"endTime": 1616826928058
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826928074,
//				"endTime": 1616826928787
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616826928804,
//				"endTime": 1616826928896
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1616826928933,
//				"endTime": 1616826928934
//		}
//  ]
//	}
}