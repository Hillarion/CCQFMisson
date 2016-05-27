package com.devel.ccqf.ccqfmisson.ReseauSocial;

import java.util.Date;

/**
 * Created by thierry on 10/05/16.
 */
public class MessagePacket {
    private int id_msg;
    private int source;
    private int convID;
    private String destinataires;
    private String message;
    private Date timestamp;
    private String attachement;
    private boolean isSelf;

    public MessagePacket(int id, int src, int cID, String dest, String msg, Date tStamp, String attach) {
        id_msg = id;
        source = src;
        destinataires = dest;
        message=msg;
        timestamp = tStamp;
        attachement=attach;
        isSelf = false;
    }

    public MessagePacket(int src, String dest, String msg, String attach){
        this(-1, src, -1, dest, msg, new Date(), attach);
    }

    public int getId_msg() {
        return id_msg;
    }

    public void setId_msg(int id_msg) {
        this.id_msg = id_msg;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getDestinataires() {
        return destinataires;
    }

    public void setDestinataires(String destinataires) {
        this.destinataires = destinataires;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getAttachement() {
        return attachement;
    }

    public void setAttachement(String attachement) {
        this.attachement = attachement;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }

    public int getConvID() {
        return convID;
    }

    public void setConvID(int convID) {
        this.convID = convID;
    }

    public String toString (){
        return "[" +source+ "]" + timestamp + " : " + message;
    }
}
