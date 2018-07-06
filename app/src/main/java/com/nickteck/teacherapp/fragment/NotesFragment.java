package com.nickteck.teacherapp.fragment;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.nickteck.teacherapp.R;
import com.nickteck.teacherapp.adapter.ClassAdapter;
import com.nickteck.teacherapp.adapter.StudentListAdapter;
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
    FlexboxLayout flexLayout;
    RadioGroup radioOptions;
    ArrayList<StudentList.StudentDetails>studentDetailList = new ArrayList<>();
    ArrayList<StudentList.StudentDetails>tempStudentDetailList = new ArrayList<>();
    RadioButton radioAll,radioSelected;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_notes, container, false);

        spnClass = (Spinner) mainView.findViewById(R.id.class_spinner);
        spnSection = (Spinner) mainView.findViewById(R.id.section_spinner);

        flexLayout = (FlexboxLayout) mainView.findViewById(R.id.flexLayout);

        radioOptions = (RadioGroup) mainView.findViewById(R.id.radioOptions);
        radioAll = (RadioButton) mainView.findViewById(R.id.radioAll);
        radioAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentDetailList = new ArrayList<>();
                studentDetailList.addAll(tempStudentDetailList);
                setSelectedStudent();
            }
        });
        radioSelected = (RadioButton)mainView.findViewById(R.id.radioSelected);
        radioSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSelectedStudent();
            }
        });

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
                classSelectedIndex = i;
                if (i != 0) {
                    tempStudentDetailList =new ArrayList<>();
                    studentDetailList = new ArrayList<>();
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
                flexLayout.removeAllViews();
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
                sectionSelectedIndex = i;
                if (sectionSelectedIndex != 0 && classSelectedIndex != 0){
                    getStudents(classList.get(classSelectedIndex),sectionList.get(sectionSelectedIndex));
                }
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
                                    studentDetailList =new ArrayList<>();
                                    for (int i =0 ; i< studentDetails.getStudent_details().size() ; i++){
                                        StudentList.StudentDetails details = studentDetails.getStudent_details().get(i);
                                        details.setChecked(false);
                                        studentDetailList.add(details);
                                        tempStudentDetailList.add(details);
                                        final LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        final View v = vi.inflate(R.layout.add_student_row, null);
                                        TextView txtStudentName = v.findViewById(R.id.txtStudentName);
                                        txtStudentName.setText(details.getStudent_name());
                                        final ImageView imgRemoveView = v.findViewById(R.id.imgRemoveView);
                                        imgRemoveView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                ((ViewManager)v.getParent()).removeView(v);
                                            }
                                        });
                                        flexLayout.addView(v);
                                    }
                                }else {
                                    tempStudentDetailList = new ArrayList<>();
                                    studentDetailList = new ArrayList<>();
                                    flexLayout.removeAllViews();
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

    public void getSelectedStudent(){
        if (tempStudentDetailList.size() != 0) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.student_list_dialog);
            RecyclerView studentRecyclerView = (RecyclerView) dialog.findViewById(R.id.studentRecyclerView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            studentRecyclerView.setLayoutManager(linearLayoutManager);
            studentDetailList = new ArrayList<>();
            studentDetailList.addAll(tempStudentDetailList);
            final StudentListAdapter studentListAdapter = new StudentListAdapter(getActivity(), studentDetailList);
            studentRecyclerView.setItemAnimator(new DefaultItemAnimator());
            studentRecyclerView.setAdapter(studentListAdapter);
            Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
            Button btnCancel = dialog.findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<Integer> checkBoxIndex = studentListAdapter.getCheckBoxIndex();
                    ArrayList<StudentList.StudentDetails> tempStudentDtails = new ArrayList<>();
                    tempStudentDtails.addAll(studentDetailList);
                    studentDetailList = new ArrayList<>();
                    for (int j = 0; j < checkBoxIndex.size(); j++) {
                        int selected = checkBoxIndex.get(j);
                        studentDetailList.add(tempStudentDtails.get(selected));
                    }
                    setSelectedStudent();
                    dialog.cancel();
                }
            });
            dialog.setCancelable(false);
            dialog.show();
        }
    }


    public void setSelectedStudent(){
        flexLayout.removeAllViews();
        for (int i = 0 ; i < studentDetailList.size() ; i ++){
            final StudentList.StudentDetails details = studentDetailList.get(i);
            final LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View v = vi.inflate(R.layout.add_student_row, null);
            TextView txtStudentName = v.findViewById(R.id.txtStudentName);
            txtStudentName.setText(details.getStudent_name());
            final ImageView imgRemoveView = v.findViewById(R.id.imgRemoveView);
            imgRemoveView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    details.setChecked(false);
                    ((ViewManager)v.getParent()).removeView(v);
                }
            });
            flexLayout.addView(v);
        }

    }
}
