package com.example.vclasslogin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "VClass_Users.db";
    public static final String TABLE_NAME1 = "all_users";
    public static final String TABLE_NAME2 = "teachers";
    public static final String TABLE_NAME3 = "students";
    public static final String TABLE_NAME4= "courses";
    public static final String TABLE_NAME5= "timeSlots";

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
        MyDB.execSQL("create Table if not exists " + TABLE_NAME3 + "(userID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, mobileNo TEXT, email TEXT, username TEXT, password TEXT, class TEXT,section TEXT)");
        MyDB.execSQL("create Table if not exists " + TABLE_NAME4 + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, courseCode TEXT, courseName TEXT, creditHrs TEXT, description TEXT)");
        MyDB.execSQL("create Table if not exists " + TABLE_NAME5 + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, classTitle TEXT, classPlace TEXT, professorName TEXT, day Integer,startHr Integer,startMin Integer,endHr Integer,endMin Integer,weekday Integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if exists " + TABLE_NAME1);
        MyDB.execSQL("drop Table if exists " + TABLE_NAME2);
        MyDB.execSQL("drop Table if exists " + TABLE_NAME3);
        MyDB.execSQL("drop Table if exists " + TABLE_NAME4);
        MyDB.execSQL("drop Table if exists " + TABLE_NAME5);
        onCreate(MyDB);
    }


    //////////////Teacher Table Functions------------------------------------------------
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
    public Boolean updateTeacherData(int id,String name, String mobileNo, String email, String username, String password, String spec) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("mobileNo", mobileNo);
        contentValues.put("email", email);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("specialization", spec);
        if(id==-1)
            return false;

        long result = MyDB.update(TABLE_NAME2,contentValues,"userid="+id,null);
        if (result == -1)
            return false;
        return true;
    }
    public Boolean deleteTeacherData(int id) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
if(id==-1)
            return false;

        long result = MyDB.delete(TABLE_NAME2,"userid="+id,null);
        if (result == -1)
            return false;
        return true;
    }

    public Boolean doesTeacherUserNameExist (String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME2 + " where username = ?", new String[] {username});

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
    public Boolean doesTeacherMobileNumberExist (String mobileNo) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME2 + " where mobileNo = ?", new String[] {mobileNo});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }
    int getTeacherID(String username) {

        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME2 + " where username = ?", new String[] {username});

        int idx =  cursor.getColumnIndex("userID");
        //Log.v("UserType", String.valueOf(idx));

        cursor.moveToFirst();

        int id = cursor.getInt(idx);
        //Log.v("UserType", str);

        return id;
    }


    ////////////////////Student Table Functions------------------------------------------
    public Boolean insertStudentData(String name, String mobileNo, String email, String username, String password, String c,String section) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("mobileNo", mobileNo);
        contentValues.put("email", email);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("class", c);
        contentValues.put("section", section);


        long result = MyDB.insert(TABLE_NAME3, null, contentValues);
        if (result == -1)
            return false;
        return true;
    }
    public Boolean updateStudentData(int id,String name, String mobileNo, String email, String username, String password, String c,String section) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("mobileNo", mobileNo);
        contentValues.put("email", email);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("class", c);
        contentValues.put("section", section);
        if(id==-1)
            return false;

        long result = MyDB.update(TABLE_NAME3,contentValues,"userid="+id,null);
        if (result == -1)
            return false;
        return true;
    }
    public Boolean deleteStudentData(int id) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        if(id==-1)
            return false;

        long result = MyDB.delete(TABLE_NAME3,"userid="+id,null);
        if (result == -1)
            return false;
        return true;
    }

    public Boolean doesStudentUserNameExist (String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME3 + " where username = ?", new String[] {username});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }
    public Boolean doesStudentEmailExist (String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME3 + " where email = ?", new String[] {email});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }
    public Boolean doesStudentMobileNumberExist (String mobileNo) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME3 + " where mobileNo = ?", new String[] {mobileNo});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }
    int getStudentID(String username) {

        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME3 + " where username = ?", new String[] {username});

        int idx =  cursor.getColumnIndex("userID");
        //Log.v("UserType", String.valueOf(idx));

        cursor.moveToFirst();

        int id = cursor.getInt(idx);
        //Log.v("UserType", str);

        return id;
    }


    ////////////////////Course Table Functions------------------------------------------
    public Boolean insertCourseData(String courseCode, String courseName, String creditHrs, String description) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("courseCode", courseCode);
        contentValues.put("courseName", courseName);
        contentValues.put("creditHrs", creditHrs);
        contentValues.put("description", description);


        long result = MyDB.insert(TABLE_NAME4, null, contentValues);
        if (result == -1)
            return false;
        return true;
    }
    public Boolean updateCourseData(int id,String courseCode, String courseName, String creditHrs, String description) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("courseCode", courseCode);
        contentValues.put("courseName", courseName);
        contentValues.put("creditHrs", creditHrs);
        contentValues.put("description", description);
        if(id==-1)
            return false;

        long result = MyDB.update(TABLE_NAME4,contentValues,"id="+id,null);
        if (result == -1)
            return false;
        return true;
    }
    public Boolean deleteCourseData(int id) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        if(id==-1)
            return false;

        long result = MyDB.delete(TABLE_NAME4,"id="+id,null);
        if (result == -1)
            return false;
        return true;
    }

    public Boolean doesCourseNameExist (String name) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME4 + " where courseName = ?", new String[] {name});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }
    public Boolean doesCourseCodeExist (String code) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME4 + " where courseCode = ?", new String[] {code});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }
    int getCourseID(String courseCode) {

        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME4 + " where courseCode = ?", new String[] {courseCode});

        int idx =  cursor.getColumnIndex("ID");
        Log.v("Course ID column", String.valueOf(idx));

        cursor.moveToFirst();

        int id = cursor.getInt(idx);
        //Log.v("UserType", str);

        return id;
    }


    ////////////////////////////////////////User Table-------------------------------------------

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

    public Boolean doesUserNameExist (String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where username = ?", new String[] {username});

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


    public Boolean doesMobileNumberExist (String mobileNo) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where mobileNo = ?", new String[] {mobileNo});

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

        return str;}




