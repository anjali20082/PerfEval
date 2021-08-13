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
    String commandsCompleted = "";

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
        driver.quit();
	}
	
    @Test
    public void sendMoneyFromWallet() throws InterruptedException {
        testName = "Pay Shradha Re. 1/-";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
        String phoneno = "9467913234";
        String ui;
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/image_container_1"))).click();
            commandsCompleted += "scan&Pay:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/p2p_cp_search_ll"))).click();
            commandsCompleted += "clickSearch:";

            ui = "net.one97.paytm:id/iv_contact";
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ui))).click();
            commandsCompleted += "clickContacts:";

            ui = "new UiSelector().text(\"Search Name or Mobile Number\")";
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui))).sendKeys(phoneno);
            commandsCompleted += "enterNumber:";


            ui = "new UiSelector().textContains(\"" + phoneno + "\")";
            List<MobileElement> contacts = driver.findElements(MobileBy.AndroidUIAutomator(ui));
            String ui2 = "new UiSelector().textContains(\"Proceed\")";
            List<MobileElement> proceed = driver.findElements(MobileBy.AndroidUIAutomator(ui2));

            while (contacts.size() < 2 && proceed.isEmpty()) {
//                System.out.println(contacts.size() + Boolean.toString(contacts.size() > 1));

                contacts = driver.findElements(MobileBy.AndroidUIAutomator(ui));
                proceed = driver.findElements(MobileBy.AndroidUIAutomator(ui2));
            }

            if (proceed.isEmpty()) {
                contacts.get(1).click();
            }
            else {
                proceed.get(0).click();
            }

//            ui = "new UiSelector().textContains(\"Pay\")";
            ui = "net.one97.paytm:id/buttonText";
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ui))).click();
            commandsCompleted += "clickPay:";

//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/amount_et"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).click();
            commandsCompleted += "clickMoneyField:";

//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/amount_et"))).sendKeys("1");
            driver.findElement(By.className("android.widget.EditText")).sendKeys("1");
            commandsCompleted += "enterAmount:";


            if (!driver.findElements(By.id("net.one97.paytm:id/bankImageCollapsedView")).isEmpty()) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/selectedBankDownArrowTV"))).click();
                commandsCompleted += "clickChangeModeDropdown:";

                ui = "new UiSelector().text(\"Paytm Wallet\")";
                wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui))).click();
                commandsCompleted += "changeModeToWallet:";

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/proceed"))).click();
                commandsCompleted += "clickProceed:";
            }
            else {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/ll_uni_pay"))).click();
                commandsCompleted += "clickPay:";
            }

            /* sending money time measurement starts */

//            List<MobileElement> elements = driver.findElements(By.id("net.one97.paytm:id/iv_close_icon"));
//            if (!elements.isEmpty()) {
//                elements.get(0).click();
//            }

            ui2 = "net.one97.paytm:id/button_cross";
            ui = "net.one97.paytm:id/tvRefNum";

            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(By.id(ui)),
                    ExpectedConditions.visibilityOfElementLocated(By.id(ui2))));
            commandsCompleted += "checkingTransactionIdOrButtonCross";

            if (!driver.findElements(By.id(ui2)).isEmpty()) {
                driver.findElement(By.id(ui2)).click();
                commandsCompleted += "clickButtonCross";
            }

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ui))).isDisplayed();
            commandsCompleted += "checkTransactionId:";
            /* sending money time measurement stops */

            if (driver.findElements(By.id("net.one97.paytm:id/lavSuccess")).isEmpty()) {
                testStatusReason = "Payment failed";
                commandsCompleted += "F";
            } else {
                testStatusReason = "Payment successful";
                commandsCompleted += "P";
            }
        } catch (Exception e) {
            testStatusReason = "Payment Failed\n" + e.toString();
            throw e;
        }
//        JSON COMMANDS
    }


//    net.one97.paytm:id/bankImageCollapsedView
//    net.one97.paytm:id/selectedBankDownArrowTV
//
//    Paytm Wallet - text
//
//
//    net.one97.paytm:id/proceed


//    net.one97.paytm:id/button_cross

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

