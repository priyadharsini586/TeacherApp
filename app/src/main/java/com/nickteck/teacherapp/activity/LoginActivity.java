package com.nickteck.teacherapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;

import com.nickteck.teacherapp.R;
import com.nickteck.teacherapp.additional_class.HelperClass;
import com.nickteck.teacherapp.api.ApiClient;
import com.nickteck.teacherapp.api.ApiInterface;
import com.nickteck.teacherapp.database.DataBaseHandler;
import com.nickteck.teacherapp.model.LoginDetails;
import com.nickteck.teacherapp.service.MyApplication;
import com.nickteck.teacherapp.service.NetworkChangeReceiver;
import com.nickteck.teacherapp.utilclass.Constants;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements NetworkChangeReceiver.ConnectivityReceiverListener {

    EditText editPhoneNo;
    Button btnSubmit,btnActivationSubmit;
    boolean netWorkConnection;
    RelativeLayout mainView;
    TSnackbar tSnackbar;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private boolean isPhone;
    private EditText mMobileNo;
    private String deviceId;
    private String getMobileNo;
    ApiInterface apiInterface;
    LoginDetails loginDetails;
    private SmsVerifyCatcher smsVerifyCatcher;
    private EditText meditActivationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_screen);
        mainView = (RelativeLayout) findViewById(R.id.sclMainView);
        MyApplication.getInstance().setConnectivityListener(this);

        tSnackbar = HelperClass.showTopSnackBar(mainView, "Network not connected");
        if (HelperClass.isNetworkAvailable(getApplicationContext())) {
            netWorkConnection = true;
            if (tSnackbar.isShown())
                tSnackbar.dismiss();
        }
        else {
            netWorkConnection = false;
            tSnackbar.show();
        }

        // getting permission for app
        getPermission();

        init();
        setOnclickListener();
    }

    private void getPermission() {

        permissions.add(Manifest.permission.READ_SMS);
        permissions.add(Manifest.permission.RECEIVE_SMS);
        permissions.add(Manifest.permission.SEND_SMS);
        permissions.add(Manifest.permission.READ_PHONE_STATE);
        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= 23) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                        ALL_PERMISSIONS_RESULT);

            }
        }
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    private void init() {
        editPhoneNo = (EditText) findViewById(R.id.editPhoneNo);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        meditActivationCode = (EditText)findViewById(R.id.editActivationCode);
        btnActivationSubmit = (Button) findViewById(R.id.btnActivationSubmit);

        //init SmsVerifyCatcher
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                meditActivationCode.setText(code);//set code in edit text
                //then you can send verification code to server
            }
        });

    }

    private void setOnclickListener() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPhone = HelperClass.isValidMobile(editPhoneNo.getText().toString());
                if (isPhone) {
                    checkLogin();
                } else {
                    validation();
                }
            }
        });

        btnActivationSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOTP();
            }
        });

    }

    private void submitOTP(){
        if (netWorkConnection) {
            getDeviceId();
            getMobileNo = editPhoneNo.getText().toString();
            // api call for the add  mobile no validation
            apiInterface = ApiClient.getClient().create(ApiInterface.class);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("phone", getMobileNo);
                jsonObject.put("otp", loginDetails.getOTP());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Call<LoginDetails> checkMobileNo = apiInterface.verifyOtp(jsonObject);
            checkMobileNo.enqueue(new Callback<LoginDetails>() {

                @Override
                public void onResponse(Call<LoginDetails> call, Response<LoginDetails> response) {
                    if (response.isSuccessful()) {
                        LoginDetails loginDetails = response.body();
                        if (loginDetails.getStatus_code() != null) {
                            if (loginDetails.getStatus_code().equals(Constants.SUCESS)) {
                                DataBaseHandler  dataBaseHandler = new DataBaseHandler(getApplicationContext());
                                dataBaseHandler.insertLoginTable("0",getMobileNo,deviceId);
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getApplicationContext(),loginDetails.getStatus_message(),Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginDetails> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });



        }else {
            HelperClass.showTopSnackBar(mainView, "Network not connected");
        }

    }

    private void validation() {
        if (!isPhone) {
            editPhoneNo.setError("Invalid Phone number");
        }
    }

    private void checkLogin() {
        if (netWorkConnection) {
            getDeviceId();
            getMobileNo = editPhoneNo.getText().toString();
            // api call for the add  mobile no validation
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("phone", getMobileNo);
                jsonObject.put("device_id", deviceId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Call<LoginDetails> checkMobileNo = apiInterface.checkMobileNo(jsonObject);
            checkMobileNo.enqueue(new Callback<LoginDetails>() {

                @Override
                public void onResponse(Call<LoginDetails> call, Response<LoginDetails> response) {
                    if (response.isSuccessful()) {
                        loginDetails = response.body();
                        if (loginDetails.getStatus_code() != null) {
                            if (loginDetails.getStatus_code().equals(Constants.SUCESS)) {
                                DataBaseHandler dataBaseHandler = new DataBaseHandler(getApplicationContext());
                                dataBaseHandler.insertLoginTable("0",getMobileNo,deviceId);
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else if (loginDetails.getStatus_code().equals(Constants.NOT_VERIFIED)) {
                                Toast.makeText(LoginActivity.this, loginDetails.getStatus_message(), Toast.LENGTH_SHORT).show();
                                getOptApi();
                            }else if (loginDetails.getStatus_code().equals(Constants.DEVICE_ID_NOT_MATCHED)){
                                Toast.makeText(LoginActivity.this, loginDetails.getStatus_message(), Toast.LENGTH_SHORT).show();

                            }


                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginDetails> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });



        }else {
            HelperClass.showTopSnackBar(mainView, "Network not connected");
        }
    }

    private void getDeviceId() {

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            deviceId = telephonyManager.getDeviceId();
            return;
        }

    }

    private void getOptApi() {
        // get opt if status code is -1
        if (netWorkConnection) {
            getMobileNo = editPhoneNo.getText().toString();
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("phone", getMobileNo);
            }catch (JSONException e){
                e.printStackTrace();
            }
            Call<LoginDetails> checkMobileNo = apiInterface.checkOpt(jsonObject);
            checkMobileNo.enqueue(new Callback<LoginDetails>() {

                @Override
                public void onResponse(Call<LoginDetails> call, Response<LoginDetails> response) {
                    if (response.isSuccessful()) {
                        loginDetails = response.body();
                        if (loginDetails.getStatus_code() != null) {
                            if (loginDetails.getStatus_code().equals(Constants.SUCESS)) {
                                Toast.makeText(LoginActivity.this, "otp generated successfully", Toast.LENGTH_SHORT).show();
                                enableActivationBox();
                            }else {
                                Toast.makeText(LoginActivity.this, "Unregistered user", Toast.LENGTH_SHORT).show();
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

    private void enableActivationBox(){

        // after login api sucess then only it should start
        editPhoneNo.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);
        meditActivationCode.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this,R.anim.edit_text_animation));
        btnActivationSubmit.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this,R.anim.edit_text_animation));
        meditActivationCode.setVisibility(View.VISIBLE);
        btnActivationSubmit.setVisibility(View.VISIBLE);
        checkForOtp();
    }

    private void checkForOtp() {

        //init SmsVerifyCatcher
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                meditActivationCode.setText(code);//set code in edit text
                //then you can send verification code to server
            }
        });

        //set phone number filter if needed
        smsVerifyCatcher.setPhoneNumberFilter("777");
    }

    /**
     * Parse verification code
     *
     * @param message sms message
     * @return only four numbers from massage string
     */
    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }



    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                Log.d("request", "onRequestPermissionsResult");
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            String msg = "These permissions are mandatory for the application. Please allow access.";
                            showMessageOKCancel(msg,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(
                                                        new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);

                                            }
                                        }
                                    });
                            Log.e("check","inpermission");
                            return;
                        }
                    }
                } else {
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage(message)
                .setPositiveButton("OK",okListener )
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        netWorkConnection = isConnected;
        if (mainView != null) {
            if (!isConnected) {
                tSnackbar.show();
            }else {
                if (tSnackbar.isShown())
                    tSnackbar.dismiss();
            }
        }

    }
}
