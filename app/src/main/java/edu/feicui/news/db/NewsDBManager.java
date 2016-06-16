package edu.feicui.news.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.feicui.news.bean.News;

/**
 * 往数据库表内添加数据
 * Created by Administrator on 2016/6/15.
 */
public class NewsDBManager {
    private DBOpenHelper dbHelper;
    private Context      context;
    private List<News.DataBean> mData=new ArrayList<>();

    public  NewsDBManager(Context context){
        this.context=context;
        dbHelper=new DBOpenHelper(context);
    }

//    添加数据的方法
    public boolean saveLoveNews(String summary, String icon, String stamp, String title, String nid, String link, String type){
        try {
            SQLiteDatabase db=dbHelper.getWritableDatabase();
            Cursor cursor=db.rawQuery("select * from lovenews where nid="+nid,null);
            if(cursor.moveToFirst()){
                cursor.close();
                return false;
            }
            cursor.close();
            ContentValues values=new ContentValues();
            values.put("type", type);
            values.put("nid", nid);
            values.put("stamp", stamp);
            values.put("icon", icon);
            values.put("title", title);
            values.put("summary", summary);
            values.put("link", link);
            db.insert("lovenews", null, values);
            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    读取数据库表的方法
    public List<News.DataBean> queryNews() {
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        String sql="select * from lovenews ";
        Cursor cursor=db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String nid = cursor.getString(cursor.getColumnIndex("nid"));
                String stamp = cursor.getString(cursor.getColumnIndex("stamp"));
                String icon = cursor.getString(cursor.getColumnIndex("icon"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String summary = cursor.getString(cursor.getColumnIndex("summary"));
                String link = cursor.getString(cursor.getColumnIndex("link"));
                News.DataBean news = new News.DataBean(summary,icon, stamp,title, nid, link,type);
                mData.add(news);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return mData;
    }
}
