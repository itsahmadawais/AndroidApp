package com.example.challanformgenerator;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public UserLoginTable user = new UserLoginTable();
    public StudentInfoTable student = new StudentInfoTable();
    public ChallanForm challanForm = new ChallanForm();

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ChallanFormGenerator.db";
    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //User Login Table Creation
        db.execSQL(
                "CREATE TABLE "+user.TABLE_NAME+ "("+user.COLUMN_NAME_ID+" TEXT  primary key, "+user.COLUMN_NAME_PASSWORD+" TEXT,"+user.COLUMN_NAME_ROLE+" TEXT)"
        );
        //Insert Data
        db.execSQL(
                "INSERT INTO "+user.TABLE_NAME+ "("+user.COLUMN_NAME_ID+" , "+user.COLUMN_NAME_PASSWORD+" , "+user.COLUMN_NAME_ROLE+") VALUES(\"admin\",\"123\",\"admin\")"
        );
        db.execSQL(
                "INSERT INTO "+user.TABLE_NAME+ " ("+user.COLUMN_NAME_ID+" , "+user.COLUMN_NAME_PASSWORD+" , "+user.COLUMN_NAME_ROLE+") VALUES(\"coordinator\",\"123\",\"coordinator\")"
        );
        db.execSQL(
                "INSERT INTO "+user.TABLE_NAME+ " ("+user.COLUMN_NAME_ID+" , "+user.COLUMN_NAME_PASSWORD+" , "+user.COLUMN_NAME_ROLE+") VALUES(\"student\",\"123\",\"student\")"
        );

        Log.i("DB_Coordinator","tables created");

        //Student Tabel Cretion
        db.execSQL(
                "CREATE TABLE "+student.TABLE_NAME+ "("+student.COLUMN_NAME_LOGIN_ID+" TEXT,"+student.COLUMN_NAME_FIRST_NAME+" TEXT,"+student.COLUMN_NAME_LAST_NAME+" TEXT,"+student.COLUMN_NAME_DEPARTMENT+" TEXT,"+student.COLUMN_NAME_PROGRAM+" TEXT,"+student.COLUMN_NAME_ROLL_NO+" TEXT)"
        );

        db.execSQL(
                "INSERT INTO "+student.TABLE_NAME+ "("+student.COLUMN_NAME_LOGIN_ID+","+student.COLUMN_NAME_FIRST_NAME+","+student.COLUMN_NAME_LAST_NAME+","+student.COLUMN_NAME_DEPARTMENT+","+student.COLUMN_NAME_PROGRAM+","+student.COLUMN_NAME_ROLL_NO+") VALUES(\"student\",\"Awais\",\"Ahmad\",\"CS\",\"BS\",\"17271519-122\")"
        );

        //Challan Form Table Creation
        db.execSQL(
                "CREATE TABLE "+challanForm.TABLE_NAME+ "("+challanForm.COLUMN_NAME_PRIMARY_KEY+" INTEGER PRIMARY KEY AUTOINCREMENT,"+challanForm.COLUMN_NAME_REQUEST_USER_ID+" TEXT,"+challanForm.COLUMN_NAME_TITLE+" TEXT,"+challanForm.COLUMN_NAME_MESSAGE+" TEXT,"+challanForm.COLUMN_NAME_STATUS+" TEXT,"+challanForm.COLUMN_NAME_DATE+" DATETIME DEFAULT CURRENT_TIMESTAMP)"
        );
        db.execSQL(
                "INSERT INTO "+challanForm.TABLE_NAME+ "("+challanForm.COLUMN_NAME_REQUEST_USER_ID+","+challanForm.COLUMN_NAME_TITLE+","+challanForm.COLUMN_NAME_MESSAGE+","+challanForm.COLUMN_NAME_STATUS+") VALUES(\"student\",\"Challan Request\",\"Please isse me a challan form so I can subnmit the fee.\",\"pending\")"
        );
        Log.i("DB","tables created");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+user.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS"+student.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS"+challanForm.TABLE_NAME);
        onUpgrade(db, oldVersion, newVersion);
    }
    public Cursor getUserData(String u, String p)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM "+user.TABLE_NAME+" WHERE "+user.COLUMN_NAME_ID+"=\""+u+"\" AND "+user.COLUMN_NAME_PASSWORD+"=\""+p+"\";",null);
        return data;
    }
    public Cursor getMyFormRequests(String userID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM "+challanForm.TABLE_NAME+" WHERE "+challanForm.COLUMN_NAME_REQUEST_USER_ID+"=\""+userID+"\"",null);
        return data;
    }
    public Cursor getAllRequests()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM "+challanForm.TABLE_NAME,null);
        return data;
    }
    public void PostStudentRequest(String stdID, String title, String message)
    {
        stdID="\""+stdID+"\"";
        title="\""+title+"\"";
        message="\""+message+"\"";
        String status="\"pending\"";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(
                "INSERT INTO "+challanForm.TABLE_NAME+ "("+challanForm.COLUMN_NAME_REQUEST_USER_ID+","+challanForm.COLUMN_NAME_TITLE+","+challanForm.COLUMN_NAME_MESSAGE+","+challanForm.COLUMN_NAME_STATUS+") VALUES("+stdID+","+title+","+message+","+status+")"

        );
    }
    public Cursor getRequestData(int rowID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery(
                "SELECT * FROM "+challanForm.TABLE_NAME+" c INNER JOIN "+student.TABLE_NAME+" s ON s."+student.COLUMN_NAME_LOGIN_ID+"= c."+challanForm.COLUMN_NAME_REQUEST_USER_ID+" WHERE "+challanForm.COLUMN_NAME_PRIMARY_KEY+"=\""+rowID+"\"",null);
        return data;
    }
    public void UpdateStudentRequestStatus(int rowID)
    {
        String status="\"approved\"";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(
                "UPDATE "+challanForm.TABLE_NAME+" SET "+challanForm.COLUMN_NAME_STATUS+"="+status+" WHERE "+challanForm.COLUMN_NAME_PRIMARY_KEY+"=\""+rowID+"\""

        );
    }
}