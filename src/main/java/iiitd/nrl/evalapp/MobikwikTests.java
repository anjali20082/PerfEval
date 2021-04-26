package iiitd.nrl.evalapp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import com.sun.jdi.event.ExceptionEvent;
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
import io.appium.java_client.service.local.flags.GeneralServerFlag;

@SuppressWarnings("unchecked")
public class MobikwikTests {
	AndroidDriver<MobileElement> driver;
	String appName = "Mobikwik";
	String testName = "NA";
	String testStatusReason = "NA";

    @AfterClass
    public void update() {

    }
    
	@BeforeMethod
	public void launchCap() throws IOException {
		DesiredCapabilities cap=new DesiredCapabilities();
		cap.setCapability("appPackage", "com.mobikwik_new");
		cap.setCapability("appActivity", "com.mobikwik_new.MobikwikHomeScreenActivity");
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
		MyDatabase.setAppJsonCommands(jsonString);
		MyDatabase.setTestStatus(testResult.isSuccess());
		MyDatabase.setTestStatusReason(testStatusReason);
		MyDatabase.setConnType(getConnectionType());

//		long timeTaken = 0;
//
//		HashMap<String, Long> main_events = new HashMap<>();
//
//		if (testResult.isSuccess()) {
//			if (testResult.getName() == "sendMoneyFromWallet") {
//				timeTaken = MyDatabase.getTimeTaken(jsonString, -5, -3);
//				main_events.put(testResult.getName(), timeTaken);
//			}
//		}
//
//		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);
		testStatusReason = "NA";
		driver.quit();
	}
	
	@Test
	public void sendMoneyFromWallet(){
		
		testName = "Send Money From Wallet";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().textContains(\"Scan any QR\")"))).click();
			if (!driver.findElements(By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button")).isEmpty())
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button"))).click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.mobikwik_new:id/merchant_code"))).click();
			if (!driver.findElements(By.id("com.android.permissioncontroller:id/permission_allow_button")).isEmpty())
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.android.permissioncontroller:id/permission_allow_button"))).click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.mobikwik_new:id/search_edittext"))).sendKeys("9667316335");
			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().textContains(\"9667316335\")")));
			List<MobileElement> elementList = driver.findElements(MobileBy.AndroidUIAutomator("UiSelector().textContains(\"9667316335\")"));
			elementList.get(1).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.mobikwik_new:id/edt_txt_transfer_amount"))).sendKeys("5");
			/* sending money time measurement starts */
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.mobikwik_new:id/btn_p2p_action"))).click();
			wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"Money sent successfully\");")), ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"Money transfer failed\");"))));
			/* sending money time measurement stops */

			if (driver.findElements(MobileBy.AndroidUIAutomator("UiSelector().text(\"Money sent successfully\");")).isEmpty()) {
				testStatusReason = "Payment failed";
			}
			else
				testStatusReason = "Payment successful";
		} catch (Exception e) {
			testStatusReason = "Payment failed\n" + e.toString();
			throw e;
		}
//		JSON COMMANDS
	}
//	{
//		"commands": [
//		{
//			"cmd": "findElement",
//				"startTime": 1616002188832,
//				"endTime": 1616002190318
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616002190326,
//				"endTime": 1616002190433
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616002190449,
//				"endTime": 1616002191791
//		},
//		{
//			"cmd": "findElements",
//				"startTime": 1616002191807,
//				"endTime": 1616002191832
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616002191854,
//				"endTime": 1616002191879
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616002191882,
//				"endTime": 1616002191895
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616002191900,
//				"endTime": 1616002191955
//		},
//		{
//			"cmd": "findElements",
//				"startTime": 1616002191962,
//				"endTime": 1616002192551
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616002192559,
//				"endTime": 1616002193518
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616002193521,
//				"endTime": 1616002193547
//		},
//		{
//			"cmd": "setValue",
//				"startTime": 1616002193556,
//				"endTime": 1616002194220
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616002194238,
//				"endTime": 1616002195675
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616002195679,
//				"endTime": 1616002195754
//		},
//		{
//			"cmd": "findElements",
//				"startTime": 1616002195771,
//				"endTime": 1616002195924
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616002195942,
//				"endTime": 1616002196847
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616002196862,
//				"endTime": 1616002196902
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616002196905,
//				"endTime": 1616002196933
//		},
//		{
//			"cmd": "setValue",
//				"startTime": 1616002196941,
//				"endTime": 1616002197626
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616002197630,
//				"endTime": 1616002198159
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616002198162,
//				"endTime": 1616002198185
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616002198194,
//				"endTime": 1616002198258
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616002202440,
//				"endTime": 1616002202492
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616002202495,
//				"endTime": 1616002202560
//		},
//		{
//			"cmd": "findElements",
//				"startTime": 1616002202565,
//				"endTime": 1616002202785
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1616002202798,
//				"endTime": 1616002202798
//		}
//  ]
//	}
}
