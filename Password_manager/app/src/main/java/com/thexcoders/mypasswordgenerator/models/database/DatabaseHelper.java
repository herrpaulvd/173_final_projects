package com.thexcoders.mypasswordgenerator.models.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.thexcoders.mypasswordgenerator.models.password.Password;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "PASSWORDS";
    private static final int DB_VERSION = 1;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE PASSWORDS" +
                        "(id INTEGER PRIMARY KEY, name TEXT UNIQUE, password TEXT, login TEXT, update_date DATE);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS PASSWORDS");
        onCreate(sqLiteDatabase);
    }

    public boolean insert(Password password){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = password.getContentValues();
        return db.insert("PASSWORDS",null,values) != -1;
    }

    public boolean update(String old_name, Password password){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = password.getContentValues();
        return db.update("PASSWORDS", values, "name = ?",
                new String[] {old_name}) != -1;
    }

    public boolean delete(String name){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("PASSWORDS","name = ?",
                new String[] {name}) != -1;
    }

    public Password get_by_name(String name){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PASSWORDS where name = '" + name + "'",null);
        cursor.moveToFirst();

        return Password.fromCursor(cursor);
    }

    public List<Password> getPasswordList(){
        List<Password> passwordList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PASSWORDS",null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            Password pwd = Password.fromCursor(cursor);
            passwordList.add(pwd);
            cursor.moveToNext();
        }

        return passwordList;
    }

    public List<String> getNamesList(){
        List<String> namesList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PASSWORDS",null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            Password pwd = Password.fromCursor(cursor);
            namesList.add(pwd.getContentValues().get("name").toString());
            cursor.moveToNext();
        }

        return namesList;
    }
}