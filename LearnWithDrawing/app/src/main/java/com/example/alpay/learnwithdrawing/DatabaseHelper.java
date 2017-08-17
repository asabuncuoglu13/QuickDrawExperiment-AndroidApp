package com.example.alpay.learnwithdrawing;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by alpay1 on 12.08.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String TAG = "DatabaseHelper";

    private static final String DB_NAME = "DRAWINGAPP";
    private static final int DB_VERSION = 1;

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE TABLE USERS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "USERID TEXT,"
                + "FULLNAME TEXT, "
                + "POINT INTEGER);");
    }

    public static void addUser(SQLiteDatabase db, String user_id, String full_name, int point)
    {
        Log.d(TAG, "addUser: "+full_name+" "+point);
        ContentValues user = new ContentValues();
        user.put("USERID", user_id);
        user.put("FULLNAME", full_name);
        user.put("POINT", point);
        db.insert("USERS", null, user);
    }


}
