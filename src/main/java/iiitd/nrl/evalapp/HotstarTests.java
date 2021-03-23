package iiitd.nrl.evalapp;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.android.Activity;
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

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

@SuppressWarnings("unchecked")
public class HotstarTests {
	AndroidDriver<MobileElement> driver;
	String appName = "Hotstar";
	String testName = "NA";
	String testStatusReason = "NA";

	@AfterClass
    public void update() {

    }
    
	@BeforeMethod
	public void launchCap() {
		driver = MainLauncher.driver;
		driver.startActivity(new Activity("in.startv.hotstaronly","in.startv.hotstar.rocky.home.HomeActivity"));
//		DesiredCapabilities cap=new DesiredCapabilities();
//		cap.setCapability("appPackage", "in.startv.hotstaronly");
//		cap.setCapability("appActivity", "in.startv.hotstar.rocky.home.HomeActivity");
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
//			MyDatabase.addTestResult(appName, testName, null, "NA" , false, "App Not Installed");
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
		String jsonString = driver.getEvents().getJsonData();
//		System.out.println(jsonString);
		long timeTaken = 0;

		HashMap<String, Long> main_events = new HashMap<String, Long>();

		if (testResult.isSuccess()) {
			if (testResult.getName() == "searchTest") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -2);
//				main_events.put(testResult.getName(), timeTaken);
			} else if (testResult.getName() == "trendingTest") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -2);
//				main_events.put(testResult.getName(), timeTaken);
			}
		}

		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);

//		driver.quit();
	}

	@Test
	public void searchTest(){

		testName = "Search Test";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
		try {

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Search"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("in.startv.hotstar:id/search_text"))).sendKeys("dil bechara");
			/* search video time measurement starts*/
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.LinearLayout/androidx.recyclerview.widget.RecyclerView/android.widget.FrameLayout[1]/android.widget.FrameLayout/android.widget.ImageView[2]"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("in.startv.hotstar:id/metadata_download")));
			/* search video time measurement stops*/
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}

	@Test
	public void trendingTest(){

		testName = "Trending Test";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Open navigation drawer"))).click();
			/* load trending videos page time measurement starts*/
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"Trending\")"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("in.startv.hotstar:id/frame_player")));
			/* load trending videos page time measurement stops*/
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}

}
