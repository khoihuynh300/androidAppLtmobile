package com.example.ltmobile.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ltmobile.R;
import com.google.android.material.textfield.TextInputLayout;

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
                    Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
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