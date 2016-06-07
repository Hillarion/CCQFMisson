package com.devel.ccqf.ccqfmisson.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;

import com.devel.ccqf.ccqfmisson.ReseauSocial.ConversationHead;
import com.devel.ccqf.ccqfmisson.ReseauSocial.MessagePacket;
import com.devel.ccqf.ccqfmisson.Users;

import java.util.ArrayList;
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

    public void writeMessage(MessagePacket msg){

        ContentValues values = new ContentValues();
        values.put("id_msg", msg.getId_msg());
        values.put("source", msg.getSource());
        values.put("conversationID", msg.getConvID());
        values.put("destinataires", msg.getDestinataires());
        values.put("message", msg.getMessage());
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.put("timestamp", sdf.format(msg.getTimestamp()));
        values.put("attachement", msg.getAttachement());
        db.insert("Messages", null, values);

    }

    public void setUser(int userId, String nom, String prenom, String compagnie){
        if(db != null){
            Cursor cursor = null;
            cursor = db.rawQuery("SELECT * FROM Utilisateur WHERE user_id = ?" , new String[]{"" + userId});
            cursor.moveToFirst();
            if(cursor == null){// Usager n'existe pas
                ContentValues values = new ContentValues();
                values.put("user_id", "" + userId);
                values.put("prenom", prenom);
                values.put("nom", nom);
                values.put("compagnie", compagnie);
                values.put("lastMsg", "-1");
                values.put("lastSurvey","-1");
                values.put("privilege","-1");
                db.insert("Utilisateur", null, values);
            }
        }
    }
    public int isUserEmpty(){
        int cursorCount = 0;
        if(db!=null){
            Cursor cursor;
            cursor = db.rawQuery("SELECT * FROM Utilisateur", null);
            cursor.moveToFirst();
            cursorCount = cursor.getCount();
        }
        return cursorCount;
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


    public void setPrivilege(int UserId, InterfaceDB.privilegeType privilege){
        int priv = 0;
        if(privilege == InterfaceDB.privilegeType.ADMIN)
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

    public List<Integer> getMessageThreadList(){
        ArrayList<Integer> convList = null;
        if(db != null){
            Cursor cursor = db.rawQuery("SELECT DISTINCT conversationID FROM Messages", null);
            System.out.print("CCQF getMessageThreadList cursor = " + cursor + "\n\n");
            System.out.flush();
            cursor.moveToFirst();
            System.out.print("CCQF getMessageThreadList cursor (bis) = " + cursor + "\n\n");
            System.out.flush();
            if(cursor != null){
                if(cursor.getCount() > 0) {
                    System.out.print("CCQF getMessageThreadList cursor.count = " + cursor.getCount() + "\n\n");
                    System.out.flush();
                    convList = new ArrayList<Integer>();
                    int loopCount = 0;
                    while (loopCount < cursor.getCount()) {
                        convList.add(new Integer(cursor.getInt(0)));
                        cursor.moveToNext();
                        loopCount++;
                    }
                    System.out.print("CCQF getMessageThreadList convList = " + convList + "\n\n");
                    System.out.flush();
                }
            }
        }
        return (List)convList;
    }

    public ConversationHead getmessageHead(int conversationID){
        ConversationHead cHead = null;
        if(db != null){
            Cursor cursor = db.rawQuery("SELECT source, timestamp, message FROM Messages ORDER BY msg_id, DESC LIMIT 1 WHERE conversationID = ?", new String[] {""+conversationID});
            cursor.moveToFirst();
            if(cursor!=null){
                cHead = new ConversationHead(
                        ""+conversationID,
                        cursor.getString(0),
                        cursor.getString(2),
                        cursor.getString(1));
            }
        }
        return cHead;
    }
}
