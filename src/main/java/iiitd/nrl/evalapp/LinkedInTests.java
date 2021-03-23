package iiitd.nrl.evalapp;


import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
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
		driver = MainLauncher.driver;
		driver.startActivity(new Activity("com.linkedin.android","com.linkedin.android.authenticator.LaunchActivity"));
//		DesiredCapabilities cap=new DesiredCapabilities();
//		cap.setCapability("appPackage", "com.linkedin.android");
//		cap.setCapability("appActivity", "com.linkedin.android.authenticator.LaunchActivity");
//		cap.setCapability("noReset", "true");
//		cap.setCapability("fullReset", "false");
//		cap.setCapability("autoGrantPermissions", true);
//		cap.setCapability("autoAcceptAlerts", true);
//		cap.setCapability("uiautomator2ServerInstallTimeout", 60000);
//
//		URL url;
//		try {
//			url = new URL("http://127.0.0.1:4723/wd/hub");
//			driver= new AndroidDriver<MobileElement>(url, cap);
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

		HashMap<String, Long> main_events = new HashMap<String, Long>();

		if (testResult.isSuccess()) {
			if (testResult.getName() == "viewProfile") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -2);
				main_events.put(testResult.getName(), timeTaken);
			} else if (testResult.getName() == "myConnections") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -2);
				main_events.put(testResult.getName(), timeTaken);
			}
			else if (testResult.getName() == "searchPerson") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, -6, -2);
				main_events.put(testResult.getName(), timeTaken);
			}
			else if (testResult.getName() == "sendMessage") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, -9, -3);
				main_events.put(testResult.getName(), timeTaken);
			}
		}

		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);

//		driver.quit();
	}

	@Test
	public void viewProfile() throws InterruptedException{

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

	}

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
	}

	@Test
	public void searchPerson() throws InterruptedException{

		testName = "search person";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/search_bar_text"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/search_bar_edit_text"))).sendKeys("Bill Gates");
			((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));

			/* search time measurement starts*/
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/search_kcard_header_name"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/profile_view_messob_top_card_profile_picture")));
			/* search time measurement stops*/
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}

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
