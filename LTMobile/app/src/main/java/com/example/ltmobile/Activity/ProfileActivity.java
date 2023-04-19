package com.example.ltmobile.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ltmobile.Model.User;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ImagePicker;
import com.example.ltmobile.Utils.RealPathUtil;
import com.example.ltmobile.Utils.ServiceAPI;
import com.example.ltmobile.Utils.SharedPrefManager;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    Context context = this;
    private ActivityResultLauncher<Intent> mActivityResuItLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null)
                            return;
                        Uri uri = data.getData();
                        uriImageAvatar = uri;
//                        String IMAGE_PATH = RealPathUtil.getRealPath(context, uriImageAvatar);
//                        Log.e("ffff", IMAGE_PATH);
//                        Toast.makeText(context, "12334", Toast.LENGTH_LONG).show();
                        //                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        imageView.setImageURI(uri);
                        Toast.makeText(context, "oke", Toast.LENGTH_LONG).show();
                    }
                }
            }
    );
    ImagePicker imagePicker = new ImagePicker((Activity) context, mActivityResuItLauncher);
    //View
    ImageView imageView;
    TextView tvId, tvEmail;
    EditText inputFname, inputGender;
    Button btnChooseFile, btnUploadFile;
    private Uri uriImageAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        connectView();
        getDataFromSharedPref();
        eventHandle();
    }


    private void connectView() {
        imageView = (ImageView) findViewById(R.id.imvAvt);
        tvId = (TextView) findViewById(R.id.tvId);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        inputFname = (EditText) findViewById(R.id.inputFname);
        inputGender = (EditText) findViewById(R.id.inputGender);
        btnChooseFile = (Button) findViewById(R.id.btnChooseImage);
        btnUploadFile = (Button) findViewById(R.id.btnSave);
    }

    void getDataFromSharedPref(){
        User user = SharedPrefManager.getInstance(context).getUser();
        tvId.setText(String.valueOf(user.getUserId()));
        tvEmail.setText(user.getEmail());
        inputFname.setText(user.getFullname());
        inputGender.setText(user.getGender());
        inputGender.setFocusable(false);
        inputGender.setCursorVisible(false);
        inputGender.setKeyListener(null);
        Glide.with(context).load(user.getAvatar()).into(imageView);
    }

    private void eventHandle() {
        //Chọn ảnh
        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker.pickPhoto();
            }
        });
        //Upload Ảnh
        btnUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = String.valueOf(SharedPrefManager.getInstance(context).getUser().getUserId());
                RequestBody requestId = RequestBody.create(MediaType.parse("multipart/form-data"), id);

                RequestBody requestFname = RequestBody.create(MediaType.parse("multipart/form-data"), inputFname.getText().toString());

                RequestBody requestGender = RequestBody.create(MediaType.parse("multipart/form-data"), inputGender.getText().toString());

                if (uriImageAvatar != null) {
                    String IMAGE_PATH = RealPathUtil.getRealPath(context, uriImageAvatar);
                    File file = new File(IMAGE_PATH);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

                    ServiceAPI.serviceapi.updateWithImage(requestId,requestFname,requestGender, filePart)
                            .enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject responseJson = new JSONObject(response.body().toString());
                                    String message = responseJson.getString("message");
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                    if(responseJson.has("result")){
                                        JSONObject userJson = responseJson.getJSONObject("result");
                                        User user = new User(
                                                userJson.getInt("userId"),
                                                userJson.getString("email"),
                                                userJson.getString("fullname"),
                                                userJson.getString("gender"),
                                                userJson.getString("avatar"),
                                                userJson.getString("role"));
                                        SharedPrefManager.getInstance(context).userLogin(user);
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    ServiceAPI.serviceapi.updateWithoutImage(requestId,requestFname,requestGender).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if(response.isSuccessful()){
                                if (response.isSuccessful()) {
                                    try {
                                        JSONObject responseJson = new JSONObject(response.body().toString());
                                        String message = responseJson.getString("message");
//                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                        if(responseJson.has("result")){
                                            JSONObject userJson = responseJson.getJSONObject("result");
                                            Toast.makeText(context, userJson.getString("fullname"), Toast.LENGTH_LONG).show();

                                            User user = new User(
                                                    userJson.getInt("userId"),
                                                    userJson.getString("email"),
                                                    userJson.getString("fullname"),
                                                    userJson.getString("gender"),
                                                    userJson.getString("avatar"),
                                                    userJson.getString("role"));
                                            SharedPrefManager.getInstance(context).userLogin(user);
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
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
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == ImagePicker.REQUEST_CODE_CHOOSE_IMAGE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                openGallery();
//            }
//        }
//    }

}