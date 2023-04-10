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

public class LoginActivity extends AppCompatActivity {
    Context context = this;
    //View
    EditText inputEmail, inputPassword;
    TextInputLayout txtinpEmail,txtinpPassword;
    Button btnLogin;
    TextView tvSignup, tvForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(SharedPrefManager.getInstance(context).isLoggedIn()){
            finish();
            startActivity(new Intent(context, MainActivity.class));
        }
        connectView();
        eventHandle();
    }
    void connectView(){
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        txtinpEmail = (TextInputLayout) findViewById(R.id.textInputEmail);
        txtinpPassword = (TextInputLayout) findViewById(R.id.textInputPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvForgetPassword = (TextView) findViewById(R.id.tvForgetPassword);
        tvSignup = (TextView) findViewById(R.id.tvSignup);

        //test
//        txtinpPassword.setError("email error");
//        txtinpPassword.setError("");
    }

    void  eventHandle(){
        // chuyển trang đăng ký
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SignupActivity.class));
            }
        });
        // Đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true; // true là hợp lệ --> đăng nhập

                check = checkInputNull(inputPassword, txtinpPassword) && check;
                check = checkInputNull(inputEmail, txtinpEmail) && check;

                if(check){
                    //đăng nhập
                    ServiceAPI.serviceapi.login(inputEmail.getText().toString().trim(),
                            inputPassword.getText().toString().trim()).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject responseJson = new JSONObject(response.body().toString());
                                    String message = responseJson.getString("message");
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                    if(responseJson.has("user")){
                                        JSONObject userJson = responseJson.getJSONObject("user");
                                        User user = new User(
                                                userJson.getInt("userId"),
                                                userJson.getString("email"),
                                                userJson.getString("fullname"),
                                                userJson.getString("gender"),
                                                userJson.getString("avatar"),
                                                userJson.getString("role"));
                                        SharedPrefManager.getInstance(context).userLogin(user);
                                        startActivity(new Intent(context, MainActivity.class));
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