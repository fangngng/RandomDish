package com.example.fangngng.randomdish.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.fangngng.randomdish.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fangngng on 2016/8/24.
 */
public class DishItem {
    private List<Map<String, Object>> listItemInfo = new ArrayList<>();



    public void add(Context context, String title, String info, String type, int img) {
        DatabaseHelper dbHelper = new DatabaseHelper(context, "db_randomDish");
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        db.execSQL("insert into Dishs values( '" + title +
                "','" + info + "','" + type + "','" + img + "') ");

        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("info", info);
        map.put("type", type);
        map.put("img", img);
        db.close();

        listItemInfo.add(map);
    }


    public void remove(Context context, int i) {
        DatabaseHelper dbHelper = new DatabaseHelper(context, "db_randomDish");
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "delete from Dishs where DishTitle = '" + listItemInfo.get(i).get("title") +"'" ;
        db.execSQL(sql);
        db.close();

        listItemInfo.remove(i);
    }


    public List<Map<String, Object>> get() {
        return listItemInfo;
    }

    public DishItem(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context, "db_randomDish");
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "select * from Dishs ";
        Cursor c = db.rawQuery(sql, new String[]{});
        Log.i("c.getCount:",String.valueOf(c.getCount()));
        if ( c.moveToFirst()) {
            c.moveToFirst();
            while(!c.isAfterLast()){
                Log.i("c.getCount:",String.valueOf(c.getCount()));

                String title = c.getString(c.getColumnIndex("DishTitle"));
                Log.i("title",title);
                String info = c.getString(c.getColumnIndex("DishInfo"));
                Log.i("info",info);
                String type = c.getString(c.getColumnIndex("DishType"));
                Log.i("type",type);
                int imgID = c.getInt(c.getColumnIndex("DishImg"));
                log.i("imgID", String.ValueOf(imgID));

                Map<String, Object> map = new HashMap<>();
                map.put("title", title);
                map.put("info", info);
                map.put("type", type);
                map.put("img", imgID);
                listItemInfo.add(map);

                c.moveToNext();
            }

        }
        c.close();
        db.close();


    }
}
