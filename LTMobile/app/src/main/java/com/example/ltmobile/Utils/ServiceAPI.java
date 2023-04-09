package com.example.ltmobile.Utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ServiceAPI {
    ServiceAPI serviceapi = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constant.ROOT_URL)
//            .baseUrl("http://192.168.1.18:3000/")
            .build()
            .create(ServiceAPI.class);

    @FormUrlEncoded
    @POST("login")
    Call<JsonObject> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("signup")
    Call<JsonObject> signup(@Field("email") String email,@Field("fname") String fname , @Field("password") String password);

    @FormUrlEncoded
    @POST("signup/verify")
    Call<JsonObject> verifySignup(@Field("email") String email,@Field("fname") String fname , @Field("password") String password, @Field("otp") int otp);


}
