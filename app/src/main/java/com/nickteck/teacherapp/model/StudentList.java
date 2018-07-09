package com.nickteck.teacherapp.model;

import java.util.ArrayList;

/**
 * Created by admin on 7/5/2018.
 */

public class StudentList {

    private String Status_code;
    private String attendnce_taken;
    private String tot_absent;
    private String teacher_name;
    private ArrayList<StudentDetails> student_details;

    public ArrayList<StudentDetails> getStudent_details() {
        return student_details;
    }

    public void setStudent_details(ArrayList<StudentDetails> student_details) {
        this.student_details = student_details;
    }

    public String getStatus_code() {
        return Status_code;
    }

    public void setStatus_code(String status_code) {
        Status_code = status_code;
    }

    public String getAttendnce_taken() {
        return attendnce_taken;
    }

    public void setAttendnce_taken(String attendnce_taken) {
        this.attendnce_taken = attendnce_taken;
    }

    public String getTot_absent() {
        return tot_absent;
    }

    public void setTot_absent(String tot_absent) {
        this.tot_absent = tot_absent;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }


    public class StudentDetails{
        private String student_id;
        private String roll_no;
        private String student_name;
        private String student_photo;
        private String attendance;
        private boolean isChecked = true;


        public String getStudent_id() {
            return student_id;
        }

        public void setStudent_id(String student_id) {
            this.student_id = student_id;
        }

        public String getRoll_no() {
            return roll_no;
        }

        public void setRoll_no(String roll_no) {
            this.roll_no = roll_no;
        }

        public String getStudent_name() {
            return student_name;
        }

        public void setStudent_name(String student_name) {
            this.student_name = student_name;
        }

        public String getStudent_photo() {
            return student_photo;
        }

        public void setStudent_photo(String student_photo) {
            this.student_photo = student_photo;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getAttendance() {
            return attendance;
        }

        public void setAttendance(String attendance) {
            this.attendance = attendance;
        }
    }



}
