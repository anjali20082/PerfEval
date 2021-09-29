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
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
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
public class FacebookTests_search {
    AndroidDriver<MobileElement> driver;
    String appName = "Facebook_search";
    String testName = "NA";
    String testStatusReason = "NA";
    String commandsCompleted = "";

    @BeforeMethod
    public void launchCap() {
        DesiredCapabilities cap=new DesiredCapabilities();
        cap.setCapability("appPackage", "com.facebook.katana");
        cap.setCapability("appActivity", "com.facebook.katana.activity.FbMainTabActivity");
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

//        List<List<Object>> performanceData = driver.getPerformanceData("com.facebook.katana", "memoryinfo", 5);
//        List<List<Object>> performanceData1 = driver.getPerformanceData("com.facebook.katana", "batteryinfo", 5);
//        List<List<Object>> performanceData2 = driver.getPerformanceData("com.facebook.katana", "networkinfo", 5);
////        System.out.println("memory info: " + performanceData.toString());
//        System.out.println("battery info: " + performanceData1.toString());
//        System.out.println("network info: " + performanceData2.toString());

        testStatusReason = "NA";
        driver.quit();
    }


    @Test
    public void searchPerson() throws InterruptedException{

        testName = "search person";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Search"))).click();
            commandsCompleted += "clickSearch:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).click();
            commandsCompleted += "searchPerson:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("IIIT Delhi");
            commandsCompleted += "enterName:";

            ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
            commandsCompleted += "pressEnter:";
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().description(\"Posts search results\")")));
            commandsCompleted += "searchSuccess:";

//            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Posts\")"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().description(\"Posts search results\")"))).click();
            commandsCompleted += "clickPosts:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Profile Picture\")"))).click();
            commandsCompleted += "clickProfilePicture:";
            /* Search person time measurement starts */

            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Profile photo\")")));
            commandsCompleted += "searchPersonProfile:";
            /* Search person time measurement stops */

            commandsCompleted += "P";

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
//				"startTime": 1615993346046,
//				"endTime": 1615993347796
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615993347808,
//				"endTime": 1615993349953
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615993349965,
//				"endTime": 1615993350031
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615993350034,
//				"endTime": 1615993350051
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615993350061,
//				"endTime": 1615993350106
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615993359903,
//				"endTime": 1615993360781
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615993360784,
//				"endTime": 1615993360808
//		},
//		{
//			"cmd": "setValue",
//				"startTime": 1615993360828,
//				"endTime": 1615993361512
//		},
//		{
//			"cmd": "pressKeyCode",
//				"startTime": 1615993361530,
//				"endTime": 1615993363319
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615993366799,
//				"endTime": 1615993367478
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615993367492,
//				"endTime": 1615993367570
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615993368414,
//				"endTime": 1615993368885
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1615993368901,
//				"endTime": 1615993368901
//		}
//  ]
//	}
}
