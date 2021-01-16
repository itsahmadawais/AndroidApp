package com.example.challanformgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class RequestFormActivity extends AppCompatActivity {
    EditText txtUserID,txtTitle,txtMessage;
    Button btnCancel, btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_form);
        SessionManager sessionData = new SessionManager(this);
        HashMap<String,String> userData = sessionData.getUserDetails();
        txtUserID = findViewById(R.id.txtStudentID);
        txtUserID.setText(userData.get(SessionManager.USER_ID));
        txtTitle = findViewById(R.id.txtTitle);
        txtMessage = findViewById(R.id.txtMessage);
        btnCancel= findViewById(R.id.btncancel);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent();
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtTitle.getText().toString().trim().length()<=0 && txtMessage.getText().toString().trim().length()<=0)
                {
                    Toast.makeText(RequestFormActivity.this, "Please enter title and message", Toast.LENGTH_SHORT).show();
                }
                else if(txtTitle.getText().toString().trim().length()<=0)
                {
                    Toast.makeText(RequestFormActivity.this, "Please enter title", Toast.LENGTH_SHORT).show();
                }
                else if(txtMessage.getText().toString().trim().length()<=0)
                {
                    Toast.makeText(RequestFormActivity.this, "Please enter message", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intentSubmit = new Intent();
                    intentSubmit.putExtra("title",txtTitle.getText().toString().trim());
                    intentSubmit.putExtra("message",txtMessage.getText().toString().trim());
                    setResult(RESULT_OK,intentSubmit);
                    finish();
                }
            }
        });
    }
}