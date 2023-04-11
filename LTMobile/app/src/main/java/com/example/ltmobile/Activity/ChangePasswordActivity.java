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
import com.example.ltmobile.Utils.SharedPrefManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    Context context = this;

    EditText inputOldPassword, inputNewPassword, inputNewPassword2;
    TextInputLayout tilOldPassword, tilNewPassword, tilNewPassword2;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        connectView();
        setViewListeners();
    }
    void connectView(){
        inputOldPassword = (EditText) findViewById(R.id.inputOldPassword);
        inputNewPassword = (EditText) findViewById(R.id.inputNewPassword);
        inputNewPassword2 = (EditText) findViewById(R.id.inputNewPassword2);
        tilOldPassword = (TextInputLayout) findViewById(R.id.textInputOldPassword);
        tilNewPassword = (TextInputLayout) findViewById(R.id.textInputNewPassword);
        tilNewPassword2 = (TextInputLayout) findViewById(R.id.textInputNewPassword2);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
    }
    void setViewListeners(){
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAllInputValid = true;
                isAllInputValid = isInputNotEmpty(inputOldPassword,tilOldPassword);
                isAllInputValid = isInputNotEmpty(inputNewPassword,tilNewPassword);
                isAllInputValid = isInputNotEmpty(inputNewPassword2,tilNewPassword2);

                String oldPassword = inputOldPassword.getText().toString();
                String newPassword = inputNewPassword.getText().toString();
                String newPassword2 = inputNewPassword2.getText().toString();

                if(!newPassword.equals(newPassword2)){
                    tilNewPassword2.setError(context.getResources().getText(R.string.password_not_equal_error));
                    isAllInputValid = false;
                }
                if(isAllInputValid){
                    int id = SharedPrefManager.getInstance(context).getUser().getUserId();
                    ServiceAPI.serviceapi.changePassword(id, newPassword, oldPassword).enqueue(
                            new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    if(response.isSuccessful()){
                                        Toast.makeText(context, response.body().get("message").getAsString(), Toast.LENGTH_LONG).show();
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