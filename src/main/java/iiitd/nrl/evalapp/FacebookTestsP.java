package iiitd.nrl.evalapp;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
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
public class FacebookTestsP {
    AndroidDriver<MobileElement> driver;
    String appName = "Facebook_post";
    String testName = "NA";
    String testStatusReason = "NA";
    String commandsCompleted = "";
    ArrayList<Integer> txrx;
    String tx_bytes = "";
    String rx_bytes = "";
    Integer rx_initial ;
    Integer tx_initial ;
    Timestamp timestamp1, timestamp2;
    long timestamp1_time, timestamp2_time, difference;
    @AfterClass
    public void update() {

    }

    @BeforeMethod
    public void launchCap() throws IOException {

        txrx = NetStats.getstats("10346");
        rx_initial = txrx.get(0);
         tx_initial = txrx.get(1);
         timestamp1 = new Timestamp(System.currentTimeMillis());
        timestamp1_time = timestamp1.getTime();
//        System.out.println("pcap capture started at" + timestamp1);
//
//        tx_bytes += tx_initial+":";
//        rx_bytes += rx_initial+":";

        DesiredCapabilities cap=new DesiredCapabilities();
        cap.setCapability("appPackage", "com.facebook.katana");
        cap.setCapability("appActivity", "com.facebook.katana.activity.FbMainTabActivity");
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

        System.out.println(jsonString);

        MyDatabase.setCurrentApp(appName);
        MyDatabase.setAppJsonCommands(jsonString);
        MyDatabase.setCommands(commandsCompleted);
        MyDatabase.setTestStatus(testResult.isSuccess());
        MyDatabase.setTestStatusReason(testStatusReason);
        MyDatabase.setConnType(getConnectionType());
        MyDatabase.set_TX_RX_Bytes(tx_bytes, rx_bytes);
        driver.quit();
    }



