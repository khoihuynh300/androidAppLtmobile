package com.example.ltmobile.Utils;

import com.example.ltmobile.Model.CommentInn;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceAPI {
    ServiceAPI serviceapi = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constant.ROOT_URL)
//            .baseUrl("http://192.168.1.18:3000/")
            .build()
            .create(ServiceAPI.class);

    @FormUrlEncoded
    @POST("login")
    Call<JsonObject> login(@Field("email") String email,
                           @Field("password") String password);

    @FormUrlEncoded
    @POST("signup")
    Call<JsonObject> signup(@Field("email") String email,@Field("fname") String fname,
                            @Field("gender") String gender ,
                            @Field("password") String password);

    @FormUrlEncoded
    @POST("signup/verify")
    Call<JsonObject> verifySignup(@Field("email") String email,
                                  @Field("fname") String fname ,
                                  @Field("gender") String gender ,
                                  @Field("password") String password,
                                  @Field("otp") int otp);

    @Multipart
    @POST("update-profile")
    Call<JsonObject> updateWithImage(@Part("id") RequestBody id,
                                     @Part("fname") RequestBody fname,
                                     @Part("gender") RequestBody gender,
                                     @Part MultipartBody.Part image);
    @Multipart
    @POST("update-profile")
    Call<JsonObject> updateWithoutImage(@Part("id") RequestBody id,
                                        @Part("fname") RequestBody fname,
                                        @Part("gender") RequestBody gender);

    @FormUrlEncoded
    @POST("forget-password")
    Call<JsonObject> getOtpForgetPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("forget-password/verify")
    Call<JsonObject> verifyForgetPassword(@Field("email") String email,
                                          @Field("newpassword") String newpassword,
                                          @Field("otp") int otp);

    @FormUrlEncoded
    @POST("change-password")
    Call<JsonObject> changePassword(@Field("id") int id,
                                    @Field("password") String newpassword,
                                    @Field("oldpassword") String oldpassword);

    @GET("inns")
    Call<JsonArray> getAllInns();

    @GET("inns/search")
    Call<JsonArray> searchInns(@Query("address") String address, @Query("gtePrice") Double gtePrice, @Query("ltePrice") Double ltePrice);

    @GET("inns/{id}")
    Call<JsonObject> getInnById(@Path("id") int id);

    @GET("commentInn/{innId}")
    Call<JsonArray> getAllCommentOfInn(@Path("innId") int innId);

    @POST("commentInn/add")
    Call<JsonObject> createCommentOfInn(@Body CommentInn commentInn);
}
