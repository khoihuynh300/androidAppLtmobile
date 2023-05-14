package com.example.ltmobile.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.example.ltmobile.Adapter.InnAdapter;
import com.example.ltmobile.Adapter.NavigationAdapter;
import com.example.ltmobile.Fragment.InnFragment;
import com.example.ltmobile.Model.ImageInn;
import com.example.ltmobile.Model.Inn;
import com.example.ltmobile.Model.User;
import com.example.ltmobile.R;
import com.example.ltmobile.Utils.Constant;
import com.example.ltmobile.Utils.RealPathUtil;
import com.example.ltmobile.Utils.ServiceAPI;
import com.example.ltmobile.Utils.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommenInnActivity extends AppCompatActivity {
    private String[] location = {"Quan 1", "Quan 2", "Quan 4", "Quan 9", "Bình Thạnh", "Thủ Đức"};
    private String locationStr = "address";
    private int person = 2;
    private ArrayList<Uri> imageUris;
    private static final int PICK_IMAGES_CODE = 0;
    int position = 0;
    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public static final int MY_REQUEST_CODE = 100;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };
    public static String[] permissions() {
        String[] p;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }
    TextView txtlocation, txtpersion, headline, fullname;
    AutoCompleteTextView autoCompleteLocation;
    ImageSwitcher imagesIs;
    ArrayAdapter<String> adapterLocation;
    RelativeLayout filterMinus, filterPlus, btnBack, btnUpload, btnNext, continue_bu, avatar;
    EditText txtAddress, txtPhone, txtPrice, txtPriceWater, txtPriceElec, txtDes;
    CircleImageView imageView;
    ImageView imvAvatar;
    NavigationView navigationView;
    DrawerLayout navigationDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommen_inn);
        imageUris = new ArrayList<>();

        mapping();
        adapter();
        getDataFromSharedPref();

        autoCompleteLocation.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                locationStr = item;
                txtlocation.setText(item);
            }
        });

        filterMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(person > 1)
                {
                    person--;
                    txtpersion.setText(String.valueOf(person) + " Người");
                }
            }
        });

        filterPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(person < 5)
                {
                    person++;
                    txtpersion.setText(String.valueOf(person) + " Người");
                }
            }
        });

        imagesIs.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                return imageView;
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position > 0) {
                    position--;
                    imagesIs.setImageURI(imageUris.get(position));
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position < imageUris.size() - 1) {
                    position++;
                    imagesIs.setImageURI(imageUris.get(position));
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckPermissions();
            }
        });

        continue_bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recommendInn();
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationDrawer.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationAdapter(this));
    }

    private void mapping() {
        txtlocation = (TextView) findViewById(R.id.txtlocation);
        txtpersion = (TextView) findViewById(R.id.txtpersion);
        headline = (TextView) findViewById(R.id.headline);
        autoCompleteLocation = (AutoCompleteTextView) findViewById(R.id.autoCompleteLocation);
        filterMinus = (RelativeLayout) findViewById(R.id.filterMinus);
        filterPlus = (RelativeLayout) findViewById(R.id.filterPlus);
        avatar = (RelativeLayout) findViewById(R.id.avatar);
        imagesIs = (ImageSwitcher) findViewById(R.id.imagesIs);
        btnBack = (RelativeLayout) findViewById(R.id.btnBack);
        btnNext = (RelativeLayout) findViewById(R.id.btnNext);
        btnUpload = (RelativeLayout) findViewById(R.id.btnUpload);
        continue_bu = (RelativeLayout) findViewById(R.id.continue_bu);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtPrice = (EditText) findViewById(R.id.txtPrice);
        txtPriceWater = (EditText) findViewById(R.id.txtPriceWater);
        txtPriceElec = (EditText) findViewById(R.id.txtPriceElec);
        txtDes = (EditText) findViewById(R.id.txtDes);
        imageView = (CircleImageView) findViewById(R.id.imageView);
        navigationDrawer = (DrawerLayout) findViewById(R.id.navigationDrawer);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        imvAvatar = headerView.findViewById(R.id.imvAvatar);
        fullname = headerView.findViewById(R.id.tvFullName);
    }


    private void adapter() {
        adapterLocation = new ArrayAdapter<String>(this, R.layout.list_item, location);
        autoCompleteLocation.setAdapter(adapterLocation);
    }

    void getDataFromSharedPref(){
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        headline.setText("Hello, " + user.getFullname() + "!");
        fullname.setText(user.getFullname());
        Glide.with(getApplicationContext()).load(Constant.ROOT_URL + "upload/" + user.getAvatar()).into(imvAvatar);
        Glide.with(getApplicationContext()).load(Constant.ROOT_URL + "upload/" + user.getAvatar()).into(imageView);
    }

    private void CheckPermissions() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            requestPermissions(permissions(), MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture(s)"));
    }

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data.getClipData() != null) {
                            int count = data.getClipData().getItemCount();
                            for (int i = 0; i<count; i++) {
                                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                imageUris.add(imageUri);
                            }

                            imagesIs.setImageURI(imageUris.get(0));
                            position = 0;
                        } else {
                            Uri imageUri = data.getData();
                            imageUris.add(imageUri);
                            imagesIs.setImageURI(imageUris.get(0));
                            position = 0;
                        }
                    }
                }
            }
    );

    private void recommendInn() {
        String location = txtlocation.getText().toString();
        String address = txtAddress.getText().toString();
        String phone = txtPhone.getText().toString();
        String price = txtPrice.getText().toString();
        String priceWater = txtPriceWater.getText().toString();
        String priceElec = txtPriceElec.getText().toString();
        String des = txtDes.getText().toString();

        if (location == getResources().getString(R.string.location)){
            Toast.makeText(getApplicationContext(), "Vui long chon dia diem", Toast.LENGTH_SHORT).show();
            return;
        }
        if (address.isEmpty()){
            Toast.makeText(getApplicationContext(), "Vui long điền dia chi", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.isEmpty()){
            Toast.makeText(getApplicationContext(), "Vui long điền số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        }
        if (des.isEmpty()){
            Toast.makeText(getApplicationContext(), "Vui long điền mô tả", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if(price.isEmpty() || Double.valueOf(price) < 0) {
                Toast.makeText(getApplicationContext(), "Vui long điền giá phòng", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "Nhập chính xác giá tiền", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if(priceWater.isEmpty() || Double.valueOf(priceWater) < 0) {
                Toast.makeText(getApplicationContext(), "Vui long điền giá nước", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "Nhập chính xác giá tiền", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if(priceElec.isEmpty() || Double.valueOf(priceElec) < 0) {
                Toast.makeText(getApplicationContext(), "Vui long điền giá điện", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "Nhập chính xác giá tiền", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody requestAddress = RequestBody.create(MediaType.parse("multipart/form-data"), address + ", " + location);
        RequestBody requestPhone = RequestBody.create(MediaType.parse("multipart/form-data"), phone);
        RequestBody requestproposedId = RequestBody.create(MediaType.parse("multipart/form-data"), "1");
        RequestBody requestprice = RequestBody.create(MediaType.parse("multipart/form-data"), price);
        RequestBody requestpriceWater = RequestBody.create(MediaType.parse("multipart/form-data"), priceWater);
        RequestBody requestpriceElec = RequestBody.create(MediaType.parse("multipart/form-data"), priceElec);
        RequestBody requestDes = RequestBody.create(MediaType.parse("multipart/form-data"), des);
        RequestBody requestSize = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(person));

        List<MultipartBody.Part> files = new ArrayList<>();
        for (Uri imageUri: imageUris) {
            String IMAGE_PATH = RealPathUtil.getRealPath(this, imageUri);
            File file = new File(IMAGE_PATH);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("imageFiles", file.getName(), requestBody);
            files.add(filePart);
        }

        ServiceAPI.serviceapi.recommendInn(requestSize, requestpriceWater, requestpriceElec, requestAddress, requestprice, requestPhone, requestDes, requestproposedId, files).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "OKE", Toast.LENGTH_LONG).show();
//                    User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG", t.toString());
                Toast.makeText(getApplicationContext(), "failed connect API", Toast.LENGTH_LONG).show();
            }
        });
    }
}