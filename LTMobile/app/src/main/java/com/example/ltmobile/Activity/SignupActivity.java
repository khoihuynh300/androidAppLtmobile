package com.example.ltmobile.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ltmobile.R;
import com.google.android.material.textfield.TextInputLayout;

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
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}