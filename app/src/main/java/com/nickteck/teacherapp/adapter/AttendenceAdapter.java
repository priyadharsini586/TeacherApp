package com.nickteck.teacherapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nickteck.teacherapp.R;
import com.nickteck.teacherapp.model.StudentList;
import com.nickteck.teacherapp.utilclass.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 7/2/2018.
 */

public class AttendenceAdapter extends RecyclerView.Adapter<AttendenceAdapter.ViewHolder> {

    Activity activity;
    ArrayList<StudentList.StudentDetails> studentDetailsArrayList;
    Context context;

    public AttendenceAdapter(Activity activity, ArrayList<StudentList.StudentDetails> studentDetailsArrayList, Context context) {
        this.activity = activity;
        this.studentDetailsArrayList = studentDetailsArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendence_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(Constants.STUDENT_IMAGE_URI+
                studentDetailsArrayList.get(position).getStudent_photo())
                .placeholder(R.drawable.camera_icon)
                .into(holder.student_image);

        holder.student_name.setText(studentDetailsArrayList.get(position).getStudent_name());
        holder.student_roll_no.setText(studentDetailsArrayList.get(position).getRoll_no());


    }

    @Override
    public int getItemCount() {
        return studentDetailsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView student_image;
        private TextView student_name;
        private TextView student_roll_no;
        private Button button_status;



        public ViewHolder(View itemView) {
            super(itemView);

            student_image = itemView.findViewById(R.id.student_image);
            student_name = itemView.findViewById(R.id.student_name);
            student_roll_no = itemView.findViewById(R.id.student_roll_no);
            button_status = itemView.findViewById(R.id.button_status);


        }
    }
}
