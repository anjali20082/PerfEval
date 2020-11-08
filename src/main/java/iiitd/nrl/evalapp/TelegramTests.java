package iiitd.nrl.evalapp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
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
    @AfterClass
    public void update() {

    }

    @BeforeMethod
    public void launchCap() {
        DesiredCapabilities cap=new DesiredCapabilities();
        cap.setCapability("appPackage", "org.telegram.messenger");
        cap.setCapability("appActivity", "org.telegram.ui.LaunchActivity");
        cap.setCapability("noReset", "true");
        cap.setCapability("fullReset", "false");
        cap.setCapability("autoGrantPermissions", true);
        cap.setCapability("autoAcceptAlerts", true);
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
    public void sendMessage(){
        testName = "send message";
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Search"))).click();
        //  android.widget.EditText  class sendkeys
        //  Automation Testing EvalApp, 2 members   text
        //  android.widget.EditText  class sendkeys
        //   Send   acc-id click
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("automation testing evalapp");
        List<MobileElement> results= (List<MobileElement>) driver.findElementsByClassName("android.view.ViewGroup");
        results.get(0).click();

//		wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("UiSelector().textContains(\"Automation Testing EvalApp\")"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("and we meet here as well");
        wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Send"))).click();
    }
}