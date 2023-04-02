package com.example.ltmobile.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ltmobile.Model.User;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ServiceAPI;
import com.example.ltmobile.Utils.SharedPrefManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    Context context = this;
    //View
    EditText inputEmail, inputFname, inputPassword, inputPassword2;
    TextInputLayout textInputEmail, textInputFname, textInputPassword, textInputPassword2;
    Button btnSignup;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        connectView();
        eventHandle();
    }

    void connectView(){
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputFname = (EditText) findViewById(R.id.inputFname);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputPassword2 = (EditText) findViewById(R.id.inputPassword2);
        textInputEmail = (TextInputLayout) findViewById(R.id.textInputEmail);
        textInputFname = (TextInputLayout) findViewById(R.id.textInputFname);
        textInputPassword = (TextInputLayout) findViewById(R.id.textInputPassword);
        textInputPassword2 = (TextInputLayout) findViewById(R.id.textInputPassword2);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
    }
    void eventHandle(){
        //trở về đăng nhập
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = inputEmail.getText().toString().trim();
                String Fname = inputFname.getText().toString().trim();
                String Password = inputPassword.getText().toString().trim();
                String Password2 = inputPassword2.getText().toString().trim();

                boolean check = true;
                check = checkInputNull(inputEmail, textInputEmail) && check;
                check = checkInputNull(inputFname, textInputFname) && check;
                check = checkInputNull(inputPassword, textInputPassword) && check;

                if(!Password.equals(Password2)){
                    check = false;
                    inputPassword2.requestFocus();
                    textInputPassword2.setError(context.getResources().getText(R.string.password_not_equal_error));
                }
                else {
                    textInputPassword2.setError("");
                }

                if(check){
                    ServiceAPI.serviceapi.signup(Email, Fname, Password).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject responseJson = new JSONObject(response.body().toString());
                                    String message = responseJson.getString("message");
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();

                                    // reset text

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

            }
        });
    }

    boolean checkInputNull(EditText editText, TextInputLayout textInputLayout){
        //nếu hợp lệ trả về true, ngược lại false
        String string = editText.getText().toString().trim();
        if(string == null || string.equals("")){
            textInputLayout.setError(context.getResources().getText(R.string.input_null));
            editText.requestFocus();
            return false;
        }
        else {
            textInputLayout.setError("");
            return true;
        }
    }
}