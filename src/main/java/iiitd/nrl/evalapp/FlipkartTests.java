package iiitd.nrl.evalapp;



import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
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

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

@SuppressWarnings("unchecked")
public class FlipkartTests {
	AndroidDriver<MobileElement> driver;
	String appName = "Flipkart";
	String testName = "NA";
	String testStatusReason = "NA";
	boolean addToCartClicked = false;

	@AfterClass
    public void update() {

    }
    
	@BeforeMethod
	public void launchCap() {
		DesiredCapabilities cap=new DesiredCapabilities();
		cap.setCapability("appPackage", "com.flipkart.android");
		cap.setCapability("appActivity", "com.flipkart.android.activity.HomeFragmentHolderActivity");
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

//		HashMap<String, Long> main_events = new HashMap<>();

//		if (testResult.isSuccess()) {
//			if (testResult.getName() == "getProduct") {
//				timeTaken = MyDatabase.getTimeTaken(jsonString, 9, 9) + MyDatabase.getTimeTaken(jsonString, 10, 11);
//				main_events.put("searchProduct", timeTaken);
//
//				if (addToCartClicked) {
//					timeTaken = MyDatabase.getTimeTaken(jsonString, 17, 18);
//					main_events.put("addToCart", timeTaken);
//				}
//
//				timeTaken = MyDatabase.getTimeTaken(jsonString, -8, -7);
//				main_events.put("goToCart", timeTaken);
//
//				timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -3);
//				main_events.put("removeFromCart", timeTaken);
//			}
//		}
//		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);
		testStatusReason = "NA";
		driver.quit();
	}

	@Test
	public void getProduct() throws InterruptedException {
		testName = "search product";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.flipkart.android:id/search_widget_textbox"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.flipkart.android:id/search_autoCompleteTextView"))).sendKeys("phone");

			/* search product test measurement starts */
			String ui = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/androidx.drawerlayout.widget.DrawerLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.LinearLayout/androidx.recyclerview.widget.RecyclerView/android.widget.RelativeLayout[1]";
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ui))).click();

			ui = "new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView(" + "new UiSelector().textContains(\"★\"));";
			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui))).click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/androidx.drawerlayout.widget.DrawerLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup")));
			/* search product test measurement stops */

			if (driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().text(\"GO TO CART\");")).isEmpty()) {
				addToCartClicked = true;
				testStatusReason = "add to cart clicked";
				ui = "new UiSelector().text(\"ADD TO CART\");";
				wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui)));
				/* add to cart test measurement starts */
				driver.findElement(MobileBy.AndroidUIAutomator(ui)).click();
				/* add to cart test measurement stops */

				wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Back Button"))).click();
			} else {
				testStatusReason = "add to cart not clicked";
			}

			ui = "new UiSelector().text(\"GO TO CART\");";
			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui)));
			driver.findElement(MobileBy.AndroidUIAutomator(ui)).click();

			/* remove from cart test measurement starts */
			ui = "new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("	+ "new UiSelector().textContains(\"Remove\"));";
			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui))).click();

			ui = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.view.ViewGroup[2]";
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ui)));

			driver.findElement(By.xpath(ui)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiSelector().text(\"My Cart\");")));
			/* remove from cart test measurement stop */


			Thread.sleep(1000);
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
//				"startTime": 1616827798151,
//				"endTime": 1616827799397
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616827799408,
//				"endTime": 1616827800067
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616827800074,
//				"endTime": 1616827800151
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616827800158,
//				"endTime": 1616827801258
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616827801263,
//				"endTime": 1616827801291
//		},
//		{
//			"cmd": "setValue",
//				"startTime": 1616827801299,
//				"endTime": 1616827803391
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616827803398,
//				"endTime": 1616827805420
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616827805425,
//				"endTime": 1616827805569
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616827805575,
//				"endTime": 1616827805947
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616827805953,
//				"endTime": 1616827806795
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616827806803,
//				"endTime": 1616827814826
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616827814831,
//				"endTime": 1616827814959
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616827814965,
//				"endTime": 1616827815126
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616827815133,
//				"endTime": 1616827815982
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616827816583,
//				"endTime": 1616827819622
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616827819627,
//				"endTime": 1616827820066
//		},
//		{
//			"cmd": "findElements",
//				"startTime": 1616827820072,
//				"endTime": 1616827820298
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616827820306,
//				"endTime": 1616827820508
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616827820512,
//				"endTime": 1616827820631
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616827820637,
//				"endTime": 1616827820866
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616827820876,
//				"endTime": 1616827821719
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616827822394,
//				"endTime": 1616827823367
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616827823373,
//				"endTime": 1616827823401
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616827823407,
//				"endTime": 1616827823465
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616827823471,
//				"endTime": 1616827824942
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616827824947,
//				"endTime": 1616827825045
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616827825051,
//				"endTime": 1616827825238
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616827825246,
//				"endTime": 1616827827487
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616827827493,
//				"endTime": 1616827827648
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616827827653,
//				"endTime": 1616827827748
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616827827755,
//				"endTime": 1616827828865
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616827828871,
//				"endTime": 1616827828952
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616827828957,
//				"endTime": 1616827828986
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616827828992,
//				"endTime": 1616827829059
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616827829065,
//				"endTime": 1616827829117
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616827829668,
//				"endTime": 1616827830388
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616827830393,
//				"endTime": 1616827830458
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1616827831467,
//				"endTime": 1616827831467
//		}
//  ]
//	}
}
