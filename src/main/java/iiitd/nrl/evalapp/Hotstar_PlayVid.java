package iiitd.nrl.evalapp;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.android.Activity;
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
public class Hotstar_PlayVid {
    AndroidDriver<MobileElement> driver;
    String appName = "Hotstar_PlayVid";
    String testName = "NA";
    String testStatusReason = "NA";
    String commandsCompleted = "";
    ArrayList<Integer> txrx;
    String tx_bytes = "";
    String rx_bytes = "";
    Integer rx_initial ;
    Integer tx_initial ;

    @BeforeMethod
    public void launchCap() throws IOException {

        txrx = NetStats.getstats("10369");
        rx_initial = txrx.get(0);
        tx_initial = txrx.get(1);
//        System.out.println(rx_initial + "  "+ tx_initial);
//
//        tx_bytes += tx_initial+":";
//        rx_bytes += rx_initial+":";

        DesiredCapabilities cap=new DesiredCapabilities();
        cap.setCapability("appPackage", "in.startv.hotstar");
//		cap.setCapability("appPackage", "in.startv.hotstaronly");
        cap.setCapability("appActivity", "in.startv.hotstar.rocky.launch.splash.SplashActivity");
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
    public void restart(ITestResult testResult) throws Exception {
        String jsonString = driver.getEvents().getJsonData();

        MyDatabase.setCurrentApp(appName);
        MyDatabase.setCommands(commandsCompleted);
        MyDatabase.setAppJsonCommands(jsonString);
        MyDatabase.setTestStatus(testResult.isSuccess());
        MyDatabase.setTestStatusReason(testStatusReason);
        MyDatabase.setConnType(getConnectionType());
        MyDatabase.set_TX_RX_Bytes(tx_bytes, rx_bytes);
        testStatusReason = "NA";
        driver.quit();
    }


    @Test
    public void playTest() throws IOException {

        testName = "Play Video";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("in.startv.hotstar:id/action_search"))).click();
            commandsCompleted += "clickSearch:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("in.startv.hotstar:id/search_text"))).sendKeys("dil bechara");
            commandsCompleted += "enterText:";
            /* search video time measurement starts*/

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.ImageView")));
            commandsCompleted += "findImage:";

            txrx = NetStats.getstats("10369");
            Integer rx_1 = txrx.get(0);
            Integer tx_1 = txrx.get(1);
//            System.out.println(rx_1 + "  "+ tx_1);
            tx_bytes += tx_1 - tx_initial +":";
            rx_bytes += rx_1 - rx_initial +":";


            List<MobileElement> elements = driver.findElements(By.className("android.widget.ImageView"));

            elements.get(2).click();
            commandsCompleted += "click1stImage:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("in.startv.hotstar:id/top_info")));
            commandsCompleted += "checkMovieInfo:";
            commandsCompleted += "P";

            txrx = NetStats.getstats("10369");
            Integer rx_2 = txrx.get(0);
            Integer tx_2 = txrx.get(1);
//            System.out.println(rx_2 + "  "+ tx_2);

            tx_bytes += tx_2-tx_1;
            rx_bytes += rx_2-rx_1;

            System.out.println("TX: "+tx_bytes);
            System.out.println("RX: "+rx_bytes);
            /* search video time measurement stops*/
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
//				"startTime": 1615909158466,
//				"endTime": 1615909159818
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615909159834,
//				"endTime": 1615909161401
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615909161409,
//				"endTime": 1615909162252
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615909162256,
//				"endTime": 1615909162278
//		},
//		{
//			"cmd": "setValue",
//				"startTime": 1615909162300,
//				"endTime": 1615909162870
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615909164075,
//				"endTime": 1615909164502
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615909164504,
//				"endTime": 1615909164522
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615909164541,
//				"endTime": 1615909164630
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615909164649,
//				"endTime": 1615909166482
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615909166485,
//				"endTime": 1615909166506
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1615909166521,
//				"endTime": 1615909166521
//		}
//  ]
//	}

}