package com.nickteck.teacherapp.model;

import java.util.ArrayList;

/**
 * Created by admin on 7/4/2018.
 */

public class AttendenceClassDetails {

    private String Status_code;
    private ArrayList<ClassDetails> class_details;

    public String getStatus_code() {
        return Status_code;
    }

    public void setStatus_code(String status_code) {
        Status_code = status_code;
    }

    public ArrayList<ClassDetails> getClass_details() {
        return class_details;
    }

    public void setClass_details(ArrayList<ClassDetails> class_details) {
        this.class_details = class_details;
    }

    public class ClassDetails{

        private String class_name;
        private ArrayList<ClassSection> sections;

        public ClassDetails(String class_name, ArrayList<ClassSection> sections) {
            this.class_name = class_name;
            this.sections = sections;
        }

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        public ArrayList<ClassSection> getSections() {
            return sections;
        }

        public void setSections(ArrayList<ClassSection> sections) {
            this.sections = sections;
        }
    }

    public class ClassSection {

        private String section;

        public String getSections() {
            return section;
        }

        public void setSections(String sections) {
            this.section = sections;
        }
    }
}
