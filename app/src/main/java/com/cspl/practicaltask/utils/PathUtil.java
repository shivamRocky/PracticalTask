package com.cspl.practicaltask.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.net.URISyntaxException;

/**
 * Created by Aki on 1/7/2017.
 */

public class PathUtil {
    public static final int IMAGE_HEIGHT = 658;
    public static final int IMAGE_WIDTH = 842;
    /*
     * Gets the file path of the given Uri.
     */

    @SuppressLint("NewApi")
    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{split[1]};
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {

        Bitmap output = null;
        try {
            output = Bitmap.createBitmap (wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas (output);
            Matrix m = new Matrix ();
            m.setScale ((float) wantedWidth / bitmap.getWidth (), (float) wantedHeight / bitmap.getHeight ());
            canvas.drawBitmap (bitmap, m, new Paint());
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return output;
    }

    public static void hideKeyboard(Activity activity) {
        try {

            InputMethodManager imm = (InputMethodManager) activity.getSystemService (Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus ();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View (activity);
            }
            imm.hideSoftInputFromWindow (view.getWindowToken (), 0);


        } catch (Exception e) {
            e.printStackTrace ();
        }
    }
}
