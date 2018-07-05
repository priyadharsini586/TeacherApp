package com.nickteck.teacherapp.api;

import com.nickteck.teacherapp.model.AttendenceClassDetails;
import com.nickteck.teacherapp.model.LoginDetails;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by admin on 7/2/2018.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("teacher_login.php")
    Call<LoginDetails> checkMobileNo(@Field("x") JSONObject object);

    @FormUrlEncoded
    @POST("get_teacher_otp.php")
    Call<LoginDetails> checkOpt(@Field("x") JSONObject object);

    @FormUrlEncoded
    @POST("verify_teacher_otp.php")
    Call<LoginDetails> verifyOtp(@Field("x") JSONObject object);

    @POST("get_class_details.php")
    Call<AttendenceClassDetails> getClassDetails();


}
