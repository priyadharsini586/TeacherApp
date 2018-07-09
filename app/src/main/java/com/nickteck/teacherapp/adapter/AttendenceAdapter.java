package com.nickteck.teacherapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nickteck.teacherapp.R;
import com.nickteck.teacherapp.fragment.AttendenceFragment;
import com.nickteck.teacherapp.model.StudentIdAttendenceStatus;
import com.nickteck.teacherapp.model.StudentList;
import com.nickteck.teacherapp.utilclass.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 7/2/2018.
 */

public class AttendenceAdapter extends RecyclerView.Adapter<AttendenceAdapter.ViewHolder> implements Filterable {

    Activity activity;
    ArrayList<StudentList.StudentDetails> studentDetailsArrayList = new ArrayList<>();
    ArrayList<StudentList.StudentDetails> mfilteredDataDetailsArrayList = new ArrayList<>();
    Context context;
    private ItemFilter mFilter = new ItemFilter();

    String button_state;
    private List<String> studentIdArrayListData = new ArrayList<>();
    private List<String> studentAttendenceListData = new ArrayList<>();

    private List<StudentIdAttendenceStatus.StudentAttendeceDetails> studentIdArrayListData1 = new ArrayList<>();

    public AttendenceAdapter(Activity activity, ArrayList<StudentList.StudentDetails> studentDetailsArrayList, Context context) {
        this.activity = activity;
        this.studentDetailsArrayList = studentDetailsArrayList;
        this.mfilteredDataDetailsArrayList = studentDetailsArrayList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendence_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final StudentList.StudentDetails studentDetails = mfilteredDataDetailsArrayList.get(position);
        Picasso.get().load(Constants.STUDENT_IMAGE_URI+
                studentDetails.getStudent_photo())
                .placeholder(R.drawable.camera_icon)
                .into(holder.student_image);

        holder.student_name.setText(studentDetails.getStudent_name());
        holder.student_roll_no.setText(studentDetails.getRoll_no());

        if(studentDetails.getAttendance().equals("")){
            holder.button_status.setBackgroundResource(R.drawable.custom_attendance_green);
            for(int i=0; i<studentDetails.getStudent_id().length(); i++){
                StudentIdAttendenceStatus.StudentAttendeceDetails idAttendenceStatus = new StudentIdAttendenceStatus.StudentAttendeceDetails(studentDetails.getStudent_id(),"1");
                studentIdArrayListData1.add(idAttendenceStatus);
                /*studentIdArrayListData.add(studentDetails.getStudent_id());
                studentAttendenceListData.add("1");*/
            }
            setAttendenceArrayList();

        } else if(studentDetails.getAttendance().equals("1")){
            holder.button_status.setBackgroundResource(R.drawable.custom_attendance_green);

        }else if(studentDetails.getAttendance().equals("0")){
            holder.button_status.setBackgroundResource(R.drawable.custom_attendance_orange);

        }else if(studentDetails.getAttendance().equals("-1")){
            holder.button_status.setBackgroundResource(R.drawable.custom_attendance_red);
        }


        holder.button_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getStudentName = studentDetails.getStudent_name();
                String getStudent_id = studentDetails.getStudent_id();
                String getStudent_roll = studentDetails.getRoll_no();

                if(studentDetails.getAttendance().equals("1")){
                    holder.button_status.setBackgroundResource(R.drawable.custom_attendance_orange);
                    holder.button_status.setText("Leave");
                    button_state = "0";
                }/*else if(){

                }*/



            }
        });

    }



    private void setAttendenceArrayList() {
        ArrayList<String> row = new ArrayList<>();
       /* String strArray1;

        String[][] strArray = new String[studentIdArrayListData.size()][studentAttendenceListData.size()];

        for(int i=0; i<studentIdArrayListData.size();i++){
            for(int j=0; j<studentAttendenceListData.size();j++){
                strArray1 = Arrays.deepToString(strArray);

                Toast.makeText(context, ""+strArray1, Toast.LENGTH_SHORT).show();

            }

        }*/
        /*String[] strArray = new String[studentIdArrayListData.size()];
        strArray = studentIdArrayListData.toArray(strArray);
        Toast.makeText(context, ""+strArray, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, ""+strArray, Toast.LENGTH_SHORT).show();*/




        String[][] array = new String[studentIdArrayListData1.size()][];
        for (int i = 0; i < studentIdArrayListData1.size(); i++) {
            row.add(studentIdArrayListData1.get(i).getAttendenceStatus());
            array[i] = row.toArray(new String[row.size()]);
        }


     /*//   student_id_data = new String[Integer.parseInt(studentDetails.getStudent_id())];
        student_id = String.valueOf(studentDetailsArrayList.toArray(new String[]{student_id}));
        Toast.makeText(context, ""+student_id, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, ""+student_id, Toast.LENGTH_SHORT).show();*/



    }

    @Override
    public int getItemCount() {
        return mfilteredDataDetailsArrayList.size();
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

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();

            if (filterString.isEmpty()) {
                mfilteredDataDetailsArrayList = studentDetailsArrayList;
            }else {
                ArrayList<StudentList.StudentDetails> filteredList = new ArrayList<>();
                for (StudentList.StudentDetails row : studentDetailsArrayList) {
                    if (row.getStudent_name().toLowerCase().contains(filterString.toLowerCase()) ||
                            row.getRoll_no().contains(filterString)) {
                        filteredList.add(row);
                    }

                }
                mfilteredDataDetailsArrayList = filteredList;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = mfilteredDataDetailsArrayList;
            filterResults.count = mfilteredDataDetailsArrayList.size();
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(results.count == 0){
                mfilteredDataDetailsArrayList.clear();
                AttendenceFragment.txtNoDataTextview.setVisibility(View.VISIBLE);
            }else {
                mfilteredDataDetailsArrayList = (ArrayList<StudentList.StudentDetails>) results.values;
                AttendenceFragment.txtNoDataTextview.setVisibility(View.GONE);
                notifyDataSetChanged();

            }


        }
    }
}
