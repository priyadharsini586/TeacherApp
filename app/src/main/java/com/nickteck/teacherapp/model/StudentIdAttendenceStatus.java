package com.nickteck.teacherapp.model;

import java.util.ArrayList;

/**
 * Created by admin on 7/7/2018.
 */

public class StudentIdAttendenceStatus {

    private ArrayList<StudentAttendeceDetails> attendeceDetails;

    public ArrayList<StudentAttendeceDetails> getAttendeceDetails() {
        return attendeceDetails;
    }

    public void setAttendeceDetails(ArrayList<StudentAttendeceDetails> attendeceDetails) {
        this.attendeceDetails = attendeceDetails;
    }

    public static class StudentAttendeceDetails{
        private String studentId;
        private String attendenceStatus;

        public StudentAttendeceDetails(String studentId, String attendenceStatus) {
            this.studentId = studentId;
            this.attendenceStatus = attendenceStatus;
        }

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }

        public String getAttendenceStatus() {
            return attendenceStatus;
        }

        public void setAttendenceStatus(String attendenceStatus) {
            this.attendenceStatus = attendenceStatus;
        }
    }



}
