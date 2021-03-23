package iiitd.nrl.evalapp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

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

		HashMap<String, Long> main_events = new HashMap<>();

		if (testResult.isSuccess()) {
			if (testResult.getName() == "sendMoneyFromWallet") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -2);
				main_events.put(testResult.getName(), timeTaken);
			}
		}

		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);

		driver.quit();
	}
	
	@Test
	public void sendMoneyFromWallet(){
		
		testName = "Send Money From Wallet";
		WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

		try {

			if (!driver.findElements(By.id("com.mobikwik_new:id/cross_button")).isEmpty()) {
				driver.findElement(By.id("com.mobikwik_new:id/cross_button")).click();
			}

			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"Via Wallet\")"))).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.mobikwik_new:id/edit_field"))).sendKeys("9667316335");
//			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.mobikwik_new:id/edit_field"))).sendKeys("9667316335");
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
	}
}
