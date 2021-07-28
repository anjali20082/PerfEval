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
public class FacebookTestsP_Search {
    AndroidDriver<MobileElement> driver;
    String appName = "Facebook_Search";
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
//		Package package = new Package("com.facebook.katana");
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
    public void searchPerson() throws InterruptedException{

        testName = "search person";
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
//                wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("new UiSelector().descriptionContains(\"(?i)Search(?-i)\")"))).click();
//            else
                wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Search Facebook"))).click();
            commands += "search:";
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).click();
            commands += "clickTextField:";
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("Kangana Ranaut");
            commands += "enterName:";
            ((AndroidDriver<?>) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
            commands += "pressEnter:";

            /* Search person time measurement starts */
//			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Kangana Ranaut Page · Artist · Actor · KanganaRanaut · 2M like this"))).click();
            if(versionId == 1){
                wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("People"))).click();
                commands += "clickPeople:";
                wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Kangana Ranaut Page"))).click();
                commands += "openPersonPage:";
                wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Profile picture")));
                commands += "profilePicture:";

            }
            else if (versionId <= 3) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[3]/android.view.ViewGroup"))).click();

//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup"))).click();
                commands += "openPersonPage:";

                if (versionId == 2) {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Profile photo")));
                }
                else {
                    wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Profile picture")));
                }
                commands += "profilePicture:";
            }

            commands += "P";

//            else {
//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup/androidx.recyclerview.widget.RecyclerView/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]"))).click();
//                commands += "openPersonPage:";
//            }
//			wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("	new UiSelector().descriptionContains(\"Kangana Ranaut Page\")"))).click();
            //wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textMatches(\"(?i)About(?-i)\")"))).click();
            /* Search person time measurement stops */

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