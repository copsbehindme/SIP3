package com.example.copsbehindme.sip;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by copsbehindme on 28/04/17.
 */

public class UserDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "user.db";
    public static final String TABLE_NAME = "users";
    private final static String TABLE_NAME2 = "siptable";
    public static final String COL1 = "name";
    public static final String COL2 = "password";
    private static final String TAG = "UserDatabaseHelper";
    public static final String COL11 = "sipName";
    public static final String COL22 = "uname";
    public static final String COL33 = "monthlyInvestment";
    public static final String COL44 = "totalReturn";
    public static final String COL55 = "rate";
    public static final String COL66 = "months";
    public static final String COL77 = "temp";
    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: ");
        db.execSQL("CREATE TABLE `users` (\n" +
                "\t`name`\tTEXT,\n" +
                "\t`password`\tINTEGER,\n" +
                "\tPRIMARY KEY(`name`)\n" +
                ")");

        db.execSQL("CREATE TABLE siptable ( `sipID` INTEGER PRIMARY KEY AUTOINCREMENT, `sipName` TEXT UNIQUE, `uname` TEXT NOT NULL, `monthlyInvestment` TEXT NOT NULL, `totalReturn` TEXT NOT NULL, `rate` TEXT NOT NULL, `months` TEXT NOT NULL, `temp` TEXT )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: Inside onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(db);
    }

    public boolean validate(String username , String password){
        Log.d(TAG, "validate: ");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_NAME + " WHERE name = '" + username + "' AND password = " + password, null );
        if (cursor.getCount() == 0)
            return false;
        return true;
    }

    public boolean signup(String username, String password){
        Log.d(TAG, "signup: ");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1,username);
        contentValues.put(COL2,password);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1)
            return  false;
        return true;
    }

    public boolean addSIP(String sipName, String uname , double monthlyInvestment, double totalReturns , int rate , int months){
        Log.d(TAG, "addSIP: ");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL11,sipName);
        contentValues.put(COL22,uname);
        contentValues.put(COL33,String.valueOf(monthlyInvestment));
        contentValues.put(COL44,String.valueOf(totalReturns));
        contentValues.put(COL55,String.valueOf(rate));
        contentValues.put(COL66,String.valueOf(months));
        long result = db.insert(TABLE_NAME2,null,contentValues);
        if(result == -1)
            return  false;
        return true;
    }

    public boolean deleteSIP(String username , String sipName){
        Log.d(TAG, "deleteSIP: ");
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME2,"sipName=? AND uname=?" , new String[]{sipName,username}) == 1 ? true : false ;
    }

    public boolean whetherSIPNameExists(String sipname, String username){
        Log.d(TAG, "whetherSIPNameExists: ");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+ TABLE_NAME2 + " WHERE sipName= '"+sipname+"' AND uname='" +username +"'" ,null);
        if (res.getCount() < 1)
            return false;
        return true;

    }

    public boolean updateSIP(String sipName, String uname , double monthlyInvestment, double totalReturns , int rate , int months){
        Log.d(TAG, "updateSIP: ");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL22,uname);
        contentValues.put(COL33,monthlyInvestment);
        contentValues.put(COL44,totalReturns);
        contentValues.put(COL55,rate);
        contentValues.put(COL66,months);
        contentValues.put(COL77,0.0f);
        db.update(TABLE_NAME2,contentValues,"sipName = ?", new String[] {sipName}) ;
        return true;
    }

    public ArrayList<String> retreiveAllUserSIP(String username){
        Log.d(TAG, "retreiveAllUserSIP: ");
        ArrayList<String> allUserSIP = new ArrayList<String>();
        StringBuffer stringBuffer = new StringBuffer();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT sipName FROM "+ TABLE_NAME2 + " WHERE uname = '" + username + "'",null);
        if(res.getCount() > 0){
            res.moveToFirst();
            Log.d(TAG, "retreiveAllUserSIP: total SIP received for user = " + res.getCount());
            Log.d(TAG, "retreiveAllUserSIP: "+ res.getString(0));
            res.moveToFirst();
            Boolean b = true;
            while(b){
                allUserSIP.add(res.getString(0));
                if (res.isLast())
                    b = false;
                res.moveToNext();
            }
        }
        return allUserSIP;

    }

    public Cursor retreiveSIP(String username, String sipName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT monthlyInvestment, rate, months FROM " + TABLE_NAME2 + " WHERE uname = '" +username +"' AND sipName = '" +sipName + "'" ,null);
        return cursor;
    }
}
