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

import io.appium.java_client.android.Activity;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.ScopeMetadata;
import org.testng.ITestResult;
import org.testng.annotations.*;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

@SuppressWarnings("unchecked")
public class TelegramTestsP {
    AndroidDriver<MobileElement> driver;
    String appName = "Telegram";
    String testName = "NA";
    String testStatusReason = "NA";
    long start_time, end_time;
    int versionId;
    String commands = "";

    @BeforeClass
    public void setUp() throws IOException, InterruptedException {
        versionId = MyDatabase.getVersionSelected();
        System.out.println("APP: " + appName + " Version ID: " + versionId);
    }

    @BeforeMethod
    public void launchCap() {

//        driver = MainLauncher.driver;
//
//        Activity activity = new Activity("org.telegram.messenger","org.telegram.ui.LaunchActivity");
//        activity.setAppWaitActivity("org.telegram.ui.IntroActivity");
//        driver.startActivity(activity);
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
    public void sendMessage() throws Exception {
        testName = "send message";
        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
        Random rand = new Random();
        int rand_int = rand.nextInt(1000);
        String rand_str = Integer.toString(rand_int);
        String message = "Hi, this is an automated text:" + rand_str;
        String verifiy_sent = message + "\nSent";

        try {

            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"Start Messaging\");")),
                    ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"OK\");"))));

            if (!driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().text(\"Start Messaging\");")).isEmpty()) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"Start Messaging\");"))).click();
                commands += "startMessaging:";

                wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().className(\"android.widget.EditText\");")));
                List<MobileElement> phone = driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().className(\"android.widget.EditText\");"));

//                phone.get(0).sendKeys("91");
                phone.get(1).sendKeys("8802647803");
                commands += "enterMobile:";

                wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().description(\"Done\");"))).click();
                commands += "done:";
//                wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"OK\");"))).click();

                ////////////////////
//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.android.packageinstaller:id/permission_deny_button"))).click();

            } else {
                wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"OK\");"))).click();
                commands += "OK:";
            }

            if( MainLauncher.osId == 8) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"NOT NOW\");"))).click();
                commands += "notNow:";

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.android.packageinstaller:id/permission_deny_button"))).click();
                commands += "deny:";
            }

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.ImageButton[@content-desc=\"Search\"]/android.widget.ImageView"))).click();
            commands += "search:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("Abhivandan");
            commands += "enterName:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().textContains(\"Abhivandan IIITD\");"))).click();
            commands += "clickResult:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys(message);
            commands += "enterMessage:";
//            start_time =System.currentTimeMillis();

            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Send"))).click();
            commands += "send:";

            wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"" + verifiy_sent + "\");")));
            commands += "checkSent:";
            commands += "P";
//            end_time = System.currentTimeMillis();








//            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"START MESSAGING\");"))).click();
//            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout[1]/android.widget.ScrollView/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.EditText[1]"))).sendKeys("91");
//            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout[1]/android.widget.ScrollView/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.EditText[2]"))).sendKeys("8802647803");
//            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout[1]/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.ImageView"))).click();

//            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Search"))).click();
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("Saved Messages");

            //version 1st
//            if (versionId == 1)
//                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout[2]/org.telegram.messenger.support.widget.RecyclerView/android.view.View[1]"))).click();

//            List<MobileElement> results= (List<MobileElement>) driver.findElementsByClassName("android.view.View");
            //version latest
//            List<MobileElement> results= (List<MobileElement>) driver.findElementsByClassName("android.view.ViewGroup");

            // version 5.11
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout[1]/android.widget.FrameLayout[2]/android.view.View/android.view.View[2]"))).click();

            // version 5.13
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout[1]/android.widget.FrameLayout[2]/android.view.View/android.view.View[2]"))).click();

            // version 7.0
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout[1]/android.widget.FrameLayout/android.widget.FrameLayout[2]/android.view.View/android.view.View[2]"))).click();

            // version 7.3
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout[1]/android.widget.FrameLayout/android.widget.FrameLayout[2]/android.view.View/android.view.View[2]"))).click();

//            results.get(0).click();

