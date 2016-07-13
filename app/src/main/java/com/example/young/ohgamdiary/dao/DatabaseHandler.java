package com.example.young.ohgamdiary.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.young.ohgamdiary.model.DiaryContent;

/**
 * Created by WonYoung on 16. 5. 22..
 */
public class DatabaseHandler {

    private DatabaseManager databaseManager;
    private SQLiteDatabase db;

    private DatabaseHandler(Context ctx){
        this.databaseManager = new DatabaseManager(ctx);
        this.db = databaseManager.getWritableDatabase();
    }

    public static DatabaseHandler open(Context ctx) throws SQLException{
        DatabaseHandler databaseHandler = new DatabaseHandler(ctx);
        return databaseHandler;
    }

    public void close(){
        databaseManager.close();
    }

    public Cursor selectDiaryList(int listCount) throws SQLException{

        Cursor cursor = db.rawQuery("SELECT * FROM diary ORDER BY iddiary DESC LIMIT 10 OFFSET " + listCount, null);
        //Cursor cursor = db.rawQuery("SELECT * FROM diary ORDER BY iddiary", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }


    //디비에 생성하는 코드 START
    public long insertDiaryContent(DiaryContent diaryContent){
        ContentValues values = new ContentValues();

        values.put("contents", diaryContent.content);
        values.put("writeDate", diaryContent.year + "-" + diaryContent.month + "-" + diaryContent.date);
        values.put("writeTime", diaryContent.hour+ ":" + diaryContent.minute + ":" + diaryContent.second);
        values.put("user_iduser", 0);

        return db.insert("diary", null, values);
    }
    //디비에 생성하는 코드 END

    //디비에 업데이트하는 코드 START
    public void updateDiaryContent(DiaryContent diaryContent) {

        db.execSQL("UPDATE diary SET contents = '" + diaryContent.content + "', writeDate = '" + diaryContent.year + "-" + diaryContent.month + "-" + diaryContent.date + "', writeTime = '" + diaryContent.hour+ ":" + diaryContent.minute + ":" + diaryContent.second + "', user_iduser = " + 0 + " WHERE iddiary = " + diaryContent.id + ";");
        Log.d("update문", "UPDATE diary SET contents = '" + diaryContent.content + "', writeDate = '" + diaryContent.year + "-" + diaryContent.month + "-" + diaryContent.date + "', writeTime = '" + diaryContent.hour+ ":" + diaryContent.minute + ":" + diaryContent.second + "', user_iduser = " + 0 + " WHERE iddiary = " + diaryContent.id + ";");
    }
    //디비에 업데이트하는 코드 END
}
