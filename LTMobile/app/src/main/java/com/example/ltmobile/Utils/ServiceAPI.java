package com.example.ltmobile.Utils;

import com.example.ltmobile.Model.CommentInn;
import com.example.ltmobile.Model.Inn;
import com.example.ltmobile.Model.User;
import com.example.ltmobile.Model.Messages;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    Call<JsonArray> getAllInnsConfirmed();

    @GET("inns/search")
    Call<JsonArray> searchInns(@Query("address") String address, @Query("gtePrice") Double gtePrice, @Query("ltePrice") Double ltePrice, @Query("size") int size);

    @GET("inns/{id}")
    Call<JsonObject> getInnById(@Path("id") int id);

    @GET("commentInn/{innId}")
    Call<JsonArray> getAllCommentOfInn(@Path("innId") int innId);

    @POST("commentInn/add")
    Call<JsonObject> createCommentOfInn(@Body CommentInn commentInn);

    @GET("getAllQuestion")
    Call<JsonArray> getAllQuestions();

    @Multipart
    @POST("inns/add")
    Call<JsonObject> recommendInn(@Part("size") RequestBody size,
                                  @Part("priceWater") RequestBody priceWater,
                                  @Part("priceELec") RequestBody priceELec,
                                  @Part("address") RequestBody address,
                                  @Part("price") RequestBody price,
                                  @Part("phoneNumber") RequestBody phoneNumber,
                                  @Part("describe") RequestBody describe,
                                  @Part("proposedId") RequestBody proposedId,
                                  @Part List<MultipartBody.Part> imageFiles);

    @GET("admin/inns")
    Call<JsonObject> getInns();


    @GET("admin/inns")
    Call<JsonObject> getInnsFilter(@Query("offset") int offset,
                                   @Query("ascending") boolean ascending,
                                   @Query("isDeleted") boolean isDeleted,
                                   @Query("Address") String Address,
                                   @Query("isConfirmed") String isConfirmed);

    @FormUrlEncoded
    @PUT("admin/inns/confirm/{innId}")
    Call<JsonObject> confirmInn(@Path("innId") int innId, @Field("userId") int userId);

    @DELETE("admin/inns/delete/{innId}")
    Call<JsonObject> deleteInn(@Path("innId") int innId);

    @GET("admin/users")
    Call<JsonObject> getUserFilter(@Query("offset") int offset,
                                   @Query("ascending") boolean ascending,
                                   @Query("isActive") boolean isActive,
                                   @Query("name") String name);

    @FormUrlEncoded
    @PUT("admin/users/lock")
    Call<JsonObject> lockUser(@Field("id") int userId);

    @FormUrlEncoded
    @POST("addMessage")
    Call<JsonObject> addMessage(@Field("message") String message,
                                @Field("userId") int userId,
                                @Field("questionId") int questionId);

    @GET("getAllMessageByQuestionId/{id}")
    Call<JsonArray> loadMessages(@Path("id") int questionId);

    @POST("admin/users/create")
    Call<JsonObject> addUser(@Body User user);

    @GET("users/{user_id}")
    Call<JsonObject> getUserById(@Path("user_id") int userId);
    @GET("users/{user_id}/inns")
    Call<JsonObject> findInnByUserProposed(@Path("user_id") int userId);

    @FormUrlEncoded
    @POST("addQuestion")
    Call<JsonObject> addQuestion(@Field("title") String title,
                                 @Field("askedId") int askedId,
                                 @Field("message") String message);

    @GET("users/{user_id}/questions")
    Call<JsonObject> findQuestionsByUserProposed(@Path("user_id") int userId);

    @GET("notify/{user_id}")
    Call<JsonObject> getAllNotifyByUser(@Path("user_id") int userId);

    @GET("question/{id}")
    Call<JsonObject> getQuestionById(@Path("id") int id);

    @GET("notify/viewed/{id}")
    Call<JsonObject> viewed(@Path("id") int id);
}
