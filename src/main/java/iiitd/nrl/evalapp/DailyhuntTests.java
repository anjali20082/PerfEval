package iiitd.nrl.evalapp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
	
	@AfterClass
    public void update() {

    }
    
	@BeforeMethod
	public void launchCap() {
		DesiredCapabilities cap=new DesiredCapabilities();
		cap.setCapability("appPackage", "com.eterno");
		cap.setCapability("appActivity", "com.newshunt.appview.common.ui.activity.HomeActivity");
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

		HashMap<String, Long> main_events = new HashMap<String, Long>();

		if (testResult.isSuccess()) {
			if (testResult.getName() == "searchNews") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, -5, -3); // 8 and 10
				main_events.put(testResult.getName(), timeTaken);
			} else if (testResult.getName() == "livetvTest") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -2); // 7 and 9
				main_events.put(testResult.getName(), timeTaken);
			}
		}

		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);

		driver.quit();
	}

	@Test
	public void searchNews(){
		testName = "search news";
		WebDriverWait wait = new WebDriverWait(driver, 300);
		try {
//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/global_search"))).click();
//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/search_box"))).sendKeys("sports");
//			((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"News\")"))).click();
//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/news_title"))).click();
			wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().resourceId(\"com.eterno:id/news_home_view_pager\").scrollable(true)).scrollIntoView("
							+ "new UiSelector().resourceId(\"com.eterno:id/news_title\"));")))).click();
			/* Search news time measurement starts*/
//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"News\")"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/icon"))).isDisplayed();
			/* Search news time measurement stops*/
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}
	@Test
	public void livetvTest() throws InterruptedException{
		testName = "live tv";
		WebDriverWait wait = new WebDriverWait(driver, 300);
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/scrollable_bottom_container")));
			List<MobileElement> bottomBar = (List<MobileElement>) driver.findElementsById("com.eterno:id/navbar_appsection_icon");
			bottomBar.get(1).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/root_view")));
			List<MobileElement> news = (List<MobileElement>) driver.findElementsById("com.eterno:id/root_view");
			/* live tv time measurement starts*/
			news.get(0).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/constraint_lyt")));
			/* live tv time measurement stops*/
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}

}
