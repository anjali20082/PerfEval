package iiitd.nrl.evalapp;


import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class GooglenewsTests {
	AndroidDriver<MobileElement> driver;
	String appName = "GoogleNews";
	String testName = "NA";
	String testStatusReason = "NA";
	String commandsCompleted = "";
	String loc = "", lat = "", longi = "", alt ="";

    @AfterClass
    public void update() {

    }
	
	@BeforeMethod
	public void launchCap() {
		DesiredCapabilities cap=new DesiredCapabilities();
		cap.setCapability("appPackage", "com.google.android.apps.magazines");
		cap.setCapability("appActivity", "com.google.apps.dots.android.app.activity.CurrentsStartActivity");
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
			return "Wifi 2";
		else if (connType == 4)
			return "MobileData 4";
		else if (connType == 6)
			return "Wifi & MobileData 6";
		return "Wifi " + connType;
	}

	@AfterMethod
	public void restart(ITestResult testResult) {
		String jsonString = driver.getEvents().getJsonData();

		MyDatabase.setCurrentApp(appName);
		MyDatabase.setCommands(commandsCompleted);
		MyDatabase.setAppJsonCommands(jsonString);
		MyDatabase.setTestStatus(testResult.isSuccess());
		MyDatabase.setTestStatusReason(testStatusReason);
		MyDatabase.setConnType(getConnectionType());
		MyDatabase.setLocation(lat, longi, alt);

		testStatusReason = "NA";
		driver.quit();
	}

	@Test
	public void searchTest() throws InterruptedException, IOException {

		loc = GetLocation.getlocation();
		lat = loc.split( " ")[0];
		longi = loc.split(" ")[1];
		alt = loc.split(" ")[2];
		System.out.println("Location in fbp is : " + loc);

		testName = "Search News Test";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

		try {
			/* search news  test measurement starts*/
			String ui = "new UiScrollable(new UiSelector().resourceIdMatches(\"com.google.android.apps.magazines:id/home_fragment_content\").scrollable(true)).scrollIntoView(new UiSelector().resourceId(\"com.google.android.apps.magazines:id/title\"));";
			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui))).click();
			commandsCompleted += "clickNews:";

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/hero_action_button"))).click();
			commandsCompleted += "viewFullNews:";

//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/edition_pager_header_icon_label")));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/title")));
			commandsCompleted += "checkNewsTitle:";
			/* load news  test measurement stops*/

			commandsCompleted += "P";
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
//				"startTime": 1615992361258,
//				"endTime": 1615992363003
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992363020,
//				"endTime": 1615992365128
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992365144,
//				"endTime": 1615992365201
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992365207,
//				"endTime": 1615992367675
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992368593,
//				"endTime": 1615992369167
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1615992369176,
//				"endTime": 1615992369177
//		}
//  ]
//	}


}