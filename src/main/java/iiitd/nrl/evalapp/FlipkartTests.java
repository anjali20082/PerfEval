package iiitd.nrl.evalapp;



import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
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
public class FlipkartTests {
	AndroidDriver<MobileElement> driver;
	String appName = "Flipkart";
	String testName = "NA";
	String testStatusReason = "NA";
	boolean addToCartClicked = false;

	int versionId;

	@BeforeClass
	public void setUp() throws IOException, InterruptedException {
		versionId = MyDatabase.getVersionSelected();
//		versionId = 3;
		System.out.println("APP: " + appName + " Version ID: " + versionId);

	}
    
	@BeforeMethod
	public void launchCap() {
		driver = MainLauncher.driver;
		driver.startActivity(new Activity("com.flipkart.android","com.flipkart.android.activity.HomeFragmentHolderActivity"));
//		DesiredCapabilities cap=new DesiredCapabilities();
//		cap.setCapability("appPackage", "com.flipkart.android");
//		cap.setCapability("appActivity", "com.flipkart.android.activity.HomeFragmentHolderActivity");
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
		System.out.println(jsonString);
		long timeTaken = 0;

		HashMap<String, Long> main_events = new HashMap<>();

		if (testResult.isSuccess()) {
			if (testResult.getName() == "getProduct") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, 8, 8) + MyDatabase.getTimeTaken(jsonString, 10, 12);
				main_events.put("searchProduct", timeTaken);

				if (addToCartClicked) {
					timeTaken = MyDatabase.getTimeTaken(jsonString, 17, 19);
					main_events.put("addToCart", timeTaken);
				}

				timeTaken = MyDatabase.getTimeTaken(jsonString, -8, -7);
				main_events.put("goToCart", timeTaken);

				timeTaken = MyDatabase.getTimeTaken(jsonString, -6, -2);
				main_events.put("removeFromCart", timeTaken);
			}
		}

		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);

//		driver.quit();
	}

	@Test
	public void getProduct() throws InterruptedException {
		testName = "search product";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

		try {

//			if (versionId <= 2)
//				wait.until(ExpectedConditions.visibilityOfElementLocated((By.id("com.flipkart.android:id/btn_skip")))).click();
//			else if (versionId == 4) {
//				wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
//						"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
//								+ "new UiSelector().text(\"English\"));"))).click();
//
//				wait.until(ExpectedConditions.visibilityOfElementLocated((By.id("com.flipkart.android:id/select_btn")))).click();
//			}

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.flipkart.android:id/search_widget_textbox"))).click();


			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.flipkart.android:id/search_autoCompleteTextView"))).sendKeys("realme c11");

			/* search product test measurement starts */
			if (versionId == 1)
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.support.v4.widget.DrawerLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.support.v7.widget.RecyclerView/android.widget.RelativeLayout[1]"))).click();
			else
				wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"in Mobiles\");"))).click();


			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().textContains(\"★\"));"))).click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"★\");")));

			/* search product test measurement stops */

			/* add to cart test measurement starts */
			if (driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().text(\"GO TO CART\");")).isEmpty()) {
				addToCartClicked = true;
				testStatusReason = "add to cart clicked";
				wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"ADD TO CART\");"))).click();
			} else {
				addToCartClicked = false;
				testStatusReason = "add to cart not clicked";
			}

			if (addToCartClicked && versionId <= 4)
				wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"SKIP & GO TO CART\");"))).click();
			else
				wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"GO TO CART\");"))).click();
			/* add to cart test measurement stops */

			/* remove from cart test measurement starts */

			if (versionId == 1)
				wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
						"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
								+ "new UiSelector().description(\"Remove\"));"))).click();

			else if (versionId >= 2)
				wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
						"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
								+ "new UiSelector().textContains(\"Remove\"));"))).click();

			if (versionId == 1)
				wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.flipkart.android:id/drawer_layout"))).click();
			// version 6.15
			else if (versionId == 2) {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View[2]"))).click();
			}
			else if (versionId >= 3)
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View[2]"))).click();

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiSelector().textContains(\"My Cart\");"))).isDisplayed();
			/* remove from cart test measurement stop */

		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}

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
