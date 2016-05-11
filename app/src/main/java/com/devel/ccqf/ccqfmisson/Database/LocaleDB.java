package com.devel.ccqf.ccqfmisson.Database;

import android.content.ContentValues;
import android.content.Context;
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

}
