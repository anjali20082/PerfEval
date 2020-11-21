package iiitd.nrl.evalapp;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

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
		System.out.println(jsonString);
		long timeTaken = 0;

		HashMap<String, Long> main_events = new HashMap<String, Long>();

		if (testResult.isSuccess()) {
			if (testResult.getName() == "searchTest") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, -8, -6);
				main_events.put("search", timeTaken);
				timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -2);
				main_events.put("full coverage", timeTaken);
			}
		}

		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);

		driver.quit();
	}

	@Test
	public void searchTest() throws InterruptedException{

		testName = "Search News Test";
		WebDriverWait wait = new WebDriverWait(driver, 300);

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/search_button"))).click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/open_search_view_edit_text"))).sendKeys("delhi");

			/* search news  test measurement starts*/
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().resourceIdMatches(\"com.google.android.apps.magazines:id/suggest_text\").scrollable(true)).scrollIntoView("
							+ "new UiSelector().textMatches(\"Delhi\"));"))).click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/rect_icon")));
			/* search news  test measurement stops*/

			/* load news  test measurement starts*/
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().resourceIdMatches(\"com.google.android.apps.magazines:id/recycler_view\").scrollable(true)).scrollIntoView("
							+ "new UiSelector().textContains(\"View Full coverage\"));"))).click();

			wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().resourceIdMatches(\"com.google.android.apps.magazines:id/card\"));"))));
			/* load news  test measurement stops*/
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}

}