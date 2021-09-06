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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class LinkedInTests_myConnections {
    AndroidDriver<MobileElement> driver;
    String appName = "LinkedIn_myConnections";
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
        txrx = NetStats.getstats("10352");
        rx_initial = txrx.get(0);
        tx_initial = txrx.get(1);
//        System.out.println(rx_initial + "  "+ tx_initial);
//        tx_bytes += tx_initial+":";
//        rx_bytes += rx_initial+":";
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
            driver= new AndroidDriver<MobileElement>(url, cap);
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
    public void myConnections() throws InterruptedException, IOException {
        testName = "check my connections";
        WebDriverWait wait = new WebDriverWait(driver,MyDatabase.testTimeLimit);

        try {

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/tab_relationships"))).click();
            commandsCompleted += "clickRelationships:";

            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.linkedin.android:id/mynetwork_my_communitities_entry_point_container"))).click();
            commandsCompleted += "myCommunity:";

            /* my connection time measurement starts*/
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"Connections\")"))).click();
            commandsCompleted += "connections:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.linkedin.android:id/search_entity_result_universal_template_container")));
            commandsCompleted += "checkConnections:";

            commandsCompleted += "P";
            /* my connection time measurement stops*/
            txrx = NetStats.getstats("10352");
            Integer rx_1 = txrx.get(0);
            Integer tx_1 = txrx.get(1);
//            System.out.println(rx_1 + "  "+ tx_1);
            tx_bytes += tx_1 - tx_initial;
            rx_bytes += rx_1 - rx_initial;

            System.out.println("TX: "+tx_bytes);
            System.out.println("RX: "+rx_bytes);
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
//				"startTime": 1615909015758,
//				"endTime": 1615909017193
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615909017204,
//				"endTime": 1615909017245
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615909017253,
//				"endTime": 1615909017322
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615909017330,
//				"endTime": 1615909017528
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615909017532,
//				"endTime": 1615909019075
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615909020458,
//				"endTime": 1615909020603
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615909020606,
//				"endTime": 1615909020672
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615909020691,
//				"endTime": 1615909023335
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615909023347,
//				"endTime": 1615909023470
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615909023473,
//				"endTime": 1615909023567
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1615909023576,
//				"endTime": 1615909023576
//		}
//  ]
//	}

}