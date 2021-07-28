package iiitd.nrl.evalapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.*;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

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
	Long start_time , end_time;
	int versionId;
	String commands = "";

	@BeforeClass
	public void setUp() throws IOException, InterruptedException {
		versionId = MyDatabase.getVersionSelected();
		System.out.println("APP: " + appName + " Version ID: " + versionId);
	}
	
	@BeforeMethod
	public void launchCap() {
//		driver = MainLauncher.driver;
//		Activity activity = new Activity("com.google.android.apps.magazines","com.google.apps.dots.android.app.activity.CurrentsStartActivity");
//		activity.setAppWaitActivity("com.google.apps.dots.android.newsstand.home.HomeActivity");
//		driver.startActivity(activity);
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
		System.out.println(commands);

		MyDatabase.setCurrentApp(appName);
		MyDatabase.setCommands(commands);
		MyDatabase.setAppJsonCommands(jsonString);
		MyDatabase.setTestStatus(testResult.isSuccess());
		MyDatabase.setTestStatusReason(testStatusReason);
		MyDatabase.setConnType(getConnectionType());

//		driver.quit();
	}

	@Test
	public void searchTest() throws InterruptedException, IOException {
		testName = "Search News Test";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
		Process process = null;

		try {
			// Version 5.12 & 5.16 & 5.19 & 5.23

			if (versionId == 1) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(
						"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().descriptionMatches(\"(?i)View full coverage(?-i).*\"));"))).click();
				commands += "viewFullCoverage:";

				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/title")));
				commands += "checkTitle:";
			}

			else if (versionId == 2) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/story_metadata_icon"))).click();
				commands += "viewFullCoverage:";

				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/title")));
				commands += "checkTitle:";
			}

			else if (versionId == 3) {

				wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(
						"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().descriptionMatches(\"(?i)View Full coverage(?-i).*\"));"))).click();
				commands += "viewFullCoverage:";

				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/title")));
				commands += "checkTitle:";
			}

			// Version 5.27 : add scroll below feature
			else {
				wait.until(ExpectedConditions.visibilityOfElementLocated((By.id("com.google.android.apps.magazines:id/story_metadata_icon")))).click();
				commands += "viewFullCoverage:";

				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.magazines:id/title")));
				commands += "checkTitle:";
			}

			commands += "P";


			/* load news  test measurement stops*/
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}

}