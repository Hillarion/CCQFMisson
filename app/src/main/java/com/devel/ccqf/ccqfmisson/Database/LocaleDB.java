package com.devel.ccqf.ccqfmisson.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.devel.ccqf.ccqfmisson.ReseauSocial.MessagePacket;

import java.util.List;

/**
 * Created by thierry on 10/05/16.
 */
public class LocaleDB {

    static SQLiteDatabase db = null;

    public LocaleDB(Context cntx){
        if(db == null)
            db = (new SQLiteHelper(cntx)).getReadableDatabase();
    }

    public void writeOutMessage(MessagePacket msg){

        ContentValues values = new ContentValues();
        values.put("id_msg", msg.getId_msg());
        values.put("source", msg.getSource());
        values.put("destinataires", msg.getDestinataires());
        values.put("message", msg.getMessage());
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.put("timestamp", sdf.format(msg.getTimestamp()));
        values.put("attachement", msg.getAttachement());
        db.insert("MessageOut", null, values);

    }

    public void setUser(int userId, String userName){
        if(db != null){
            Cursor cursor = null;
            cursor = db.rawQuery("SELECT * FROM Utilisateur WHERE user_id = ?" , new String[]{"" + userId});
            cursor.moveToFirst();
            if(cursor == null){
                ContentValues values = new ContentValues();
                values.put("user_id", "" + userId);
                values.put("userName", userName);
                values.put("lastMsg", "-1");
                values.put("lastSurvey","-1");
                values.put("privilege","-1");
                db.insert("Utilisateur", null, values);
            }
        }
    }

    public void writeInMessage(MessagePacket msg){
        ContentValues values = new ContentValues();
        values.put("id_msg", msg.getId_msg());
        values.put("source", msg.getSource());
        values.put("destinataires", msg.getDestinataires());
        values.put("message", msg.getMessage());
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.put("timestamp", sdf.format(msg.getTimestamp()));
        values.put("attachement", msg.getAttachement());
        db.insert("MessageIn", null, values);
    }

    public List<MessagePacket> readOutMessage(int startMsgId, int nMessage){
        List<MessagePacket> msgList = null;
        return msgList;
    }

    public List<MessagePacket> readInMessage(int startMsgId, int nMessage){
        List<MessagePacket> msgList = null;
        return msgList;
    }

    public int getPrivilege(int UserId){
        int privilege = -1;
        if(db != null){
            Cursor cursor;
            cursor = db.rawQuery("SELECT privilege FROM Utilisateur WHERE user_id = ?" , new String[]{"" + UserId});
            cursor.moveToFirst();
            if(cursor != null){
                privilege = cursor.getInt(0);
            }
        }
        return privilege;
    }


    public void setPrivilege(int UserId, interfaceDB.privilegeType privilege){
        int priv = 0;
        if(privilege == interfaceDB.privilegeType.ADMIN)
            priv=1;
        if(db != null){
            ContentValues values = new ContentValues();
            values.put("privilege", "" + priv);
            db.update("Utilisateur", values, "user_id = " + UserId, null);
        }
    }

    public int getLastSurveyIndex(int UserId){
        int sIdx=-1;
        if(db != null) {
            Cursor cursor;
            cursor = db.rawQuery("SELECT lastSurvey FROM Utilisateur WHERE user_id = ?", new String[]{"" + UserId});
            cursor.moveToFirst();
            if (cursor != null) {
                sIdx = cursor.getInt(0);
            }
        }
        return sIdx;
    }

    public void setLastSurveyIndex(int UserId, int sIdx){
        if(db != null){
            ContentValues values = new ContentValues();
            values.put("lastSurvey", "" + sIdx);
            db.update("Utilisateur", values, "user_id = " + UserId, null);
        }
    }

    public int getLastMsgIndex(int UserId){
        int mIdx=-1;
        if(db != null) {
            Cursor cursor;
            cursor = db.rawQuery("SELECT lastMsg FROM Utilisateur WHERE user_id = ?", new String[]{"" + UserId});
            cursor.moveToFirst();
            if (cursor != null) {
                System.out.print("Chat Test ldb.getLastMsgIndex() cursor (bis)= " + cursor + "\n\n");
                System.out.flush();
                try {
                    mIdx = cursor.getInt(0);
                }catch(CursorIndexOutOfBoundsException cioobe) {//entry does not exist
                   mIdx = -1;
                }
            }
        }
        return mIdx;
    }

    public void setLastMsgIndex(int UserId, int mIdx){
        if(db != null){
            ContentValues values = new ContentValues();
            values.put("lastMsg", "" + mIdx);
            db.update("Utilisateur", values, "user_id = " + UserId, null);
        }
    }
}
