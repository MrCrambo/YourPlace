package com.example.yourplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private int code;
    private int pageNumber = 1;

    private static String GET_URL = "https://www.alarstudios.com/test/data.cgi?code=%s&p=%s";
    private final static Integer PORT = 443;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        code = getIntent().getExtras().getInt(LoginActivity.EXTRA_ID);

    }

    

    @Override
    public void onBackPressed() {

        // intent to the main screen of phone
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
