package iiitd.nrl.evalapp;


import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class LinkedInTests {
	AndroidDriver<MobileElement> driver;
	String appName = "LinkedIn";
	String testName = "NA";
	String testStatusReason = "NA";

	@AfterClass
    public void update() {

    }
	@BeforeMethod
	public void launchCap() {
		DesiredCapabilities cap=new DesiredCapabilities();
		cap.setCapability("appPackage", "com.linkedin.android");
		cap.setCapability("appActivity", "com.linkedin.android.authenticator.LaunchActivity");
		cap.setCapability("noReset", "true");
		cap.setCapability("fullReset", "false");
		cap.setCapability("autoGrantPermissions", true);
		cap.setCapability("autoAcceptAlerts", true);
		cap.setCapability("uiautomator2ServerInstallTimeout", 60000);

		URL url;
		try {
			url = new URL("http://127.0.0.1:4723/wd/hub");
			driver= new AndroidDriver<MobileElement>(url, cap);
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
			if (testResult.getName() == "viewProfile") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -3);
				main_events.put(testResult.getName(), timeTaken);
			} else if (testResult.getName() == "myConnections") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -3);
				main_events.put(testResult.getName(), timeTaken);
			}
			else if (testResult.getName() == "searchPerson") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, -6, -6) + MyDatabase.getTimeTaken(jsonString, -4, -3);
				main_events.put(testResult.getName(), timeTaken);
			}
//			else if (testResult.getName() == "sendMessage") {
//				timeTaken = MyDatabase.getTimeTaken(jsonString, -9, -3);
//				main_events.put(testResult.getName(), timeTaken);
//			}
		}

		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);
		testStatusReason = "NA";
		driver.quit();
	}

	@Test
	public void viewProfile() throws InterruptedException {

		testName = "view profile";
		WebDriverWait wait = new WebDriverWait(driver,MyDatabase.testTimeLimit);

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/me_launcher"))).click();

			/* view profile time measurement starts*/
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/interests_panel_view_profile"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/profile_view_messob_top_card_profile_picture")));
			/* view profile time measurement stops*/
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
//	JSON Commands in the below comment
	}
//	{
//		"commands": [
//		{
//			"cmd": "findElement",
//				"startTime": 1615907140774,
//				"endTime": 1615907141861
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615907141871,
//				"endTime": 1615907144409
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615907144421,
//				"endTime": 1615907144536
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615907144542,
//				"endTime": 1615907146126
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615907146130,
//				"endTime": 1615907146153
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615907146171,
//				"endTime": 1615907146262
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615907147331,
//				"endTime": 1615907148413
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615907148416,
//				"endTime": 1615907148452
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1615907148468,
//				"endTime": 1615907148468
//		}
//	  ]
//	}


	@Test
	public void myConnections() throws InterruptedException{

		testName = "check my connections";
		WebDriverWait wait = new WebDriverWait(driver,MyDatabase.testTimeLimit);

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/tab_relationships"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.linkedin.android:id/mynetwork_my_communitities_entry_point_container"))).click();
//					MobileBy.AndroidUIAutomator("UiSelector().descriptionMatches(\"(?i).*Manage my network.*(?-i)\");"))).click();

			/* my connection time measurement starts*/
			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"Connections\")"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/search_entity_result_universal_template_container")));
			/* my connection time measurement stops*/
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
//				"startTime": 1615909015758,
//				"endTime": 1615909017193
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615909017204,
//				"endTime": 1615909017245
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615909017253,
//				"endTime": 1615909017322
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615909017330,
//				"endTime": 1615909017528
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615909017532,
//				"endTime": 1615909019075
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615909020458,
//				"endTime": 1615909020603
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615909020606,
//				"endTime": 1615909020672
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615909020691,
//				"endTime": 1615909023335
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615909023347,
//				"endTime": 1615909023470
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615909023473,
//				"endTime": 1615909023567
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1615909023576,
//				"endTime": 1615909023576
//		}
//  ]
//	}



	@Test
	public void searchPerson() throws InterruptedException{
		testName = "search person";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/search_bar_text"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/search_bar_edit_text"))).sendKeys("Bill Gates");
			((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));

			/* search time measurement starts*/
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/search_results_hero_entity_container"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/profile_view_messob_top_card_profile_picture")));
			/* search time measurement stops*/
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
//		JSON Commands in the below comment
		}
//		{
//			"commands": [
//			{
//				"cmd": "findElement",
//					"startTime": 1615906625233,
//					"endTime": 1615906626211
//			},
//			{
//				"cmd": "elementDisplayed",
//					"startTime": 1615906626224,
//					"endTime": 1615906626253
//			},
//			{
//				"cmd": "click",
//					"startTime": 1615906626259,
//					"endTime": 1615906626326
//			},
//			{
//				"cmd": "findElement",
//					"startTime": 1615906627901,
//					"endTime": 1615906628329
//			},
//			{
//				"cmd": "elementDisplayed",
//					"startTime": 1615906628333,
//					"endTime": 1615906628364
//			},
//			{
//				"cmd": "setValue",
//					"startTime": 1615906628380,
//					"endTime": 1615906629115
//			},
//			{
//				"cmd": "pressKeyCode",
//					"startTime": 1615906629138,
//					"endTime": 1615906630573
//			},
//			{
//				"cmd": "findElement",
//					"startTime": 1615906630587,
//					"endTime": 1615906633276
//			},
//			{
//				"cmd": "elementDisplayed",
//					"startTime": 1615906633279,
//					"endTime": 1615906633304
//			},
//			{
//				"cmd": "click",
//					"startTime": 1615906633313,
//					"endTime": 1615906633397
//			},
//			{
//				"cmd": "findElement",
//					"startTime": 1615906634210,
//					"endTime": 1615906635204
//			},
//			{
//				"cmd": "elementDisplayed",
//					"startTime": 1615906635207,
//					"endTime": 1615906635226
//			},
//			{
//				"cmd": "getLogEvents",
//					"startTime": 1615906635237,
//					"endTime": 1615906635237
//			}
//  		]
//		}


	}

	//@Test
	public void sendMessage() throws InterruptedException{

		testName = "send message";
		WebDriverWait wait = new WebDriverWait(driver,MyDatabase.testTimeLimit);

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/ad_notification_badge_icon"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/messaging_compose_button"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/msglib_recipient_input"))).sendKeys("Abhivandan Pandey");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"Name this chat\"]/androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/messaging_keyboard_text_input_container"))).sendKeys("Code says hello");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/keyboard_send_button"))).click();
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}
}
