package iiitd.nrl.evalapp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import io.appium.java_client.android.Activity;
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
		driver = MainLauncher.driver;
		driver.startActivity(new Activity("in.amazon.mShop.android.shopping","com.amazon.mShop.android.home.HomeActivity"));
//		DesiredCapabilities cap=new DesiredCapabilities();
//		cap.setCapability("appPackage", "in.amazon.mShop.android.shopping");
//		cap.setCapability("appActivity", "com.amazon.mShop.android.home.HomeActivity");
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

		HashMap<String, Long> main_events = new HashMap<>();

		if (testResult.isSuccess()) {
			if (testResult.getName() == "searchProduct") {

				timeTaken = MyDatabase.getTimeTaken(jsonString, 7, 9) + MyDatabase.getTimeTaken(jsonString, 12, 14);
				main_events.put(testResult.getName(), timeTaken);

				timeTaken = MyDatabase.getTimeTaken(jsonString, 20, -11);
				main_events.put("addToCart", timeTaken);

				timeTaken = MyDatabase.getTimeTaken(jsonString, -5, -2);
				main_events.put("deleteFromCart", timeTaken);
			}
		}

//		System.out.println("testStatusReason:" + testStatusReason);
		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);
//
//		driver.quit();
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

//			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(
//							"new UiSelector().text(\"Added to cart\");"))).isDisplayed();
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

//	@Test
	public void addToCart(){
		testName = "add to cart";
		WebDriverWait wait = new WebDriverWait(driver, 30);

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("in.amazon.mShop.android.shopping:id/rs_search_src_text"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("in.amazon.mShop.android.shopping:id/rs_search_src_text"))).sendKeys("laptop");

			((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().className(\"android.widget.TextView\").textContains(\"Sponsored\"));"))).click();

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().textContains(\"Add to Cart\"));"))).click();

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"out of 5 stars\"));"))).isDisplayed();
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}

//	@Test(dependsOnMethods={"addToCart"})
	public void deleteFromCart(){
		testName = "delete from cart";
		WebDriverWait wait = new WebDriverWait(driver, 30);

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("in.amazon.mShop.android.shopping:id/chrome_action_bar_cart_count"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().textContains(\"delete\"));"))).click();
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}
}
