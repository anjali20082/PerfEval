package iiitd.nrl.evalapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GetLocation {

    public static String getlocation() throws IOException, IOException {
//     location testing code

        String cmd = "adb shell am broadcast -a io.appium.settings.location -n io.appium.settings/.receivers.LocationInfoReceiver";
        ProcessBuilder processBuilder = new ProcessBuilder();
//      if (Config.osName.contains("Windows"))
        processBuilder.command("cmd.exe", "/c", cmd);
//      else
//      processBuilder.command("bash", "-c", cmd);

        Process p = processBuilder.start();
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

// Read the output from the command
//        System.out.println("Here is the standard output of the command:\n");
        String s = null;
        StringBuilder st = new StringBuilder();
        while ((s = stdInput.readLine()) != null) {
            st.append(s);
            System.out.println(s.trim());
        }

//            The first value in the returned data string is the current latitude, the second is the longitude
//            and the last one is the altitude. An empty string is returned if the data cannot be retrieved
//            (more details on the failure cause can be found in the logcat output).

            String st1 = (st.toString());
            String latitude = st1.split(" ")[9];
            latitude = latitude.substring(6, latitude.length());
            String longitude = st1.split(" ")[10];
            String altitude  = st1.split(" ")[11];
            altitude = altitude.substring(0, altitude.length() -1);
            System.out.println("Latitide : "+ latitude + " Longitude : " + longitude
            + " Altitude : "+ altitude);
//            String rx_bg = (st1.split(" ")[5]);
//            String tx_bg = (st1.split(" ")[7]);
//            String rx_fg = (st1.split(" ")[25]);
//            String tx_fg = (st1.split(" ")[27]);
//            ArrayList<Integer> data = new ArrayList<Integer>();
//            data.add(Integer.valueOf(rx_bg) +Integer.valueOf(rx_fg));
//            data.add(Integer.valueOf(tx_bg) +Integer.valueOf(tx_fg));
//        data.add(rx_fg);
//        data.add(tx_fg);
// Read any errors from the attempted command

        while ((s = stdError.readLine()) != null) {
            System.out.println("Here is the standard error of the command (if any):\n");
            System.out.println(s.trim());
        }
        return latitude+ " " +longitude+ " "+altitude;
    }
}
