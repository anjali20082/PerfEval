package iiitd.nrl.evalapp;


import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
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

@SuppressWarnings("unchecked")
public class GooglenewsTests {
	AndroidDriver<MobileElement> driver;
	String appName = "GoogleNews";
	String testName = "NA";
	String testStatusReason = "NA";

    @AfterClass
    public void update() {

    }
	
	@BeforeMethod
	public void launchCap() {
		DesiredCapabilities cap=new DesiredCapabilities();
		cap.setCapability("appPackage", "com.google.android.apps.magazines");
		cap.setCapability("appActivity", "com.google.apps.dots.android.app.activity.CurrentsStartActivity");
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

		MyDatabase.setCurrentApp(appName);
		MyDatabase.setAppJsonCommands(jsonString);
		MyDatabase.setTestStatus(testResult.isSuccess());
		MyDatabase.setTestStatusReason(testStatusReason);
		MyDatabase.setConnType(getConnectionType());

//		long timeTaken = 0;
//
//		HashMap<String, Long> main_events = new HashMap<String, Long>();
//
//		if (testResult.isSuccess()) {
//			if (testResult.getName() == "searchTest") {
//				timeTaken = MyDatabase.getTimeTaken(jsonString, -3, -2);
//				main_events.put("searchNews", timeTaken);
//			}
//		}
//
//		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);
		testStatusReason = "NA";
		driver.quit();
	}

	@Test
	public void searchTest() throws InterruptedException{
		testName = "Search News Test";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
		try {
//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/title_and_body"))).click();

//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/open_search_view_edit_text"))).sendKeys("delhi");

			/* search news  test measurement starts*/
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(new UiSelector().resourceIdMatches(\"com.google.android.apps.magazines:id/home_fragment_content\").scrollable(true)).scrollIntoView(new UiSelector().resourceId(\"com.google.android.apps.magazines:id/title\"));"))).click();

			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.google.android.apps.magazines:id/hero_action_button"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.google.android.apps.magazines:id/edition_pager_header_icon_label")));
//
//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/rect_icon")));
			/* search news  test measurement stops*/

			/* load news  test measurement starts*/
//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
//					"new UiScrollable(" + "new UiSelector().resourceIdMatches(\"com.google.android.apps.magazines:id/recycler_view\").scrollable(true)).scrollIntoView("
//							+ "new UiSelector().textContains(\"View Full coverage\"));"))).click();

//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
//							"new UiSelector().text(\"View Full Coverage\");"))).click();

//			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(
//					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView(" +
//							"new UiSelector().resourceId(\"com.google.android.apps.magazines:id/title\"));")));


//			wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(
//					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
//							+ "new UiSelector().resourceIdMatches(\"com.google.android.apps.magazines:id/card\"));")))).click();

//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/primary_action_button")));

//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
//					"new UiSelector().text(\"TOI\");"))).isDisplayed();
			/* load news  test measurement stops*/
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
//				"startTime": 1615992361258,
//				"endTime": 1615992363003
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992363020,
//				"endTime": 1615992365128
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992365144,
//				"endTime": 1615992365201
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992365207,
//				"endTime": 1615992367675
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992368593,
//				"endTime": 1615992369167
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1615992369176,
//				"endTime": 1615992369177
//		}
//  ]
//	}


}