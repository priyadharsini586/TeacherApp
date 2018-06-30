package com.nickteck.teacherapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.nickteck.teacherapp.R;

public class LoginActivity extends AppCompatActivity {

    EditText editPhoneNo;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_screen);

        init();
        setOnclickListener();
    }

    private void init() {
        editPhoneNo = (EditText) findViewById(R.id.editPhoneNo);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

    }

    private void setOnclickListener() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
