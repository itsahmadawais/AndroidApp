package com.example.challanformgenerator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ChallanRequests extends AppCompatActivity {
    RecyclerView rcv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challan_requests);
        rcv = findViewById(R.id.rcvHolder);
        setRcvView();
    }
    public void setRcvView()
    {

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String,String> userData = sessionManager.getUserDetails();
        DatabaseHelper dbHelper;
        dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.getMyFormRequests(userData.get(SessionManager.USER_ID));

        if(userData.get(SessionManager.USER_ROLE)=="student")
        {
            cursor = dbHelper.getMyFormRequests(userData.get(SessionManager.USER_ID));
        }
        else
        {
            cursor = dbHelper.getAllRequests();
        }

        ArrayList<RequestsClass> requestsData = new ArrayList<RequestsClass>();

        if(cursor.moveToNext())
        {
            do
            {
                RequestsClass dataHolder = new RequestsClass();
                dataHolder.title="Some Title";
                ChallanForm cf = new ChallanForm();
                dataHolder.status=cursor.getString(cursor.getColumnIndex(cf.COLUMN_NAME_STATUS));
                dataHolder.title=cursor.getString(cursor.getColumnIndex(cf.COLUMN_NAME_TITLE));
                dataHolder.requested_by=cursor.getString(cursor.getColumnIndex(cf.COLUMN_NAME_REQUEST_USER_ID));
                dataHolder.date=cursor.getString(cursor.getColumnIndex(cf.COLUMN_NAME_DATE));
                dataHolder.rowID=cursor.getInt(cursor.getColumnIndex(cf.COLUMN_NAME_PRIMARY_KEY));
                requestsData.add(dataHolder);
            }
            while(cursor.moveToNext());
        }


        CustomAdapter customAdapter = new CustomAdapter(this,requestsData);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mainmenu,menu);
        SessionManager sessionManager = new SessionManager(this);
        if(sessionManager.getUserrole().equals("admin") || sessionManager.getUserrole().equals("coordinator"))
        {
            menu.findItem(R.id.requestForm).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                SessionManager sessionManager = new SessionManager(this);
                sessionManager.logout();
                finish();
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.requestForm:
                Intent intent1 = new Intent(this,RequestFormActivity.class);
                startActivityForResult(intent1,123);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123)
        {
            if(resultCode==RESULT_OK)
            {
                String title = data.getStringExtra("title");
                String message = data.getStringExtra("message");
                SessionManager sessionData = new SessionManager(this);
                HashMap<String,String> userData = sessionData.getUserDetails();
                DatabaseHelper dbHelper;
                dbHelper = new DatabaseHelper(this);
                dbHelper.PostStudentRequest(userData.get(SessionManager.USER_ID),title,message);
                setRcvView();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRcvView();
    }
}