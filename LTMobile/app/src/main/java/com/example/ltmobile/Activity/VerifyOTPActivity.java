package com.example.ltmobile.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ServiceAPI;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOTPActivity extends AppCompatActivity {
    Context context = this;
    //view
    EditText inputOtp;
    TextInputLayout textInputOtp;

    Button btnResend, btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otpactivity);

        connectView();

        String action = getIntent().getStringExtra("action");
        if(action.equals("signup")){
            String Email = getIntent().getStringExtra(SignupActivity.KEY_EMAIL);
            String Fname = getIntent().getStringExtra(SignupActivity.KEY_FNAME);
            String Gender = getIntent().getStringExtra(SignupActivity.KEY_GENDER);
            String Password = getIntent().getStringExtra(SignupActivity.KEY_PASSWORD);

            Toast.makeText(context, Email, Toast.LENGTH_LONG).show();
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int otp = Integer.parseInt(inputOtp.getText().toString().trim());
                    ServiceAPI.serviceapi.verifySignup(Email, Fname, Gender, Password, otp).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject responseJson = new JSONObject(response.body().toString());
                                    String message = responseJson.getString("message");
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                    if(!responseJson.getBoolean("error")){

                                        // trở về đăng nhập
                                        startActivity(new Intent(context, LoginActivity.class));
                                    }


                                } catch (JSONException e) {
                                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.e("TAG", t.toString());
                            Toast.makeText(context, "failed connect API", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    }
    void  connectView(){
        inputOtp = (EditText) findViewById(R.id.inputOtp);
        textInputOtp = (TextInputLayout) findViewById(R.id.textInputOtp);
        btnResend = (Button) findViewById(R.id.btnResend);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
    }
}