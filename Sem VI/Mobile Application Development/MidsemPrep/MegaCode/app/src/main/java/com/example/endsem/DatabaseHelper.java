package com.example.endsem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "students.db";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_STUDENTS = "students";

    // User Table Columns
    public static final String COL_USER_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    // Student Table Columns
    public static final String COL_STU_ID = "id";
    public static final String COL_STU_NAME = "name";
    public static final String COL_STU_REGNO = "regno";
    public static final String COL_STU_DEPT = "department";
    public static final String COL_STU_GENDER = "gender";
    public static final String COL_STU_INTERESTS = "interests";
    public static final String COL_STU_DOB = "dob";
    public static final String COL_STU_TIME = "time";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_PASSWORD + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_STUDENTS + " (" +
                COL_STU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_STU_NAME + " TEXT, " +
                COL_STU_REGNO + " TEXT, " +
                COL_STU_DEPT + " TEXT, " +
                COL_STU_GENDER + " TEXT, " +
                COL_STU_INTERESTS + " TEXT, " +
                COL_STU_DOB + " TEXT, " +
                COL_STU_TIME + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }

    // User Operations
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USERNAME + "=? AND " + COL_PASSWORD + "=?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Student Operations
    public boolean insertStudent(String name, String regno, String dept, String gender, String interests, String dob, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STU_NAME, name);
        values.put(COL_STU_REGNO, regno);
        values.put(COL_STU_DEPT, dept);
        values.put(COL_STU_GENDER, gender);
        values.put(COL_STU_INTERESTS, interests);
        values.put(COL_STU_DOB, dob);
        values.put(COL_STU_TIME, time);
        long result = db.insert(TABLE_STUDENTS, null, values);
        return result != -1;
    }

    public Cursor getAllStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_STUDENTS, null);
    }

    public boolean updateStudent(int id, String name, String regno, String dept, String gender, String interests, String dob, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_STU_NAME, name);
        values.put(COL_STU_REGNO, regno);
        values.put(COL_STU_DEPT, dept);
        values.put(COL_STU_GENDER, gender);
        values.put(COL_STU_INTERESTS, interests);
        values.put(COL_STU_DOB, dob);
        values.put(COL_STU_TIME, time);
        int result = db.update(TABLE_STUDENTS, values, COL_STU_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_STUDENTS, COL_STU_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}
