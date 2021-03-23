package iiitd.nrl.evalapp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

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

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

@SuppressWarnings("unchecked")
public class AmazonTests {
	AndroidDriver<MobileElement> driver;
	String appName = "Amazon";
	String testName = "NA";
	String testStatusReason = "NA";

    @AfterClass
    public void update() {

    }
    
	@BeforeMethod
	public void launchCap() throws IOException {
		DesiredCapabilities cap=new DesiredCapabilities();
		cap.setCapability("appPackage", "in.amazon.mShop.android.shopping");
		cap.setCapability("appActivity", "com.amazon.mShop.android.home.HomeActivity");
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
			if (testResult.getName() == "searchProduct") {

				timeTaken = MyDatabase.getTimeTaken(jsonString, 6, 7) + MyDatabase.getTimeTaken(jsonString, 11, 12);
				main_events.put(testResult.getName(), timeTaken);

				timeTaken = MyDatabase.getTimeTaken(jsonString, 21, -11);
				main_events.put("addToCart", timeTaken);

				timeTaken = MyDatabase.getTimeTaken(jsonString, -9, -8);
				main_events.put("goToCart", timeTaken);

				timeTaken = MyDatabase.getTimeTaken(jsonString, -6, -5);
				main_events.put("deleteFromCart", timeTaken);
			}
		}

		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);
		testStatusReason = "NA";
		driver.quit();
	}

	@Test
	public void searchProduct() throws InterruptedException {
		testName = "search product";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("in.amazon.mShop.android.shopping:id/rs_search_src_text"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("in.amazon.mShop.android.shopping:id/rs_search_src_text"))).sendKeys("laptop");

			((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"Prime Eligible\");"))).isDisplayed();

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().className(\"android.widget.TextView\").textContains(\"Sponsored\"));"))).click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"out of 5 stars\");"))).isDisplayed();
			// Search Product Completed - 14th

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("in.amazon.mShop.android.shopping:id/chrome_action_bar_cart_count")));
			MobileElement cartValueElement = driver.findElement(By.id("in.amazon.mShop.android.shopping:id/chrome_action_bar_cart_count"));
			int cartValueBefore = Integer.parseInt(cartValueElement.getText());
			System.out.println("cart value before:" + cartValueBefore);

			/* add product test measurement starts 18th*/

			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().resourceId(\"add-to-cart-button\"));"))).click();

			int cartValueAfter = Integer.parseInt(cartValueElement.getText());

			while (cartValueAfter == cartValueBefore)
				cartValueAfter = Integer.parseInt(cartValueElement.getText());

			System.out.println("cart value after:" + cartValueAfter);
			/* add product test measurement stops 20th*/

			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().description(\"Cart\");"))).click();
//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("in.amazon.mShop.android.shopping:id/chrome_action_bar_cart")))).click();

			/* delete product test measurement starts*/
			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"Delete\");"))).click();
//					"new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
//							+ "new UiSelector().textMatches(\"(?i)Delete(?-i)\"));"))).click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(
							"new UiSelector().textContains(\"was removed from Shopping Cart\");"))).isDisplayed();
			/* delete product test measurement stops*/

		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}

//	JSON COMMANDS

//	{
//		"commands": [
//		{
//			"cmd": "findElement",
//				"startTime": 1615913955283,
//				"endTime": 1615913956006
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615913956017,
//				"endTime": 1615913956786
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615913956802,
//				"endTime": 1615913959664
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615913959683,
//				"endTime": 1615913961670
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615913961674,
//				"endTime": 1615913961704
//		},
//		{
//			"cmd": "setValue",
//				"startTime": 1615913961714,
//				"endTime": 1615913962424
//		},
//		{
//			"cmd": "pressKeyCode",
//				"startTime": 1615913962447,
//				"endTime": 1615913963407
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615913963420,
//				"endTime": 1615913964685
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615913964688,
//				"endTime": 1615913965221
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615913965223,
//				"endTime": 1615913965289
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615913965294,
//				"endTime": 1615913967918
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615913967929,
//				"endTime": 1615913969155
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615913969749,
//				"endTime": 1615913971425
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615913971428,
//				"endTime": 1615913972474
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615913972476,
//				"endTime": 1615913972541
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615913972545,
//				"endTime": 1615913972576
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615913972579,
//				"endTime": 1615913972590
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615913972609,
//				"endTime": 1615913972636
//		},
//		{
//			"cmd": "proxyReqRes",
//				"startTime": 1615913972639,
//				"endTime": 1615913972658
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615913972670,
//				"endTime": 1615913980372
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615913980376,
//				"endTime": 1615913980436
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615913980447,
//				"endTime": 1615913982213
//		},
//		{
//			"cmd": "proxyReqRes",
//				"startTime": 1615913982215,
//				"endTime": 1615913982244
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615913982261,
//				"endTime": 1615913982320
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615913982323,
//				"endTime": 1615913982370
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615913982387,
//				"endTime": 1615913984291
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615913984304,
//				"endTime": 1615913984366
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615913984369,
//				"endTime": 1615913985194
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615913985197,
//				"endTime": 1615913987088
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615913987106,
//				"endTime": 1615913987161
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615913987164,
//				"endTime": 1615913987209
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615913987212,
//				"endTime": 1615913987251
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1615913987260,
//				"endTime": 1615913987260
//		}
//  ]
//	}
}
