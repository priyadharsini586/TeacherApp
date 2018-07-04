package com.nickteck.teacherapp.fragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.chootdev.blurimg.BlurImage;
import com.nickteck.teacherapp.R;

import com.nickteck.teacherapp.additional_class.HelperClass;
import com.nickteck.teacherapp.api.ApiClient;
import com.nickteck.teacherapp.api.ApiInterface;
import com.nickteck.teacherapp.database.DataBaseHandler;
import com.nickteck.teacherapp.model.LoginDetails;
import com.nickteck.teacherapp.utilclass.Constants;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends Fragment {

    View mainView;
    ImageView school_image,teacher_profile_image;
    TextView teacher_mname,school_mname;
    TextView Designation_position;
    TextView main_sub,teacher_name_drawer,school_name_drawer;
    ApiInterface apiInterface;
    DataBaseHandler dataBaseHandler;
    TSnackbar tSnackbar;
    private String deviceId;
    boolean isNetworkConnected= false;
    LoginDetails loginDetails;
    public ImageView imageView_drawer,blur_background_image;



    public DashBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView =  inflater.inflate(R.layout.fragment_dash_board, container, false);

        init();

        return mainView;
    }

    private void init() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        dataBaseHandler = new DataBaseHandler(getActivity());

        school_image = (ImageView) mainView.findViewById(R.id.school_image);
        teacher_mname = (TextView) mainView.findViewById(R.id.teacher_mname);
        Designation_position = (TextView) mainView.findViewById(R.id.Designation_position);
        main_sub = (TextView) mainView.findViewById(R.id.main_sub);
        teacher_profile_image = (ImageView) mainView.findViewById(R.id.teacher_profile_image);
        school_mname = (TextView) mainView.findViewById(R.id.school_mname);
        blur_background_image = (ImageView)mainView.findViewById(R.id.blur_background_image);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tSnackbar = HelperClass.showTopSnackBar(mainView, "Network not connected");
        if (HelperClass.isNetworkAvailable(getActivity())) {
            isNetworkConnected = true;
            if (tSnackbar.isShown())
                tSnackbar.dismiss();
        }else {
            isNetworkConnected = false;
            tSnackbar.show();
        }
        if (isNetworkConnected) {
            getDataFromServer();

        }
        else
            setIntoView();



    }

    public void getDataFromServer(){
        if (isNetworkConnected){
            getDeviceId();

            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("phone", dataBaseHandler.getMobileNumber());
                jsonObject.put("device_id", deviceId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Call<LoginDetails> checkMobileNo = apiInterface.checkMobileNo(jsonObject);
            checkMobileNo.enqueue(new Callback<LoginDetails>() {

                @Override
                public void onResponse(Call<LoginDetails> call, Response<LoginDetails> response) {
                    if (response.isSuccessful()) {
                        LoginDetails loginDetails = response.body();
                        if (loginDetails.getStatus_code() != null) {
                            if (loginDetails.getStatus_code().equals(Constants.SUCESS)) {
                                JSONObject teacherObject = loginDetails.toJSON();
                                JSONObject schoolObject = loginDetails.toJSONS();
                                dataBaseHandler.dropTeacherDetails();
                                dataBaseHandler.insertTeacherDetails(loginDetails.getTeacher_details().get(0).getId(),teacherObject.toString(),schoolObject.toString());

                                setIntoView();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginDetails> call, Throwable t) {

                }
            });
        }
    }

    private void getDeviceId() {

        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            deviceId = telephonyManager.getDeviceId();
            return;
        }

    }

    private void setIntoView() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                String getParentDetails = dataBaseHandler.getTeacherDetails();
                try {
                    JSONObject getParentObject = new JSONObject(getParentDetails);
                    JSONArray parentArray = getParentObject.getJSONArray("teacher_details");
                    for (int i =0 ; i < parentArray.length() ; i ++) {
                        JSONObject jsonObject = parentArray.getJSONObject(i);
                        teacher_mname.setText(jsonObject.getString("name"));
                        main_sub.setText(jsonObject.getString("subject"));
                        String teacher_image = jsonObject.getString("photo");
                        Picasso.with(getActivity()).load(Constants.TEACHER_PROFILE_IMAGE_URI+teacher_image)
                                .placeholder(R.drawable.camera_icon).into(teacher_profile_image);

                        /*teacher_name_drawer = (TextView)getActivity().findViewById(R.id.teacher_name_drawer);
                        teacher_name_drawer.setText(jsonObject.getString("name"));*/
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }

                String getschoolDetails = dataBaseHandler.getSchoolDetails();
                try {
                    JSONObject getParentObject = new JSONObject(getschoolDetails);
                    JSONArray parentArray = getParentObject.getJSONArray("school_details");
                    for (int i =0 ; i < parentArray.length() ; i ++) {
                        JSONObject jsonObject = parentArray.getJSONObject(i);
                        school_mname.setText(jsonObject.getString("school_name"));
                        String school_image_uri = jsonObject.getString("photo");
                        String school_logo_image = jsonObject.getString("logo");

                        blur_background_image = (ImageView)mainView.findViewById(R.id.blur_background_image);

                    BlurImage.withContext(getActivity()).blurFromUri(Constants.SCHOOL_IMAGE_URI+school_image_uri).
                            into(blur_background_image);

                        Picasso.with(getActivity()).load(Constants.SCHOOL_IMAGE_URI+school_image_uri)
                                .placeholder(R.drawable.camera_icon).into(school_image);

                        imageView_drawer = (ImageView)getActivity().findViewById(R.id.imageView_drawer);

                        school_name_drawer = (TextView)getActivity().findViewById(R.id.school_name_drawer);

                        Picasso.with(getActivity()).load(Constants.SCHOOL_IMAGE_URI+school_logo_image)
                                .placeholder(R.drawable.camera_icon).into(imageView_drawer);

                        school_name_drawer.setText(jsonObject.getString("school_name"));

                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }


}
