package com.cruz.fyp.virtualassistant.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class Database {

    private ULDatabaseHelper ulDatabaseHelper;
    transient private SQLiteDatabase database;
    Context context;

    public Database(Context context){
        this.context = context;
        ulDatabaseHelper = new ULDatabaseHelper(context);
        database = ulDatabaseHelper.getWritableDatabase();
    }

    public Cursor runQuery(String query, String[] args) {
        Cursor mCursor = database.rawQuery(query, args);
        if (mCursor.moveToFirst()) {
            mCursor.moveToFirst();
        }
        return mCursor; // returns array of data
    }

    public String checkCode(String code) {
        code = "AC2001";
        Cursor c = database.rawQuery("Select * from Modules where Module_Code = '"+ code + "';",null);
        ArrayList<String> resultArray = new ArrayList<>();
        Log.d("timetable", "THIS IS new result: " + c.getCount());
        if(c.getCount() >= 0){
            while (c.moveToNext()) {
//                resultArray.add(c.getString(1));
                Log.d("timetable", "THIS IS new result: " + c.getString(1));
                return c.getString(1);
            }
        }
        else{
            c = database.rawQuery("Select * from Rooms where Room = '"+ code + "';",null);
            while (c.moveToNext()) {
//                resultArray.add(c.getString(1));
                Log.d("timetable", "THIS IS new result: " + c.getString(1));
                return c.getString(1);
            }
        }
        c.close();
//        Log.d("timetable", "THIS IS new result: " + resultArray.get(0));

        return "this";
    }

}


