package com.seasia.poojasarees.api;

import com.google.gson.JsonObject;
import com.seasia.poojasarees.model.request.AddressIn;
import com.seasia.poojasarees.model.request.AdminIn;
import com.seasia.poojasarees.model.request.SignUpIn;
import com.seasia.poojasarees.model.request.UpdateProfileIn;
import com.seasia.poojasarees.model.response.AddressOut;
import com.seasia.poojasarees.model.response.AllStatesOut;
import com.seasia.poojasarees.model.response.AllTownsOut;
import com.seasia.poojasarees.model.response.HomeOut;
import com.seasia.poojasarees.model.response.LoginOut;
import com.seasia.poojasarees.model.response.OtpOut;
import com.seasia.poojasarees.model.response.ProfileOut;
import com.seasia.poojasarees.model.response.ProfilePicOut;
import com.seasia.poojasarees.model.response.SignUpOut;
import com.seasia.poojasarees.model.response.SignupPhoneNoOut;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

    @POST("integration/admin/token")
    Call<String> getAdminToken(@Body AdminIn adminIn);

    @POST("customers")
    Call<SignUpOut> signUpUser(@Body SignUpIn signUpIn);

    @POST("validateByPhone")
    Call<LoginOut> loginUser(@Body JsonObject loginIn);

    @POST("sendOtp")
    Call<OtpOut> sendOtp(@Body JsonObject otpIn);

    @POST("verifyOtp")
    Call<OtpOut> verifyOtp(@Body JsonObject otpIn);

    @POST("setPasswordByPhone")
    Call<OtpOut> changePassword(@Body JsonObject otpIn);

    @POST("getHomeContent")
    Call<HomeOut> getHomeContent(@Body JsonObject homeIn);


    // User Profile
    @PUT("customers/{id}")
    Call<ProfileOut> updateProfile(@Path("id") String customerId, @Body UpdateProfileIn updateProfileIn);

    @GET("customers/{id}")
    Call<ProfileOut> getProfile(@Path("id") String customerId);

    @GET("customers/me")
    Call<ProfileOut> getProfileMe();

    @GET("directory/countries")
    Call<ArrayList<AllStatesOut>> getAllStates();

    @POST("profilePic")
    Call<ProfilePicOut> updateProfilePic(@Body JsonObject profilePicIn);

//    @POST("profilePic")
//    Call<ProfilePicOut> updateProfilePic(@PartMap HashMap<String, RequestBody> mHashMap,
//                                         @Part MultipartBody.Part image);

    @POST("getCustomerByPhone")
    Call<SignupPhoneNoOut> uniquePhoneNoCheck(@Body JsonObject phoneNoIn);


    // Address
    @PUT("customers/{id}")
    Call<AddressOut> addOrUpdateAddress(@Path("id") String customerId, @Body AddressIn addressIn);

    @GET("cityOptions")
    Call<ArrayList<AllTownsOut>> getAllTowns();
}