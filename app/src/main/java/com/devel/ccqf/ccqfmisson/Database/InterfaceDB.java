package com.devel.ccqf.ccqfmisson.Database;

import android.content.Context;
import android.net.ConnectivityManager;

import com.devel.ccqf.ccqfmisson.ReseauSocial.ConversationHead;
import com.devel.ccqf.ccqfmisson.ReseauSocial.MessagePacket;
import com.devel.ccqf.ccqfmisson.SurveyStruct.*;
import com.devel.ccqf.ccqfmisson.Users;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by thierry on 10/05/16.
 */
public class InterfaceDB {
    public enum privilegeType {NO_PRIVILEGE, ADMIN};

    private LocaleDB lDb = null;
    private RemoteDB rDb = null;

    private Context parentContext = null;
    private boolean workLocaly = true;
    private boolean parsingComplete = false;
    private String ligneResult = null;

    private int currentUser;

    public InterfaceDB(Context cntx) {
        currentUser = -1;
        parentContext = cntx;

        if (isOnline()) {
            if (testConnexionToServer() == true)
                workLocaly = false;
        }
        lDb = new LocaleDB(cntx);

        if (workLocaly == false) {
            rDb = new RemoteDB(cntx);
            rDb.setLocalBackUp(lDb);
        }
    }

    private boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) parentContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean testConnexionToServer() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://thierrystpierre.ddns.net:81/CCQFMission/index.php");
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity2 = response.getEntity();
                    ligneResult = EntityUtils.toString(entity2);
                    parsingComplete = true;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        thread.start();
        while (parsingComplete == false) ;
        return (ligneResult != null);
    }

     /*
     *  Méthode pour valider un usager. Si le userName et mot de passe sont valide,
     *      Recoit un nom d'usager, et le mot de passe
     *      Retourne un identifiant pouvant être utilisé pour les conversations et les sondages.
     */
    public int registerUser(String nom, String prenom, String compagnie) {
        if(rDb != null)
            currentUser = rDb.registerUser(nom, prenom, compagnie) ;
        return currentUser;
    }

    public void registerEvent(String destinataire, String hDebut, String hFin,
                             String compagnie, String nom, String poste,
                             String telephone, String email, String adresse,
                             boolean batimentExterier){
        if(rDb != null)
            rDb.registerEvent(destinataire, hDebut, hFin, compagnie, nom,
                    poste, telephone, email, adresse, batimentExterier);
    }

    /*
    *  Méthode pour retrouver le privilège associé à l'usager,
    *       Reçoit l'identifiant de l'usager
    *       Retourne le niveau de privilege ( NO_PRIVILEGE ou ADMIN) de l'usager
    */
    public privilegeType getPrivilege(int UserId) {
        privilegeType privilege = null;

        int priv = -1;
        if ((priv = lDb.getPrivilege(UserId)) != -1) {
            switch (priv) {
                case 0:
                    privilege = (privilegeType.NO_PRIVILEGE);
                    break;
                case 1:
                    privilege = (privilegeType.ADMIN);
                    break;
            }
        } else if (!workLocaly) {
            privilege = rDb.getPrivilege(UserId);
            lDb.setPrivilege(UserId, privilege);
        }
        return privilege;
    }

    /*
    * Méthode qui envois la réponse à une question de sondage sur les serveur
    *       Reçoit l'identifiant de l'usager ainsi qu'un SurveyAnswer contenant la réponse
    *       Ne retourne rien
    */
    public void answerSurveyQuestion(SurveyAnswer sQuestion) {
        if (rDb != null)
            rDb.answerSurveyQuestion(currentUser, sQuestion);
    }

    /*
    * Méthode qui lis la listes des sondages non lu.
    *       Utilise l'identifiant du dernier sondage déjà lu tels que gardé dans la base de données locale
    *       Retourne un tableau contenant la liste des identifiant non-lu
    */
    public int[] readSurveyList() {
        int[] tbl = null;
        int lastSurveyID = lDb.getLastSurveyIndex(currentUser);

        if (rDb != null)
            tbl = rDb.readSurveyList(lastSurveyID);
        return tbl;
    }

    /*
    * Méthode qui lis un sondage
    *       Reçoit l'identifiant su sondage à lire
    *       Retourne un SurveyGroup contenant la liste des questions
    */
    public SurveyGroup readSurvey(int surveyID) {
        SurveyGroup sGrp = null;

        if (rDb != null) {
            sGrp = rDb.readSurvey(surveyID);
            lDb.setLastSurveyIndex(currentUser, sGrp.getId());
        }
        return sGrp;
    }

    public void sendSurvey(SurveyGroup sGrp) {
        if (rDb != null)
            rDb.sendSurvey(sGrp);
    }
    /*
    * Méthode qui lis tout les messages destiné à l'usager, qui sont plus vieux que lastMessageID
    *       Reçoit l'identifiant de l'usager ainsi que l'identifiant du dernier message reçu.
    *       Retourne une Liste de MessagePacket ou null s'il n'y a pas de nouveau message
    */
    public List<MessagePacket> readMessages(int userId) {
        List<MessagePacket> msgList = null;
        if(userId > 0) {
            int lastMessageID = lDb.getLastMsgIndex(userId);
            if (rDb != null)
                msgList = rDb.readMessages(userId, lastMessageID);
        }
        return msgList;
    }

    /*
    * Méthode qui envoi un message au system de réseau social.
    *       Reçoit un MessagePacket
    *       Ne retourne rien
    */
    public int sendMessage(MessagePacket msg){
        int msgId = -1;
        if(rDb != null)
            msgId = rDb.sendMessage(msg);
        return msgId;
    }

    public int sendMessage(String dest, String msgText){
        int msgId = -1;
        if(rDb != null) {
            MessagePacket msg = new MessagePacket(-1, currentUser, -1, dest, msgText, new Date(), "");
            msgId = rDb.sendMessage(msg);
        }
        return msgId;
    }


    public ArrayList<SurveyObjectResults> readSurveyResults(int surveyId){
        ArrayList<SurveyObjectResults> rsltList = null;

        if(rDb != null) {
            rsltList = rDb.readSurveyResults(surveyId);
        }
        return rsltList;
    }

    public List<Integer> getMessageThreadList() {
        List<Integer> list = null;
        if(lDb != null)
            list = lDb.getMessageThreadList();
        return list;
    }

    public ConversationHead getmessageHead(int conversationID) {
        ConversationHead cHead = null;
        if(lDb != null)
            cHead = lDb.getmessageHead(conversationID);
        return cHead;
    }

    public ArrayList<Users> getUserList(){
        ArrayList<Users> uList = null;
        if(rDb != null){
            uList = rDb.getUserList(currentUser);
        }
        return uList;
    }
}