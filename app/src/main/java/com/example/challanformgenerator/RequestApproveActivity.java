package com.example.challanformgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RequestApproveActivity extends AppCompatActivity {
    EditText txtName,txtStdID,txtProgram,txtDepartment,txtMessageTitle,txtMessageBody;
    Button btnApprove,btnCancel;
    DatabaseHelper dbHelper;
    int rID=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_approve);
        Intent intent = getIntent();
        txtName = findViewById(R.id.txtStudentName);
        txtStdID = findViewById(R.id.txtStudentID);
        txtProgram = findViewById(R.id.txtProgram);
        txtDepartment = findViewById(R.id.txtDepartment);
        txtMessageTitle = findViewById(R.id.txtMessageTitle);
        txtMessageBody = findViewById(R.id.txtMessage);
        dbHelper = new DatabaseHelper(this);
        btnApprove = findViewById(R.id.btnApprove);
        btnCancel = findViewById(R.id.btnGoBack);
        int rowID = intent.getIntExtra("rowID",-1);
        if(rowID!=-1)
        {
            rID=rowID;
            Cursor cursor= dbHelper.getRequestData(rowID);
            if(cursor.moveToNext())
            {
                ChallanForm cfDBSchema = new ChallanForm();
                StudentInfoTable studentInfoTable = new StudentInfoTable();
                do{
                    txtName.setText(cursor.getString(cursor.getColumnIndex(studentInfoTable.COLUMN_NAME_FIRST_NAME))+cursor.getString(cursor.getColumnIndex(studentInfoTable.COLUMN_NAME_LAST_NAME)));
                    txtStdID.setText(cursor.getString(cursor.getColumnIndex(cfDBSchema.COLUMN_NAME_REQUEST_USER_ID)));
                    txtProgram.setText(cursor.getString(cursor.getColumnIndex(studentInfoTable.COLUMN_NAME_PROGRAM)));
                    txtDepartment.setText(cursor.getString(cursor.getColumnIndex(studentInfoTable.COLUMN_NAME_DEPARTMENT)));
                    txtMessageTitle.setText(cursor.getString(cursor.getColumnIndex(cfDBSchema.COLUMN_NAME_TITLE)));
                    txtMessageBody.setText(cursor.getString(cursor.getColumnIndex(cfDBSchema.COLUMN_NAME_MESSAGE)));
                    if(cursor.getString(cursor.getColumnIndex(cfDBSchema.COLUMN_NAME_STATUS)).equals("approved"))
                    {
                        btnApprove.setVisibility(View.GONE);
                    }
                }
                while(cursor.moveToNext());
            }
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.UpdateStudentRequestStatus(rID);
                finish();
            }
        });
    }
}