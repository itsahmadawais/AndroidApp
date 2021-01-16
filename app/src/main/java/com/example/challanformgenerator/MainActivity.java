package com.example.challanformgenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    Button btnLogin;
    EditText txtUserId;
    EditText txtUserPassword;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        btnLogin = findViewById(R.id.btnLogin);
        txtUserId = findViewById(R.id.txtUsername);
        txtUserPassword=findViewById(R.id.txtPassword);
        final SessionManager sessionManager = new SessionManager(this);
        //sessionManager.logout();
        if(sessionManager.isLoggedIn())
        {
            //txtUserId.setText(sessionManager.getUserrole());
            finish();
            Intent intent = new Intent(MainActivity.this,ChallanRequests.class);
            startActivity(intent);
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean flag=false;
                if(txtUserId.getText().toString().trim().length()==0)
                {
                    Toast.makeText(MainActivity.this, "Please enter username!", Toast.LENGTH_SHORT).show();
                    flag=true;
                }
                if(txtUserPassword.getText().toString().trim().length()==0)
                {
                    if(flag)
                    {
                        Toast.makeText(MainActivity.this, "Please enter useraname and password!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Please enter password!", Toast.LENGTH_SHORT).show();
                    }
                    flag=true;
                }
                if(!flag)
                {
                    Cursor cursor = dbHelper.getUserData(txtUserId.getText().toString().trim(),txtUserPassword.getText().toString().trim());
                    if(cursor.moveToNext())
                    {

                        do
                         {
                             Toast.makeText(MainActivity.this, "Log In", Toast.LENGTH_SHORT).show();
                             User user = new User();
                             user.userID = cursor.getString(cursor.getColumnIndex("login_id"));
                             user.role = cursor.getString(cursor.getColumnIndex("user_role"));
                             sessionManager.createLoginSession(user);
                             finish();
                             Intent intent = new Intent(MainActivity.this,ChallanRequests.class);
                             startActivity(intent);
                         }
                        while(cursor.moveToNext());
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Wrong credentials!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public class GetAsynTask extends AsyncTask<String, String, String> {
        public String username;
        public String password;
        public GetAsynTask(String username, String password)
        {
            this.username=username;
            this.password=password;
        }
        @Override
        protected String doInBackground(String... params) {

            try {
                // Creating & connection Connection with url and required Header.
                URL url = new URL(new APICaller().baseURL);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");   //POST or GET
                urlConnection.connect();

                // Create JSONObject Request
                JSONObject jsonRequest = new JSONObject();
                jsonRequest.put("email", username);
                jsonRequest.put("password", password);


                // Write Request to output stream to server.
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(jsonRequest.toString());
                out.close();

                // Check the connection status.
                int statusCode = urlConnection.getResponseCode();

                // Connection success. Proceed to fetch the response.
                if (statusCode == 200) {
                    InputStream it = new BufferedInputStream(urlConnection.getInputStream());
                    InputStreamReader read = new InputStreamReader(it);
                    BufferedReader buff = new BufferedReader(read);
                    StringBuilder dta = new StringBuilder();
                    String chunks;
                    while ((chunks = buff.readLine()) != null) {
                        dta.append(chunks);
                    }
                    String returndata = dta.toString();
                    return returndata;
                } else {
                    Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String resultData) {
            super.onPostExecute(resultData);
            try {
                SessionManager sessionManager = new SessionManager(MainActivity.this);
                JSONObject obj = new JSONObject(resultData);
                String name= obj.getString("api_token");
                sessionManager.setToken(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}