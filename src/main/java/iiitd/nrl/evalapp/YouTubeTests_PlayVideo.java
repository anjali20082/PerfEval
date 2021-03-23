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
//		System.out.println(jsonString);
		long timeTaken = 0;

		HashMap<String, Long> main_events = new HashMap<>();

		if (testResult.isSuccess()) {
			if (testResult.getName() == "playTest") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, 6, 7);
				main_events.put("searchVideo", timeTaken);

				timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -3);
				main_events.put("playVideo", timeTaken);

			} else if (testResult.getName() == "channelTest") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, 6, 7);
				main_events.put("searchChannel", timeTaken);

				timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -3);
				main_events.put("openChannelPage", timeTaken);
			}
		}

		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);
		testStatusReason = "NA";
		driver.quit();
	}


	@Test
	public void playTest() throws InterruptedException {
		testName = "play test";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.youtube:id/menu_item_1")));
			driver.findElement(By.id("com.google.android.youtube:id/menu_item_1")).click();
			driver.findElement(By.id("com.google.android.youtube:id/search_edit_text")).sendKeys("manikarnika");

			/* searching video time measurement starts */
			((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().descriptionContains(\"Official Trailer\"));"))).isDisplayed();
			/* searching video time measurement starts */


			driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Official Trailer\")")).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Video player"))).isDisplayed();

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
