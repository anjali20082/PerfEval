package iiitd.nrl.evalapp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

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
import io.appium.java_client.service.local.flags.GeneralServerFlag;

@SuppressWarnings("unchecked")
public class AmazonTests {
	AndroidDriver<MobileElement> driver;
	String appName = "Amazon";
	String testName = "NA";

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
	        MyDatabase.addTestResult(appName, "App Not Installed", null, "Not Connected" , false);
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
		long timeTaken = 0;

		HashMap<String, Long> main_events = new HashMap<>();

		if (testResult.isSuccess()) {
			if (testResult.getName() == "playTest") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -2);
				main_events.put(testResult.getName(), timeTaken);
			} else if (testResult.getName() == "channelTest") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, -8, -2);
				main_events.put(testResult.getName(), timeTaken);
			}
		}

//		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess());

		driver.quit();
	}

	@Test
	public void searchProduct() throws InterruptedException {
		testName = "search product";
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(By.id("in.amazon.mShop.android.shopping:id/rs_search_src_text")))).click();
		wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(By.id("in.amazon.mShop.android.shopping:id/rs_search_src_text")))).sendKeys("laptop");

		((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
		wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"Prime Eligible\");")))).isDisplayed();

		wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
				"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
						+ "new UiSelector().className(\"android.widget.TextView\").textContains(\"Sponsored\"));")))).click();

		wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"out of 5 stars\");")))).isDisplayed();
		// Search Product Completed

		wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(
				"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
						+ "new UiSelector().textContains(\"Add to Cart\"));")))).click();

		wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(By.id("in.amazon.mShop.android.shopping:id/chrome_action_bar_cart_count")))).click();

		wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(
				"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
						+ "new UiSelector().textContains(\"delete\"));")))).click();

	}

	@Test
	public void addToCart(){
		testName = "add to cart";
		WebDriverWait wait = new WebDriverWait(driver, 30);
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
	}

	@Test(dependsOnMethods={"addToCart"})
	public void deleteFromCart(){
		testName = "delete from cart";
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("in.amazon.mShop.android.shopping:id/chrome_action_bar_cart_count"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
				"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
						+ "new UiSelector().textContains(\"delete\"));"))).click();
	}
}
