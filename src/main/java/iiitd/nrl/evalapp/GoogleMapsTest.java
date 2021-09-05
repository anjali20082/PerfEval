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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class GoogleMapsTest {
    AndroidDriver<MobileElement> driver;
    String appName = "GoogleMaps";
    String testName = "NA";
    String testStatusReason = "NA";

    @BeforeMethod
    public void launchCap() throws IOException {
        DesiredCapabilities cap=new DesiredCapabilities();
        cap.setCapability("appPackage", "com.google.android.apps.maps");
        cap.setCapability("appActivity", "com.google.android.maps.MapsActivity");
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
    public void upload_stats()throws Exception{
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
        Activity activity = new Activity("com.hawk.trakbytes", "com.hawk.trakbytes.MainActivity");
        driver.startActivity(activity);
        String packetData = "NA";
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.hawk.trakbytes:id/upload_stats"))).click();
            Thread.sleep(3000);
            System.out.println("Upload Stats clicked");
            packetData = driver.findElement(By.id("com.hawk.trakbytes:id/stats_text")).getText();

            System.out.println(packetData);
        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
        MyDatabase.setPacket_sizes_after(packetData);
        MyDatabase.addTestResult();
    }
    @AfterMethod
    public void restart(ITestResult testResult) throws Exception {
        String jsonString = driver.getEvents().getJsonData();

        MyDatabase.setCurrentApp(appName);
        MyDatabase.setAppJsonCommands(jsonString);
        MyDatabase.setTestStatus(testResult.isSuccess());
        MyDatabase.setTestStatusReason(testStatusReason);
        MyDatabase.setConnType(getConnectionType());
        testStatusReason = "NA";
//        upload_stats();
        driver.quit();
    }

    @Test
    public void searchPlace() throws InterruptedException {
        testName = "search place";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/search_omnibox_text_box"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/search_omnibox_edit_text"))).sendKeys("india gate");
            ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
//            com.google.android.apps.maps:id/search_omnibox_text_box
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.google.android.apps.maps:id/street_view_thumbnail")));
            while(!driver.findElements(By.id("com.google.android.apps.maps:id/scalebar_widget")).isEmpty());

        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
//        JSON COMMANDS
    }

//  "commands": [
//    {
//        "cmd": "findElement",
//            "startTime": 1615915868952,
//            "endTime": 1615915870188
//    },
//    {
//        "cmd": "elementDisplayed",
//            "startTime": 1615915870202,
//            "endTime": 1615915870228
//    },
//    {
//        "cmd": "click",
//            "startTime": 1615915870243,
//            "endTime": 1615915870429
//    },
//    {
//        "cmd": "findElement",
//            "startTime": 1615915870448,
//            "endTime": 1615915871161
//    },
//    {
//        "cmd": "elementDisplayed",
//            "startTime": 1615915871167,
//            "endTime": 1615915872440
//    },
//    {
//        "cmd": "setValue",
//            "startTime": 1615915872460,
//            "endTime": 1615915873204
//    },
//    {
//        "cmd": "pressKeyCode",
//            "startTime": 1615915873228,
//            "endTime": 1615915874508
//    },
//    {
//        "cmd": "findElement",
//            "startTime": 1615915875785,
//            "endTime": 1615915877652
//    },
//    {
//        "cmd": "elementDisplayed",
//            "startTime": 1615915877656,
//            "endTime": 1615915877677
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915877686,
//            "endTime": 1615915877808
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915877826,
//            "endTime": 1615915877910
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915877920,
//            "endTime": 1615915877974
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915877980,
//            "endTime": 1615915878047
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915878060,
//            "endTime": 1615915878147
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915878154,
//            "endTime": 1615915878259
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915878278,
//            "endTime": 1615915878370
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915878388,
//            "endTime": 1615915878458
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915878466,
//            "endTime": 1615915878544
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915878560,
//            "endTime": 1615915878639
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915878655,
//            "endTime": 1615915878722
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915878733,
//            "endTime": 1615915878806
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915878825,
//            "endTime": 1615915878885
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915878901,
//            "endTime": 1615915878955
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915878965,
//            "endTime": 1615915879024
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915879042,
//            "endTime": 1615915879114
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915879118,
//            "endTime": 1615915879182
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915879195,
//            "endTime": 1615915879268
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915879287,
//            "endTime": 1615915879382
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915879395,
//            "endTime": 1615915879475
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915879489,
//            "endTime": 1615915879561
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915879581,
//            "endTime": 1615915879663
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915879677,
//            "endTime": 1615915879776
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915879784,
//            "endTime": 1615915879862
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915879878,
//            "endTime": 1615915879956
//    },
//    {
//        "cmd": "findElements",
//            "startTime": 1615915879971,
//            "endTime": 1615915880597
//    },
//    {
//        "cmd": "getLogEvents",
//            "startTime": 1615915880617,
//            "endTime": 1615915880617
//    }
//  ]
//}

}