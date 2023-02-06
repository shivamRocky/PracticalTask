package com.cspl.practicaltask.utils;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



public class NetUtils {
    /**
     * CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
     */
    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet
            //Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                System.out.println("wifi");
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                System.out.println("mobile");
                return true;
            }
        }
        System.out.println("no intetnet");
        return false;
    }



}
