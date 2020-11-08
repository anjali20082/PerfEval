package iiitd.nrl.evalapp;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
	        MyDatabase.addTestResult(appName, testName, 0, "App Not Installed" , false);
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
		long time = testResult.getEndMillis() - testResult.getStartMillis();
        String connType = getConnectionType();

        MyDatabase.addTestResult(appName, testName, time, connType, testResult.isSuccess());
        driver.quit();
	}
	
	@Test
	public void searchTest() throws InterruptedException{

		testName = "Search News Test";
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/search_button"))).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/open_search_view_edit_text"))).sendKeys("delhi");
//		driver.findElement(By.id("com.google.android.apps.magazines:id/open_search_view_edit_text")).click();
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/suggest_text")));
//		List<MobileElement> results= (List<MobileElement>) driver.findElementsById("com.google.android.apps.magazines:id/suggest_text");
//		results.get(0).click();

		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
				"new UiScrollable(" + "new UiSelector().resourceIdMatches(\"com.google.android.apps.magazines:id/suggest_text\").scrollable(true)).scrollIntoView("
						+ "new UiSelector().textMatches(\"Delhi\"));"))).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/edition_pager_header_default_content")));
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
				"new UiScrollable(" + "new UiSelector().resourceIdMatches(\"com.google.android.apps.magazines:id/recycler_view\").scrollable(true)).scrollIntoView("
						+ "new UiSelector().textContains(\"View Full coverage\"));"))).click();



	}
	
	@Test
	public void headlinesTest() throws InterruptedException{

		testName = "Headlines Test";
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/tab_headlines"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("India"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
				"new UiScrollable(" + "new UiSelector().resourceIdMatches(\"com.google.android.apps.magazines:id/recycler_view\").scrollable(true)).scrollIntoView("
						+ "new UiSelector().resourceId(\"com.google.android.apps.magazines:id/story_metadata_icon\"));"))).click();		
	}

}