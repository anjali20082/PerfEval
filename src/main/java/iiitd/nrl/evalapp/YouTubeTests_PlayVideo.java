package iiitd.nrl.evalapp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.clipboard.ClipboardContentType;
import io.appium.java_client.serverevents.TimedEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.offset.PointOption;

@SuppressWarnings("unchecked")
public class YouTubeTests_PlayVideo {
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
	public void restart(ITestResult testResult) {
		String jsonString = driver.getEvents().getJsonData();

		MyDatabase.setCurrentApp(appName);
		MyDatabase.setCommands(commandsCompleted);
		MyDatabase.setAppJsonCommands(jsonString);
		MyDatabase.setTestStatus(testResult.isSuccess());
		MyDatabase.setTestStatusReason(testStatusReason);
		MyDatabase.setConnType(getConnectionType());

		MyDatabase.addTestResult();

		testStatusReason = "NA";
		driver.quit();
	}


	@Test
	public void playTest() throws InterruptedException {
		testName = "play test";
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

			ui = "new UiSelector().descriptionContains(\"Official Trailer\")";
			driver.findElement(MobileBy.AndroidUIAutomator(ui)).click();
			commandsCompleted += "clickSearchResult:";

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Video player"))).isDisplayed();
			commandsCompleted += "checkVideoPlayer:";

			commandsCompleted += "P";
			System.out.println(commandsCompleted);

		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
//		json commands
	}
//	{
//  "commands": [
//	{
//		"cmd": "findElement",
//			"startTime": 1615909317071,
//			"endTime": 1615909318231
//	},
//	{
//		"cmd": "elementDisplayed",
//			"startTime": 1615909318241,
//			"endTime": 1615909318675
//	},
//	{
//		"cmd": "findElement",
//			"startTime": 1615909318693,
//			"endTime": 1615909320156
//	},
//	{
//		"cmd": "click",
//			"startTime": 1615909320167,
//			"endTime": 1615909320229
//	},
//	{
//		"cmd": "findElement",
//			"startTime": 1615909320246,
//			"endTime": 1615909320536
//	},
//	{
//		"cmd": "setValue",
//			"startTime": 1615909320554,
//			"endTime": 1615909322195
//	},
//	{
//		"cmd": "pressKeyCode",
//			"startTime": 1615909322206,
//			"endTime": 1615909323324
//	},
//	{
//		"cmd": "findElement",
//			"startTime": 1615909323342,
//			"endTime": 1615909326598
//	},
//	{
//		"cmd": "elementDisplayed",
//			"startTime": 1615909326601,
//			"endTime": 1615909326654
//	},
//	{
//		"cmd": "findElement",
//			"startTime": 1615909326667,
//			"endTime": 1615909326709
//	},
//	{
//		"cmd": "click",
//			"startTime": 1615909326714,
//			"endTime": 1615909328197
//	},
//	{
//		"cmd": "findElement",
//			"startTime": 1615909328203,
//			"endTime": 1615909328239
//	},
//	{
//		"cmd": "elementDisplayed",
//			"startTime": 1615909328243,
//			"endTime": 1615909329234
//	},
//	{
//		"cmd": "getLogEvents",
//			"startTime": 1615909329246,
//			"endTime": 1615909329246
//	}
//  ]
//}
}
