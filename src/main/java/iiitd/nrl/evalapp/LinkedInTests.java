package iiitd.nrl.evalapp;



import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

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

@SuppressWarnings("unchecked")
public class LinkedInTests {
	AndroidDriver<MobileElement> driver;
	String appName = "LinkedIn";
    String testName;
	
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
			driver=new AndroidDriver<MobileElement>(url,cap);	
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WebDriverException e) {
	        MyDatabase.addTestResult(appName, testName, 0, "App Not Installed" , false);
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
		long time = testResult.getEndMillis() - testResult.getStartMillis();
        String connType = getConnectionType();

//        MyDatabase.addTestResult(appName, testName, time, connType, testResult.isSuccess());
        driver.quit();
	}
	
	@Test
	public void viewProfile() throws InterruptedException{

		testName = "View Profile";
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/me_launcher"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/interests_panel_view_profile"))).click();
		Thread.sleep(2000);
		
	}
	
	@Test
	public void myConnections() throws InterruptedException{
	
		testName = "Check my connections";
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/tab_relationships"))).click();
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/mynetwork_my_communitities_entry_point_container"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().descriptionMatches(\"(?i).*Manage my network.*(?-i)\");"))).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"Connections\")"))).click();
		Thread.sleep(2000);		
	}
	
	@Test
	public void searchPerson() throws InterruptedException{

		testName = "Search Person";
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/search_bar_text"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/search_bar_edit_text"))).sendKeys("Bill Gates");
		((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
//		wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"People\")"))).click();
//		wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().resourceId(\"com.linkedin.android:id/search_kcard_header_container\").text(\"People\")"))).click();

		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
				"new UiScrollable(" + "new UiSelector().resourceIdMatches(\"com.linkedin.android:id/search_kcard_header_name\").scrollable(true)).scrollIntoView("
						+ "new UiSelector().text(\"Bill Gates\"));"))).click();

//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[1]"))).click();

	}
	
	@Test
	public void sendMessage() throws InterruptedException{
	
		testName = "Send Message";
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/ad_notification_badge_icon"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/messaging_compose_button"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/msglib_recipient_input"))).sendKeys("Abhivandan Pandey");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.view.ViewGroup[@content-desc=\"Name this conversation\"]/androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[1]"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/messaging_keyboard_text_input_container"))).sendKeys("Code says hello");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/keyboard_send_button"))).click();
	}	
}