    @Test
    public void postGroup() throws InterruptedException, IOException {

        testName = "post in a group";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

        Random rand = new Random();
        int rand_int = rand.nextInt(1000);
        String rand_str = Integer.toString(rand_int);
        String message = "Hi, this is an automated post:" + rand_str;
        String ui = "";
        try {


            // location testing code

            String cmd = "adb shell am broadcast -a io.appium.settings.location -n io.appium.settings/.receivers.LocationInfoReceiver";
            ProcessBuilder processBuilder = new ProcessBuilder();
//      if (Config.osName.contains("Windows"))
            processBuilder.command("cmd.exe", "/c", cmd);
//      else
//      processBuilder.command("bash", "-c", cmd);

            Process p = processBuilder.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

// Read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            String s = null;
            StringBuilder st= new StringBuilder() ;
            while ((s = stdInput.readLine()) != null) {
                st.append(s);
            System.out.println(s.trim());
            }

//            The first value in the returned data string is the current latitude, the second is the longitude
//            and the last one is the altitude. An empty string is returned if the data cannot be retrieved
//            (more details on the failure cause can be found in the logcat output).

//            String st1 = (st.toString());
//            String rx_bg = (st1.split(" ")[5]);
//            String tx_bg = (st1.split(" ")[7]);
//            String rx_fg = (st1.split(" ")[25]);
//            String tx_fg = (st1.split(" ")[27]);
//            ArrayList<Integer> data = new ArrayList<Integer>();
//            data.add(Integer.valueOf(rx_bg) +Integer.valueOf(rx_fg));
//            data.add(Integer.valueOf(tx_bg) +Integer.valueOf(tx_fg));
//        data.add(rx_fg);
//        data.add(tx_fg);
// Read any errors from the attempted command

            while ((s = stdError.readLine()) != null) {
                System.out.println("Here is the standard error of the command (if any):\n");
                System.out.println(s.trim());
            }





//			ui = "new UiSelector().descriptionMatches(\".*(?i)Groups(?-i).*\")";
//			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui))).click();
//			commandsCompleted += "groups:";
//
//			ui = "new UiSelector().descriptionMatches(\"(?i)Your Groups(?-i)\")";
//			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui))).click();
//			commandsCompleted += "yourGroups:";
//
//			ui = "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().descriptionMatches(\"(?i)Evaluation of Apps Button(?-i)\"));";
//			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui))).click();
//			commandsCompleted += "evalApp:";


//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Username"))).sendKeys("iiitdevalapp@gmail.com");
//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Password"))).sendKeys("nrl_evalapp");
//
//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Log In"))).click();
//
//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textMatches(\"(?i)Not Now(?-i)\")"))).click();



            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Search"))).click();
//			wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.EditText"))).click();
            commandsCompleted += "clickSearch:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).click();
            commandsCompleted += "searchPerson:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("Evaluation of Apps");
            commandsCompleted += "enterName:";

            ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
            commandsCompleted += "pressEnter:";

            ui = "new UiSelector().description(\"Visit\");";
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui))).click();
            commandsCompleted += "evalapp:";

            ui = "new UiScrollable(" + "new UiSelector().scrollable(true)).scrollToBeginning(20);";
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui)));
            commandsCompleted += "scrollingAbove:";

            ui = "new UiSelector().descriptionContains(\"Create a post\");";
            String ui1 = "new UiSelector().descriptionContains(\"Write something\");";
            wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui)),
                    ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui1))));

            if (!driver.findElements(MobileBy.AndroidUIAutomator(ui)).isEmpty()) {
                driver.findElement(MobileBy.AndroidUIAutomator(ui)).click();
            }
            else {
                driver.findElement(MobileBy.AndroidUIAutomator(ui1)).click();
            }
            commandsCompleted += "writePost:";

            ui = "android.widget.AutoCompleteTextView";
            ui1 = "android.widget.EditText";
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(By.className(ui)),
                    ExpectedConditions.visibilityOfElementLocated(By.className(ui1))));
            if (!driver.findElements(By.className(ui)).isEmpty()) {
                driver.findElement(By.className(ui)).click();
                driver.findElement(By.className(ui)).sendKeys(message);
            }
            else {
                driver.findElement(By.className(ui1)).click();
                driver.findElement(By.className(ui1)).sendKeys(message);
            }
            commandsCompleted += "writeMsg:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("POST"))).click();
            commandsCompleted += "clickPost:";


            ui = "new UiSelector().descriptionMatches(\"(?i)profile picture(?-i)\");";
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui)));
            timestamp2 = new Timestamp(System.currentTimeMillis());
            System.out.println("facebook ui reflected at : " +timestamp2 );
            timestamp2_time = timestamp2.getTime();
            difference = timestamp2_time - timestamp1_time;

            System.out.println("difference in timestamps : "+ difference);

            commandsCompleted += "profilePicture:";

            commandsCompleted += "P";

            txrx = NetStats.getstats("10346");
            Integer rx_1 = txrx.get(0);
            Integer tx_1 = txrx.get(1);
//            System.out.println(rx_1 + "  "+ tx_1);

            tx_bytes += tx_1-tx_initial;
            rx_bytes += rx_1-rx_initial;

            System.out.println("TX: "+tx_bytes);
            System.out.println("RX: "+rx_bytes);


            /* post group time measurement stops */
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
//				"startTime": 1615992974046,
//				"endTime": 1615992975100
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992975115,
//				"endTime": 1615992977351
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992977365,
//				"endTime": 1615992977418
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992977429,
//				"endTime": 1615992979230
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992979235,
//				"endTime": 1615992979271
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992979283,
//				"endTime": 1615992981302
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992981319,
//				"endTime": 1615992984217
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992984342,
//				"endTime": 1615992984387
//		},
//		{
//			"cmd": "findElements",
//				"startTime": 1615992984405,
//				"endTime": 1615992984490
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992984497,
//				"endTime": 1615992984544
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992984561,
//				"endTime": 1615992985710
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992985716,
//				"endTime": 1615992985770
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615992985775,
//				"endTime": 1615992985795
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992985809,
//				"endTime": 1615992987656
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992987664,
//				"endTime": 1615992987722
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1615992987725,
//				"endTime": 1615992987751
//		},
//		{
//			"cmd": "setValue",
//				"startTime": 1615992987774,
//				"endTime": 1615992988555
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992988571,
//				"endTime": 1615992989358
//		},
//		{
//			"cmd": "click",
//				"startTime": 1615992989364,
//				"endTime": 1615992989427
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1615992989442,
//				"endTime": 1615992991379
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1615992991387,
//				"endTime": 1615992991388
//		}
//  ]
//	}



}