package iiitd.nrl.evalapp;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class YouTubeTests_SearchChannel {
	AndroidDriver<MobileElement> driver;

	AppiumDriverLocalService service;
	String appName = "Youtube_searchChannel";
	String testName = "NA";
	String testStatusReason = "NA";

	@BeforeMethod
	public void launchCap() {
		DesiredCapabilities cap=new DesiredCapabilities();
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
	public void channelTest() throws InterruptedException{
		testName = "find channel";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.youtube:id/menu_item_1")));
			driver.findElement(By.id("com.google.android.youtube:id/menu_item_1")).click();

			driver.findElement(By.id("com.google.android.youtube:id/search_edit_text")).sendKeys("unacademy upsc");
			((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().resourceId(\"com.google.android.youtube:id/channel_item\"));"))).isDisplayed();

			driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"com.google.android.youtube:id/channel_item\")")).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Subscribe to Unacademy UPSC."))).isDisplayed();
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}
//	JSON COMMANDS
}
//{
//		"commands": [
//		{
//		"cmd": "findElement",
//		"startTime": 1615913151118,
//		"endTime": 1615913152226
//		},
//		{
//		"cmd": "elementDisplayed",
//		"startTime": 1615913152244,
//		"endTime": 1615913152678
//		},
//		{
//		"cmd": "findElement",
//		"startTime": 1615913152694,
//		"endTime": 1615913154228
//		},
//		{
//		"cmd": "click",
//		"startTime": 1615913154244,
//		"endTime": 1615913154308
//		},
//		{
//		"cmd": "findElement",
//		"startTime": 1615913154322,
//		"endTime": 1615913154495
//		},
//		{
//		"cmd": "setValue",
//		"startTime": 1615913154508,
//		"endTime": 1615913156201
//		},
//		{
//		"cmd": "pressKeyCode",
//		"startTime": 1615913156217,
//		"endTime": 1615913157099
//		},
//		{
//		"cmd": "findElement",
//		"startTime": 1615913157109,
//		"endTime": 1615913159925
//		},
//		{
//		"cmd": "elementDisplayed",
//		"startTime": 1615913159928,
//		"endTime": 1615913159979
//		},
//		{
//		"cmd": "findElement",
//		"startTime": 1615913159985,
//		"endTime": 1615913160032
//		},
//		{
//		"cmd": "click",
//		"startTime": 1615913160048,
//		"endTime": 1615913161586
//		},
//		{
//		"cmd": "findElement",
//		"startTime": 1615913161593,
//		"endTime": 1615913161692
//		},
//		{
//		"cmd": "elementDisplayed",
//		"startTime": 1615913161696,
//		"endTime": 1615913161724
//		},
//		{
//		"cmd": "getLogEvents",
//		"startTime": 1615913161733,
//		"endTime": 1615913161734
//		}
//		]
//		}
