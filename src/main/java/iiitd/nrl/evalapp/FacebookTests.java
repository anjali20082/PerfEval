package iiitd.nrl.evalapp;



import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
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
public class FacebookTests {
	AndroidDriver<MobileElement> driver;
	String appName = "Facebook";
	String testName = "NA";
	String testStatusReason = "NA";
	
	@AfterClass
    public void update() {

    }
    
	@BeforeMethod
	public void launchCap() {
		DesiredCapabilities cap=new DesiredCapabilities();
		cap.setCapability("appPackage", "com.facebook.katana");
		cap.setCapability("appActivity", "com.facebook.katana.activity.FbMainTabActivity");
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
		System.out.println(jsonString);
		long timeTaken = 0;

		HashMap<String, Long> main_events = new HashMap<String, Long>();

		if (testResult.isSuccess()) {
			if (testResult.getName() == "postGroup") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, -3, -2);
				main_events.put(testResult.getName(), timeTaken);
			}
			else if (testResult.getName() == "searchPerson") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, -5, -2);
				main_events.put("search person", timeTaken);

			}
		}

		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);

		driver.quit();
	}



	@Test
	public void postGroup() throws InterruptedException{

		testName = "post in a group";
		WebDriverWait wait = new WebDriverWait(driver, 300);
		try {

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionMatches(\".*(?i)Groups(?-i).*\")"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionMatches(\"(?i)Your Groups(?-i)\")"))).click();

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().descriptionMatches(\"(?i)Evaluation of Apps Button(?-i)\"));"))).click();


			new TouchAction(driver).press(new PointOption().withCoordinates(new Point(500, 350))).waitAction(new WaitOptions().withDuration(Duration.ofSeconds(1))).moveTo(new PointOption().withCoordinates(new Point(500, 750))).release().perform();

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Write something…"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("It does not matter how slow you go, as long as you don't stop    -Confucius");
			/* post group time measurement starts */
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("POST"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiSelector().textMatches(\"(?i)like(?-i)\");")));
			/* post group time measurement stops */
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}

	@Test
	public void searchPerson() throws InterruptedException{

		testName = "follow/unfollow";
		WebDriverWait wait = new WebDriverWait(driver, 300);

		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Search Facebook"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("Kangana Ranaut");
			((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));

			/* Search person time measurement starts */
//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Kangana Ranaut Page · Artist · Actor · KanganaRanaut · 2M like this"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]"))).click();

//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("	new UiSelector().descriptionContains(\"Kangana Ranaut Page\")"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textMatches(\"(?i)About(?-i)\")"))).click();
			/* Search person time measurement stops */
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}
}
