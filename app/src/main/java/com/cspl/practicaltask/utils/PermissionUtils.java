package com.cspl.practicaltask.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;


public class PermissionUtils {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS=0;

    public static final int REQUEST_PERMISSION_CAMERA = 1;
    public static final int REQUEST_PERMISSION_LOCATION = 2;
    public static final int REQUEST_WRITE_EXTERNAL = 3;
    public static final int REQUEST_READ_EXTERNAL = 4;


    private PermissionUtils(){}

    public static boolean checkAndRequestPermissions(Activity activity){

        int camera=ContextCompat.checkSelfPermission(activity,Manifest.permission.CAMERA);
        int storage=ContextCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int storage1=ContextCompat.checkSelfPermission(activity,Manifest.permission.READ_EXTERNAL_STORAGE);
        int loc1=ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION);
        int loc2=ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_COARSE_LOCATION);
        int wi=ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_WIFI_STATE);
        int network_state=ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_NETWORK_STATE);
        int cal=ContextCompat.checkSelfPermission(activity,Manifest.permission.CALL_PHONE);

        List<String> listPermissionNeeded=new ArrayList<>();

        if(camera!=PackageManager.PERMISSION_GRANTED){
            listPermissionNeeded.add(Manifest.permission.CAMERA);
        }

        if(storage!=PackageManager.PERMISSION_GRANTED){
            listPermissionNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if(storage1!=PackageManager.PERMISSION_GRANTED){
            listPermissionNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if(loc1!=PackageManager.PERMISSION_GRANTED){
            listPermissionNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if(loc2!=PackageManager.PERMISSION_GRANTED){
            listPermissionNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if(wi!=PackageManager.PERMISSION_GRANTED){
            listPermissionNeeded.add(Manifest.permission.ACCESS_WIFI_STATE);
        }

        if(network_state!=PackageManager.PERMISSION_GRANTED){
            listPermissionNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }

        if(cal!=PackageManager.PERMISSION_GRANTED){
            listPermissionNeeded.add(Manifest.permission.CALL_PHONE);
        }

        if(!listPermissionNeeded.isEmpty()){
            ActivityCompat.requestPermissions(activity,listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;

    }

    /**
     * Requests the Camera permission. If the permission has been denied
     * previously, a SnackBar will prompt the user to grant the permission,
     * otherwise it is requested directly.
     */
    public static void requestCameraPermission(Activity activity) {

        // Here, thisActivity is the current activity
        // System.out.println("requestCameraPermission() INITIAL");
        // Toast.makeText(this, "requestCameraPermission() INITIAL",
        // Toast.LENGTH_LONG).show();
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                // Toast.makeText(activity, "Camera permission is
                // needed for this app to run ",
                // Toast.LENGTH_SHORT).show();
                // System.out.println("requestCameraPermission() SHOW INFO");

                // Show an explanation to the user *asynchronously* -- don't
                // block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                        REQUEST_PERMISSION_CAMERA);

            } else {
                // No explanation need we can reqed,uest the permission.
                // System.out.println("requestCameraPermission() ASK
                // PERMISSION");

                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                        REQUEST_PERMISSION_CAMERA);
            }
            // Permission is granted
        } else {
            System.out.println("requestCameraPermission() PERMISSION ALREADY GRANTED");

        }

    }


    public static void requestWriteExternalPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(activity, "Write permission is needed to create Excel file ", Toast.LENGTH_SHORT).show();
                // Show an explanation to the user *asynchronously* -- don't
                // block this thread waiting for the user's response! After the
                // user sees the explanation, try again to request the
                // permission.
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_EXTERNAL);

                Toast.makeText(activity, "REQUEST LOCATION PERMISSION", Toast.LENGTH_LONG).show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_EXTERNAL);

            }
        }
    }

    public static void requestReadExternalPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(activity, "Write permission is needed to create Excel file ", Toast.LENGTH_SHORT).show();
                // Show an explanation to the user *asynchronously* -- don't
                // block this thread waiting for the user's response! After the
                // user sees the explanation, try again to request the
                // permission.
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_EXTERNAL);

                Toast.makeText(activity, "REQUEST LOCATION PERMISSION", Toast.LENGTH_LONG).show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_EXTERNAL);

            }
        }
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkWriteExternalPermission(String permission,Activity activity)
    {
        int res = activity.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

}