package com.cspl.practicaltask.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.cspl.practicaltask.R;
import com.cspl.practicaltask.databinding.ActivityLoginBinding;
import com.cspl.practicaltask.utils.NetUtils;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        binding.btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetUtils.checkConnection(LoginActivity.this)) {
                    validationPhoneNumber();
                } else {
                    Toast.makeText(LoginActivity.this, "Please connect to internet to login",
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

    private void validationPhoneNumber() {

        String phone_number = binding.userPhoneText.getText().toString();
        //checking if phoneNumber is empty
        if (TextUtils.isEmpty(phone_number)) {
            binding.userPhoneText.setError("Enter Mobile Number");
            binding.userPhoneText.requestFocus();

            return;
        }

        if (binding.userPhoneText.length() < 10) {
            binding.userPhoneText.setError("Enter a valid phone number");
            return;
        }


        login(phone_number);
    }

    private void login(String phone_number) {

        String number = phone_number;

        StringBuilder stringBuilder = new StringBuilder(number);
        StringBuilder b = stringBuilder.delete(2,8);

        String otp = b.toString();

        customDialogOtp(phone_number,otp);
    }

    private void customDialogOtp(String phone_number, String otp) {
        dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.custom_layout_otp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        PinView pinView = dialog.findViewById(R.id.otp_pin_text);
        ImageView imageView = dialog.findViewById(R.id.cancel_popup);

        imageView.setOnClickListener(v -> dialog.dismiss());

        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strOtp = editable.toString().trim();

                if (editable.length() ==4){

                    if (strOtp.equalsIgnoreCase(otp)) {

                        Intent i = new Intent (LoginActivity.this, OTPValidationActivity.class);
                        i.putExtra ("number", phone_number);
                        i.putExtra("otp",otp);
                        startActivity (i);
                        binding.userPhoneText.setText("");
                        dialog.dismiss();

                    } else if (!strOtp.equalsIgnoreCase(otp)) {

                        Toast.makeText(LoginActivity.this, "OTP is mismatched. Please enter valid OTP !", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }


}