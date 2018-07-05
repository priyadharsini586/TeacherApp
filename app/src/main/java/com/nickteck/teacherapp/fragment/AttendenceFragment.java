package com.nickteck.teacherapp.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.nickteck.teacherapp.R;
import com.nickteck.teacherapp.additional_class.HelperClass;
import com.nickteck.teacherapp.api.ApiClient;
import com.nickteck.teacherapp.api.ApiInterface;
import com.nickteck.teacherapp.model.AttendenceClassDetails;
import com.nickteck.teacherapp.utilclass.Constants;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendenceFragment extends Fragment {

    View view;
    Spinner select_class_spinner,select_sec_spinner;
    ApiInterface apiInterface;
    TSnackbar tSnackbar;
    boolean isNetworkConnected= false;
    ProgressBar progress;
    ArrayList<AttendenceClassDetails.ClassDetails> commonArrayList;
    ArrayList<AttendenceClassDetails.ClassSection> section_arrayList;
    ArrayList<String> section_arrayListText;
    ArrayList<String> class_name_arrayList;
    ArrayList<String> sec_arrayList;
    private int getSelectedItem;
    private String getSelectedSectionValue;
    private String getSelectedClassValue;


    public AttendenceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.attendence_fragment, container, false);

        init();

        return view;
    }

    private void init() {
        class_name_arrayList = new ArrayList<>();
        commonArrayList = new ArrayList<>();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        select_class_spinner = (Spinner) view.findViewById(R.id.select_class_spinner);
        select_sec_spinner = (Spinner) view.findViewById(R.id.select_sec_spinner);
        progress = (ProgressBar) view.findViewById(R.id.progress);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tSnackbar = HelperClass.showTopSnackBar(view, "Network not connected");
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
        else{
            Toast.makeText(getActivity(), "Internet Not Connected", Toast.LENGTH_SHORT).show();
        }
    }

    public void getDataFromServer() {
        if (isNetworkConnected) {
            progress.setVisibility(View.VISIBLE);
            final Call<AttendenceClassDetails> classDetails = apiInterface.getClassDetails();
            classDetails.enqueue(new Callback<AttendenceClassDetails>() {

                @Override
                public void onResponse(Call<AttendenceClassDetails> call, Response<AttendenceClassDetails> response) {
                    if (response.isSuccessful()) {
                        AttendenceClassDetails details = response.body();
                        if (details.getStatus_code() != null) {
                            if (details.getStatus_code().equals(Constants.SUCESS)) {
                                if(details.getClass_details().size()>0){
                                    for(int i=0; i<details.getClass_details().size();i++){

                                        String attendenceClassDetails = details.getClass_details().get(i).getClass_name();
                                        class_name_arrayList.add(attendenceClassDetails);


                                        AttendenceClassDetails.ClassDetails classDetails1 = details.getClass_details().get(i);
                                        classDetails1.setClass_name(details.getClass_details().get(i).getClass_name());
                                        classDetails1.setSections(details.getClass_details().get(i).getSections());
                                        commonArrayList.add(classDetails1);


                                        progress.setVisibility(View.GONE);
                                    }
                                    // getting class name alone in the arraylist
                                    setValueToSpinner(class_name_arrayList);


                                }
                            }

                        }
                    }else {

                    }

                }

                @Override
                public void onFailure(Call<AttendenceClassDetails> call, Throwable t) {

                }
            });


        }else {
            Toast.makeText(getActivity(),"Internet Not Connected", Toast.LENGTH_SHORT).show();
        }
    }

    private void setValueToSpinner(final ArrayList<String> class_name_arrayList){
        // setting class name alone in the adapter
        ArrayAdapter<String> classNameAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                class_name_arrayList);

        classNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // setting default value for the class name arraylist
        String defaultValue = "Select Class";
        class_name_arrayList.add(0,defaultValue);

        select_class_spinner.setAdapter(classNameAdapter);
        int selectionPosition= classNameAdapter.getPosition("Select Class");
        // by default showing default value in the spinner
        select_class_spinner.setSelection(selectionPosition);
        select_class_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               getSelectedClassValue = class_name_arrayList.get(position);

              // in case user again clicks the select class to handle that added default value in the sec arraylist
              if(getSelectedClassValue.equals("Select Class")){
                  section_arrayListText = new ArrayList<>();
                  section_arrayListText.add(0,"Select Sec");
                  SetValueSectionSpinnerText(section_arrayListText);


              }else {
                  // choosing other then select class getting specific class section arraylist
                  for(int i=0; i<commonArrayList.size(); i++){
                      if(commonArrayList.get(i).getClass_name().equals(getSelectedClassValue)){
                          section_arrayList = new ArrayList<>();
                          for(int j=0; j<commonArrayList.get(i).getSections().size(); j++){
                              //  section_arrayList = new ArrayList<>();
                              section_arrayList.add(commonArrayList.get(i).getSections().get(j));
                          }
                          // geeting section for specific class selected
                          SetValueSectionSpinner(section_arrayList);
                      }else {
                          Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                      }
                  }

              }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void SetValueSectionSpinnerText(ArrayList<String> section_arrayList) {
        // showing default value in  the class sec spinner arraylist
        ArrayAdapter<String> secNameAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, section_arrayList);
        secNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_sec_spinner.setAdapter(secNameAdapter);
    }

    private void SetValueSectionSpinner(ArrayList<AttendenceClassDetails.ClassSection> section_arrayList) {
        sec_arrayList = new ArrayList<>();
        for(int i=0; i<section_arrayList.size();i++){
            sec_arrayList.add(section_arrayList.get(i).getSections());
        }

        ArrayAdapter<String> secNameAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sec_arrayList);
        secNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String defaultValue = "Select Sec";
        sec_arrayList.add(0,defaultValue);

        select_sec_spinner.setAdapter(secNameAdapter);
        select_sec_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSelectedSectionValue = sec_arrayList.get(position);
                if(checkValue(getSelectedSectionValue)){
                    // if class and sec are choosed then sending selected values for the api
                    getStudentListApi(getSelectedClassValue,getSelectedSectionValue);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private boolean checkValue(String getSelectedSectionValue) {
        if(getSelectedSectionValue.equals("Select Sec")){
            Toast.makeText(getActivity(), "reachd false", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    private void getStudentListApi(String getSelectedClassValue, String getSelectedSectionValue) {
        if (isNetworkConnected) {
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject jsonObject = new JSONObject();
            
        }








    }


}
