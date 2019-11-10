package com.example.yourplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

public class MainActivity extends AppCompatActivity {

    private String code;
    private int pageNumber = 1;

    private static String GET_URL = "https://www.alarstudios.com/test/data.cgi?code=%s&p=%s";
    private final static Integer PORT = 443;

    RecyclerView recyclerView;
    List<MapPointsClass> mapPointsClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        code = getIntent().getExtras().getString(LoginActivity.EXTRA_ID);
        mapPointsClass = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);

        sendGETRequest();
    }

    private void sendGETRequest(){

        new Thread(() -> {
            try {
                URL urlObject = new URL(String.format(GET_URL, code, pageNumber));

                // socket without ssl returns 301 error, so using ssl socket for working with https
                Socket socket = SSLSocketFactory.getDefault().createSocket(urlObject.getHost(), PORT);

                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.println("GET " + urlObject.getFile() + " HTTP/1.0\r\nHost: " + urlObject.getHost()+ "\r\n\r\n");
                printWriter.flush();

                BufferedReader response = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String responseStr = response.readLine();
                while (responseStr != null && responseStr != "") {
                    if (responseStr.contains("status")){
                        JSONObject obj = new JSONObject(responseStr);
                        if (obj.get("status").equals("ok")){
                            for (int i=0; i < obj.getJSONArray("data").length(); i++) {
                                JSONObject mapItem = obj.getJSONArray("data").getJSONObject(i);
                                mapPointsClass.add(new MapPointsClass(mapItem.getString("id"), mapItem.getString("name"), mapItem.getString("country"), mapItem.getDouble("lat"), mapItem.getDouble("lon")));
                            }

                            System.out.println(mapPointsClass.get(0).getName());
                        }
                        break;
                    }
                    System.out.println(responseStr);
                    responseStr = response.readLine();
                }
                response.close();
                printWriter.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
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
