package com.nickteck.teacherapp.additional_class;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import com.androidadvance.topsnackbar.TSnackbar;
import com.nickteck.teacherapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by admin on 6/13/2018.
 */

public class HelperClass {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    public static boolean isValidMobile(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() < 6 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;

            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;
    }

    public static ArrayList getAllvaluesFromHashMap (HashMap<Object,Object> hashmap){
        ArrayList<Object> values = new ArrayList<>();
        for ( Map.Entry<Object, Object> entry : hashmap.entrySet()) {
            Object key = entry.getValue();
            values.add(key);
        }
        return values;
    }

    public static ArrayList getAllKeyFromHashMap (HashMap<Object,Object> hashmap){
        ArrayList<Object> values = new ArrayList<>();
        for ( Map.Entry<Object, Object> entry : hashmap.entrySet()) {
            Object key = entry.getKey();
            values.add(key);
        }
        return values;
    }



    public static TSnackbar showTopSnackBar(View view, String content) {
        TSnackbar snackbar = TSnackbar.make(view, content, TSnackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#e50000"));
        snackbar.setMaxWidth(view.getWidth());
        snackbar.setDuration(50000);
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        textView.setTypeface(Typeface.DEFAULT_BOLD);

        return snackbar;
    }
    /*private void openCustomDialoge(View v, Activity activity) {
        // common custom alert dialoge
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.custom_alert_dialoge);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }*/



    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static class MyNetworkTask extends AsyncTask<URL, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(URL... urls) {

            Bitmap networkBitmap = null;

            URL networkUrl = urls[0]; //Load the first element
            try {
                networkBitmap = BitmapFactory.decodeStream(
                        networkUrl.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return networkBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

        }


    }

    public static void replaceFragment(Fragment fragment, String fragmentTag, AppCompatActivity context) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.replace(R.id.rldMainContainer, fragment, fragmentTag);
        fragmentTransaction.commit();


    }

    //Convert Date to Calendar
    public static Calendar dateToCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;

    }

    //Convert Calendar to Date
    public static Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }
}
