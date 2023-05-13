package com.example.ltmobile.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class ChangePasswordFragment extends Fragment {

    Context context;

    //View
    EditText inputOldPassword, inputNewPassword, inputNewPassword2;
    TextInputLayout tilOldPassword, tilNewPassword, tilNewPassword2;
    Button btnConfirm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        connectView(view);
        setViewListeners();

        return view;
    }

    void connectView(View view){
        inputOldPassword = (EditText) view.findViewById(R.id.inputOldPassword);
        inputNewPassword = (EditText) view.findViewById(R.id.inputNewPassword);
        inputNewPassword2 = (EditText) view.findViewById(R.id.inputNewPassword2);
        tilOldPassword = (TextInputLayout) view.findViewById(R.id.textInputOldPassword);
        tilNewPassword = (TextInputLayout) view.findViewById(R.id.textInputNewPassword);
        tilNewPassword2 = (TextInputLayout) view.findViewById(R.id.textInputNewPassword2);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }
}