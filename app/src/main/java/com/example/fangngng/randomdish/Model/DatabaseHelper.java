package com.example.fangngng.randomdish.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fangngng on 2016/8/20.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "randomDish.db";
    private static final int version = 1;

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table IF NOT EXISTS Dishs(" +
                " ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE" +
                ", DishTitle varchar(500) not null" +
                ", DishInfo varchar(8000) " +
                ", DishType varchar(500)" +
                ", DishImg varchar(500)" +
                ")";
        db.execSQL(sql);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name) {
        this(context, name, null, version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
