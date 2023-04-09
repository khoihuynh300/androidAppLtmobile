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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ltmobile.R;
import com.example.ltmobile.Utils.ImagePicker;
import com.example.ltmobile.Utils.RealPathUtil;

import java.io.File;
import java.io.IOException;

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
                        uriImageAvatar = uri;String IMAGE_PATH = RealPathUtil.getRealPath(context, uriImageAvatar);
                        Log.e("ffff", IMAGE_PATH);
                        Toast.makeText(context, "12334", Toast.LENGTH_LONG).show();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imageView.setImageBitmap(bitmap);
                            Toast.makeText(context, "12334", Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    ImagePicker imagePicker = new ImagePicker((Activity) context, mActivityResuItLauncher);
    ImageView imageView;
    Button btnChooseFile, btnUploadFile;
    private Uri uriImageAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        connectView();
        eventHandle();
    }


    private void connectView() {
        imageView = (ImageView) findViewById(R.id.imvAvt);
        btnChooseFile = (Button) findViewById(R.id.btnChooseImage);
        btnUploadFile = (Button) findViewById(R.id.btnSave);
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
                if (uriImageAvatar != null) {
                    UploadImage();
                }
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

    private void UploadImage() {

        Log.e("ffff", uriImageAvatar.toString());
        String IMAGE_PATH = RealPathUtil.getRealPath(context, uriImageAvatar);
        Log.e("ffff", IMAGE_PATH);
    }
}