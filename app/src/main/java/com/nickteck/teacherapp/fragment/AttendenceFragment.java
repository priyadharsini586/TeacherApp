package com.nickteck.teacherapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nickteck.teacherapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendenceFragment extends Fragment {

    View view;


    public AttendenceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.attendence_fragment, container, false);

        return view;
    }

}
