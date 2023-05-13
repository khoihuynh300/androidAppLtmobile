package com.example.ltmobile.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.ltmobile.Model.User;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ServiceAPI;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAddUserActivity extends AppCompatActivity {
    Context context = this;

    //View
    EditText inputEmail, inputFname, inputGender, inputRole, inputPassword, inputPassword2;
    TextInputLayout textInputEmail, textInputFname,textInputGender, textInputRole, textInputPassword, textInputPassword2;
    Button btnAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_user);

        connectView();
        setupEvent();
    }

    private void connectView(){
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputFname = (EditText) findViewById(R.id.inputFname);
        inputGender = (EditText) findViewById(R.id.inputGender);
        inputGender.setFocusable(false);
        inputGender.setCursorVisible(false);
        inputGender.setKeyListener(null);
        inputRole = (EditText) findViewById(R.id.inputRole);
        inputRole.setFocusable(false);
        inputRole.setCursorVisible(false);
        inputRole.setKeyListener(null);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputPassword2 = (EditText) findViewById(R.id.inputPassword2);
        textInputEmail = (TextInputLayout) findViewById(R.id.textInputEmail);
        textInputFname = (TextInputLayout) findViewById(R.id.textInputFname);
        textInputGender = (TextInputLayout) findViewById(R.id.textInputGender);
        textInputRole = (TextInputLayout) findViewById(R.id.textInputRole);
        textInputPassword = (TextInputLayout) findViewById(R.id.textInputPassword);
        textInputPassword2 = (TextInputLayout) findViewById(R.id.textInputPassword2);
        btnAddUser = (Button) findViewById(R.id.btnAddUser);
    }

    private void setupEvent(){
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = inputEmail.getText().toString().trim();
                String Fname = inputFname.getText().toString().trim();
                String Gender = inputGender.getText().toString().trim();
                String Role = inputGender.getText().toString().trim();
                String Password = inputPassword.getText().toString().trim();
                String Password2 = inputPassword2.getText().toString().trim();

                String RoleCode;
                if(Role.equals("Manager")){
                    RoleCode = "manager";
                }
                else if(Role.equals("Tư vấn viên")){
                    RoleCode = "sinhvien";
                }
                else if(Role.equals("Sinh viên")){
                    RoleCode = "tuvanvien";
                }

                boolean check = true;
                check = isInputNotEmpty(inputEmail, textInputEmail) && check;
                check = isInputNotEmpty(inputGender, textInputGender) && check;
                check = isInputNotEmpty(inputRole, textInputRole) && check;
                check = isInputNotEmpty(inputFname, textInputFname) && check;
                check = isInputNotEmpty(inputPassword, textInputPassword) && check;

                if(!Password.equals(Password2)){
                    check = false;
                    inputPassword2.requestFocus();
                    textInputPassword2.setError(context.getResources().getText(R.string.password_not_equal_error));
                }
                else {
                    textInputPassword2.setError("");
                }

                if(check){
                    User user = new User(0, Email, Fname, Gender, null, Role);
                    user.setPassword(Password);
                    ServiceAPI.serviceapi.addUser(user)
                            .enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    if (response.isSuccessful()){
                                        String message = response.body().get("message").getAsString();
                                        Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
//                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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

        inputGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_gender, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menuMale:
                                inputGender.setText("MALE");
                                break;
                            case R.id.menuFemale:
                                inputGender.setText("FEMALE");
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        inputRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_role, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menuManager:
                                inputRole.setText("Manager");
                                break;
                            case R.id.menuTVV:
                                inputRole.setText("Tư vấn viên");
                                break;
                            case R.id.menuSV:
                                inputRole.setText("Sinh viên");
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
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