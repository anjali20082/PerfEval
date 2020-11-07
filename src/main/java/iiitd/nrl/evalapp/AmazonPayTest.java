package iiitd.nrl.evalapp;



import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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
public class AmazonPayTest {
	AndroidDriver<MobileElement> driver;
	String appName = "AmazonPay";
    String testName;

	@AfterClass
    public void update() {

    }
    
	@BeforeMethod
	public void launchCap() throws IOException {
		DesiredCapabilities cap=new DesiredCapabilities();
		cap.setCapability("appPackage", "in.amazon.mShop.android.shopping");
		cap.setCapability("appActivity", "com.amazon.mShop.android.home.HomeActivity");
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
	        MyDatabase.addTestResult(appName, "App Not Installed", 0, "Not Connected" , false);
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

        MyDatabase.addTestResult(appName, testName, time, connType, testResult.isSuccess());
        driver.quit();
	}
	
	@Test
    public void payUsingAmazonPay() {
		
        testName = "pay using amazon pay upi";
        WebDriverWait wait = new WebDriverWait(driver, 30);     
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
				"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
						+ "new UiSelector().textContains(\"Amazon Pay\"));"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"Send Money\")"))).click();
        
		if (!driver.findElements(By.xpath("//android.widget.Button[@content-desc=\"Close\"]/android.widget.ImageView")).isEmpty())
            driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Close\"]/android.widget.ImageView")).click();
		
        if (!driver.findElements(MobileBy.AndroidUIAutomator("UiSelector().text(\"Allow Access\")")).isEmpty())
        	driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"Allow Access\")")).click();
		
		if (!driver.findElements(MobileBy.AndroidUIAutomator("UiSelector().text(\"Allow\")")).isEmpty())
			driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().text(\"Allow\")")).click();

		
		wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"To UPI ID\")"))).click();
//		wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"E.g. phonenumber@apl\")"))).sendKeys("sachdevanikhil204@okaxis");
		wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"E.g. phonenumber@apl\")"))).sendKeys("wlccpnas@okaxis");
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"Verify and proceed\")"))).click();
			
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("1");
		((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));

		wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"Send Money\")"))).click();

        wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"Copy Details\")"))).click();
    }
}
