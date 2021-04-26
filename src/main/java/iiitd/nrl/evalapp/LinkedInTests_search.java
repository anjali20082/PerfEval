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
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class LinkedInTests_search {
    AndroidDriver<MobileElement> driver;
    String appName = "LinkedIn_search";
    String testName = "NA";
    String testStatusReason = "NA";
    String commandsCompeleted = "";

    @BeforeMethod
    public void launchCap() {
        DesiredCapabilities cap=new DesiredCapabilities();
        cap.setCapability("appPackage", "com.linkedin.android");
        cap.setCapability("appActivity", "com.linkedin.android.authenticator.LaunchActivity");
        cap.setCapability("noReset", "true");
        cap.setCapability("fullReset", "false");
        cap.setCapability("autoGrantPermissions", true);
        cap.setCapability("autoAcceptAlerts", true);
        cap.setCapability("uiautomator2ServerInstallTimeout", 60000);

        URL url;
        try {
            url = new URL("http://127.0.0.1:4723/wd/hub");
            driver= new AndroidDriver<MobileElement>(url, cap);
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
        MyDatabase.setCommands(commandsCompeleted);
        MyDatabase.setAppJsonCommands(jsonString);
        MyDatabase.setTestStatus(testResult.isSuccess());
        MyDatabase.setTestStatusReason(testStatusReason);
        MyDatabase.setConnType(getConnectionType());

        testStatusReason = "NA";
        driver.quit();
    }


    @Test
    public void searchPerson() throws InterruptedException{
        testName = "search person";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
        try {

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/search_bar_text"))).click();
            commandsCompeleted += "clickSearch:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/search_bar_edit_text"))).sendKeys("Bill Gates");
            commandsCompeleted += "enterName:";

            ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
            commandsCompeleted += "pressEnter:";

            /* search time measurement starts*/
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/search_results_hero_entity_container"))).click();
            commandsCompeleted += "searchResult:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/profile_view_messob_top_card_profile_picture")));
            commandsCompeleted += "checkProfilePicture:";

            commandsCompeleted += "P";
            /* search time measurement stops*/
        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
//		JSON Commands in the below comment
        }
//		{
//			"commands": [
//			{
//				"cmd": "findElement",
//					"startTime": 1615906625233,
//					"endTime": 1615906626211
//			},
//			{
//				"cmd": "elementDisplayed",
//					"startTime": 1615906626224,
//					"endTime": 1615906626253
//			},
//			{
//				"cmd": "click",
//					"startTime": 1615906626259,
//					"endTime": 1615906626326
//			},
//			{
//				"cmd": "findElement",
//					"startTime": 1615906627901,
//					"endTime": 1615906628329
//			},
//			{
//				"cmd": "elementDisplayed",
//					"startTime": 1615906628333,
//					"endTime": 1615906628364
//			},
//			{
//				"cmd": "setValue",
//					"startTime": 1615906628380,
//					"endTime": 1615906629115
//			},
//			{
//				"cmd": "pressKeyCode",
//					"startTime": 1615906629138,
//					"endTime": 1615906630573
//			},
//			{
//				"cmd": "findElement",
//					"startTime": 1615906630587,
//					"endTime": 1615906633276
//			},
//			{
//				"cmd": "elementDisplayed",
//					"startTime": 1615906633279,
//					"endTime": 1615906633304
//			},
//			{
//				"cmd": "click",
//					"startTime": 1615906633313,
//					"endTime": 1615906633397
//			},
//			{
//				"cmd": "findElement",
//					"startTime": 1615906634210,
//					"endTime": 1615906635204
//			},
//			{
//				"cmd": "elementDisplayed",
//					"startTime": 1615906635207,
//					"endTime": 1615906635226
//			},
//			{
//				"cmd": "getLogEvents",
//					"startTime": 1615906635237,
//					"endTime": 1615906635237
//			}
//  		]
//		}


    }
}
