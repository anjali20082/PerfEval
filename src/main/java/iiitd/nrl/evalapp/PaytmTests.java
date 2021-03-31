package iiitd.nrl.evalapp;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unchecked")
public class PaytmTests {
    AndroidDriver<MobileElement> driver;
    AppiumDriverLocalService service;
    String appName = "Paytm";
    String testName = "NA";
    String testStatusReason = "NA";

    @AfterClass
    public void update() {

    }
    
    @BeforeMethod
    public void launchCap() {
        DesiredCapabilities cap=new DesiredCapabilities();
        cap.setCapability("appPackage", "net.one97.paytm");
        cap.setCapability("appActivity", "net.one97.paytm.landingpage.activity.AJRMainActivity");
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
	        driver.quit();
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

        MyDatabase.setCurrentApp(appName);
        MyDatabase.setAppJsonCommands(jsonString);
        MyDatabase.setTestStatus(testResult.isSuccess());
        MyDatabase.setTestStatusReason(testStatusReason);
        MyDatabase.setConnType(getConnectionType());

//        long timeTaken = 0;
//
//        HashMap<String, Long> main_events = new HashMap<>();
//
//        if (testResult.isSuccess()) {
//            if (testResult.getName() == "sendMoneyFromWallet") {
//                timeTaken = MyDatabase.getTimeTaken(jsonString, -7, -4);
//                main_events.put(testResult.getName(), timeTaken);
//            }
//        }
//
//        MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);
        testStatusReason = "NA";
        driver.quit();
	}
	
    @Test
    public void sendMoneyFromWallet() throws InterruptedException {
        testName = "Pay Nikhil Re. 1/-";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

        try {

            if (!driver.findElements(By.id("net.one97.paytm:id/iv_close")).isEmpty()) {
                driver.findElement(By.id("net.one97.paytm:id/iv_close")).click();
            }

            if (!driver.findElements(By.id("net.one97.paytm:id/iv_cross_background")).isEmpty()) {
                driver.findElement(By.id("net.one97.paytm:id/iv_cross_background")).click();
            }

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/image_container_1"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/p2p_cp_search_ll"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"Enter Name or Mobile Number\")"))).sendKeys("8802647803");
//            wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"Proceed\")")), ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"918802647803\")"))));
//
//            if (driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"Proceed\")")).isEmpty()) {
//                driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"918802647803\")")).click();
//            }
//            else {
//                driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"Proceed\")")).click();
//            }

            List<MobileElement> contacts = driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"8802647803\")"));
            while (contacts.size() < 2) {
                System.out.println(contacts.size() + Boolean.toString(contacts.size() > 1));
                contacts = driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"8802647803\")"));
            }

            contacts.get(1).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/amount_et"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/amount_et"))).sendKeys("1");

            /* sending money time measurement starts */
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/ll_uni_pay"))).click();
            List<MobileElement> elements = driver.findElements(By.id("net.one97.paytm:id/iv_close_icon"));
            if (!elements.isEmpty())
                elements.get(0).click();

//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/p2p_amount_tv"))).isDisplayed();
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"Transaction ID\");"))).isDisplayed();
            /* sending money time measurement stops */

            if (driver.findElements(By.id("net.one97.paytm:id/p2p_success_status_lav")).isEmpty()) {
                testStatusReason = "Payment failed";
            } else {
                testStatusReason = "Payment successful";
            }
        } catch (Exception e) {
            testStatusReason = "Payment Failed\n" + e.toString();
            throw e;
        }
//        JSON COMMANDS
    }
//    {
//        "commands": [
//        {
//            "cmd": "findElements",
//                "startTime": 1616001294682,
//                "endTime": 1616001295819
//        },
//        {
//            "cmd": "findElements",
//                "startTime": 1616001295838,
//                "endTime": 1616001295864
//        },
//        {
//            "cmd": "findElement",
//                "startTime": 1616001297599,
//                "endTime": 1616001299370
//        },
//        {
//            "cmd": "elementDisplayed",
//                "startTime": 1616001299379,
//                "endTime": 1616001299409
//        },
//        {
//            "cmd": "click",
//                "startTime": 1616001299419,
//                "endTime": 1616001299485
//        },
//        {
//            "cmd": "findElement",
//                "startTime": 1616001301326,
//                "endTime": 1616001301947
//        },
//        {
//            "cmd": "elementDisplayed",
//                "startTime": 1616001301951,
//                "endTime": 1616001301990
//        },
//        {
//            "cmd": "click",
//                "startTime": 1616001302004,
//                "endTime": 1616001302073
//        },
//        {
//            "cmd": "findElement",
//                "startTime": 1616001303271,
//                "endTime": 1616001304527
//        },
//        {
//            "cmd": "elementDisplayed",
//                "startTime": 1616001304530,
//                "endTime": 1616001304605
//        },
//        {
//            "cmd": "setValue",
//                "startTime": 1616001304613,
//                "endTime": 1616001305411
//        },
//        {
//            "cmd": "findElement",
//                "startTime": 1616001306098,
//                "endTime": 1616001306144
//        },
//        {
//            "cmd": "elementDisplayed",
//                "startTime": 1616001306147,
//                "endTime": 1616001306218
//        },
//        {
//            "cmd": "findElements",
//                "startTime": 1616001306223,
//                "endTime": 1616001306270
//        },
//        {
//            "cmd": "findElement",
//                "startTime": 1616001306285,
//                "endTime": 1616001306327
//        },
//        {
//            "cmd": "click",
//                "startTime": 1616001306332,
//                "endTime": 1616001308553
//        },
//        {
//            "cmd": "findElement",
//                "startTime": 1616001308563,
//                "endTime": 1616001308648
//        },
//        {
//            "cmd": "elementDisplayed",
//                "startTime": 1616001308651,
//                "endTime": 1616001308679
//        },
//        {
//            "cmd": "click",
//                "startTime": 1616001308686,
//                "endTime": 1616001309517
//        },
//        {
//            "cmd": "findElement",
//                "startTime": 1616001309535,
//                "endTime": 1616001309594
//        },
//        {
//            "cmd": "elementDisplayed",
//                "startTime": 1616001309597,
//                "endTime": 1616001309626
//        },
//        {
//            "cmd": "setValue",
//                "startTime": 1616001309644,
//                "endTime": 1616001310355
//        },
//        {
//            "cmd": "findElement",
//                "startTime": 1616001310371,
//                "endTime": 1616001311797
//        },
//        {
//            "cmd": "elementDisplayed",
//                "startTime": 1616001311801,
//                "endTime": 1616001311824
//        },
//        {
//            "cmd": "click",
//                "startTime": 1616001311832,
//                "endTime": 1616001311900
//        },
//        {
//            "cmd": "findElements",
//                "startTime": 1616001311912,
//                "endTime": 1616001312038
//        },
//        {
//            "cmd": "findElement",
//                "startTime": 1616001312053,
//                "endTime": 1616001313844
//        },
//        {
//            "cmd": "elementDisplayed",
//                "startTime": 1616001313847,
//                "endTime": 1616001313862
//        },
//        {
//            "cmd": "elementDisplayed",
//                "startTime": 1616001313865,
//                "endTime": 1616001313878
//        },
//        {
//            "cmd": "findElements",
//                "startTime": 1616001313896,
//                "endTime": 1616001313949
//        },
//        {
//            "cmd": "getLogEvents",
//                "startTime": 1616001313959,
//                "endTime": 1616001313959
//        }
//  ]
//    }
}

