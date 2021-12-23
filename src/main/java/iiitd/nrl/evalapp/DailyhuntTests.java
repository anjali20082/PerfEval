package iiitd.nrl.evalapp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	String commandsCompleted = "";
	ArrayList<Integer> txrx;
	String tx_bytes = "";
	String rx_bytes = "";
	Integer rx_initial ;
	Integer tx_initial ;

	@BeforeMethod
	public void launchCap() throws IOException {
		txrx = NetStats.getstats("10346");
		rx_initial = txrx.get(0);
		tx_initial = txrx.get(1);
//		System.out.println(rx_initial + "  "+ tx_initial);

//		tx_bytes += tx_initial+":";
//		rx_bytes += rx_initial+":";

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
			return "Wifi 2";
		else if (connType == 4)
			return "MobileData 4";
		else if (connType == 6)
			return "Wifi & MobileData 6";
		return "Wifi " + connType;
	}

	@AfterMethod
	public void restart(ITestResult testResult) throws Exception {
		String jsonString = driver.getEvents().getJsonData();

		MyDatabase.setCurrentApp(appName);
		MyDatabase.setCommands(commandsCompleted);
		MyDatabase.setAppJsonCommands(jsonString);
		MyDatabase.setTestStatus(testResult.isSuccess());
		MyDatabase.setTestStatusReason(testStatusReason);
		MyDatabase.setConnType(getConnectionType());
		MyDatabase.set_TX_RX_Bytes(tx_bytes, rx_bytes);

		testStatusReason = "NA";
		driver.quit();
	}

	@Test
	public void searchNews() throws IOException {

		testName = "search news";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/global_search"))).click();
			commandsCompleted += "clickSearch:";

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/search_box"))).sendKeys("sports");
			commandsCompleted += "enterName:";

			((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
			commandsCompleted += "pressEnter:";

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"News\")"))).click();
			commandsCompleted += "clickNews:";


			/* Search news time measurement starts*/
//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/follow_button"))).isDisplayed();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.eterno:id/news_title"))).isDisplayed();
			commandsCompleted += "checkNews:";
			/* Search news time measurement stops*/
			commandsCompleted += "P";

			txrx = NetStats.getstats("10346");
			Integer rx_1 = txrx.get(0);
			Integer tx_1 = txrx.get(1);
//			System.out.println(rx_1 + "  "+ tx_1);

			tx_bytes += tx_1 -tx_initial;
			rx_bytes += rx_1 - rx_initial;

			System.out.println("TX: "+tx_bytes);
			System.out.println("RX: "+rx_bytes);

		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
//		JSON COMMMANDS
	}
//	{
//		"commands": [
//		{
//			"cmd": "findElement",
//				"startTime": 1615991691553,
//				"endTime": 1615991692387
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615991692404,
//				"endTime": 1615991693419
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615991693428,
//				"endTime": 1615991694589
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615991695256,
//				"endTime": 1615991695797
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615991695800,
//				"endTime": 1615991695830
//		},
//		{
//			"cmd": "setValue",
//				"startTime": 1615991695841,
//				"endTime": 1615991696648
//		},
//		{
//			"cmd": "pressKeyCode",
//				"startTime": 1615991696674,
//				"endTime": 1615991697763
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615991697782,
//				"endTime": 1615991699835
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615991699853,
//				"endTime": 1615991702045
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615991702049,
//				"endTime": 1615991702628
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615991702631,
//				"endTime": 1615991702650
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615991702653,
//				"endTime": 1615991702672
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1615991702683,
//				"endTime": 1615991702683
//		}
//  ]
//	}

	//	@Test
	public void livetvTest() throws InterruptedException{

		testName = "live tv";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

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