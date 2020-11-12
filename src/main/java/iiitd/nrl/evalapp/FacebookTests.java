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
	        MyDatabase.addTestResult(appName, testName, null, "App Not Installed" , false);
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

		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess());

		driver.quit();
	}
	
	@Test
	public void searchPerson() throws InterruptedException{

		testName = "search person";
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Search Facebook"))).click();
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("Kangana Ranaut");
		((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Kangana Ranaut Page · Artist · Actor · KanganaRanaut · 2M like this"))).click();
//		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().descriptionMatches(\"(?i).*Kangana Ranaut.*(?-i)\");"))).click();
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup"))).click();
	}


	@Test
	public void postGroup() throws InterruptedException{
		
		testName = "post in a group";
		WebDriverWait wait = new WebDriverWait(driver, 30);
//		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionMatches(\".*(?i)Groups(?-i).*\")"))).click();
		if (!driver.findElements(MobileBy.AccessibilityId("Groups, Tab 3 of 6, 1 new")).isEmpty())
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Groups, Tab 3 of 6, 1 new"))).click();

		if (!driver.findElements(MobileBy.AccessibilityId("Groups, Tab 3 of 6")).isEmpty())
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Groups, Tab 3 of 6"))).click();

//		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.View[@content-desc=\"Groups, Tab 3 of 6\"]"))).click();
//		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"Your Groups\"]"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Your Groups"))).click();
//		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionMatches(\"(?i)Your Groups(?-i)\")"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
				"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
						+ "new UiSelector().descriptionMatches(\"(?i)Evaluation of Apps Button(?-i)\"));"))).click();
//		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"Evaluation of Apps Button\"]"))).click();
//		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Write something…"))).click();

//		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
//				"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
//						+ "new UiSelector().descriptionContains(\"Write something\")"))).click();

		new TouchAction(driver).press(new PointOption().withCoordinates(new Point(500, 350))).waitAction(new WaitOptions().withDuration(Duration.ofSeconds(1))).moveTo(new PointOption().withCoordinates(new Point(500, 750))).release().perform();

//		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
//				"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
//						+ "new UiSelector().descriptionMatches(\"(?i).*Write something.*(?-i)\"));"))).click();

		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Write something…"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("It does not matter how slow you go, as long as you don't stop    -Confucius");
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("POST"))).click();
	}

	@Test
	public void followUnfollow() throws InterruptedException{
		
		testName = "follow/unfollow";
		WebDriverWait wait = new WebDriverWait(driver, 30);

		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Search Facebook"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("Kangana Ranaut");
		((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Kangana Ranaut Page · Artist · Actor · KanganaRanaut · 2M like this"))).click();

//		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionMatches(\"(?i)Kangana Ranaut Page · Artist · Actor · KanganaRanaut · 2M like this(?-i)\")"))).click();
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup"))).click();
//		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().descriptionMatches(\"(?i).*Kangana Ranaut.*(?-i)\");"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("like button"))).click();

//		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionMatches(\"(?i)Like Button(?-i)\")"))).click();
//
//		if(!driver.findElementsByAccessibilityId("like button").isEmpty()) {
//			driver.findElementByAccessibilityId("like button").click();
//		}
//		else if(!driver.findElementsByAccessibilityId("Like button").isEmpty()) {
//			driver.findElementByAccessibilityId("Like button").click();
//		}
//
		if (!driver.findElements(MobileBy.AccessibilityId("Unlike")).isEmpty()) {
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Unlike"))).click();
		}
	}

}
