package com.nickteck.teacherapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 7/2/2018.
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TEACHERDB";
    private static final int DATABASE_VERSION = 1;

    //staff table
    private static final String LOGIN_CHECK = "LOGIN_CHECK";
    private static final String MOBILE_NUMBER = "MOBILE_NUMBER";
    private static final String Id = "ID";
    private static final String DEVICE_ID  = "DEVICE_ID";

    // staff Details
    private static final String TEACHER_TABLE= "TABLE_TEACHER";
    private static final String TEACHER_ID = "TEACHER_ID";
    private static final String TEACHER_DETAILS = "TEACHER_DETAILS";
    private static final String SCHOOL_DETAILS = "SCHOOL_DETAILS";

    public DataBaseHandler(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + LOGIN_CHECK + "("
                + Id + " TEXT ,"
                + DEVICE_ID + " TEXT ,"
                + MOBILE_NUMBER + " TEXT " + ")";

        String CREATE_TEACHER_TABLE = "CREATE TABLE " + TEACHER_TABLE + "("
                + TEACHER_ID + " TEXT ,"
                + TEACHER_DETAILS + " TEXT ,"
                + SCHOOL_DETAILS + " TEXT " + ")";

        sqLiteDatabase.execSQL(CREATE_LOGIN_TABLE);
        sqLiteDatabase.execSQL(CREATE_TEACHER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LOGIN_CHECK);

    }

    public void insertLoginTable(String id, String mobileNumber, String device_id){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Id, id);
        values.put(MOBILE_NUMBER,mobileNumber);
        values.put(DEVICE_ID,device_id);

        // Inserting Row
        db.insert(LOGIN_CHECK, null, values);
        db.close();


    }

    public void dropTeacherDetails(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "DELETE  FROM " + TEACHER_TABLE;
        db.execSQL(selectQuery);
        db.close();

    }


    public void dropLoginDetails(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "DELETE  FROM " + LOGIN_CHECK;
        db.execSQL(selectQuery);
        db.close();

    }

    public void insertTeacherDetails(String teacherId,String teacherDetails,String schoolDetails){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TEACHER_ID, teacherId);
        values.put(TEACHER_DETAILS,teacherDetails);
        values.put(SCHOOL_DETAILS,schoolDetails);

        // Inserting Row
        db.insert(TEACHER_TABLE, null, values);
        db.close();

    }

    public String getTeacherDetails(){
        String teacherDetails = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TEACHER_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null)
            cursor.moveToFirst();
        teacherDetails = cursor.getString(1);
        cursor.close();
        db.close();
        return teacherDetails;

    }
    public String getSchoolDetails(){
        String schoolDetails = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TEACHER_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null)
            cursor.moveToFirst();
        schoolDetails = cursor.getString(2);
        cursor.close();
        db.close();
        return schoolDetails;

    }

    public String getMobileNumber(){
        String mobileNumber = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + LOGIN_CHECK;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        mobileNumber = cursor.getString(2);
        cursor.close();
        db.close();

        return mobileNumber;
    }

    public boolean  checkTableIsEmpty(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " +LOGIN_CHECK, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        if(count > 0){
            cursor.close();
            return false;
        }else {
            cursor.close();
            return true;
        }
    }
}
