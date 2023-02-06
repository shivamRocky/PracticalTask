package com.cspl.practicaltask.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cspl.practicaltask.R;
import com.cspl.practicaltask.databinding.ActivityMainBinding;
import com.cspl.practicaltask.utils.ImageHelper;
import com.cspl.practicaltask.utils.NetUtils;
import com.cspl.practicaltask.utils.PathUtil;
import com.cspl.practicaltask.utils.PermissionUtils;
import com.cspl.practicaltask.utils.WebAPI;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    File folder;
    String PATH;
    Uri picUri1;
    String ImagePath1;
    File file1;
    Bitmap bp = null;
    public int RESULT_CODE = -1;
    private String fileName1 = "", encodedStr1 = "";
    private RequestQueue volleyQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        volleyQueue = Volley.newRequestQueue(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionUtils.checkAndRequestPermissions(MainActivity.this);
        }
        folder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/practical task");
        if (!folder.exists()) {
            folder.mkdir();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
        String imageDate = sdf.format(new Date());

        PATH = folder.getAbsolutePath() + File.separator + imageDate ;

        binding.btnChoosefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });
        binding.toolbarDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (NetUtils.checkConnection(MainActivity.this)) {
                    validation();
                } else {
                    Toast.makeText(MainActivity.this, "Please connect to internet to login",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void validation() {
        String firstName = binding.editFirstName.getText().toString();
        String lastname = binding.editLastName.getText().toString();
        String phone_number = binding.editphone.getText().toString();

        if (TextUtils.isEmpty(firstName)) {
            binding.editFirstName.setError("Enter first Name");
            binding.editFirstName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(lastname)) {
            binding.editLastName.setError("Enter Last Name");
            binding.editLastName.requestFocus();
            return;
        }


        //checking if phoneNumber is empty
        if (TextUtils.isEmpty(phone_number)) {
            binding.editphone.setError("Enter Mobile Number");
            binding.editphone.requestFocus();

            return;
        }

        if (binding.editphone.length() < 10) {
            binding.editphone.setError("Enter a valid phone number");
            return;
        }
        create(firstName,lastname,phone_number);

    }

    private void chooseFile() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = getOutputMediaFile("_1");
                    if (file != null) {
                        ImagePath1 = file.getAbsolutePath();
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            picUri1 = Uri.fromFile(file); // create
                        } else {
                            picUri1 = FileProvider.getUriForFile(getApplicationContext()
                                    , getApplicationContext().getPackageName() + ".provider", file);
                        }
                        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        i.putExtra(MediaStore.EXTRA_OUTPUT, picUri1); // set the image file
                        startActivityForResult(i, 1);
                        RESULT_CODE = 1;
                        Log.d("requestCode:", ":" + 1);
                    } else {
                        Toast.makeText(MainActivity.this, "Could not create file", Toast.LENGTH_SHORT).show();
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    file1 = new File(PATH + "_GL_1.jpg");
                    ImagePath1 = file1.getAbsolutePath();
                    RESULT_CODE = 11;
                    startActivityForResult(photoPickerIntent, 11);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 11:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectImage = data.getData();
                    try {
                        File mFile = new File(ImagePath1);
                        file1 = mFile;
                        ImagePath1 = file1.getAbsolutePath();
                        InputStream inputStream = getContentResolver().openInputStream(selectImage);
                        Bitmap bitmap11 = BitmapFactory.decodeStream(inputStream);
                        FileOutputStream out11 = new FileOutputStream(ImagePath1);
                        try {
                            String filePath = PathUtil.getPath(this, selectImage);

                            bitmap11 = ImageHelper.compressImageFromURI(filePath, this);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("error:", "URISyntaxException");
                            bitmap11 = PathUtil.scaleBitmap(bitmap11, PathUtil.IMAGE_WIDTH, PathUtil.IMAGE_HEIGHT);
                        }
                        bitmap11.compress(Bitmap.CompressFormat.JPEG, 90, out11);
                        Picasso.get().load(file1).resize(150, 150).memoryPolicy(MemoryPolicy.NO_CACHE).
                                centerCrop().into(binding.profileImage);
                        System.out.println("Image is browsed from gallary");
                        bp = bitmap11;
                        encodedMethodImg1(ImagePath1);
                    } catch (FileNotFoundException e1) {
                        // TODO Auto-generated catch block
                        Toast.makeText(MainActivity.this,
                                "Please take Pic in landscape mode", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "You have Canceled the image from gallery ", Toast.LENGTH_SHORT).show();
                }
                break;

            case 1:
                if (resultCode==Activity.RESULT_OK) {

                    try {
                        String path1 = ImagePath1;
                        file1 = new File(path1);
                        Log.d("TAG", "onActivityResultIP1: " + ImagePath1);
                        Log.d("TAG", "onActivityResultP1: " + path1);
                        scanFile(path1);
                        Bitmap bitmap;
                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                        bitmap = BitmapFactory.decodeFile(file1.getAbsolutePath(),
                                bitmapOptions);
                        Log.d("TAG", "onActivityResultBi: " + bitmap);
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(path1);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "File Not Found Exception", Toast.LENGTH_SHORT).show();
                        }
                        bitmap = ImageHelper.compressImage(bitmap, bitmapOptions, path1);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out);
                        binding.profileImage.setImageBitmap(bitmap);
                        BitmapFactory.Options bitmapOptions111 = new BitmapFactory.Options();
                        Bitmap bitmap111;
                        bitmap111 = BitmapFactory.decodeFile(file1.getAbsolutePath(),
                                bitmapOptions111);
                        Log.d("TAG", "ResultSignImgPath: +" + file1.getAbsolutePath());
                        bitmap111 = Shrinkmethod(file1.getAbsolutePath(), 500, 500);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap111.compress(Bitmap.CompressFormat.PNG, 90, stream);
                        byte[] byte_arr = stream.toByteArray();
                        // Encode Image to String
                        encodedStr1 = Base64.encodeToString(byte_arr, 0);
                        Log.d("TAG", "onActEncodedStr1 " + encodedStr1);
                        Log.d("ImageUplaodInstall", "path:" + file1.getAbsolutePath());
                        fileName1 = file1.getAbsolutePath().substring(file1.getAbsolutePath().lastIndexOf("/") + 1);
                        Log.d("TAG", "onActFileName1: " + fileName1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void scanFile(String file) {

        MediaScannerConnection.scanFile(MainActivity.this,
                new String[]{file}, null,
                (path, uri) -> {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                });

    }

    private Bitmap Shrinkmethod(String file, int width, int height) {
        BitmapFactory.Options bitopt = new BitmapFactory.Options();
        bitopt.inJustDecodeBounds = true;
        Bitmap bit = BitmapFactory.decodeFile(file, bitopt);
        int h = (int) Math.ceil(bitopt.outHeight / (float) height);
        int w = (int) Math.ceil(bitopt.outWidth / (float) width);
        if (h > 1 || w > 1) {
            if (h > w) {
                bitopt.inSampleSize = h;
            } else {
                bitopt.inSampleSize = w;
            }
        }
        bitopt.inJustDecodeBounds = false;
        bit = BitmapFactory.decodeFile(file, bitopt);
        return bit;
    }

    private void encodedMethodImg1(String path) {
        if (path != null) {
            String[] fileNameSegments = path.split("/");
            fileName1 = fileNameSegments[fileNameSegments.length - 1];

            if (new File(path).exists()) {
                Bitmap bitmap = Shrinkmethod(path, 500, 500);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedStr1 = Base64.encodeToString(byte_arr, 0);
                Log.d("image_check", "EncodeGal1 :" + encodedStr1);
                Log.d("TAG", "galFileName1 : " + fileName1);
            } else {
                encodedStr1 = "";
                Log.d("image_check", " null SendImageToencodedProdImg: " + encodedStr1);
            }
        } else {
            fileName1 = "";
            encodedStr1 = "";
        }
    }

    private File getOutputMediaFile(String fileName) {

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getAbsolutePath(), "/practical Images");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + fileName + ".jpg");
        return mediaFile;

    }

    private void create(String firstName, String lastname, String phone_number){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("first_name", firstName);
            jsonObject.put("last_name", lastname);
            jsonObject.put("phone", phone_number);
            jsonObject.put("fileName", fileName1);
            jsonObject.put("encodedString", encodedStr1);

            sendDataToServer(jsonObject);

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void sendDataToServer(JSONObject jsonObject) {

        if (NetUtils.checkConnection(MainActivity.this)) {
            //pd.show();
            Log.d("TAG", "Api URL=>" + WebAPI.BASE_URL + jsonObject);
            try {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, WebAPI.BASE_URL, response -> {
                    Log.d("Response", response);

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObjectResponse = jsonArray.getJSONObject(0);


                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }, error -> {
                    Log.d("DEMO20:", "EROOR: requestType:" + " ,Tesponse =>ERROR----" + error);
                    error.printStackTrace();

                }) {
                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        if (response.headers == null) {
                            // cant just set a new empty map because the member is final.
                            response = new NetworkResponse(
                                    response.statusCode,
                                    response.data,
                                    Collections.emptyMap(), // this is the important line, set an empty but non-null map.
                                    response.notModified,
                                    response.networkTimeMs);
                        }
                        return super.parseNetworkResponse(response);
                    }

                    @Override
                    protected Map<String, String> getParams() {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("", jsonObject.toString());
                        return map;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        (1000 * 60) * 5,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                volleyQueue.add(stringRequest);
                volleyQueue.getCache().clear();
            } catch (Exception e) {
                e.printStackTrace();

            }
        } else {
            Toast.makeText(MainActivity.this, "Please Connect to internet", Toast.LENGTH_SHORT).show();
            //finish();
        }
    }

}