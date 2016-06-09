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

        System.out.print("CCQF LocalDb writeMessage values = " + values + "\n\n");
        System.out.flush();
        db.insert("Messages", null, values);
    }

    public void setUser(int userId, String nom, String prenom, String compagnie){
        System.out.print("CCQF LocalDB setUser (entry)");
        System.out.flush();
        if(db != null){
            Cursor cursor = null;
            cursor = db.rawQuery("SELECT * FROM Utilisateur WHERE user_id = ?" , new String[]{"" + userId});
            cursor.moveToFirst();
            if(cursor.getCount() == 0){// Usager n'existe pas
                System.out.print("CCQF LocalDB setUser (writing)");
                System.out.flush();
                ContentValues values = new ContentValues();
                values.put("user_id", "" + userId);
                values.put("prenom", prenom);
                values.put("nom", nom);
                values.put("compagnie", compagnie);
                values.put("lastMsg", "-1");
                values.put("lastSurvey", "-1");
                values.put("privilege", "-1");
                long val = db.insert("Utilisateur", null, values);
                System.out.print("CCQF LocalDB setUser (finally) val = " + val + "\n\n");
                System.out.flush();
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

    public String getCurrentUserLogin(){
        String userLogin = "";
        if(db!=null){
            Cursor cursor;
            cursor = db.rawQuery("SELECT * FROM Utilisateur", null);
            cursor.moveToFirst();
            if( cursor.getCount() != 0){
                int user_id = cursor.getInt(0);
                String prenom = cursor.getString(1);
                String nom = cursor.getString(2);
                String companie = cursor.getString(3);
                userLogin = prenom+"_"+nom+"@"+companie;
            }

        }
        return userLogin;
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
            if(cursor.getCount() != 0){
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
            if (cursor.getCount() != 0) {
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
            if (cursor.getCount() != 0) {
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
            cursor.moveToFirst();
            if(cursor.getCount() != 0){
                if(cursor.getCount() > 0) {
                    convList = new ArrayList<Integer>();
                    int loopCount = 0;
                    while (loopCount < cursor.getCount()) {
                        convList.add(new Integer(cursor.getInt(0)));
                        cursor.moveToNext();
                        loopCount++;
                    }
                }
            }
        }
        return (List)convList;
    }

    public ConversationHead getMessageHead(int conversationID){
        ConversationHead cHead = null;
        if(db != null){
            Cursor cursor = db.rawQuery("SELECT source, timestamp, message FROM Messages WHERE conversationID = ? ORDER BY id_msg DESC LIMIT 1", new String[] {""+conversationID});
            cursor.moveToFirst();
            if(cursor.getCount() != 0){
                cHead = new ConversationHead(
                        ""+conversationID,
                        cursor.getString(0),
                        cursor.getString(2),
                        cursor.getString(1));
            }
        }
        return cHead;
    }

    public int getCurrentUserID(){
        int user = -1;
        if(db != null){
            Cursor cursor = db.rawQuery("Select user_id from Utilisateur", null);
            cursor.moveToFirst();
            if(cursor.getCount() != 0){
                user = cursor.getInt(0);
            }
        }
        return user;
    }

    public void initCommenditaires(int maxPages, int maxBanners) {
        ContentValues values = new ContentValues();
        values.put("maxPages", maxPages);
        values.put("Pages", "0");
        values.put("maxBanners",maxBanners);
        values.put("Banners","0");
        db.update("Commenditaires", values, null, null);
    }
}
