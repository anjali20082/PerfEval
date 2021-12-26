package iiitd.nrl.evalapp;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class YouTubeTests {
	AndroidDriver<MobileElement> driver;

	AppiumDriverLocalService service;
	String appName = "Youtube";
	String testName = "NA";
	String testStatusReason = "NA";
	String video_time = "";
	String copy_info = "";
	String loc = "", lat = "", longi = "", alt ="";

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

	@BeforeMethod
	public void launchCap() {
		DesiredCapabilities cap=new DesiredCapabilities();
		cap.setCapability("appPackage", "com.google.android.youtube");
		cap.setCapability("appActivity", "com.google.android.youtube.HomeActivity");
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

		System.out.println(video_time);

		System.out.println(copy_info);

		HashMap<String, Long> main_events = new HashMap<>();

		if (testResult.isSuccess()) {
			if (testResult.getName() == "playTest") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, 6, 8);
				main_events.put("searchVideo", timeTaken);

				timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -2);
				main_events.put("playVideo", timeTaken);

			} else if (testResult.getName() == "channelTest") {
				timeTaken = MyDatabase.getTimeTaken(jsonString, 6, 8);
				main_events.put("searchChannel", timeTaken);

				timeTaken = MyDatabase.getTimeTaken(jsonString, -4, -2);
				main_events.put("openChannelPage", timeTaken);
			}
		}

//		MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);

        driver.quit();
	}

//	@Test
	public void copyDebugInfo() throws InterruptedException {
		testName = "copy debug info";
		WebDriverWait wait = new WebDriverWait(driver, 300);

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.youtube:id/menu_item_1")));
			driver.findElement(By.id("com.google.android.youtube:id/menu_item_1")).click();
			driver.findElement(By.id("com.google.android.youtube:id/search_edit_text")).sendKeys("manikarnika");

			/* searching video time measurement starts */
			((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().descriptionContains(\"Official Trailer\"));"))).isDisplayed();
			/* searching video time measurement starts */


			driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Official Trailer\")")).click();
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.google.android.youtube:id/title"))).isDisplayed();

			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.google.android.youtube:id/player_overflow_button"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"Stats for nerds\")"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.google.android.youtube:id/copy_debug_info_button"))).click();

			System.out.println(driver.getClipboardText());

//			Thread.sleep(500);

		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}

//	def start_dump(self):
//	p = subprocess.Popen("tcpdump -i " + configuration.interface
//                             + " -v -tt -n -B 12288 \"udp or tcp\" > "
//									 + os.path.join(self.get_path(),
//	configuration.location + "_PC_tcpdump" + "_scen_"
//			+ self.scenario_index + "_vid_" + self.video_id
//                                            + "_rep_" + str(self.iteration) + ".log"),
//	shell=True, preexec_fn=os.setsid, stdout=subprocess.PIPE,
//	stderr=subprocess.PIPE)
//			self.processes.append(p)
//	log_output(p, False)
//        logger.debug("TCPDUMP started on the PC")
//
//	p = subprocess.Popen('''adb -s ''' + Measurement.get_current_device() +
//			''' shell "su -c tcpdump -i wlan0 -v -tt -n -B 12288''' \
//			+ ''' 'udp or tcp' > /sdcard/tcpdump.log"''',
//	shell=True, preexec_fn=os.setsid, stdout=subprocess.PIPE,
//	stderr=subprocess.PIPE)
//
//			self.processes.append(p)
//	log_output(p, False)
//        logger.debug("TCPDUMP started on the Phone")
//
//	p = subprocess.Popen("tcpdump -tt -i " + configuration.interface + " udp port 53 > "
//			+ os.path.join(self.get_path(),
//	configuration.location + "_PC_dns" + "_scen_"
//			+ self.scenario_index + "_vid_" + self.video_id + "_rep_"
//			+ str(self.iteration) + ".log"),
//	shell=True, preexec_fn=os.setsid, stdout=subprocess.PIPE,
//	stderr=subprocess.PIPE)
//			self.processes.append(p)
//	log_output(p, False)
//        logger.debug("DNS logging started on the PC")

	@Test
	public void copyVideoTime() throws InterruptedException {
		testName = "copy debug info";
		WebDriverWait wait = new WebDriverWait(driver, 10);

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.youtube:id/menu_item_1")));
			driver.findElement(By.id("com.google.android.youtube:id/menu_item_1")).click();
			driver.findElement(By.id("com.google.android.youtube:id/search_edit_text")).sendKeys("manikarnika");

			/* searching video time measurement starts */
			((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().descriptionContains(\"Official Trailer\"));"))).isDisplayed();
			/* searching video time measurement starts */


			driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Official Trailer\")")).click();
//			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.google.android.youtube:id/title"))).isDisplayed();

			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.google.android.youtube:id/player_overflow_button"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"Stats for nerds\")"))).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.google.android.youtube:id/copy_debug_info_button")));
			MobileElement copyInfoButton = driver.findElement(By.id("com.google.android.youtube:id/copy_debug_info_button"));

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textMatches(\"\\d*:\\d*\")")));
			MobileElement video_seeker = driver.findElementByAndroidUIAutomator("new UiSelector().textMatches(\"\\d*:\\d*\")");
			int i = 0;

			Thread copyInfoThread = new Thread(
				new Runnable() {
					@Override
					public void run() {

						int i = 0;
						while (i++ < 10) {
							copyInfoButton.click();
							copy_info += driver.getClipboardText();
							copy_info += "\n";
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}

			);

			copyInfoThread.start();

			while (i++ < 10) {
				video_time += video_seeker.getText();
				video_time += "\n";
				Thread.sleep(300);
			}

			copyInfoThread.join();


		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}


	@Test
	public void playTest() throws InterruptedException {
		testName = "play test";
		WebDriverWait wait = new WebDriverWait(driver, 300);

		try {

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.youtube:id/menu_item_1")));
			driver.findElement(By.id("com.google.android.youtube:id/menu_item_1")).click();
			driver.findElement(By.id("com.google.android.youtube:id/search_edit_text")).sendKeys("manikarnika");

			/* searching video time measurement starts */
			((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().descriptionContains(\"Official Trailer\"));"))).isDisplayed();
			/* searching video time measurement starts */


			driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Official Trailer\")")).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.google.android.youtube:id/title"))).isDisplayed();

		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}
	
	@Test
	public void channelTest() throws InterruptedException{
		testName = "find channel";
		WebDriverWait wait = new WebDriverWait(driver, 300);

		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.youtube:id/menu_item_1")));
			driver.findElement(By.id("com.google.android.youtube:id/menu_item_1")).click();

			driver.findElement(By.id("com.google.android.youtube:id/search_edit_text")).sendKeys("unacademy upsc");
			((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));

			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator(
					"new UiScrollable(" + "new UiSelector().scrollable(true)).scrollIntoView("
							+ "new UiSelector().resourceId(\"com.google.android.youtube:id/channel_item\"));"))).isDisplayed();

			driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"com.google.android.youtube:id/channel_item\")")).click();
			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Subscribe to Unacademy UPSC."))).isDisplayed();
		} catch (Exception e) {
			testStatusReason = e.toString();
			throw e;
		}
	}
}
