package com.devel.ccqf.ccqfmisson.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thierry on 11/05/16.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "CCQFMission";
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        sql = "CREATE TABLE IF NOT EXISTS Messages (" +
                "id_msg INTEGER PRIMARY KEY, " +
                "conversationID INTEGER," +
                "source INTEGER," +
                "destinataires TEXT, " +
                "message TEXT, " +
                "timestamp TEXT, " +
                "attachement TEXT)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS Utilisateur (" +
              "user_id INTEGER PRIMARY KEY," +
              "prenom TEXT," +
              "nom  TEXT," +
              "compagnie TEXT," +
              "privilege INTEGER," +
              "lastMsg INTEGER," +
              "lastSurvey INTEGER)";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXIST Commenditaires ("+
                "maxPages INTEGER,"+
                "Pages INTEGER,"+
                "maxBanners INTEGER,"+
                "Banners INTEGER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Messages");
        db.execSQL("DROP TABLE IF EXISTS Utilisateur");
        db.execSQL("DROP TABLE IF EXISTS Commenditaires");
        onCreate(db);
    }

}
