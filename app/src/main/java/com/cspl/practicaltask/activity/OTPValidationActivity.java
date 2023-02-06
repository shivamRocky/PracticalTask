package com.cspl.practicaltask.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cspl.practicaltask.R;
import com.cspl.practicaltask.databinding.ActivityOtpvalidationBinding;
import com.cspl.practicaltask.utils.NetUtils;

public class OTPValidationActivity extends AppCompatActivity {

    private ActivityOtpvalidationBinding binding;
    String number,otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otpvalidation);

        if (getIntent () != null) {
            number = getIntent ().getStringExtra ("number");
            otp = getIntent().getStringExtra("otp");
            Log.d ("TAG", "OtpVerifyActivity: " + number);
        }
        binding.mobileText.setText ("+91-" + number);

        binding.btnValidateOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetUtils.checkConnection(OTPValidationActivity.this)) {
                    validationOtp();
                } else {
                    Toast.makeText(OTPValidationActivity.this, "Please connect to internet to login",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.toolbarDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void validationOtp() {
        String strOtp = binding.otpPinText.getText().toString().trim();

        if (TextUtils.isEmpty(strOtp) || strOtp.equals("")) {
            Toast.makeText(OTPValidationActivity.this, "Please enter the OTP first", Toast.LENGTH_SHORT).show();
            return;
        }

        if (strOtp.length() == 4) {

            Log.d("TAG", "onClick: " + strOtp);
            if (strOtp.equals(otp)) {
                Intent i = new Intent ( OTPValidationActivity.this, MainActivity.class);
                startActivity (i);


            } else if (!strOtp.equals(otp)) {
                Toast.makeText(OTPValidationActivity.this, "OTP is mismatched. Please enter valid OTP !", Toast.LENGTH_SHORT).show();
            }

        } else {

            Toast.makeText(OTPValidationActivity.this, "Please enter valid OTP !", Toast.LENGTH_SHORT).show();
        }

    }
}