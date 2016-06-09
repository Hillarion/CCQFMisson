package com.devel.ccqf.ccqfmisson.ReseauSocial;

import java.util.Date;

/**
 * Created by thierry on 26/05/16.
 */
public class ConversationHead {
    private String convID;
    private String from;
    private String when;
    private String lastMsg;

    public ConversationHead() {
        this("", "","", "");
    }

    public ConversationHead(String cID, String src, String msg, String when){
        convID = cID;
        from = src;
        this.when = when;
        lastMsg = msg;
    }

    public String getConvID() {
        return convID;
    }

    public String getFrom() {
        return from;
    }

    public String getWhen() {
        return when;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public String toString(){
        return "{" + convID + ", from :"+ from +", "+when + ", " +lastMsg +"}";
    }
}
