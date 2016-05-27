package com.devel.ccqf.ccqfmisson.ReseauSocial;

import java.util.Date;

/**
 * Created by thierry on 26/05/16.
 */
public class ConversationHead {
    private String from;
    private Date when;
    private String lastMsg;

    public ConversationHead() {
        this("","", new Date(0));
    }

    public ConversationHead(String src, String msg, Date when){
        from = src;
        this.when = when;
        lastMsg = msg;
    }

    public String getFrom() {
        return from;
    }

    public Date getWhen() {
        return when;
    }

    public String getLastMsg() {
        return lastMsg;
    }
}
