package com.example.vclasslogin;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "VClass_Users.db";
    public static final String TABLE_NAME1 = "all_users";
    public static final String TABLE_NAME2 = "teachers";
    public static final String TABLE_NAME3 = "students";
    public static final String TABLE_NAME4 = "courses";
    public static final String TABLE_NAME5 = "timeSlots";
    public static final String TABLE_NAME6 = "teacher_course";
    public static final String TABLE_NAME7 = "student_course";
    public static final String TABLE_CLASS_DETAILS = "class_details";

    private Context context;
    private static String FIREBASE_URL = "https://vclass-47776.firebaseio.com/";
    private Firebase mRef;
    private Firebase mBoardsRef;
    private Firebase mSegmentsRef;
    private FirebaseListAdapter<HashMap> mBoardListAdapter;
    private ValueEventListener mConnectedListener;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        Firebase.setAndroidContext(context);
        mRef = new Firebase(FIREBASE_URL);
        mBoardsRef = mRef.child("boardmetas");
        mBoardsRef.keepSynced(true); // keep the board list in sync
        mSegmentsRef = mRef.child("boardsegments");
        SyncedBoardManager.setContext(context);
        SyncedBoardManager.restoreSyncedBoards(mSegmentsRef);
        //  MyDB.execSQL("create Table if not exists " + TABLE_NAME1 + "(userID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, mobileNo TEXT, email TEXT, username TEXT, password TEXT, user_type TEXT)");
        //  MyDB.execSQL("create Table if not exists " + TABLE_NAME2 + "(userID INTEGER PRIMARY KEY, specialization TEXT, FOREIGN KEY (userID) references all_users (userID)  )");


    }

    public void createTables() {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        MyDB.execSQL("create Table if not exists " + TABLE_NAME1 + "(userID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, mobileNo TEXT, email TEXT, username TEXT, password TEXT, user_type TEXT)");
        MyDB.execSQL("create Table if not exists " + TABLE_NAME2 + "(userID INTEGER PRIMARY KEY, specialization TEXT, FOREIGN KEY (userID) references all_users (userID)  )");
        MyDB.execSQL("create Table if not exists " + TABLE_NAME3 + "(userID INTEGER PRIMARY KEY, class TEXT,section TEXT, FOREIGN KEY (userID) references all_users (userID)  )");
        MyDB.execSQL("create Table if not exists " + TABLE_NAME4 + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, courseCode TEXT, courseName TEXT, creditHrs TEXT, description TEXT)");
        MyDB.execSQL("create Table if not exists " + TABLE_NAME5 + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, classTitle TEXT, classPlace TEXT, professorName TEXT, day Integer,startHr Integer,startMin Integer,endHr Integer,endMin Integer,weekday Integer)");
        MyDB.execSQL("create Table if not exists " + TABLE_NAME6 + "(teacherID INTEGER, courseID INTEGER, PRIMARY KEY (teacherID, courseID),FOREIGN KEY (teacherID) references teachers(userid) on delete cascade, FOREIGN KEY (courseID) references courses (id) on delete cascade)");
        MyDB.execSQL("create Table if not exists " + TABLE_NAME7 + "(studentID INTEGER, courseID INTEGER, PRIMARY KEY(studentID, courseID),FOREIGN KEY (studentID) references students(userid) on delete cascade, FOREIGN KEY (courseID) references courses (id) on delete cascade)");
        MyDB.execSQL("create Table if not exists " + TABLE_CLASS_DETAILS + "(class_title TEXT PRIMARY KEY, whiteboard_id TEXT, chat_pass TEXT)");
    }

    public void initTeachersAndStudents() {
        insertStudentData("student1", "0123456781", "s1@gmail.com", "student1", "1234", "2017", "A");
        insertStudentData("student2", "0123456782", "s2@gmail.com", "student2", "1234", "2017", "A");
        insertStudentData("student3", "0123456783", "s3@gmail.com", "student3", "1234", "2017", "A");
        insertStudentData("student4", "0123456784", "s4@gmail.com", "student4", "1234", "2017", "A");
        insertStudentData("student5", "0123456785", "s5@gmail.com", "student5", "1234", "2017", "A");
        insertStudentData("student6", "0123456786", "s6@gmail.com", "student6", "1234", "2017", "A");
        insertStudentData("student7", "0123456787", "s7@gmail.com", "student7", "1234", "2017", "A");
        insertStudentData("student8", "0123456788", "s8@gmail.com", "student8", "1234", "2017", "A");
        insertStudentData("student9", "0123456789", "s9@gmail.com", "student9", "1234", "2017", "A");

        insertTeacherData("teacher1", "01234567891", "t1@gmail.com", "teacher1", "1234", "something");
        insertTeacherData("teacher2", "01234567892", "t2@gmail.com", "teacher2", "1234", "something");
        insertTeacherData("teacher3", "01234567893", "t3@gmail.com", "teacher3", "1234", "something");
        insertTeacherData("teacher4", "01234567894", "t4@gmail.com", "teacher4", "1234", "something");
        insertTeacherData("teacher5", "01234567895", "t5@gmail.com", "teacher5", "1234", "something");
        insertTeacherData("teacher6", "01234567896", "t6@gmail.com", "teacher6", "1234", "something");
        insertTeacherData("teacher7", "01234567897", "t7@gmail.com", "teacher7", "1234", "something");
        insertTeacherData("teacher8", "01234567898", "t8@gmail.com", "teacher8", "1234", "something");
        insertTeacherData("teacher9", "01234567899", "t9@gmail.com", "teacher9", "1234", "something");

    }

    public void initStudentCourse() {
        setDefaultStudentCourse("CS118");
        setDefaultStudentCourse("CS217");
        setDefaultStudentCourse("CS218");
        setDefaultStudentCourse("EE227");
        setDefaultStudentCourse("CS211");

    }

    private void setDefaultStudentCourse(String crs) {
        insertStudentCourseData(getStudentID("student1"), getCourseID(crs));
        insertStudentCourseData(getStudentID("student2"), getCourseID(crs));
        insertStudentCourseData(getStudentID("student3"), getCourseID(crs));
        insertStudentCourseData(getStudentID("student4"), getCourseID(crs));
        insertStudentCourseData(getStudentID("student5"), getCourseID(crs));
        insertStudentCourseData(getStudentID("student6"), getCourseID(crs));
        insertStudentCourseData(getStudentID("student7"), getCourseID(crs));
        insertStudentCourseData(getStudentID("student8"), getCourseID(crs));
        insertStudentCourseData(getStudentID("student9"), getCourseID(crs));

    }

    public void initTeacherCourse() {
        insertTeacherCourseData(getTeacherID("teacher1"), getCourseID("CS118"));
        insertTeacherCourseData(getTeacherID("teacher2"), getCourseID("CS217"));
        insertTeacherCourseData(getTeacherID("teacher3"), getCourseID("CS218"));
        insertTeacherCourseData(getTeacherID("teacher4"), getCourseID("EE227"));
        insertTeacherCourseData(getTeacherID("teacher5"), getCourseID("CS211"));
    }


    public void deleteTables() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.execSQL("drop Table if exists " + TABLE_NAME1);

        MyDB.execSQL("drop Table if exists " + TABLE_NAME2);
        MyDB.execSQL("drop Table if exists " + TABLE_NAME3);
        MyDB.execSQL("drop Table if exists " + TABLE_NAME4);
        MyDB.execSQL("drop Table if exists " + TABLE_NAME5);
        MyDB.execSQL("drop Table if exists " + TABLE_NAME6);
        MyDB.execSQL("drop Table if exists " + TABLE_NAME7);
        MyDB.execSQL("drop Table if exists " + TABLE_CLASS_DETAILS);
        //adminonCreate(MyDB);
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
        ContentValues contentValues1 = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("mobileNo", mobileNo);
        contentValues.put("email", email);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("user_type", "teacher");
        long result = MyDB.insert(TABLE_NAME1, null, contentValues);
        if (result == -1)
            return false;

        contentValues1.put("userID", getTeacherID(username));
        contentValues1.put("specialization", spec);
        result = MyDB.insert(TABLE_NAME2, null, contentValues1);
        if (result == -1)
            return false;

        return true;
    }

    public Boolean updateTeacherData(int id, String name, String mobileNo, String email, String username, String password, String spec) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("mobileNo", mobileNo);
        contentValues.put("email", email);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues1.put("specialization", spec);
        if (id == -1)
            return false;

        long result = MyDB.update(TABLE_NAME1, contentValues, "userid=" + id, null);
        if (result == -1)
            return false;
        result = MyDB.update(TABLE_NAME2, contentValues1, "userid=" + id, null);
        if (result == -1)
            return false;
        return true;
    }

    public Boolean deleteTeacherData(int id) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        if (id == -1)
            return false;

        long result = MyDB.delete(TABLE_NAME2, "userid=" + id, null);
        if (result == -1)
            return false;
        result = MyDB.delete(TABLE_NAME1, "userid=" + id, null);
        if (result == -1)
            return false;
        return true;
    }

    public Boolean doesTeacherUserNameExist(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where username = ?", new String[]{username});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    public Boolean doesTeacherEmailExist(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where email = ?", new String[]{email});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    public Boolean doesTeacherMobileNumberExist(String mobileNo) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where mobileNo = ?", new String[]{mobileNo});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    int getTeacherID(String username) {

        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where username = ?", new String[]{username});

        int idx = cursor.getColumnIndex("userID");
        //Log.v("UserType", String.valueOf(idx));

        cursor.moveToFirst();

        int id = cursor.getInt(idx);
        //Log.v("UserType", str);

        return id;
    }


    ////////////////////Student Table Functions------------------------------------------
    public Boolean insertStudentData(String name, String mobileNo, String email, String username, String password, String c, String section) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("mobileNo", mobileNo);
        contentValues.put("email", email);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("user_type", "student");

        long result = MyDB.insert(TABLE_NAME1, null, contentValues);

        if (result == -1)
            return false;
        int id = this.getStudentID(username);
        contentValues1.put("userID", id);
        contentValues1.put("class", c);
        contentValues1.put("section", section);

        result = MyDB.insert(TABLE_NAME3, null, contentValues1);
        if (result == -1)
            return false;

        return true;
    }

    public Boolean updateStudentData(int id, String name, String mobileNo, String email, String username, String password, String c, String section) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        ContentValues contentValues1 = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("mobileNo", mobileNo);
        contentValues.put("email", email);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues1.put("class", c);
        contentValues1.put("section", section);
        if (id == -1)
            return false;

        long result = MyDB.update(TABLE_NAME1, contentValues, "userid=" + id, null);
        if (result == -1)
            return false;
        result = MyDB.update(TABLE_NAME3, contentValues1, "userid=" + id, null);
        if (result == -1)
            return false;
        return true;
    }

    public Boolean deleteStudentData(int id) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        if (id == -1)
            return false;

        long result = MyDB.delete(TABLE_NAME3, "userid=" + id, null);
        if (result == -1)
            return false;
        result = MyDB.delete(TABLE_NAME1, "userid=" + id, null);
        if (result == -1)
            return false;

        return true;
    }

    public Boolean doesStudentUserNameExist(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where username = ?", new String[]{username});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    public Boolean doesStudentEmailExist(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where email = ?", new String[]{email});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    public Boolean doesStudentMobileNumberExist(String mobileNo) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where mobileNo = ?", new String[]{mobileNo});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    int getStudentID(String username) {

        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where username = ?", new String[]{username});

        int idx = cursor.getColumnIndex("userID");
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

    public Boolean updateCourseData(int id, String courseCode, String courseName, String creditHrs, String description) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("courseCode", courseCode);
        contentValues.put("courseName", courseName);
        contentValues.put("creditHrs", creditHrs);
        contentValues.put("description", description);
        if (id == -1)
            return false;

        long result = MyDB.update(TABLE_NAME4, contentValues, "id=" + id, null);
        if (result == -1)
            return false;
        return true;
    }

    public Boolean deleteCourseData(int id) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        if (id == -1)
            return false;

        long result = MyDB.delete(TABLE_NAME4, "id=" + id, null);
        if (result == -1)
            return false;
        return true;
    }

    public Boolean doesCourseNameExist(String name) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME4 + " where courseName = ?", new String[]{name});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    public Boolean doesCourseCodeExist(String code) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME4 + " where courseCode = ?", new String[]{code});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    int getCourseID(String courseCode) {

        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME4 + " where courseCode = ?", new String[]{courseCode});

        int idx = cursor.getColumnIndex("ID");
        Log.v("Course ID column", String.valueOf(idx));

        cursor.moveToFirst();

        int id = cursor.getInt(idx);
        //Log.v("UserType", str);

        return id;
    }

    String getCourseName(int id) {

        String query = "Select * from courses where id=" + id;
        Cursor cursor = this.execReadQuery(query);
        int idx = cursor.getColumnIndex("courseName");

        cursor.moveToFirst();

        String cname = cursor.getString(idx);
        //Log.v("UserType", str);

        return cname;
    }

    int getCourseIDFromCourseName(String courseName) {

        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME4 + " where courseName = ?", new String[]{courseName});

        int idx = cursor.getColumnIndex("ID");

        cursor.moveToFirst();

        int id = cursor.getInt(idx);
        Log.v("Course ID column", String.valueOf(id));

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

    public Boolean doesUserNameExist(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where username = ?", new String[]{username});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    public Boolean doesEmailExist(String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where email = ?", new String[]{email});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }


    public Boolean doesMobileNumberExist(String mobileNo) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where mobileNo = ?", new String[]{mobileNo});

        if (cursor.getCount() > 0)
            return true;
        return false;
    }


    public Boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where username = ? and password = ?", new String[]{username, password});
        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    public Boolean checkTeacherUsernamePassword(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME2 + " where username = ? and password = ?", new String[]{username, password});
        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    public Boolean checkUser(String username, String password, String type) {

        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where username = ? and password = ? and user_type = ?", new String[]{username, password, type});

        if (cursor.getCount() > 0)
            return true;
        return false;

    }

    public String getUserType(String username, String password) {

        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from " + TABLE_NAME1 + " where username = ? and password = ?", new String[]{username, password});

        int idx = cursor.getColumnIndex("user_type");
        //Log.v("UserType", String.valueOf(idx));

        cursor.moveToFirst();

        String str = cursor.getString(idx);
        //Log.v("UserType", str);

        return str;
    }


    //////////////////////// Other Functions------------------------------------------
    Cursor execWriteQuery(String qu) {
        try {
            SQLiteDatabase MyDB = this.getWritableDatabase();
            return MyDB.rawQuery(qu, null);
        } catch (Exception e) {
            Log.e("databaseHandler", qu);
//            Toast.makeText(activity,"Error Occured for execAction",Toast.LENGTH_LONG).show();
        }
        return null;
    }

    Cursor execReadQuery(String qu) {
        try {
            SQLiteDatabase MyDB = this.getReadableDatabase();
            return MyDB.rawQuery(qu, null);
        } catch (Exception e) {
            Log.e("databaseHandler", qu);
//            Toast.makeText(activity,"Error Occured for execAction",Toast.LENGTH_LONG).show();
        }
        return null;
    }

