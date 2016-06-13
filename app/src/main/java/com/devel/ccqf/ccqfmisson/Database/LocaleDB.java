package com.devel.ccqf.ccqfmisson.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;

import com.devel.ccqf.ccqfmisson.ReseauSocial.ConversationHead;
import com.devel.ccqf.ccqfmisson.ReseauSocial.MessagePacket;
import com.devel.ccqf.ccqfmisson.Users;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by thierry on 10/05/16.
 */
public class LocaleDB {

    static SQLiteDatabase db = null;
    SimpleDateFormat dateFormat;

    public LocaleDB(Context cntx){
        dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
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
            if(cursor.getCount() == 0){// Usager n'existe pas
                ContentValues values = new ContentValues();
                values.put("user_id", "" + userId);
                values.put("prenom", prenom);
                values.put("nom", nom);
                values.put("compagnie", compagnie);
                values.put("lastMsg", "-1");
                values.put("lastSurvey", "-1");
                values.put("privilege", "-1");
                long val = db.insert("Utilisateur", null, values);
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
            if( cursor.getCount() > 0){
                int user_id = cursor.getInt(0);
                String prenom = cursor.getString(1);
                String nom = cursor.getString(2);
                String companie = cursor.getString(3);
                userLogin = prenom+"_"+nom+"@"+companie;
            }

        }
        return userLogin;
    }


    public MessagePacket getMessage(int MsgId){
        MessagePacket msg = null;
        if(db!= null){
            Cursor cursor;
            cursor = db.rawQuery("SELECT * from Messages WHERE id_msg = ?", new String[]{"" + MsgId});
            cursor.moveToFirst();
            if(cursor.getCount() > 0) {
                Date convertedDate = new Date();
                try {
                    convertedDate = dateFormat.parse(cursor.getString(5));
                }catch (ParseException pe) {
                    pe.printStackTrace();
                }

                msg = new MessagePacket(cursor.getInt(0), // msgID
                                        cursor.getInt(2), // source
                                        cursor.getInt(1), // convID
                                        cursor.getString(3),  // dest
                                        cursor.getString(4), // message
                                        convertedDate, // timestamp
                                        cursor.getString(6)  // attachement
                                        );
            }
        }
        return msg;
    }

    public List<MessagePacket> getMessages(int convID){
        List<MessagePacket> lstMsg = null;
        if(db != null){
            Cursor cursor;
            cursor = db.rawQuery("SELECT * from Messages WHERE conversationID = ?", new String[]{"" + convID});
            cursor.moveToFirst();
            if(cursor.getCount() > 0) {
                lstMsg = new ArrayList<>();
                int loopCount = 0;
                while (loopCount < cursor.getCount()) {

                    Date convertedDate = new Date();
                    try {
                        convertedDate = dateFormat.parse(cursor.getString(5));
                    } catch (ParseException pe) {
                        pe.printStackTrace();
                    }
                    MessagePacket msg = new MessagePacket(cursor.getInt(0), // msgID
                                            cursor.getInt(2), // source
                                            cursor.getInt(1), // convID
                                            cursor.getString(3),  // dest
                                            cursor.getString(4), // message
                                            convertedDate, // timestamp
                                            cursor.getString(6)  // attachement
                                            );
                    lstMsg.add(msg);
                    cursor.moveToNext();
                    loopCount++;
                }
            }

        }
        return lstMsg;
    }

    public int getPrivilege(int UserId){
        int privilege = -1;
        if(db != null){
            Cursor cursor;
            cursor = db.rawQuery("SELECT privilege FROM Utilisateur WHERE user_id = ?" , new String[]{"" + UserId});
            cursor.moveToFirst();
            if(cursor.getCount() > 0){
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
            if (cursor.getCount() > 0) {
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
            if (cursor.getCount() > 0) {
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
            if(cursor.getCount() > 0){
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
            if(cursor.getCount() > 0){
                user = cursor.getInt(0);
            }
        }
        return user;
    }

    public void initCommanditaires(int maxPages, int maxBanners) {
        if(db != null) {
            ContentValues values = new ContentValues();
            values.put("maxPages", maxPages);
            values.put("Pages", "0");
            values.put("maxBanners", maxBanners);
            values.put("Banners", "0");
            Cursor cursor = db.rawQuery("SELECT * FROM Commanditaires", null);
            cursor.moveToFirst();
            if(cursor.getCount() > 0)
                db.update("Commanditaires", values, null, null);
            else
                db.insert("Commanditaires", null, values);
        }
    }

    public int getMaxPageCommanditaire(){
        int mxpg = 0;
        if(db != null) {
            Cursor cursor = db.rawQuery("SELECT maxPages FROM Commanditaires", null);
            cursor.moveToFirst();
            if(cursor.getCount() > 0){
                mxpg = cursor.getInt(0);
            }
        }
        return mxpg;
    }

    public int getCurrentPageCommanditaire(){
        int pg = 0;
        if(db != null) {
            Cursor cursor = db.rawQuery("SELECT Pages FROM Commanditaires", null);
            cursor.moveToFirst();
            if(cursor.getCount() > 0){
                pg = cursor.getInt(0);
            }
        }
        return pg;
    }

    public void setNextPageCommanditaire(int val){
        ContentValues values = new ContentValues();
        if(db != null){
            values.put("Pages", ""+val);
            db.update("Commanditaires", values, null, null);

        }
    }

    public int getMaxBannerCommanditaire(){
        int mxpg = 0;
        if(db != null) {
            Cursor cursor = db.rawQuery("SELECT maxBanners FROM Commanditaires", null);
            cursor.moveToFirst();
            if(cursor.getCount() > 0){
                mxpg = cursor.getInt(0);
            }
        }
        return mxpg;
    }

    public int getCurrentBannerCommanditaire(){
        int pg = 0;
        if(db != null) {
            Cursor cursor = db.rawQuery("SELECT Banners FROM Commanditaires", null);
            cursor.moveToFirst();
            if(cursor.getCount() > 0){
                pg = cursor.getInt(0);
            }
        }
        return pg;
    }

    public void setNextBannerCommanditaire(int val){
        ContentValues values = new ContentValues();
        if(db != null){
            values.put("Banners", ""+val);
            db.update("Commanditaires", values, null, null);

        }
    }
}
