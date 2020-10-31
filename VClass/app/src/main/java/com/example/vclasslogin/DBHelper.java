package com.example.vclasslogin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "VClass_Users.db";
    public static final String TABLE_NAME1 = "all_users";
    public static final String TABLE_NAME2 = "teachers";
    public static final String TABLE_NAME3 = "students";


    public DBHelper(Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {

        MyDB.execSQL("create Table if not exists " + TABLE_NAME1 + "(userID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, mobileNo TEXT, email TEXT, username TEXT, password TEXT, user_type TEXT)");
        MyDB.execSQL("create Table if not exists " + TABLE_NAME2 + "(userID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, mobileNo TEXT, email TEXT, username TEXT, password TEXT, specialization TEXT)");
      //  MyDB.execSQL("create Table " + TABLE_NAME3 + "(userID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, mobileNo TEXT, email TEXT, username TEXT, password TEXT, user_type TEXT)");


    }
    public void createTables(){
        SQLiteDatabase MyDB=this.getWritableDatabase();
        MyDB.execSQL("create Table if not exists " + TABLE_NAME1 + "(userID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, mobileNo TEXT, email TEXT, username TEXT, password TEXT, user_type TEXT)");
        MyDB.execSQL("create Table if not exists " + TABLE_NAME2 + "(userID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, mobileNo TEXT, email TEXT, username TEXT, password TEXT, specialization TEXT)");

    }
    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if exists " + TABLE_NAME1);
        MyDB.execSQL("drop Table if exists " + TABLE_NAME2);
        onCreate(MyDB);
    }

    public Boolean insertData(String name, String mobileNo, String email, String username, String password, String type) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("mobileNo", mobileNo);
        contentValues.put("email", email);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("user_type", type);

        long result = MyDB.insert(TABLE_NAME1, null, contentValues);
        if (result == -1)
            return false;
        return true;
    }
    public Boolean insertTeacherData(String name, String mobileNo, String email, String username, String password, String spec) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("mobileNo", mobileNo);
        contentValues.put("email", email);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("specialization", spec);

        long result = MyDB.insert(TABLE_NAME2, null, contentValues);
        if (result == -1)
            return false;
        return true;
    }

    public Boolean doesUserNameExist (String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where username = ?", new String[] {username});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    public Boolean doesTeacherUserNameExist (String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME2 + " where username = ?", new String[] {username});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    public Boolean doesEmailExist (String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where email = ?", new String[] {email});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    public Boolean doesTeacherEmailExist (String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME2 + " where email = ?", new String[] {email});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    public Boolean doesMobileNumberExist (String mobileNo) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where mobileNo = ?", new String[] {mobileNo});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }
    public Boolean doesTeacherMobileNumberExist (String mobileNo) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME2 + " where mobileNo = ?", new String[] {mobileNo});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }


    public Boolean checkUsernamePassword (String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where username = ? and password = ?", new String[] {username, password});
        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    public Boolean checkTeacherUsernamePassword (String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME2 + " where username = ? and password = ?", new String[] {username, password});
        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    public Boolean checkUser (String username, String password, String type) {

        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where username = ? and password = ? and user_type = ?", new String[] {username, password, type});

        if (cursor.getCount() > 0)
            return true;
        return false;

    }

    public String getUserType (String username, String password) {

        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where username = ? and password = ?", new String[] {username, password});

        int idx =  cursor.getColumnIndex("user_type");
        //Log.v("UserType", String.valueOf(idx));

        cursor.moveToFirst();

        String str = cursor.getString(idx);
        //Log.v("UserType", str);

        return str;
    }

    Cursor execWriteQuery(String qu)
    {
        try {
            SQLiteDatabase MyDB=this.getWritableDatabase();
            return MyDB.rawQuery(qu,null);
        }catch (Exception e)
        {
            Log.e("databaseHandler", qu);
//            Toast.makeText(activity,"Error Occured for execAction",Toast.LENGTH_LONG).show();
        }
        return null;
    }
    Cursor execReadQuery(String qu)
    {
        try {
            SQLiteDatabase MyDB=this.getReadableDatabase();
            return MyDB.rawQuery(qu,null);
        }catch (Exception e)
        {
            Log.e("databaseHandler", qu);
//            Toast.makeText(activity,"Error Occured for execAction",Toast.LENGTH_LONG).show();
        }
        return null;
    }
}
