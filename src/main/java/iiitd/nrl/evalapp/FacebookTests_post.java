package iiitd.nrl.evalapp;



import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
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
public class FacebookTests_post {
	AndroidDriver<MobileElement> driver;
	String appName = "Facebook_post";
	String testName = "NA";
	String testStatusReason = "NA";
	String commandsCompleted = "";
	
	@AfterClass
    public void update() {

    }
    
	@BeforeMethod
	public void launchCap() {
		DesiredCapabilities cap=new DesiredCapabilities();
		cap.setCapability("appPackage", "com.facebook.katana");
		cap.setCapability("appActivity", "com.facebook.katana.activity.FbMainTabActivity");
		cap.setCapability("noReset", "true");
		cap.setCapability("fullReset", "false");
		cap.setCapability("autoGrantPermissions", true);
		cap.setCapability("autoAcceptAlerts", true);

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

		System.out.println(jsonString);

		MyDatabase.setCurrentApp(appName);
		MyDatabase.setAppJsonCommands(jsonString);
		MyDatabase.setCommands(commandsCompleted);
		MyDatabase.setTestStatus(testResult.isSuccess());
		MyDatabase.setTestStatusReason(testStatusReason);
		MyDatabase.setConnType(getConnectionType());
		MyDatabase.addTestResult();

		driver.quit();
	}



	@Test
	public void postGroup() throws InterruptedException, IOException {

		testName = "post in a group";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

		Random rand = new Random();
		int rand_int = rand.nextInt(1000);
		String rand_str = Integer.toString(rand_int);
		String message = "Hi, this is an automated post:" + rand_str;
		String ui = "";
		try {
//			ui = "new UiSelector().descriptionMatches(\".*(?i)Groups(?-i).*\")";
//			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui))).click();
//			commandsCompleted += "groups:";
//
//			ui = "new UiSelector().descriptionMatches(\"(?i)Your Groups(?-i)\")";
//			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui))).click();
//			commandsCompleted += "yourGroups:";
//
//			ui = "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().descriptionMatches(\"(?i)Evaluation of Apps Button(?-i)\"));";
//			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui))).click();
//			commandsCompleted += "evalApp:";

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Search Facebook"))).click();
			commandsCompleted += "clickSearch:";

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).click();
			commandsCompleted += "searchPerson:";

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("Evaluation of Apps");
			commandsCompleted += "enterName:";

			((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
			commandsCompleted += "pressEnter:";

			ui = "new UiSelector().description(\"Visit\");";
			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui))).click();
			commandsCompleted += "evalapp:";

			ui = "new UiScrollable(" + "new UiSelector().scrollable(true)).scrollToBeginning(20);";
			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui)));
			commandsCompleted += "scrollingAbove:";

			ui = "new UiSelector().descriptionContains(\"Create a post\");";
			String ui1 = "new UiSelector().descriptionContains(\"Write something\");";
			wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui)),
					ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui1))));

			if (!driver.findElements(MobileBy.AndroidUIAutomator(ui)).isEmpty()) {
				driver.findElement(MobileBy.AndroidUIAutomator(ui)).click();
			}
			else {
				driver.findElement(MobileBy.AndroidUIAutomator(ui1)).click();
			}
			commandsCompleted += "writePost:";

			ui = "android.widget.AutoCompleteTextView";
			ui1 = "android.widget.EditText";
			wait.until(ExpectedConditions.or(
					ExpectedConditions.visibilityOfElementLocated(By.className(ui)),
					ExpectedConditions.visibilityOfElementLocated(By.className(ui1))));
			if (!driver.findElements(By.className(ui)).isEmpty()) {
				driver.findElement(By.className(ui)).click();
				driver.findElement(By.className(ui)).sendKeys(message);
			}
			else {
				driver.findElement(By.className(ui1)).click();
				driver.findElement(By.className(ui1)).sendKeys(message);
			}
			commandsCompleted += "writeMsg:";

			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("POST"))).click();
			commandsCompleted += "clickPost:";

			ui = "new UiSelector().descriptionMatches(\"(?i)profile picture(?-i)\");";
			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui)));
			commandsCompleted += "profilePicture:";

			commandsCompleted += "P";
			/* post group time measurement stops */
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
//				"startTime": 1615992974046,
//				"endTime": 1615992975100
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992975115,
//				"endTime": 1615992977351
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992977365,
//				"endTime": 1615992977418
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992977429,
//				"endTime": 1615992979230
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992979235,
//				"endTime": 1615992979271
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992979283,
//				"endTime": 1615992981302
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992981319,
//				"endTime": 1615992984217
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992984342,
//				"endTime": 1615992984387
//		},
//		{
//			"cmd": "findElements",
//				"startTime": 1615992984405,
//				"endTime": 1615992984490
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992984497,
//				"endTime": 1615992984544
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992984561,
//				"endTime": 1615992985710
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992985716,
//				"endTime": 1615992985770
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615992985775,
//				"endTime": 1615992985795
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992985809,
//				"endTime": 1615992987656
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992987664,
//				"endTime": 1615992987722
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615992987725,
//				"endTime": 1615992987751
//		},
//		{
//			"cmd": "setValue",
//				"startTime": 1615992987774,
//				"endTime": 1615992988555
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992988571,
//				"endTime": 1615992989358
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992989364,
//				"endTime": 1615992989427
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992989442,
//				"endTime": 1615992991379
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1615992991387,
//				"endTime": 1615992991388
//		}
//  ]
//	}



}
