package com.devel.ccqf.ccqfmisson.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thierry on 11/05/16.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "CCQFMisson";
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        sql = "CREATE TABLE IF NOT EXISTS MessageOut (" +
                "id_msg INTEGER PRIMARY KEY, " +
                "source INTEGER," +
                "destinataires TEXT, " +
                "message TEXT, " +
                "timestamp TEXT, " +
                "attachement TEXT)";
        db.execSQL(sql);
        sql = "CREATE TABLE IF NOT EXISTS MessageIn (" +
                "id_msg INTEGER PRIMARY KEY, " +
                "source INTEGER," +
                "destinataires TEXT, " +
                "message TEXT, " +
                "timestamp TEXT, " +
                "attachement text)";
        db.execSQL(sql);
        sql = "CREATE TABLE IF NOT EXISTS Utilisateur (" +
              "user_id INTEGER PRIMARY KEY," +
              "userName TEXT," +
              "privilege INTEGER," +
              "lastMsg INTEGER," +
              "lastSurvey INTEGER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS MessageOut");
        db.execSQL("DROP TABLE IF EXISTS MessageIn");
        db.execSQL("DROP TABLE IF EXISTS Utilisateur");
        onCreate(db);
    }

}
