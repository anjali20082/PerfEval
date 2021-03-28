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
public class LinkedInTests_profile {
	AndroidDriver<MobileElement> driver;
	String appName = "LinkedIn_profile";
	String testName = "NA";
	String testStatusReason = "NA";

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

		MyDatabase.setCurrentApp(appName);
		MyDatabase.setAppJsonCommands(jsonString);
		MyDatabase.setTestStatus(testResult.isSuccess());
		MyDatabase.setTestStatusReason(testStatusReason);
		MyDatabase.setConnType(getConnectionType());
//		long timeTaken = 0;
//
//		HashMap<String, Long> main_events = new HashMap<String, Long>();
//
//		if (testResult.isSuccess()) {
//			if (testResult.getName() == "viewProfile") {
//				timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -3);
//				main_events.put(testResult.getName(), timeTaken);
//			} else if (testResult.getName() == "myConnections") {
//				timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -3);
//				main_events.put(testResult.getName(), timeTaken);
//			}
//			else if (testResult.getName() == "searchPerson") {
//				timeTaken = MyDatabase.getTimeTaken(jsonString, -6, -6) + MyDatabase.getTimeTaken(jsonString, -4, -3);
//				main_events.put(testResult.getName(), timeTaken);
//			}
////			else if (testResult.getName() == "sendMessage") {
////				timeTaken = MyDatabase.getTimeTaken(jsonString, -9, -3);
////				main_events.put(testResult.getName(), timeTaken);
////			}
//		}
//
//		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);
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
}
