package iiitd.nrl.evalapp;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
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
        driver = MainLauncher.driver;
        driver.startActivity(new Activity("net.one97.paytm","net.one97.paytm.landingpage.activity.AJRMainActivity"));
//        DesiredCapabilities cap=new DesiredCapabilities();
//        cap.setCapability("appPackage", "net.one97.paytm");
//        cap.setCapability("appActivity", "net.one97.paytm.landingpage.activity.AJRMainActivity");
//        cap.setCapability("noReset", "true");
//		cap.setCapability("fullReset", "false");
//        cap.setCapability("autoGrantPermissions", true);
//        cap.setCapability("autoAcceptAlerts", true);
//        cap.setCapability("uiautomator2ServerInstallTimeout", 60000);
//
//        URL url;
//        try {
//            url = new URL("http://127.0.0.1:4723/wd/hub");
//            driver=new AndroidDriver<MobileElement>(url,cap);
//        } catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (WebDriverException e) {
//            MyDatabase.addTestResult(appName, testName, null, "NA" , false, "App Not Installed");
//	        driver.quit();
//		}

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
//        System.out.println(jsonString);
        long timeTaken = 0;

        HashMap<String, Long> main_events = new HashMap<>();

        if (testResult.isSuccess()) {
            if (testResult.getName() == "sendMoneyFromWallet") {
                timeTaken = MyDatabase.getTimeTaken(jsonString, -7, -4);
                main_events.put(testResult.getName(), timeTaken);
            }
        }

        MyDatabase.addTestResult(appName, testName, main_events, getConnectionType(), testResult.isSuccess(), testStatusReason);
//        driver.quit();
	}
	
    @Test
    public void sendMoneyFromWallet() throws InterruptedException {
        testName = "Pay Nikhil Re. 1/-";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);

        try {

//            if (!driver.findElements(By.id("net.one97.paytm:id/iv_close")).isEmpty()) {
//                driver.findElement(By.id("net.one97.paytm:id/iv_close")).click();
//            }

            if (!driver.findElements(By.id("net.one97.paytm:id/iv_cross_background")).isEmpty()) {
                driver.findElement(By.id("net.one97.paytm:id/iv_cross_background")).click();
            }

//            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"Scan & Pay\")"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/image_container_1"))).click();

//            if (!driver.findElements(By.id("com.android.permissioncontroller:id/permission_allow_button")).isEmpty()) {
//                driver.findElement(By.id("com.android.permissioncontroller:id/permission_allow_button")).click();
//            }


            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/p2p_cp_search_ll"))).click();

//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/enter_mobile_upi_tv"))).click();


            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().text(\"Enter Name or Mobile Number\")"))).sendKeys("8076011980");
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/searchViewEdt1"))).sendKeys("8076011980");

            wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/payTv")), ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().textContains(\"918076011980\")"))));

            if (driver.findElements(By.id("net.one97.paytm:id/payTv")).isEmpty()) {
                driver.findElement(MobileBy.AndroidUIAutomator("UiSelector().textContains(\"918076011980\")")).click();
            }
            else {
                driver.findElement(By.id("net.one97.paytm:id/payTv")).click();
            }


            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/amount_et"))).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/amount_et"))).sendKeys("1");


            /* sending money time measurement starts */
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/ll_uni_pay"))).click();
            List<MobileElement> elements = driver.findElements(By.id("net.one97.paytm:id/iv_close_icon"));
            if (!elements.isEmpty())
                elements.get(0).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/p2p_amount_tv"))).isDisplayed();
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
    }
}

