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
//		System.out.println(jsonString);
		long timeTaken = 0;

		HashMap<String, Long> main_events = new HashMap<>();

		if (testResult.isSuccess()) {
			if (testResult.getName() == "getProduct") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, 8, 9) + MyDatabase.getTimeTaken(jsonString, 10, 11);
				main_events.put("searchProduct", timeTaken);

				if (addToCartClicked) {
					timeTaken = MyDatabase.getTimeTaken(jsonString, 17, 18);
					main_events.put("addToCart", timeTaken);
				}

				timeTaken = MyDatabase.getTimeTaken(jsonString, -8, -7);
				main_events.put("goToCart", timeTaken);

				timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -3);
				main_events.put("removeFromCart", timeTaken);
			}
		}

		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);
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
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/androidx.drawerlayout.widget.DrawerLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.LinearLayout/androidx.recyclerview.widget.RecyclerView/android.widget.RelativeLayout[1]"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().textContains(\"★\"));"))).click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/androidx.drawerlayout.widget.DrawerLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup"))).isDisplayed();
			/* search product test measurement stops */

			/* add to cart test measurement starts */
			if (driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().text(\"GO TO CART\");")).isEmpty()) {
				addToCartClicked = true;
				testStatusReason = "add to cart clicked";
				wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"ADD TO CART\");"))).click();
				wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Back Button"))).click();
			} else
				testStatusReason = "add to cart not clicked";
			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"GO TO CART\");"))).click();
			/* add to cart test measurement stops */

			/* remove from cart test measurement starts */
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().textContains(\"Remove\"));"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.view.ViewGroup[2]"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiSelector().text(\"My Cart\");"))).isDisplayed();
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
//				"startTime": 1615914637126,
//				"endTime": 1615914640354
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615914640368,
//				"endTime": 1615914640408
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615914640421,
//				"endTime": 1615914640491
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615914640501,
//				"endTime": 1615914641538
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615914641544,
//				"endTime": 1615914641563
//		},
//		{
//			"cmd": "setValue",
//				"startTime": 1615914641570,
//				"endTime": 1615914642371
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615914642385,
//				"endTime": 1615914643421
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615914643424,
//				"endTime": 1615914643436
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615914643451,
//				"endTime": 1615914643496
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615914643513,
//				"endTime": 1615914645173
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615914645185,
//				"endTime": 1615914647824
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615914647836,
//				"endTime": 1615914648103
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615914648106,
//				"endTime": 1615914648118
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615914648121,
//				"endTime": 1615914648136
//		},
//		{
//			"cmd": "findElements",
//				"startTime": 1615914648151,
//				"endTime": 1615914648312
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615914648321,
//				"endTime": 1615914648510
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615914648513,
//				"endTime": 1615914648560
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615914648571,
//				"endTime": 1615914650749
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615914650754,
//				"endTime": 1615914650845
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615914650848,
//				"endTime": 1615914650899
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615914650912,
//				"endTime": 1615914652129
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615914652132,
//				"endTime": 1615914652179
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615914652196,
//				"endTime": 1615914653764
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615914653781,
//				"endTime": 1615914653865
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615914653875,
//				"endTime": 1615914654918
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615914654932,
//				"endTime": 1615914654995
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615914655011,
//				"endTime": 1615914655058
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615914655638,
//				"endTime": 1615914656217
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615914656220,
//				"endTime": 1615914656263
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1615914657294,
//				"endTime": 1615914657294
//		}
//  ]
//	}


	//	@Test
	public void searchTest() throws InterruptedException {

		testName = "search product";
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.flipkart.android:id/search_widget_textbox"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.flipkart.android:id/search_autoCompleteTextView"))).sendKeys("phone");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/androidx.drawerlayout.widget.DrawerLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.LinearLayout/androidx.recyclerview.widget.RecyclerView/android.widget.RelativeLayout[1]"))).click();
		Thread.sleep(1000);
		// Search Completed


		

	}
	
//	@Test
	public void addToCart() throws InterruptedException{

		testName = "add to cart";
		WebDriverWait wait = new WebDriverWait(driver, 30);

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.flipkart.android:id/search_widget_textbox"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.flipkart.android:id/search_autoCompleteTextView"))).sendKeys("phone");
//		((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/androidx.drawerlayout.widget.DrawerLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.LinearLayout/androidx.recyclerview.widget.RecyclerView/android.widget.RelativeLayout[1]"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().textContains(\"No Cost EMI\"));"))).click();
//		if (!driver.findElements(By.xpath("GO TO CART")).isEmpty()) {
//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"GO TO CART\")"))).click();
//		}
//		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"ADD TO CART\")"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/androidx.drawerlayout.widget.DrawerLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup"))).click();
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}

//	@Test(dependsOnMethods={"addToCart"})
	public void deleteFromCartTest() throws InterruptedException {
		
		testName = "delete from cart";
		WebDriverWait wait = new WebDriverWait(driver, 30);

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.flipkart.android:id/cart_bg_icon"))).click();

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().textContains(\"Remove\"));"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
							"new UiSelector().text(\"Remove\");"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiSelector().text(\"My Cart\");"))).isDisplayed();
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}
}
