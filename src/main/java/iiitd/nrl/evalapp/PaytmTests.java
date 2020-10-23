package iiitd.nrl.evalapp;

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

@SuppressWarnings("unchecked")
public class PaytmTests {
    AndroidDriver<MobileElement> driver;
    AppiumDriverLocalService service;
    String appName = "Paytm";
    String testName;
	
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

        URL url;
        try {
            url = new URL("http://127.0.0.1:4723/wd/hub");
            driver=new AndroidDriver<MobileElement>(url,cap);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (WebDriverException e) {
	        MyDatabase.addTestResult(appName, testName, 0, "App Not Installed" , false);
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
		long time = testResult.getEndMillis() - testResult.getStartMillis();
        String connType = getConnectionType();

        MyDatabase.addTestResult(appName, testName, time, connType, testResult.isSuccess());
        driver.quit();
	}
	
    @Test
    public void paySomeone() throws InterruptedException {
        testName = "Pay Nikhil Re. 1/-";
        if (!driver.findElements(By.id("net.one97.paytm:id/iv_close")).isEmpty()) {
            driver.findElement(By.id("net.one97.paytm:id/iv_close")).click();
        }

        WebDriverWait wait = new WebDriverWait(driver, 10);
        if (!driver.findElements(By.id("net.one97.paytm:id/iv_cross_background")).isEmpty())
        	driver.findElement(By.id("net.one97.paytm:id/iv_cross_background")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/image_container_1"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/enter_mobile_upi_tv"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/searchView"))).sendKeys("8802647803");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/rl_main_row"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/amount_et"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/amount_et"))).sendKeys("1");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/ll_uni_pay"))).click();

        if (!driver.findElements(By.id("net.one97.paytm:id/iv_cross_background")).isEmpty())
        	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/iv_cross_background"))).click();
        if (!driver.findElements(By.id("net.one97.paytm:id/iv_cross_background")).isEmpty())
        	wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("net.one97.paytm:id/iv_cross_background"))).click();

    }
}