//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys(message);
//
//            // calculate time of below 2 commands
//            int before_length = driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"" + verifiy_sent + "\");")).size();
//            System.out.println(before_length);
//
//            /* sending message time measurement starts */
//            if (versionId == 1)
//                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.FrameLayout[2]/android.widget.ImageView"))).click();
//            else
//                wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Send"))).click();
//
//            Calendar calendar = Calendar.getInstance();
//            long startTime = calendar.getTimeInMillis();
//            long currentTime = startTime;
//            long limitTime = startTime + MyDatabase.testTimeLimit * 1000;
//
//            int after_length = driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"" + verifiy_sent + "\");")).size();
//            while (currentTime < limitTime && after_length <= before_length) {
////                MobileElement element = driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"" + message + "\");"));
////                System.out.println(element.getAttribute("content-desc"));
////                if (element.getAttribute("content-desc").contains("Sent"))
////                    break;
//                after_length = driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"" + verifiy_sent + "\");")).size();
//                System.out.println(currentTime + " " + after_length);
//                calendar = Calendar.getInstance();
//                currentTime = calendar.getTimeInMillis();
//            }
//
//            if (currentTime >= limitTime) {
//                throw new Exception("Message not sent");
//            }

            /* sending message time measurement stops */

        } catch (Exception e) {
            testStatusReason = e.toString();
            throw e;
        }

//        and we meet here as well Sent at 4:59 PM, Seen
    }
}
//
//    @Test
//    public void sendMessage() throws Exception {
//        testName = "send message";
//        WebDriverWait wait = new WebDriverWait(driver, MyDatabase.testTimeLimit);
//        Random rand = new Random();
//        int rand_int = rand.nextInt(1000);
//        String rand_str = Integer.toString(rand_int);
//        String message = "Hi, this is an automated text:" + rand_str;
//        String verifiy_sent = message + "\nSent";
//
//        try {
//
//            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AndroidUIAutomator("new UiSelector().text(\"START MESSAGING\");"))).click();
//            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout[1]/android.widget.ScrollView/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.EditText[1]"))).sendKeys("91");
//            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout[1]/android.widget.ScrollView/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.EditText[2]"))).sendKeys("8802647803");
//            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout[1]/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.ImageView"))).click();
//
////            wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Search"))).click();
////            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys("Saved Messages");
//
//            //version 1st
//            if (versionId == 1)
//                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout[2]/org.telegram.messenger.support.widget.RecyclerView/android.view.View[1]"))).click();
//
////            List<MobileElement> results= (List<MobileElement>) driver.findElementsByClassName("android.view.View");
//            //version latest
////            List<MobileElement> results= (List<MobileElement>) driver.findElementsByClassName("android.view.ViewGroup");
//
//            // version 5.11
////            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout[1]/android.widget.FrameLayout[2]/android.view.View/android.view.View[2]"))).click();
//
//            // version 5.13
////            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout[1]/android.widget.FrameLayout[2]/android.view.View/android.view.View[2]"))).click();
//
//            // version 7.0
////            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout[1]/android.widget.FrameLayout/android.widget.FrameLayout[2]/android.view.View/android.view.View[2]"))).click();
//
//            // version 7.3
////            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout[1]/android.widget.FrameLayout/android.widget.FrameLayout[2]/android.view.View/android.view.View[2]"))).click();
//
////            results.get(0).click();
//
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("android.widget.EditText"))).sendKeys(message);
//
//            // calculate time of below 2 commands
//            int before_length = driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"" + verifiy_sent + "\");")).size();
//            System.out.println(before_length);
//
//            /* sending message time measurement starts */
//            if (versionId == 1)
//                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout[2]/android.widget.LinearLayout/android.widget.FrameLayout[2]/android.widget.ImageView"))).click();
//            else
//                wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Send"))).click();
//
//            Calendar calendar = Calendar.getInstance();
//            long startTime = calendar.getTimeInMillis();
//            long currentTime = startTime;
//            long limitTime = startTime + MyDatabase.testTimeLimit * 1000;
//
//            int after_length = driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"" + verifiy_sent + "\");")).size();
//            while (currentTime < limitTime && after_length <= before_length) {
////                MobileElement element = driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"" + message + "\");"));
////                System.out.println(element.getAttribute("content-desc"));
////                if (element.getAttribute("content-desc").contains("Sent"))
////                    break;
//                after_length = driver.findElements(MobileBy.AndroidUIAutomator("new UiSelector().descriptionContains(\"" + verifiy_sent + "\");")).size();
//                System.out.println(currentTime + " " + after_length);
//                calendar = Calendar.getInstance();
//                currentTime = calendar.getTimeInMillis();
//            }
//
//            if (currentTime >= limitTime) {
//                throw new Exception("Message not sent");
//            }
//
//            /* sending message time measurement stops */
//
//        } catch (Exception e) {
//            testStatusReason = e.toString();
//            throw e;
//        }
//
////        and we meet here as well Sent at 4:59 PM, Seen
//    }
//Hi, this is an automated text:834 Sent at 1:06 PM, Seen