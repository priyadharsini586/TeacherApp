package com.nickteck.teacherapp.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 7/2/2018.
 */

public class LoginDetails {

    private String Status_code;
    private String Status_message;
    private ArrayList<TeacherDetails> teacher_details;
    private ArrayList<SchoolDetails> school_details;

    private String Success;
    private String OTP;


    public String getStatus_code() {
        return Status_code;
    }

    public void setStatus_code(String status_code) {
        Status_code = status_code;
    }

    public String getStatus_message() {
        return Status_message;
    }

    public void setStatus_message(String status_message) {
        Status_message = status_message;
    }

    public ArrayList<TeacherDetails> getTeacher_details() {
        return teacher_details;
    }

    public void setTeacher_details(ArrayList<TeacherDetails> teacher_details) {
        this.teacher_details = teacher_details;
    }

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public ArrayList<SchoolDetails> getSchool_details() {
        return school_details;
    }

    public void setSchool_details(ArrayList<SchoolDetails> school_details) {
        this.school_details = school_details;
    }


    public class TeacherDetails{

        private String id;
        private String name;
        private String photo;
        private String subject;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }
    }

    public class SchoolDetails{

        private String school_name;
        private String logo;
        private String photo;

        public String getSchool_name() {
            return school_name;
        }

        public void setSchool_name(String school_name) {
            this.school_name = school_name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }


    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray teacherDetailsArray = new JSONArray();
            for (int i = 0 ; i < teacher_details.size(); i++){
                LoginDetails.TeacherDetails mteacherDetails  = teacher_details.get(i);
                JSONObject techObject = new JSONObject();
                techObject.put("id",mteacherDetails.getId());
                techObject.put("name",mteacherDetails.getName());
                techObject.put("photo",mteacherDetails.getPhoto());
                techObject.put("subject",mteacherDetails.getSubject());
                teacherDetailsArray.put(techObject);
            }
            jsonObject.put("teacher_details",teacherDetailsArray);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonObject;

    }

    public JSONObject toJSONS(){
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray schoolDetailsArray = new JSONArray();
            for (int i = 0 ; i < school_details.size(); i++){
                LoginDetails.SchoolDetails mschoolDetails  = school_details.get(i);
                JSONObject schoolObject = new JSONObject();
                schoolObject.put("school_name",mschoolDetails.getSchool_name());
                schoolObject.put("logo",mschoolDetails.getLogo());
                schoolObject.put("photo",mschoolDetails.getPhoto());
                schoolDetailsArray.put(schoolObject);
            }
            jsonObject.put("school_details",schoolDetailsArray);

        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonObject;
    }
}
