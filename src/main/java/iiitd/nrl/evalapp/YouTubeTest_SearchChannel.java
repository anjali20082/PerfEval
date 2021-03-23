package iiitd.nrl.evalapp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.android.Activity;
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
public class YouTubeTest_SearchChannel {
	AndroidDriver<MobileElement> driver;

	AppiumDriverLocalService service;
	String appName = "Youtube_searchChannel";
	String testName = "NA";
	String testStatusReason = "NA";
	int versionId;
	Long start_time_1, start_time_2 , end_time_1, end_time_2;

	@BeforeClass
	public void setUp() throws IOException, InterruptedException {
//		versionId = MyDatabase.getVersionSelected();
		versionId = 3;
		System.out.println("APP: " + appName + " Version ID: " + versionId);
	}

	@BeforeMethod
	public void launchCap() {
		driver = MainLauncher.driver;
		Activity activity = new Activity("com.google.android.youtube","com.google.android.youtube.HomeActivity");
		activity.setAppWaitActivity("com.google.android.apps.youtube.app.WatchWhileActivity");
		driver.startActivity(activity);
//		DesiredCapabilities cap=new DesiredCapabilities();
//		cap.setCapability("appPackage", "com.google.android.youtube");
//		cap.setCapability("appActivity", "com.google.android.youtube.HomeActivity");
//		cap.setCapability("noReset", "true");
//		cap.setCapability("fullReset", "false");
//		cap.setCapability("autoGrantPermissions", true);
//		cap.setCapability("autoAcceptAlerts", true);
//		cap.setCapability("uiautomator2ServerInstallTimeout", 60000);
//
//		URL url;
//		try {
//			url = new URL("http://127.0.0.1:4723/wd/hub");
//			driver=new AndroidDriver<MobileElement>(url,cap);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (WebDriverException e) {
//			MyDatabase.addTestResult(appName, testName, null, "NA", false, "App Not Installed");
//		}
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
//		String jsonString = driver.getEvents().getJsonData();
//		System.out.println(jsonString);
		long timeTaken_1 = 0, timeTaken_2 = 0;

		HashMap<String, Long> main_events = new HashMap<>();

		if (testResult.isSuccess()) {
			if (testResult.getName() == "channelTest") {
				timeTaken_1 = end_time_1 - start_time_1;
				main_events.put("searchChannel", timeTaken_1);

				timeTaken_2 = end_time_2 - start_time_2;
				main_events.put("openChannel", timeTaken_2);
			}

		}

		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);

//        driver.quit();
	}

	
	@Test
	public void channelTest() throws InterruptedException{
		testName = "find channel";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Search"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.youtube:id/search_edit_text"))).sendKeys("unacademy upsc");;
//			driver.findElement(By.id("com.google.android.youtube:id/search_edit_text")).sendKeys("unacademy upsc");

			start_time_1 =System.currentTimeMillis();
			((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().resourceId(\"com.google.android.youtube:id/channel_item\"));"))).isDisplayed();
			end_time_1 = System.currentTimeMillis();

			start_time_2 = System.currentTimeMillis();
			driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"com.google.android.youtube:id/channel_item\")")).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Subscribe to Unacademy UPSC."))).isDisplayed();
			end_time_2 = System.currentTimeMillis();
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}
}