//////////////////////// Other Functions------------------------------------------
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




    public Boolean insertTimeSlot(Schedule schedule, int day) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("classTitle", schedule.getClassTitle() );
        contentValues.put("professorName", schedule.getProfessorName());
        contentValues.put("day", schedule.getDay());
        contentValues.put("startHr", schedule.getStartTime().getHour());
        contentValues.put("startMin", schedule.getStartTime().getMinute());
        contentValues.put("endHr", schedule.getEndTime().getHour());
        contentValues.put("endMin", schedule.getEndTime().getMinute());
        contentValues.put("weekday", day);

        long result = MyDB.insert(TABLE_NAME5, null, contentValues);
        if (result == -1) {
            Log.v("inserting into timeslot","failed");
            return false;
        }
        return true;
    }
    public void deleteTimeSlotData() {

        SQLiteDatabase MyDB=this.getWritableDatabase();
        MyDB.execSQL("drop Table if exists " + TABLE_NAME5);
        MyDB.execSQL("create Table if not exists " + TABLE_NAME5 + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, classTitle TEXT, classPlace TEXT, professorName TEXT, day Integer,startHr Integer,startMin Integer,endHr Integer,endMin Integer,weekday Integer)");

    }

    public ArrayList<Schedule> getTimeSlots(int day){
        ArrayList<Schedule> arr=new ArrayList<Schedule>();

        String qu = "SELECT * FROM " +TABLE_NAME5+" where weekday ="+Integer.toString(day); //+" where weekday = "+Integer.toString(day);
        SQLiteDatabase MyDB=this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery(qu,null);
        if(cursor==null||cursor.getCount()==0)
        {
            Log.v("inGetTimeSlot","oooooo");
            return arr;
        }
        else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Schedule temp=new Schedule();
                cursor.getInt(0);
                temp.setClassTitle(cursor.getString(1));
                temp.setClassPlace(cursor.getString(2));
                temp.setProfessorName(cursor.getString(3));
                temp.setDay(cursor.getInt(4));
                Time tS=new Time();
                Time tE=new Time();
                tS.setHour(cursor.getInt(5));
                tS.setMinute(cursor.getInt(6));
                tE.setHour(cursor.getInt(7));
                tE.setMinute(cursor.getInt(8));
                temp.setStartTime(tS);
                temp.setEndTime(tE);

                arr.add(temp);

                cursor.moveToNext();
            }
        }

        return arr;
    }


}
