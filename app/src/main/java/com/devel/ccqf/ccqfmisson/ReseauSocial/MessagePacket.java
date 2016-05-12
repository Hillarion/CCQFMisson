package com.devel.ccqf.ccqfmisson.ReseauSocial;

import java.util.Date;

/**
 * Created by thierry on 10/05/16.
 */
public class MessagePacket {
    int id_msg;
    int source;
    String destinataires;
    String message;
    Date timestamp;
    String attachement;

    public MessagePacket(int id, int src, String dest, String msg, Date tStamp, String attach) {

    }
    public MessagePacket(int src, String dest, String msg, String attach){
        this(-1, src, dest, msg, new Date(), attach);
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

}