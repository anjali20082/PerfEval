package iiitd.nrl.evalapp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import io.appium.java_client.android.Activity;
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
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;

@SuppressWarnings("unchecked")
public class Amazon_GoCart {
    AndroidDriver<MobileElement> driver;
    String appName = "Amazon";
    String testName = "NA";
    String testStatusReason = "NA";
    String commandsCompleted = "";

    @AfterClass
    public void update() {

    }

    @BeforeMethod
    public void launchCap() throws IOException {
        DesiredCapabilities cap=new DesiredCapabilities();
        cap.setCapability("appPackage", "in.amazon.mShop.android.shopping");
        cap.setCapability("appActivity", "com.amazon.mShop.android.home.HomeActivity");
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
        MyDatabase.setCommands(commandsCompleted);
        MyDatabase.setTestStatus(testResult.isSuccess());
        MyDatabase.setTestStatusReason(testStatusReason);
        MyDatabase.setConnType(getConnectionType());

        testStatusReason = "NA";
        upload_stats();
        driver.quit();
    }

    @Test
    public void searchProduct() throws InterruptedException {
        testName = "search product";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
        String ui = "";

        try {
//			ui = "in.amazon.mShop.android.shopping:id/rs_search_src_text";
            ui = "in.amazon.mShop.android.shopping:id/chrome_search_hint_view";
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ui))).click();
            commandsCompleted += "searchBox:";

//			ui = "in.amazon.mShop.android.shopping:id/rs_search_src_text";
            ui = "in.amazon.mShop.android.shopping:id/rs_search_src_text";
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ui))).sendKeys("steamer");
//			driver.findElement(By.id(ui)).sendKeys("steamer");
            commandsCompleted += "enterProductName:";

            ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
            commandsCompleted += "pressEnter:";

//			ui = "new UiSelector().textContains(\"Prime Eligible\");";
//			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui)));
            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Prime Eligible"))).click();
            commandsCompleted += "searchResult:";
            System.out.println(commandsCompleted);


            ui = "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().descriptionContains(\"out of 5 stars\"))";
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui))).click();
            commandsCompleted += "clickProduct:";
            System.out.println(commandsCompleted);

//			ui = "new UiSelector().descriptionContains(\"Amazon Fresh\");";
            ui = "new UiSelector().descriptionContains(\"out of 5 stars\");";
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui)));
            commandsCompleted += "productPage:";
            System.out.println(commandsCompleted);
            // Search Product Completed - 14th

            ui = "in.amazon.mShop.android.shopping:id/cart_count";
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(ui)));
            MobileElement cartValueElement = driver.findElement(By.id(ui));


            int cartValueBefore = Integer.parseInt(cartValueElement.getText());
            System.out.println("cart value before:" + cartValueBefore);
            commandsCompleted += "checkCart:";
            System.out.println(commandsCompleted);

            /* add product test measurement starts 18th*/

            ui = "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().resourceId(\"add-to-cart-button\"));";
//			ui = "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().textContains(\"Add to Cart\"));";
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui))).click();
            commandsCompleted += "addToCart:";
            System.out.println(commandsCompleted);

            int cartValueAfter = Integer.parseInt(cartValueElement.getText());
            while (cartValueAfter == cartValueBefore) {
                cartValueAfter = Integer.parseInt(cartValueElement.getText());
            }

            System.out.println("cart value after:" + cartValueAfter);
            /* add product test measurement stops 20th*/

            ui = "new UiSelector().description(\"Cart\");";
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui))).click();//
            /* delete product test measurement starts*/
            Thread.sleep(2000);
//			ui = "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().text(\"Delete\"));";
            ui = "new UiSelector().className(\"android.widget.Button\").text(\"Delete\");";
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui)));
            commandsCompleted += "goToCart:";
            System.out.println(commandsCompleted);
//            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui))).click();
//            commandsCompleted += "removeProduct:";
//            System.out.println(commandsCompleted);
//
//            ui = "new UiSelector().textContains(\"was removed from Shopping Cart\");";
//            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator(ui)));
//            commandsCompleted += "productRemoved:";
//            System.out.println(commandsCompleted);
//            /* delete product test measurement stops*/
//
//            commandsCompleted += "P";
            System.out.println(commandsCompleted);
            Thread.sleep(1000);
        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
    }

//	JSON COMMANDS

//	{
//		"commands": [
//		{
//			"cmd": "findElement",
//				"startTime": 1616826739150,
//				"endTime": 1616826740636
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616826740705,
//				"endTime": 1616826743654
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616826743687,
//				"endTime": 1616826743908
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826743931,
//				"endTime": 1616826744945
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826747095,
//				"endTime": 1616826747196
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616826747214,
//				"endTime": 1616826747252
//		},
//		{
//			"cmd": "setValue",
//				"startTime": 1616826747286,
//				"endTime": 1616826748216
//		},
//		{
//			"cmd": "pressKeyCode",
//				"startTime": 1616826748235,
//				"endTime": 1616826749741
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826752825,
//				"endTime": 1616826753214
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616826753243,
//				"endTime": 1616826753365
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826753384,
//				"endTime": 1616826761146
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826761159,
//				"endTime": 1616826761297
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616826761306,
//				"endTime": 1616826762722
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826764368,
//				"endTime": 1616826764663
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616826764675,
//				"endTime": 1616826764778
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826764789,
//				"endTime": 1616826764847
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616826764857,
//				"endTime": 1616826765428
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826765444,
//				"endTime": 1616826765501
//		},
//		{
//			"cmd": "proxyReqRes",
//				"startTime": 1616826765519,
//				"endTime": 1616826765604
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826765632,
//				"endTime": 1616826797339
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616826797355,
//				"endTime": 1616826827987
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826827998,
//				"endTime": 1616826858520
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616826858538,
//				"endTime": 1616826882022
//		},
//		{
//			"cmd": "proxyReqRes",
//				"startTime": 1616826882039,
//				"endTime": 1616826882084
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826882095,
//				"endTime": 1616826882161
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616826882175,
//				"endTime": 1616826882641
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826882666,
//				"endTime": 1616826902915
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616826902933,
//				"endTime": 1616826924876
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826924894,
//				"endTime": 1616826926142
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616826926149,
//				"endTime": 1616826926755
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826926770,
//				"endTime": 1616826926909
//		},
//		{
//			"cmd": "click",
//				"startTime": 1616826926931,
//				"endTime": 1616826928058
//		},
//		{
//			"cmd": "findElement",
//				"startTime": 1616826928074,
//				"endTime": 1616826928787
//		},
//		{
//			"cmd": "elementDisplayed",
//				"startTime": 1616826928804,
//				"endTime": 1616826928896
//		},
//		{
//			"cmd": "getLogEvents",
//				"startTime": 1616826928933,
//				"endTime": 1616826928934
//		}
//  ]
//	}
}