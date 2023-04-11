package com.example.ltmobile.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ServiceAPI;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {
    Context context = this;
    //view
    EditText inputEmail, inputNewPassword, inputOtp;
    TextInputLayout tilEmail, tilNewPassword, tilOtp;
    Button btnSendOtp, btnConfirm ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        connectView();
        setupViewListeners();
    }

    void connectView(){
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputNewPassword = (EditText) findViewById(R.id.inputNewPassword);
        inputOtp = (EditText) findViewById(R.id.inputOtp);
        tilEmail = (TextInputLayout) findViewById(R.id.textInputEmail);
        tilNewPassword = (TextInputLayout) findViewById(R.id.textInputNewPassword);
        tilOtp = (TextInputLayout) findViewById(R.id.textInputOtp);
        btnSendOtp = (Button) findViewById(R.id.btnSendOtp);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
    }
    void setupViewListeners(){
        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInputNotEmpty(inputEmail, tilEmail)){
                    String email = inputEmail.getText().toString();
                    // gọi api gửi otp
                    ServiceAPI.serviceapi.getOtpForgetPassword(email).enqueue(
                            new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    if(response.isSuccessful()){
                                        try {
                                            JSONObject result = new JSONObject(response.body().toString());
                                            Toast.makeText(context, result.getString("message"), Toast.LENGTH_LONG).show();
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                    );
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAllInputValid = true;
                isAllInputValid = isInputNotEmpty(inputEmail, tilEmail) && isAllInputValid;
                isAllInputValid = isInputNotEmpty(inputNewPassword, tilNewPassword) && isAllInputValid;
                isAllInputValid = isInputNotEmpty(inputOtp, tilOtp) && isAllInputValid;
                if(isAllInputValid){
                    String email = inputEmail.getText().toString();
                    String newpassword = inputNewPassword.getText().toString();
                    int otp = Integer.parseInt(inputOtp.getText().toString());
                    //gọi api đổi lại mật khẩu mới
                    ServiceAPI.serviceapi.verifyForgetPassword(email, newpassword, otp).enqueue(
                            new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    if(response.isSuccessful()){
                                        try {
                                            JSONObject result = new JSONObject(response.body().toString());
                                            Toast.makeText(context, result.getString("message"), Toast.LENGTH_LONG).show();
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                    );

                }
            }
        });
    }

    boolean isInputNotEmpty(EditText editText, TextInputLayout textInputLayout){
        //nếu hợp lệ trả về true, ngược lại false
        String string = editText.getText().toString().trim();
        if(string == null || string.equals("")){
            textInputLayout.setError(context.getResources().getText(R.string.input_null));
            return false;
        }
        else {
            textInputLayout.setError("");
            return true;
        }
    }
}