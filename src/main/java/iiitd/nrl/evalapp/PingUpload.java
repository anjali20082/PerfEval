package iiitd.nrl.evalapp;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PingUpload {

    @BeforeMethod
    public void launchCap() {
        MyDatabase.sendPINGLog();
    }

    @Test
    public void dummyTest() throws InterruptedException {
        System.out.println("Uploading Ping Data :)");
    }
}
