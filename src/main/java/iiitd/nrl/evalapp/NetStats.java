package iiitd.nrl.evalapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NetStats {

    public static ArrayList<Integer> getstats(String uid) throws IOException {

        //String cmd = "adb shell ifconfig wlan0|grep \"RX bytes\" >F:\\appsData.txt ";
        String cmd = "adb shell cat /proc/net/xt_qtaguid/stats|grep "+ uid;
        ProcessBuilder processBuilder = new ProcessBuilder();
//      if (Config.osName.contains("Windows"))
        processBuilder.command("cmd.exe", "/c", cmd);
//      else
//      processBuilder.command("bash", "-c", cmd);

        Process p = processBuilder.start();
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

// Read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        String s = null;
        StringBuilder st= new StringBuilder() ;
        while ((s = stdInput.readLine()) != null) {
            st.append(s);
//            System.out.println(s.trim());
        }
        String st1 = (st.toString());
        String rx_bg = (st1.split(" ")[5]);
        String tx_bg = (st1.split(" ")[7]);
        String rx_fg = (st1.split(" ")[25]);
        String tx_fg = (st1.split(" ")[27]);
        ArrayList<Integer> data = new ArrayList<Integer>();
        data.add(Integer.valueOf(rx_bg) +Integer.valueOf(rx_fg));
        data.add(Integer.valueOf(tx_bg) +Integer.valueOf(tx_fg));
//        data.add(rx_fg);
//        data.add(tx_fg);
// Read any errors from the attempted command

        while ((s = stdError.readLine()) != null) {
            System.out.println("Here is the standard error of the command (if any):\n");
            System.out.println(s.trim());
        }

        return data;

    }



}
