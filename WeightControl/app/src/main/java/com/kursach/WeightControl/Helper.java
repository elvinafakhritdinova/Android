package com.kursach.WeightControl;

import androidx.annotation.Nullable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Helper extends SQLiteOpenHelper  {
    private static final String DATABASE_NAME = "list.db"; // название бд
    private static final int SCHEMA_VERSION = 2; // версия базы данных

    //названия таблицы и столбцов - ЗАНЯТИЯ
    static final String TABLE_NOTES = "notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_WEIGHT = "weight";

    //полная инфа с id головы
    public static String[] headersWithIdAll = new String[]{
            Helper.COLUMN_ID,
            Helper.COLUMN_DATE,
            Helper.COLUMN_WEIGHT
    };


    //полная инфа - GUI
    public static int[] guiListAll = new int[]{
            R.id.data,
            R.id.ves
    };

    public Helper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE notes (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " DATE, " +
                COLUMN_WEIGHT + " TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NOTES);
        onCreate(db);
    }

    public static Cursor getAllNotesCursor(SQLiteDatabase db){
        return db.query(
                TABLE_NOTES, headersWithIdAll, null, null,
                null, null, COLUMN_DATE
        );
    }
    public static Cursor getAllNotesCursorUp(SQLiteDatabase db){
        return db.query(
                TABLE_NOTES, headersWithIdAll, null, null,
                null, null, COLUMN_DATE+" DESC"
        );
    }
    public static Cursor getAllNotesCursorUp7(SQLiteDatabase db){
        return db.query(
                TABLE_NOTES, headersWithIdAll,null, null,
                null, null, COLUMN_DATE+" DESC LIMIT 7"
        );
    }

    public static Cursor getAllNotesCursorUp30(SQLiteDatabase db){
        return db.query(
                TABLE_NOTES, headersWithIdAll,null, null,
                null, null, COLUMN_DATE+" DESC LIMIT 30"
        );
    }
   /* public static boolean getNoteByDateCursor(SQLiteDatabase db, String date){
        if(db.query(TABLE_NOTES, headersWithIdAll, "date = ?",
                new String[]{date}, null, null, null
        )==null) return true;
        return false;
    }*/
   public static Cursor getNoteByDateCursor(SQLiteDatabase db, String date){
       return db.query(TABLE_NOTES, headersWithIdAll, "date = ?",
               new String[]{date}, null, null, null);
   }
}