package iiitd.nrl.evalapp;



import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
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
	int versionId;
	@BeforeClass
	public void setUp() throws IOException, InterruptedException {
		versionId = MyDatabase.getVersionSelected();
//        versionId = 1;
		System.out.println("APP: " + appName + " Version ID: " + versionId);

	}
    
	@BeforeMethod
	public void launchCap() {
		driver = MainLauncher.driver;
		driver.startActivity(new Activity("com.facebook.katana","com.facebook.katana.activity.FbMainTabActivity"));
//		DesiredCapabilities cap=new DesiredCapabilities();
//		cap.setCapability("appPackage", "com.facebook.katana");
//		cap.setCapability("appActivity", "com.facebook.katana.activity.FbMainTabActivity");
//		cap.setCapability("noReset", "true");
//		cap.setCapability("fullReset", "false");
//		cap.setCapability("autoGrantPermissions", true);
//		cap.setCapability("autoAcceptAlerts", true);
//		cap.setCapability("uiautomator2ServerInstallTimeout", 60000);
//
//		URL url;
//		try {
//			url = new URL("http://127.0.0.1:4723/wd/hub");
//			driver=new AndroidDriver<MobileElement>(url,cap);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (WebDriverException e) {
//	        //MyDatabase.addTestResult(appName, testName, null, "NA" , false, "App Not Installed");
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

		System.out.println(jsonString);
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
//			if (testResult.getName() == "postGroup") {
//				timeTaken = MyDatabase.getTimeTaken(jsonString, -3, -2);
//				main_events.put(testResult.getName(), timeTaken);
//			}
//			else if (testResult.getName() == "searchPerson") {
//				timeTaken = MyDatabase.getTimeTaken(jsonString, -5, -2);
//				main_events.put(testResult.getName(), timeTaken);
//
//			}
//		}
//
//		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);

//		driver.quit();
	}

	private void postGroup270(WebDriverWait wait) {
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout[1]/android.widget.LinearLayout/android.widget.FrameLayout[2]/android.widget.LinearLayout[1]/android.widget.FrameLayout[3]"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionMatches(\"(?i)Your Groups(?-i)\")"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
				"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
						+ "new UiSelector().descriptionMatches(\"(?i)Evaluation of Apps Button(?-i)\"));"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
				"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollToBeginning(20);")));

		wait.until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Create a post…")), ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Write something…"))));

		if (!driver.findElements(MobileBy.AccessibilityId("Create a post…")).isEmpty()) {
			driver.findElement(MobileBy.AccessibilityId("Create a post…")).click();
		}
		else {
			driver.findElement(MobileBy.AccessibilityId("Write something…")).click();
		}

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("Hi, this is an automated post");

		/* post group time measurement starts */
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("POST"))).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textMatches(\"(?i)like(?-i)\");")));
		/* post group time measurement stops */
	}


	@Test
	public void postGroup() throws InterruptedException{

		testName = "post in a group";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
		try {
//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionMatches(\".*(?i)Groups(?-i).*\")"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Username"))).sendKeys("iiitdevalapp@gmail.com");
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Password"))).sendKeys("nrl_evalapp");
			if(versionId == 1)
				wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Login"))).click();
			else
				wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Log In"))).click();


			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textMatches(\"(?i)Not Now(?-i)\")"))).click();
			if(versionId == 1){
				wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Menu, Tab 4 of 4"))).click();
				wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Groups"))).click();

			}
			else if(versionId == 2 ){
				wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
						"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollToBeginning(20);")));
				wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Menu, Tab 5 of 5"))).click();
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/androidx.viewpager.widget.ViewPager/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.view.View"))).click();
			}
			else{
				wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Menu, Tab 5 of 5"))).click();
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/androidx.viewpager.widget.ViewPager/android.widget.FrameLayout/android.widget.FrameLayout[1]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[5]/android.view.ViewGroup"))).click();

//				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/androidx.viewpager.widget.ViewPager/android.widget.FrameLayout/android.widget.FrameLayout[1]/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[5]/android.view.View"))).click();


			}
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionMatches(\"(?i)Your Groups(?-i)\")"))).click();

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().descriptionMatches(\"(?i)Evaluation of Apps Button(?-i)\"));"))).click();


			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollToBeginning(20);")));

//			new TouchAction(driver).press(new PointOption().withCoordinates(new Point(500, 350))).waitAction(new WaitOptions().withDuration(Duration.ofSeconds(1))).moveTo(new PointOption().withCoordinates(new Point(500, 750))).release().perform();
			if(versionId == 1){
				TouchAction touchAction = new TouchAction(driver);
				touchAction.tap(PointOption.point(270, 859)).perform();
			}
			else {
				wait.until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Create a post…")), ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Write something…"))));

				if (!driver.findElements(MobileBy.AccessibilityId("Create a post…")).isEmpty()) {
					driver.findElement(MobileBy.AccessibilityId("Create a post…")).click();
				} else {
					driver.findElement(MobileBy.AccessibilityId("Write something…")).click();
				}
			}
			//wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Write something…"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("Hi, this is an automated post");
			/* post group time measurement starts */
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("POST"))).click();

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Post Menu")));
			/* post group time measurement stops */
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}

	//@Test
	public void searchPerson() throws InterruptedException{

		testName = "search person";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

		try {
//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Username"))).sendKeys("iiitdevalapp@gmail.com");
//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Password"))).sendKeys("nrl_evalapp");
//			if(versionId == 1)
//				wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Login"))).click();
//			else
//				wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Log In"))).click();
//
//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textMatches(\"(?i)Not Now(?-i)\")"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Search Facebook"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("Kangana Ranaut");
			((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));

			/* Search person time measurement starts */
//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Kangana Ranaut Page · Artist · Actor · KanganaRanaut · 2M like this"))).click();
			if(versionId == 1){
				wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("People"))).click();
				wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Kangana Ranaut Page"))).click();
				wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Profile picture")));

			}
			else if (versionId == 2){
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View"))).click();
			}
			else {
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]"))).click();
			}
//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("	new UiSelector().descriptionContains(\"Kangana Ranaut Page\")"))).click();
				//wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textMatches(\"(?i)About(?-i)\")"))).click();
				/* Search person time measurement stops */
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Profile picture")));

		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}
}
//V240
//--group--
//acc-id:= Menu, Tab 4 of 4
//acc-id Groups
//acc-id Your Groups
//acc-id Evaluation of Apps Button
//coords  270,859
//text  What's on your mind?
//acc-id POST
//acc-id Post Menu
//
//--search profile--
//acc-id Search Facebook
//text Search  send text
//acc-id People
//acc-id Kangana Ranaut Page
//acc-id Profile picture


//acc-id  Username
//accid   Password
//acc-id  Login