/////////////////////timeSlot functions--------------------------------------------


    public Boolean insertTimeSlot(Schedule schedule, int day) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("classTitle", schedule.getClassTitle());
        contentValues.put("professorName", schedule.getProfessorName());
        contentValues.put("day", schedule.getDay());
        contentValues.put("startHr", schedule.getStartTime().getHour());
        contentValues.put("startMin", schedule.getStartTime().getMinute());
        contentValues.put("endHr", schedule.getEndTime().getHour());
        contentValues.put("endMin", schedule.getEndTime().getMinute());
        contentValues.put("weekday", day);

        long result = MyDB.insert(TABLE_NAME5, null, contentValues);
        if (result == -1) {
            Log.v("inserting into timeslot", "failed");
            return false;
        }
        return true;
    }

    public void deleteTimeSlotData(int weekday) {

        SQLiteDatabase MyDB = this.getWritableDatabase();
        //MyDB.execSQL("drop Table if exists " + TABLE_NAME5);

        //MyDB.execSQL("create Table if not exists " + TABLE_NAME5 + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, classTitle TEXT, classPlace TEXT, professorName TEXT, day Integer,startHr Integer,startMin Integer,endHr Integer,endMin Integer,weekday Integer)");

        long result = MyDB.delete(TABLE_NAME5, "weekday=" + weekday, null);


    }

    public ArrayList<Schedule> getTimeSlots(int day) {
        ArrayList<Schedule> arr = new ArrayList<Schedule>();

        String qu = "SELECT * FROM " + TABLE_NAME5 + " where weekday =" + Integer.toString(day); //+" where weekday = "+Integer.toString(day);
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery(qu, null);
        if (cursor == null || cursor.getCount() == 0) {
            Log.v("inGetTimeSlot", "oooooo");
            return arr;
        } else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Schedule temp = new Schedule();
                cursor.getInt(0);
                temp.setClassTitle(cursor.getString(1));
                temp.setClassPlace(cursor.getString(2));
                temp.setProfessorName(cursor.getString(3));
                temp.setDay(cursor.getInt(4));
                Time tS = new Time();
                Time tE = new Time();
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

    //////////////////////////////teacher-course Table-----------------------------------------------
    public Boolean insertTeacherCourseData(int teacherID, int courseID) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("teacherID", teacherID);
        contentValues.put("courseID", courseID);
        long result = -1;
        try {
            result = MyDB.insert(TABLE_NAME6, null, contentValues);
        } catch (Exception e) {
            Log.v("Exception", "insertTeacherCourseData");
        }
        if (result == -1)
            return false;
        return true;
    }

    public Boolean deleteTeacherCourseData(int teacherid) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        if (teacherid == -1)
            return false;

        long result = MyDB.delete(TABLE_NAME6, "teacherid=" + teacherid, null);
        if (result == -1)
            return false;
        return true;
    }

    //////////////////////////////student-course Table-----------------------------------------------
    public Boolean insertStudentCourseData(int studentID, int courseID) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("studentID", studentID);
        contentValues.put("courseID", courseID);
        long result = -1;
        try {
            result = MyDB.insert(TABLE_NAME7, null, contentValues);
        } catch (Exception e) {
            Log.v("Exception", "insertTeacherCourseData");
        }
        if (result == -1)
            return false;
        return true;
    }

    public Boolean deleteStudentCourseData(int studentid) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        if (studentid == -1)
            return false;

        long result = MyDB.delete(TABLE_NAME7, "studentid=" + studentid, null);
        if (result == -1)
            return false;
        return true;
    }


    public ArrayList<Integer> getStudentCourseIDs(int ID) {
        ArrayList<Integer> arr = new ArrayList<Integer>();

        String qu = "SELECT * FROM " + TABLE_NAME7 + " where studentID =" + Integer.toString(ID); //+" where weekday = "+Integer.toString(day);
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery(qu, null);
        if (cursor == null || cursor.getCount() == 0) {
            Log.v("getStudentCourseID err", "oooooo");
            return arr;
        } else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                arr.add(cursor.getInt(1));
                cursor.moveToNext();
            }
        }

        return arr;
    }

    public ArrayList<Integer> getTeacherCourseIDs(int ID) {
        ArrayList<Integer> arr = new ArrayList<Integer>();

        String qu = "SELECT * FROM " + TABLE_NAME6 + " where teacherID =" + Integer.toString(ID); //+" where weekday = "+Integer.toString(day);
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery(qu, null);
        if (cursor == null || cursor.getCount() == 0) {
            Log.v("getTeacherCourseID err", "oooooo");
            return arr;
        } else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                arr.add(cursor.getInt(1));
                cursor.moveToNext();
            }
        }

        return arr;
    }


    public ArrayList<Schedule> getStudentSchedules(String username) {
        ArrayList<Schedule> sch = new ArrayList<Schedule>();

        ArrayList<Integer> crsID = getStudentCourseIDs(getStudentID(username));

        for (Integer id : crsID) {
            String crsName = getCourseName(id);

            String qu = "SELECT * FROM " + TABLE_NAME5 + " where classTitle  = '" + crsName + "'"; //+" where weekday = "+Integer.toString(day);
            // SQLiteDatabase MyDB=this.getReadableDatabase();
            Cursor cursor = this.execReadQuery(qu);
            //   Cursor cursor = MyDB.rawQuery(qu,null);
            if (cursor == null || cursor.getCount() == 0) {
                Log.v("getStudentSchedules err", "oooooo");
                //     return sch;
            } else {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Schedule temp = new Schedule();
                    cursor.getInt(0);
                    temp.setClassTitle(cursor.getString(1));
                    temp.setClassPlace(cursor.getString(2));
                    temp.setProfessorName(cursor.getString(3));
                    temp.setDay(cursor.getInt(4));
                    Time tS = new Time();
                    Time tE = new Time();
                    tS.setHour(cursor.getInt(5));
                    tS.setMinute(cursor.getInt(6));
                    tE.setHour(cursor.getInt(7));
                    tE.setMinute(cursor.getInt(8));
                    temp.setStartTime(tS);
                    temp.setEndTime(tE);
                    temp.setDay(cursor.getInt(9));
                    sch.add(temp);

                    cursor.moveToNext();
                }
            }
        }
        return sch;
    }

    public ArrayList<Schedule> getTeacherSchedules(String username) {
        ArrayList<Schedule> sch = new ArrayList<Schedule>();

        ArrayList<Integer> crsID = getTeacherCourseIDs(getTeacherID(username));

        for (Integer id : crsID) {
            String crsName = getCourseName(id);

            String qu = "SELECT * FROM " + TABLE_NAME5 + " where classTitle  = '" + crsName + "'"; //+" where weekday = "+Integer.toString(day);
            // SQLiteDatabase MyDB=this.getReadableDatabase();
            Cursor cursor = this.execReadQuery(qu);
            //   Cursor cursor = MyDB.rawQuery(qu,null);
            if (cursor == null || cursor.getCount() == 0) {
                Log.v("getTeacherSchedules err", "oooooo");
                //     return sch;
            } else {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Schedule temp = new Schedule();
                    cursor.getInt(0);
                    temp.setClassTitle(cursor.getString(1));
                    temp.setClassPlace(cursor.getString(2));
                    temp.setProfessorName(cursor.getString(3));
                    temp.setDay(cursor.getInt(4));
                    Time tS = new Time();
                    Time tE = new Time();
                    tS.setHour(cursor.getInt(5));
                    tS.setMinute(cursor.getInt(6));
                    tE.setHour(cursor.getInt(7));
                    tE.setMinute(cursor.getInt(8));
                    temp.setStartTime(tS);
                    temp.setEndTime(tE);
                    temp.setDay(cursor.getInt(9));
                    sch.add(temp);

                    cursor.moveToNext();
                }
            }
        }
        return sch;
    }


    public Boolean canClassBeAdded(Time start, Time end, Integer day, Integer weekday) {

        String qu = "SELECT * FROM " + TABLE_NAME5 + " where day = " + Integer.toString(day) + " and weekday = " + Integer.toString(weekday); //+" where weekday = "+Integer.toString(day);
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery(qu, null);
        if (cursor == null || cursor.getCount() == 0) {
            Log.v("canClassBeAdded", "oooooo");
            return true;
        } else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Schedule temp = new Schedule();
                cursor.getInt(0);
                temp.setClassTitle(cursor.getString(1));
                temp.setClassPlace(cursor.getString(2));
                temp.setProfessorName(cursor.getString(3));
                temp.setDay(cursor.getInt(4));
                Time tS = new Time();
                Time tE = new Time();
                tS.setHour(cursor.getInt(5));
                tS.setMinute(cursor.getInt(6));
                tE.setHour(cursor.getInt(7));
                tE.setMinute(cursor.getInt(8));
                temp.setStartTime(tS);
                temp.setEndTime(tE);

                if (start.getHour() >= tS.getHour() && start.getMinute() >= tS.getMinute()) {
                    if (start.getHour() < tE.getHour() && start.getMinute() < tE.getMinute()) {
                        return false;
                    }
                }

                if (end.getHour() >= tS.getHour() && end.getMinute() >= tS.getMinute()) {
                    if (end.getHour() < tE.getHour() && end.getMinute() < tE.getMinute()) {
                        return false;
                    }
                }

                cursor.moveToNext();

            }

            return true;
        }


    }

    //////////////////////////////Chat Table-----------------------------------------------
    ArrayList<String> getClassTitles() {
        ArrayList<String> classTitles = new ArrayList<>();

        SQLiteDatabase MyDB = this.getReadableDatabase();
        String qu = "SELECT courseName FROM " + TABLE_NAME4;
        Cursor cursor = this.execReadQuery(qu);

        if (cursor == null || cursor.getCount() == 0) {
            Log.v("getTeacherSchedules err", "oooooo");
            //     return sch;
        } else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                classTitles.add(cursor.getString(0));

                cursor.moveToNext();
            }
        }
        return classTitles;
    }

    public void setClassDetailsTable() {
        Log.v("board933", "yes");
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.execSQL("drop Table if exists " + TABLE_CLASS_DETAILS);
        MyDB.execSQL("create Table if not exists " + TABLE_CLASS_DETAILS + "(class_title TEXT PRIMARY KEY, whiteboard_id TEXT, chat_pass TEXT)");

        Log.v("board938", "yes");
        ArrayList<String> classTitles = getClassTitles();

        for (int i = 0; i < 10; i++) {
            Log.v("class_title", classTitles.get(i));
        }

        Log.v("board947", "yes");
        for (int i = 0; i < 10; i++) {
            createBoard2(classTitles.get(i), classTitles.get(i));
            Log.v("board949", "yes");
        }
    }

    public void addWhiteboardForCourse(String str) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        createBoard2(str, str);
    }

    public void removeWhiteboardForCourse(String str) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.delete(TABLE_CLASS_DETAILS, "class_title=\"" + str + "\"", null);
    }

    public void updateWhiteboardForCourse(String old, String str) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("class_title", str);
        contentValues.put("chat_pass", str);
        //Log.v("board990", "yes");
        MyDB.update(TABLE_CLASS_DETAILS, contentValues, "class_title=\"" + old + "\"", null);
        //Log.v("board992", "yes");
    }


    public Boolean insertClassDetails(String ct, String key, String c_pass) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put("class_title", ct);
        //contentValues.put("whiteboard_id", "-MOlP3_hHvHq4lldVqbI");
        contentValues.put("whiteboard_id", key);
        contentValues.put("chat_pass", c_pass);

        Log.v("class", "yes");
        long result = MyDB.insert(TABLE_CLASS_DETAILS, null, contentValues);
        if (result == -1)
            return false;
        return true;
    }

    public void createBoard2(final String ct, final String c_pass) {
        // create a new board
        //final String[] str = new String[1];
        Firebase.setAndroidContext(context);
        mRef = new Firebase(FIREBASE_URL);
        mBoardsRef = mRef.child("boardmetas");
        mBoardsRef.keepSynced(true); // keep the board list in sync
        mSegmentsRef = mRef.child("boardsegments");
        SyncedBoardManager.setContext(context);
        SyncedBoardManager.restoreSyncedBoards(mSegmentsRef);

        final Firebase newBoardRef = mBoardsRef.push();
        Map<String, Object> newBoardValues = new HashMap<>();
        newBoardValues.put("createdAt", ServerValue.TIMESTAMP);
        android.graphics.Point size = new android.graphics.Point();
        newBoardValues.put("width", 1080);
        newBoardValues.put("height", 1794);

        newBoardRef.setValue(newBoardValues, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase ref) {
                if (firebaseError != null) {
                    Log.v("board176", ct + "__" + "__" + c_pass);
                    //throw firebaseError.toException();
                } else {
                    // once the board is created, start a DrawingActivity on it
                    //openBoard(newBoardRef.getKey());
                    Log.v("board1022", ct + "__" + newBoardRef.getKey() + "__" + c_pass);
                    insertClassDetails(ct, newBoardRef.getKey(), c_pass);
                    // str[0] =  newBoardRef.getKey();

                }
            }
        });
    }

    public String getWhiteboardIDFromClassTitle(String classTitle) {

        String qu = "SELECT whiteboard_id FROM " + TABLE_CLASS_DETAILS + " where class_title  = '" + classTitle + "'";
        // SQLiteDatabase MyDB=this.getReadableDatabase();
        Cursor cursor = this.execReadQuery(qu);
        //   Cursor cursor = MyDB.rawQuery(qu,null);
        if (cursor == null || cursor.getCount() == 0) {
            Log.v("board1037", "no");
            //     return sch;
        } else {
            cursor.moveToFirst();
            Log.v("board1041", cursor.getString(0));
            return cursor.getString(0);
        }
        return "";
    }
}
