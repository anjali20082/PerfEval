package iiitd.nrl.evalapp;



import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
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
    String appName = "Facebook";
    String testName = "NA";
    String testStatusReason = "NA";
    int versionId;
    String commands = "";

    @BeforeClass
    public void setUp() throws IOException, InterruptedException {
		versionId = MyDatabase.getVersionSelected();
        System.out.println("APP: " + appName + " Version ID: " + versionId);
    }

    @BeforeMethod
    public void launchCap() {
//		driver = MainLauncher.driver;
//		driver.startActivity(new Activity("com.facebook.katana","com.facebook.katana.activity.FbMainTabActivity"));
//        Activity activity = new Activity("com.facebook.katana","com.facebook.katana.activity.FbMainTabActivity");
//        activity.setAppWaitActivity("com.facebook.katana.activity.FbMainTabActivity");
//        driver.startActivity(activity);
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
            //MyDatabase.addTestResult(appName, testName, null, "NA" , false, "App Not Installed");
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
        System.out.println(commands);

        MyDatabase.setCurrentApp(appName);
        MyDatabase.setCommands(commands);
        MyDatabase.setAppJsonCommands(jsonString);
        MyDatabase.setTestStatus(testResult.isSuccess());
        MyDatabase.setTestStatusReason(testStatusReason);
        MyDatabase.setConnType(getConnectionType());

//        driver.quit();
    }

    @Test
    public void postGroup() throws InterruptedException{

        testName = "post in a group";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Username"))).sendKeys("iiitdevalapp@gmail.com");
            commands += "enterEmail:";
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Password"))).sendKeys("nrl_evalapp");
            commands += "enterPassword:";
            if(versionId == 1) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Login"))).click();
                commands += "login:";
            }
            else {
                wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Log In"))).click();
                commands += "login:";
            }

            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textMatches(\"(?i)Not Now(?-i)\")"))).click();
            commands += "notNow:";

            if(versionId == 1) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textMatches(\"(?i)DENY(?-i)\")"))).click();
                commands += "deny:";
            }
            else {
                wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Deny"))).click();
                commands += "deny:";
            }

//            if (versionId == 1)
//                wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("new UiSelector().textContains(\"(?i)Search(?-i)\")"))).click();
//            else
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Search Facebook"))).click();
            commands += "search:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).click();
            commands += "clickTextField:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("Evaluation of Apps");
            commands += "enterName:";

            ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
            commands += "pressEnter:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"Evaluation of Apps\");"))).click();
            commands += "openGroupPage:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiScrollable(" + "new UiSelector().scrollable(true)).scrollToBeginning(20);")));
            commands += "scrollAbove:";

            if(versionId == 1) {
                TouchAction touchAction = new TouchAction(driver);
                touchAction.tap(PointOption.point(270, 859)).perform();
                commands += "clickTextField:";
            }
            else {
                wait.until(ExpectedConditions.or(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Create a post…")), ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Write something…"))));
                commands += "clickTextField:";

                if (!driver.findElements(MobileBy.AccessibilityId("Create a post…")).isEmpty()) {
                    driver.findElement(MobileBy.AccessibilityId("Create a post…")).click();
                    commands += "clickCreateAPost:";
                } else {
                    driver.findElement(MobileBy.AccessibilityId("Write something…")).click();
                    commands += "clickWriteSomething:";
                }
            }

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).click();
            commands += "clickEditText:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("Hi, this is an automated post");
            commands += "enterMessage:";
            /* post group time measurement starts */
            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("POST"))).click();
            commands += "post:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Post Menu")));
            commands += "postMenu:";
            commands += "P";

            /* post group time measurement stops */
        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }
    }
}
//V240
//--group--
//acc-id:= Menu, Tab 4 of 4
//acc-id Groups
//acc-id Your Groups
//acc-id Evaluation of Apps Button
//coords  270,859
//text  What's on your mind?
//acc-id POST
//acc-id Post Menu
//
//--search profile--
//acc-id Search Facebook
//text Search  send text
//acc-id People
//acc-id Kangana Ranaut Page
//acc-id Profile picture


//acc-id  Username
//accid   Password
//acc-id  Login