package com.example.register;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AppHelper {

    private static final String TAG = "AppHelper";
    static List<User> bookMarkuserlist1 = new ArrayList<User>();
    private static SQLiteDatabase databased;
    private static String createTableuserlist1Sql = "create table if not exists userlist1" +
            "(" +
            "    _id integer PRIMARY KEY autoincrement, " +
            "    subject text, " +
            "    place text, " +
            "    time text, " +
            "    time2 text, " +
            "    time3 text, " +
            "    details text, " +
            "    lost_ID text, " +
            "    Latitude text, " +
            "    Longitude text, " +
            "    url text, " +
            "    password text"+
            ")";

    public static SQLiteDatabase openDatabase(Context context, String databaseName) {
        println("opendatabase 호출됨");

        try {
            databased = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
            if (databased != null) {
                println("데이터베이스 " + databaseName + " 오픈됨.");
                return databased;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void println(String data) {
        Log.d(TAG, data);
    }

    public static void createTable(String tableName) {
        println("createTable 호출됨 : " + tableName);

        if (databased != null) {
            if (tableName.equals("userlist1")) {
                databased.execSQL(createTableuserlist1Sql);
                println("userlist1 테이블 생성 요청됨");
            }
        } else {
            println("데이터베이스를 먼저 오픈하세요");
        }
    }

    public static void insertData(String subject, String place, String time, String time2, String time3, String details, String lost_ID, String Latitude, String Longitude, String url, String password) {
        println("insertData() 호출됨");

        if (databased != null) {
            String sql = "insert into userlist1(subject, place, time ,time2, time3, details, lost_ID, Latitude, Longitude, url, password) values(?,?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {subject, place, time, time2, time3, details, lost_ID, Latitude, Longitude, url, password};
            databased.execSQL(sql, params); // 두번째인자에 params를 넣으면 SQL이 실행되기전에 params 데이터를 물음표로 대체하면서 삽입


            println("데이터 추가함");
        } else {
            println("먼저 데이터베이스를 오픈하세요.");
        }
    }


    public static void deleteData(String tableName, String id) {
        println("delete data 호출됨");
        if (databased != null) {
            String sql = "delete from " + tableName + " where lost_ID = " + id;
            databased.execSQL(sql);
            println("삭제됫슴");
        }
    }
}