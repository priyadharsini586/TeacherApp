package com.nickteck.teacherapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nickteck.teacherapp.R;
import com.nickteck.teacherapp.model.StudentList;
import com.nickteck.teacherapp.utilclass.Constants;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 7/6/2018.
 */

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.MyViewHolder> {
    public Context mContext;
    public ArrayList<StudentList.StudentDetails>studentList;
    public ArrayList<Integer> checkBoxIndexList = new ArrayList<>();
    public StudentListAdapter(Context context, ArrayList<StudentList.StudentDetails> studentLists){
        this.mContext = context;
        this.studentList = studentLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_row, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final StudentList.StudentDetails details = studentList.get(position);
        holder.student_name.setText(details.getStudent_name());
        holder.student_roll_no.setText(details.getRoll_no());
        Picasso.get().load(Constants.STUDENT_IMAGE_URI+ details.getStudent_photo()).into(holder.student_image);
        holder.chkSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    checkBoxIndexList.add(position);
                    details.setChecked(true);
                }else {
                    details.setChecked(false);
                    int selectedPosition = checkBoxIndexList.indexOf(position);
                    if (selectedPosition != -1)
                        checkBoxIndexList.remove(selectedPosition);
                }
            }
        });
        if (details.isChecked())
            holder.chkSelect.setChecked(true);
        else
            holder.chkSelect.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return this.studentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView student_name,student_roll_no;
        CircleImageView student_image;
        CheckBox chkSelect;
        private MyViewHolder(View itemView) {
            super(itemView);
            student_name = (TextView) itemView.findViewById(R.id.student_name);
            student_roll_no = (TextView) itemView.findViewById(R.id.student_roll_no);
            student_image = (CircleImageView) itemView.findViewById(R.id.student_image);
            chkSelect = (CheckBox)itemView.findViewById(R.id.chkSelect);
        }
    }

    public ArrayList<Integer> getCheckBoxIndex(){
        return checkBoxIndexList;
    }
}
