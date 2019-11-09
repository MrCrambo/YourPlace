package com.example.yourplace;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import javax.net.ssl.SSLSocketFactory;

public class LoginActivity extends AppCompatActivity {

    EditText loginEditText;
    EditText passwordEditText;
    Button loginButton;

    private String login = "";
    private String password = "";

    private String GET_URL = "https://www.alarstudios.com/test/auth.cgi?username=%s&password=%s";
    final static Integer PORT = 443;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        loginEditText = findViewById(R.id.loginEditText);
        loginEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                login = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        passwordEditText = findViewById(R.id.passwordEditText);
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!login.isEmpty() && !password.isEmpty()){
                    try {
                        sendGETRequest(login, password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void sendGETRequest(String correctLogin, String correctPassword) throws Exception{

        final URL urlObject = new URL(String.format(GET_URL, correctLogin, correctPassword));
        System.out.println(urlObject.getFile());

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    Socket socket = SSLSocketFactory.getDefault().createSocket(urlObject.getHost(), PORT);

                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                    //String httpHeader = "GET " + urlObject.getFile() + " HTTP/1.0\r\nHost: " + urlObject.getHost()+ "\r\n\r\n";
                    printWriter.println("GET " + urlObject.getFile() + " HTTP/1.0\r\nHost: " + urlObject.getHost()+ "\r\n\r\n");
                    printWriter.flush();
                    printWriter.close();

                    BufferedReader response = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String responseStr = response.readLine();
                    while ((responseStr != null) && (responseStr != ""))
                    {
                        System.out.println(responseStr);
                        responseStr = response.readLine();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

}
