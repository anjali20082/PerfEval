package iiitd.nrl.evalapp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
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
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.offset.PointOption;

public class YouTubeTests {
	AndroidDriver<MobileElement> driver;

	AppiumDriverLocalService service;
	String appName = "Youtube";
    String testName;
//	Process process = null;
//
//	
//    @BeforeSuite
//	public void startServer() {
//      //Set Capabilities
////      DesiredCapabilities cap = new DesiredCapabilities();
////      cap.setCapability("noReset", "true");
////
////      //Build the Appium service
////      AppiumServiceBuilder builder = new AppiumServiceBuilder();
////      builder = new AppiumServiceBuilder();
////      builder.withIPAddress("127.0.0.1");
////      builder.usingPort(4723);
////      builder.withCapabilities(cap);
////      builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
////      builder.withArgument(GeneralServerFlag.LOG_LEVEL,"error");
////
////      //Start the server with the builder
////      service = AppiumDriverLocalService.buildService(builder);
////      service.start();
//    	
////    	String homeDirectory = System.getProperty("user.home");
////    	boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
//    	
////    	if (isWindows) {
////    	    process = Runtime.getRuntime()
////    	      .exec(String.format("cmd.exe /c dir %s", homeDirectory));
////    	} else {
//    	    try {
//				process = Runtime.getRuntime()
//				  .exec(String.format("appium"));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
////    	}
//    	
//      System.out.println("Service started");
//  }

//	@AfterSuite
//    public void stopServer() {
////        service.stop();
//		process.destroy();
//        System.out.println("Service stopped");
//    }
	
	@AfterClass
    public void update() {
		Main.count++;
		Main.updateTestStatus();
//		process.destroy();
    }
	
	@BeforeMethod
	public void launchCap() {
		DesiredCapabilities cap=new DesiredCapabilities();
		cap.setCapability("appPackage", "com.google.android.youtube");
		cap.setCapability("appActivity", "com.google.android.youtube.HomeActivity");
		cap.setCapability("noReset", "true");
		cap.setCapability("fullReset", "false");
        cap.setCapability("autoGrantPermissions", true);

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

        MyDatabase.addTestResult(appName, testName, time, connType, testResult.isSuccess());
        driver.quit();
	}
	
	@Test
	public void playTest() throws InterruptedException {
		testName = "Play Test";
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.youtube:id/menu_item_1")));
		driver.findElement(By.id("com.google.android.youtube:id/menu_item_1")).click();
		driver.findElement(By.id("com.google.android.youtube:id/search_edit_text")).sendKeys("manikarnika");	
		((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Official Trailer\")"))).click();
//		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//android.widget.ImageView[@content-desc=\" \"])[1]"))).click();
		Thread.sleep(2000);
				
	}
	
	@Test
	public void channelTest() throws InterruptedException{

		testName = "Find Channel Test";
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.youtube:id/menu_item_1")));
		driver.findElement(By.id("com.google.android.youtube:id/menu_item_1")).click();
		
		driver.findElement(By.id("com.google.android.youtube:id/search_edit_text")).sendKeys("unacademy upsc");	
		((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
		
		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
				"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
						+ "new UiSelector().resourceId(\"com.google.android.youtube:id/channel_item\"));"))).click();
	}
	
//	@Test
//	public void showTrending() throws InterruptedException{
//
//		testName = "Show Trending Videos Test";
//		WebDriverWait wait = new WebDriverWait(driver, 10);
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.Button[@content-desc=\"Explore\"]/android.widget.TextView"))).click();
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//android.view.ViewGroup[@content-desc=\"Trending\"])[2]"))).click();
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc=\"Navigate up\"]"))).click();
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//android.view.ViewGroup[@content-desc=\"Music\"])[2]"))).click();
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc=\"Navigate up\"]"))).click();
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//android.view.ViewGroup[@content-desc=\"Fashion & beauty\"])[2]"))).click();
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc=\"Navigate up\"]"))).click();		
//	}
	
	
}
