package ca.weizhi.timedscreenlock;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.UUID;

import static android.content.ContentValues.TAG;

public class CustomSQLiteOpenHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;



    private static final String DATABASE_NAME = "timedscreenlock.db";//数据库名字
    private static final int DATABASE_VERSION = 1;//数据库版本号
    private static final String CREATE_TABLE_COUNTDOWN = "create table countdown ("
            + "id integer primary key,"


            + "time integer);";

    private static final String CREATE_TABLE_TIMER = "create table timer ("
            + "id integer primary key,"
            + "time integer ,"
            + "repeat integer ,"

            + "ative integer);";



    public CustomSQLiteOpenHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private CustomSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);//调用到SQLiteOpenHelper中
        Log.d(TAG,"New CustomSQLiteOpenHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG,"onCreate");
        db.execSQL(CREATE_TABLE_COUNTDOWN);
        db.execSQL(CREATE_TABLE_TIMER);
        String  userGUID = UUID.randomUUID().toString();
        Log.i("guid",userGUID+"");

        Cursor cursor= db.rawQuery("select * from countdown; ",null);

        if(!cursor.moveToNext()) {

            db.execSQL("insert into countdown values(0,0);");
            db.execSQL("insert into countdown values(1,10*60*1000);");
            db.execSQL("insert into countdown values(2,20*60*1000);");
            db.execSQL("insert into countdown values(3,30*60*1000);");
            db.execSQL("insert into countdown values(4,60*60*1000);");
            db.execSQL("insert into countdown values(5,1*60*1000);");

        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//    public String getGUID(){
//
//        Cursor cursor=db.rawQuery("select * from user ",null);
//
//        String userGUID="";
//
//        if(cursor.moveToNext()){
//
//            userGUID=cursor.getString(1);
//
//        }
//
//        return userGUID;
//
//    }
//
//    public String getUsername(){
//
//        Cursor cursor=db.rawQuery("select * from user ",null);
//
//        String username="";
//
//        if(cursor.moveToNext()){
//
//            username=cursor.getString(2);
//
//        }
//
//        return username;
//
//    }
}
