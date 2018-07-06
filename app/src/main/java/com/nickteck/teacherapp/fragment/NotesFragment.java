package com.nickteck.teacherapp.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.nickteck.teacherapp.R;
import com.nickteck.teacherapp.adapter.ClassAdapter;
import com.nickteck.teacherapp.additional_class.HelperClass;
import com.nickteck.teacherapp.api.ApiClient;
import com.nickteck.teacherapp.api.ApiInterface;
import com.nickteck.teacherapp.model.AttendenceClassDetails;
import com.nickteck.teacherapp.model.StudentList;
import com.nickteck.teacherapp.service.MyApplication;
import com.nickteck.teacherapp.service.NetworkChangeReceiver;
import com.nickteck.teacherapp.utilclass.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment implements NetworkChangeReceiver.ConnectivityReceiverListener {

    View mainView;
    Spinner spnClass,spnSection;
    boolean isNetworkConnected;
    ApiInterface apiInterface;
    HashMap<String,ArrayList<String>> hashMapClassList = new HashMap<>();
    ArrayList<String> classList = new ArrayList<>();
    ArrayList<String> sectionList = new ArrayList<>();
    int classSelectedIndex,sectionSelectedIndex;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_notes, container, false);

        spnClass = (Spinner) mainView.findViewById(R.id.class_spinner);
        spnSection = (Spinner) mainView.findViewById(R.id.section_spinner);

        MyApplication.getInstance().setConnectivityListener(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        isNetworkConnected = HelperClass.isNetworkAvailable(getActivity());
        if (isNetworkConnected){
            getDataFromServer();
        }
        return mainView;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        this.isNetworkConnected = isConnected;
    }


    public void getDataFromServer(){
        if (isNetworkConnected){
            final Call<AttendenceClassDetails> classDetails = apiInterface.getClassDetails();
            classDetails.enqueue(new Callback<AttendenceClassDetails>() {
                @Override
                public void onResponse(@NonNull Call<AttendenceClassDetails> call, @NonNull Response<AttendenceClassDetails> response) {
                    if (response.isSuccessful()) {
                        AttendenceClassDetails attendenceClassDetails = response.body();
                        if (attendenceClassDetails != null) {
                            if (attendenceClassDetails.getStatus_code() != null) {
                                if (attendenceClassDetails.getStatus_code().equals(Constants.SUCESS)) {
                                    hashMapClassList = new HashMap<>();
                                    classList = new ArrayList<>();
                                    String defaultValue = "Select Class";
                                    classList.add(defaultValue);
                                    for (int i = 0; i < attendenceClassDetails.getClass_details().size(); i++) {
                                        AttendenceClassDetails.ClassDetails classDetail = attendenceClassDetails.getClass_details().get(i);
                                        classList.add(classDetail.getClass_name());
                                        ArrayList<String> sectionList = new ArrayList<>();
                                        for (int j = 0; j < classDetail.getSections().size(); j++) {
                                            AttendenceClassDetails.ClassSection classSection = classDetail.getSections().get(j);
                                            sectionList.add(classSection.getSections());
                                        }
                                        hashMapClassList.put(classDetail.getClass_name(), sectionList);
                                    }
                                    setSpinner();
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AttendenceClassDetails> call, @NonNull Throwable t) {

                }
            });
        }
    }

    public void setSpinner(){

        sectionList = new ArrayList<>();
        sectionList.add("Select Section");
        ClassAdapter classAdapter = new ClassAdapter(getActivity(),classList,Constants.CLASS);
        spnClass.setAdapter(classAdapter);
        spnClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    String selectedItem = classList.get(i);
                    ArrayList<String> getValueFromHashMap = hashMapClassList.get(selectedItem);
                    sectionList.clear();
                    sectionList.add("Select Section");
                    sectionList.addAll(getValueFromHashMap);
                }else {
                    sectionList.clear();
                    sectionList.add("Select Section");
                }
                spnSection.setSelection(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ClassAdapter sectionAdapter = new ClassAdapter(getActivity(),sectionList,Constants.SECTION);
        spnSection.setAdapter(sectionAdapter);
        spnSection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void getStudents(String  className, String sections){
        if (isNetworkConnected){
            JSONObject studentObject = new JSONObject();
            try {
                studentObject.put("class_name",className);
                studentObject.put("section",sections);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Call<StudentList> studentDetailsCall = apiInterface.getStudentList(studentObject);
            studentDetailsCall.enqueue(new Callback<StudentList>() {
                @Override
                public void onResponse(@NonNull Call<StudentList> call, @NonNull Response<StudentList> response) {
                    if (response.isSuccessful()){
                        StudentList studentDetails = response.body();
                        if (studentDetails != null) {
                            if (studentDetails.getStatus_code() != null) {
                                if (studentDetails.getStatus_code().equals(Constants.SUCESS)) {

                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StudentList> call, @NonNull Throwable t) {

                }
            });
        }
    }
}
