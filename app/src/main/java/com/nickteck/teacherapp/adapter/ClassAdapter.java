package com.nickteck.teacherapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nickteck.teacherapp.R;
import com.nickteck.teacherapp.utilclass.Constants;

import java.util.ArrayList;

/**
 * Created by admin on 7/5/2018.
 */

public class ClassAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private ArrayList<String>mClassList;
    String mFrom;
    public ClassAdapter(@NonNull Context context,ArrayList<String> classList,String from) {
        super(context, R.layout.class_list_row);
        this.mContext = context;
        this.mClassList = classList;
        this.mFrom = from;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.class_list_row, null);
        }
        TextView txtClass = convertView.findViewById(R.id.txtClassName);
        txtClass.setText(mClassList.get(position));
        txtClass.setPadding(0, 10, 0, 10);
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.class_list_row, null);
        }
        TextView txtClass = convertView.findViewById(R.id.txtClassName);
        if (mFrom.equals(Constants.CLASS)) {
            if (position != 0)
                txtClass.setText("Class " + mClassList.get(position));
            else
                txtClass.setText(mClassList.get(position));
        }else if (mFrom.equals(Constants.SECTION)){
            if (position != 0)
                txtClass.setText(mClassList.get(position) + " Sec");
            else
                txtClass.setText(mClassList.get(position));
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return mClassList.size();
    }
}
