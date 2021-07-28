package iiitd.nrl.evalapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
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

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

@SuppressWarnings("unchecked")
public class DailyhuntTests  {
	AndroidDriver<MobileElement> driver;
	String appName = "Dailyhunt";
	String testName = "NA";
	String testStatusReason = "NA";
	int versionId;
	boolean appInstalled = false;


	String[] app_version = {
		"com.eterno_13.0.5-648.apk",
		"com.eterno_14.0.6-669.apk",
		"com.eterno_15.0.7-2021.apk",
		"com.eterno_16.0.5-2106.apk",
		"com.eterno_17.0.6-2143.apk"
	};
	
	@BeforeClass
    public void setUp() throws IOException, InterruptedException {
		versionId = MyDatabase.getVersionSelected();
//		versionId = 4;
    }
    
	@BeforeMethod
	public void launchCap() {
//		driver = MainLauncher.driver;
//		if (versionId <= 3) {
//			driver.startActivity(new Activity("com.eterno","com.newshunt.newshome.view.activity.NewsHomeActivity"));
//
//		} else if (versionId >= 4) {
//			driver.startActivity(new Activity("com.eterno","com.newshunt.appview.common.ui.activity.HomeActivity"));
//
//		}

		DesiredCapabilities cap=new DesiredCapabilities();
		cap.setCapability("appPackage", "com.eterno");
		// version 13 && 14
		if (versionId <= 3) {
			cap.setCapability("appActivity", "com.newshunt.newshome.view.activity.NewsHomeActivity");
		} else if (versionId >= 4) {
			cap.setCapability("appActivity", "com.newshunt.appview.common.ui.activity.HomeActivity");
		}
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
//			if (testResult.getName() == "searchNews") {
//				timeTaken = MyDatabase.getTimeTaken(jsonString, -5, -3); // 8 and 10
//				main_events.put(testResult.getName(), timeTaken);
//			} else if (testResult.getName() == "livetvTest") {
//				timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -2); // 7 and 9
//				main_events.put(testResult.getName(), timeTaken);
//			}
//		}
//
//		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);

//		driver.quit();
	}

	@Test
	public void searchNews(){

		testName = "search news";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

		try {

			if (versionId == 1) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"English\");"))).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/global_search"))).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/search_box"))).sendKeys("india gate");
				((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.HorizontalScrollView[@content-desc=\"Tabs to select news headlines, topics and sources.\"]/android.widget.LinearLayout/android.widget.RelativeLayout[2]/android.widget.TextView"))).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/toggle_button"))).isDisplayed();
			}
			else if (versionId == 2) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"English\");"))).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/global_search"))).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/search_box"))).sendKeys("india gate");
				((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.HorizontalScrollView[@content-desc=\"Tabs to select news headlines, topics and sources.\"]/android.widget.LinearLayout/android.widget.RelativeLayout[2]/android.widget.TextView"))).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/toggle_button"))).isDisplayed();
			}
			else if (versionId == 3) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"English\");"))).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/tv_later"))).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/global_search"))).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/search_box"))).sendKeys("india gate");
				((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
				((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.BACK));
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.HorizontalScrollView[@content-desc=\"Tabs to select news headlines, topics and sources.\"]/android.widget.LinearLayout/android.widget.RelativeLayout[2]/android.widget.TextView"))).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/toggle_button"))).isDisplayed();
			}
			else if (versionId == 4) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"English\");"))).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/global_search"))).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/search_box"))).sendKeys("india gate");
				((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
//          ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.BACK));
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.HorizontalScrollView[@content-desc=\"Tabs to select news headlines, topics and sources.\"]/android.widget.LinearLayout/android.widget.RelativeLayout[2]/android.widget.TextView"))).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/follow_button"))).isDisplayed();
			}
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}

//	@Test
	public void livetvTest() throws InterruptedException{

		testName = "live tv";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

		try {
//			Version 13 && 14
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/scrollable_bottom_container")));

			List<MobileElement> bottomBar = (List<MobileElement>) driver.findElementsById("com.eterno:id/navbar_appsection_icon");
			bottomBar.get(1).click();
//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/root_view")));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/rl_parent")));

//			List<MobileElement> news = (List<MobileElement>) driver.findElementsById("com.eterno:id/root_view");
			List<MobileElement> news = (List<MobileElement>) driver.findElementsById("com.eterno:id/rl_parent");

			/* live tv time measurement starts*/
			news.get(0).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/exo_subtitles")));
//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/constraint_lyt")));

			/* live tv time measurement stops*/
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}

}
