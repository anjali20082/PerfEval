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
//		System.out.println(jsonString);
		long timeTaken = 0;

		HashMap<String, Long> main_events = new HashMap<String, Long>();

		if (testResult.isSuccess()) {
			if (testResult.getName() == "postGroup") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, -3, -2);
				main_events.put(testResult.getName(), timeTaken);
			}
			else if (testResult.getName() == "searchPerson") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, -5, -2);
				main_events.put(testResult.getName(), timeTaken);

			}
		}

		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);
		testStatusReason = "NA";
		driver.quit();
	}



	@Test
	public void postGroup() throws InterruptedException{

		testName = "post in a group";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
		try {

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionMatches(\".*(?i)Groups(?-i).*\")"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionMatches(\"(?i)Your Groups(?-i)\")"))).click();

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().descriptionMatches(\"(?i)Evaluation of Apps Button(?-i)\"));"))).click();


			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollToBeginning(20);")));

//			wait.until(ExpectedConditions.or(
//					ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Create a post…")),
//					ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Write something..."))));

			wait.until(ExpectedConditions.or(
					ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Create a post\");")),
					ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Write something\");"))));

			if (!driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Create a post\");")).isEmpty()) {
				driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Create a post\");")).click();
			}
			else {
				driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Write something\");")).click();
			}

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("Hi, this is an automated post");
			/* post group time measurement starts */
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("POST"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiSelector().textMatches(\"(?i)like(?-i)\");")));
			/* post group time measurement stops */
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
//				"startTime": 1615992974046,
//				"endTime": 1615992975100
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992975115,
//				"endTime": 1615992977351
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992977365,
//				"endTime": 1615992977418
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992977429,
//				"endTime": 1615992979230
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992979235,
//				"endTime": 1615992979271
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992979283,
//				"endTime": 1615992981302
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992981319,
//				"endTime": 1615992984217
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992984342,
//				"endTime": 1615992984387
//		},
//		{
//			"cmd": "findElements",
//				"startTime": 1615992984405,
//				"endTime": 1615992984490
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992984497,
//				"endTime": 1615992984544
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992984561,
//				"endTime": 1615992985710
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992985716,
//				"endTime": 1615992985770
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615992985775,
//				"endTime": 1615992985795
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992985809,
//				"endTime": 1615992987656
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992987664,
//				"endTime": 1615992987722
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615992987725,
//				"endTime": 1615992987751
//		},
//		{
//			"cmd": "setValue",
//				"startTime": 1615992987774,
//				"endTime": 1615992988555
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992988571,
//				"endTime": 1615992989358
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992989364,
//				"endTime": 1615992989427
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992989442,
//				"endTime": 1615992991379
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1615992991387,
//				"endTime": 1615992991388
//		}
//  ]
//	}

	@Test
	public void searchPerson() throws InterruptedException{

		testName = "search person";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Search Facebook"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("Kangana Ranaut");
			((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));

			/* Search person time measurement starts */
//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Kangana Ranaut Page · Artist · Actor · KanganaRanaut · 2M like this"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]"))).click();

//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"About\")"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Profile picture\")")));
			/* Search person time measurement stops */
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
//				"startTime": 1615993346046,
//				"endTime": 1615993347796
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615993347808,
//				"endTime": 1615993349953
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615993349965,
//				"endTime": 1615993350031
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615993350034,
//				"endTime": 1615993350051
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615993350061,
//				"endTime": 1615993350106
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615993359903,
//				"endTime": 1615993360781
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615993360784,
//				"endTime": 1615993360808
//		},
//		{
//			"cmd": "setValue",
//				"startTime": 1615993360828,
//				"endTime": 1615993361512
//		},
//		{
//			"cmd": "pressKeyCode",
//				"startTime": 1615993361530,
//				"endTime": 1615993363319
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615993366799,
//				"endTime": 1615993367478
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615993367492,
//				"endTime": 1615993367570
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615993368414,
//				"endTime": 1615993368885
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1615993368901,
//				"endTime": 1615993368901
//		}
//  ]
//	}
}
