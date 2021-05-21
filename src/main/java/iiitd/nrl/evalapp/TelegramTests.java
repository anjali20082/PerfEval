package iiitd.nrl.evalapp;

import java.util.Random;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.rules.ExpectedException;
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
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

@SuppressWarnings("unchecked")
public class TelegramTests  {
    AndroidDriver<MobileElement> driver;
    String appName = "Telegram";
    String testName = "NA";
    String testStatusReason = "NA";
    String commandsCompleted = "";

    @BeforeMethod
    public void launchCap() {
        DesiredCapabilities cap=new DesiredCapabilities();
        cap.setCapability("appPackage", "org.telegram.messenger");
        cap.setCapability("appActivity", "org.telegram.ui.LaunchActivity");
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
        MyDatabase.setCommands(commandsCompleted);
        MyDatabase.setAppJsonCommands(jsonString);
        MyDatabase.setTestStatus(testResult.isSuccess());
        MyDatabase.setTestStatusReason(testStatusReason);
        MyDatabase.setConnType(getConnectionType());

        testStatusReason = "NA";
        MyDatabase.addTestResult();
        driver.quit();
    }

    @Test
    public void sendMessage() throws Exception {
        testName = "send message";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
        Random rand = new Random();
        int rand_int = rand.nextInt(1000);
        String rand_str = Integer.toString(rand_int);
        String message = "Hi, this is an automated text:" + rand_str;
        String verifiy_sent = message + "\nSent";

        try {

            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Search"))).click();
            commandsCompleted += "searchGrp:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("EvalApp");
            commandsCompleted += "enterName:";

            List<MobileElement> results= (List<MobileElement>) driver.findElementsByClassName("android.view.ViewGroup");
            results.get(0).click();
            commandsCompleted += "clickSearchResult:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys(message);
            commandsCompleted += "enterMsg:";

            // calculate time of below 2 commands
            /* sending message time measurement starts */
            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Send"))).click();
            commandsCompleted += "send:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"" + verifiy_sent + "\");"))).click();
            commandsCompleted += "checkIfMessageSent:";
            commandsCompleted += "P";
            /* sending message time measurement stops */

        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }

//        and we meet here as well Sent at 4:59 PM, Seen

//        JSON COMMANDS
    }
//    {
//        "commands": [
//        {
//            "cmd": "findElement",
//                "startTime": 1615994217804,
//                "endTime": 1615994218818
//        },
//        {
//            "cmd": "click",
//                "startTime": 1615994218839,
//                "endTime": 1615994219681
//        },
//        {
//            "cmd": "findElement",
//                "startTime": 1615994219689,
//                "endTime": 1615994219774
//        },
//        {
//            "cmd": "elementDisplayed",
//                "startTime": 1615994219777,
//                "endTime": 1615994219812
//        },
//        {
//            "cmd": "setValue",
//                "startTime": 1615994219827,
//                "endTime": 1615994220783
//        },
//        {
//            "cmd": "findElements",
//                "startTime": 1615994220791,
//                "endTime": 1615994222005
//        },
//        {
//            "cmd": "click",
//                "startTime": 1615994222014,
//                "endTime": 1615994222185
//        },
//        {
//            "cmd": "findElement",
//                "startTime": 1615994222202,
//                "endTime": 1615994223334
//        },
//        {
//            "cmd": "elementDisplayed",
//                "startTime": 1615994223338,
//                "endTime": 1615994223390
//        },
//        {
//            "cmd": "setValue",
//                "startTime": 1615994223399,
//                "endTime": 1615994223483
//        },
//        {
//            "cmd": "findElements",
//                "startTime": 1615994223492,
//                "endTime": 1615994224235
//        },
//        {
//            "cmd": "findElement",
//                "startTime": 1615994224253,
//                "endTime": 1615994224306
//        },
//        {
//            "cmd": "click",
//                "startTime": 1615994224313,
//                "endTime": 1615994224394
//        },
//        {
//            "cmd": "findElements",
//                "startTime": 1615994224422,
//                "endTime": 1615994225467
//        },
//        {
//            "cmd": "getLogEvents",
//                "startTime": 1615994225475,
//                "endTime": 1615994225475
//        }
//  ]
//    }
}
//Hi, this is an automated text:834 Sent at 1:06 PM, Seen