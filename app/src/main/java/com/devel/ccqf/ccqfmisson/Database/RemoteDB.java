package com.devel.ccqf.ccqfmisson.Database;

import android.content.Context;

import com.devel.ccqf.ccqfmisson.AgendaObjects.Event;
import com.devel.ccqf.ccqfmisson.ReseauSocial.MessagePacket;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyGroup;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyAnswer;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyMultiple;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyObject;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyObjectResults;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyPair;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyText;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyYesNo;
import com.devel.ccqf.ccqfmisson.Users;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by thierry on 10/05/16.
 */
public class RemoteDB {
    private LocaleDB  lDb = null;
    public static final String strURL = "http://thierrystpierre.ddns.net:81/CCQFMission/script.php";
    private List<NameValuePair> paramRequete;
    private JSONParser parser;
    private SimpleDateFormat dateFormat;
    private HttpClient httpclient;
    private HttpPost httppost;
    
    public RemoteDB(Context cntx){
        dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        httpclient = new DefaultHttpClient();
        httppost = new HttpPost(strURL);
    }

    void setLocalBackUp(LocaleDB dbaLite){
        lDb = dbaLite;
    }

    /*
    * Méthodes privées pour la gestion des transaction réseaux
    */
    private String sendRequest(List<NameValuePair> pairs){
        paramRequete = pairs;
        String ligneResult = null;
        try {
            httppost.setEntity(new UrlEncodedFormEntity(paramRequete));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity2 = response.getEntity();
            ligneResult = EntityUtils.toString(entity2);
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return ligneResult;
    }

    private String sendRequestNoResponse(List<NameValuePair> pairs){
        paramRequete = pairs;
        String ligneResult = null;

        try {
            httppost.setEntity(new UrlEncodedFormEntity(paramRequete));
            HttpResponse response = httpclient.execute(httppost);
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return ligneResult;
    }

     /*
     *  Méthode pour valider un usager. Si le userName et mot de passe sont valide,
     *      Recoit un nom d'usager, et le mot de passe
     *      Retourne un identifiant pouvant être utilisé pour les conversations et les sondages.
     */

    public int registerUser(String nom, String prenom, String compagnie){
        int userId = -1;
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "registerUser"));
        //pairs.add(new BasicNameValuePair("userName", userName));
        pairs.add(new BasicNameValuePair("nom", nom));
        pairs.add(new BasicNameValuePair("prenom", prenom));
        pairs.add(new BasicNameValuePair("compagnie", compagnie));
        String ligneResult = sendRequest(pairs);
        if(ligneResult != null) {
            parser = new JSONParser(ligneResult);
            String status = parser.getStatus();
            if (!status.isEmpty()) {
                if (status.equalsIgnoreCase("Success"))
                    userId = parser.getIndex();
            }
        }
        if(userId > 0){
            if(lDb != null){
                lDb.setUser(userId, nom, prenom, compagnie);
            }
        }
        return userId;
    }

    public int registerEvent(Event e){
        int eventId = -1;
        ArrayList<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("action", "registerEvent"));
        pairs.add(new BasicNameValuePair("destinataire", e.getDestinataire()));
        pairs.add(new BasicNameValuePair("hDebut", e.getDTStart()));
        pairs.add(new BasicNameValuePair("hFin", e.getDTEnd()));
        pairs.add(new BasicNameValuePair("compagnie", e.getCompagnie()));
        pairs.add(new BasicNameValuePair("nom", e.getNom()));
        pairs.add(new BasicNameValuePair("poste", e.getPoste()));
        pairs.add(new BasicNameValuePair("telephone", e.getTelephone()));
        pairs.add(new BasicNameValuePair("email", e.getEmail()));
        pairs.add(new BasicNameValuePair("adresse", e.getAdresse()));
        pairs.add(new BasicNameValuePair("batiment", ""+e.isAutreBatiment()));

        String ligneResult = sendRequest(pairs);
        if(ligneResult != null){
            parser = new JSONParser(ligneResult);
            String status = parser.getStatus();
            if(!status.isEmpty()){
                if(status.equalsIgnoreCase("Success"))
                    eventId = parser.getIndex();
            }
        }
        return eventId;

    }

    public ArrayList<Event> getEventList(String destinataire){
        ArrayList<Event> alEvent = null;

        ArrayList<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("action", "getB2BList"));
        pairs.add(new BasicNameValuePair("destinataire", destinataire));
        String lineResult = sendRequest(pairs);

        if(lineResult != null){
            parser = new JSONParser(lineResult);
            String status = parser.getStatus();
            if(!status.isEmpty()){
                if(status.equalsIgnoreCase("Success")){
                    JSONArray jsonArray = parser.getList("B2BList");
                    if(jsonArray != null){
                        alEvent = new ArrayList<Event>();
                        for(int i = 0; i<jsonArray.length();i++){
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String hDebut = jsonObject.getString("hDebut");
                                String hFin = jsonObject.getString("hFin");
                                String compagnie = jsonObject.getString("compagnie");
                                String nom = jsonObject.getString("nom");
                                String poste = jsonObject.getString("poste");
                                String telephone = jsonObject.getString("telephone");
                                String email = jsonObject.getString("email");
                                String adresse = jsonObject.getString("adresse");
                                Boolean b = (jsonObject.getInt("batiment") == 0)?false:true;
//                                Boolean b = Boolean.valueOf(batiment);

                                Event e = new Event(destinataire, hDebut, hFin, compagnie, nom, poste,
                                        telephone, email, adresse,  b);
                                alEvent.add(e);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return alEvent;
    }


    /*
    *  Méthode pour retrouver le privilège associé à l'usager,
    *       Reçoit l'identifiant de l'usager
    *       Retourne le niveau de privilege ( NO_PRIVILEGE ou ADMIN) de l'usager
    */
    public InterfaceDB.privilegeType getPrivilege(int UserId){
        InterfaceDB.privilegeType privilege = InterfaceDB.privilegeType.NO_PRIVILEGE;
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "getPrivilege"));
        pairs.add(new BasicNameValuePair("userID", "" + UserId));
        String ligneResult = sendRequest(pairs);

        if(ligneResult != null) {
            parser = new JSONParser(ligneResult);
            String status = parser.getStatus();
            if (!status.isEmpty()) {
                if (status.equalsIgnoreCase("Success")) {
                    String val = parser.getString("privilege");
                    if (val != null) {
                        if (val.equalsIgnoreCase("admin"))
                            privilege = InterfaceDB.privilegeType.ADMIN;
                    }
                }
            }
        }
        return privilege;
    }

    public ArrayList<Users> getUserList(int currentUser){
        ArrayList<Users> uList = null;
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "getUserList"));
        String ligneResult = sendRequest(pairs);

        if(ligneResult != null) {
            parser = new JSONParser(ligneResult);
            String status = parser.getStatus();
            if (!status.isEmpty()) {
                if (status.equalsIgnoreCase("Success")) {
                    JSONArray jArray = parser.getList("users");
                    if (jArray != null) {
                        uList = new ArrayList<Users>();
                        for (int idx = 0; idx < jArray.length(); idx++) {
                            try {
                                JSONObject jsonObject = jArray.getJSONObject(idx);
                                int uId = jsonObject.getInt("user_id");
//                                if(uId != currentUser){
                                    String nom = jsonObject.getString("nom");
                                    String prenom = jsonObject.getString("prenom");
                                    String compagnie = jsonObject.getString("compagnie");
                                    Users user=new Users(uId, nom, prenom, compagnie, "");
                                    uList.add(user);
//                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return uList;
    }

    /*
    * Méthode qui envoi un message au system de réseau social.
    *       Reçoit un MessagePacket
    *       Ne retourne rien
    * Pour tout message envoyé une copie est insctite dans la BD locale, si celle-ci existe.
    */
    public int sendMessage(MessagePacket msg){
        int convID = -1;
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "sendMessage"));
        pairs.add(new BasicNameValuePair("messageContent", msg.getMessage()));
        pairs.add(new BasicNameValuePair("msgSource", "" + msg.getSource()));
        pairs.add(new BasicNameValuePair("destinataires", msg.getDestinataires()));
        pairs.add(new BasicNameValuePair("conversationID", ""+msg.getConvID()));
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date tStmp = msg.getTimestamp();
		String time = sdf.format(tStmp);
        pairs.add(new BasicNameValuePair("timeStamp", time));
        pairs.add(new BasicNameValuePair("attachement", msg.getAttachement()));

        String ligneResult = sendRequest(pairs);

        if(ligneResult != null) {
            parser = new JSONParser(ligneResult);
            String status = parser.getStatus();
            if (!status.isEmpty()) {
                if (status.equalsIgnoreCase("Success")) {
                    convID = parser.getInt("conversationID");
                }
            }
        }
        return convID;
    }

    /*
    * Méthode qui lis tout les messages destiné à l'usager, qui sont plus vieux que lastMessageID
    *       Reçoit l'identifiant de l'usager ainsi que l'identifiant du dernier message reçu.
    *       Retourne une Liste de MessagePacket ou null s'il n'y a pas de nouveau message
    */
    public List<MessagePacket> readMessages(int userId, int lastMessageID){
        ArrayList<MessagePacket> msgList = null;
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "readMessages"));
        pairs.add(new BasicNameValuePair("userID", "" + userId));
        pairs.add(new BasicNameValuePair("msgID", "" + lastMessageID));
        String ligneResult = sendRequest(pairs);

        if(ligneResult != null) {
            parser = new JSONParser(ligneResult);
            String status = parser.getStatus();
            if (!status.isEmpty()) {
                if (status.equalsIgnoreCase("Success")) {
                    JSONArray jArray = parser.getList("msgQueue");
                    if (jArray != null) {
                        msgList = new ArrayList<MessagePacket>();
                        for (int idx = 0; idx < jArray.length(); idx++) {
                            try {
                                JSONObject jsonObject = jArray.getJSONObject(idx);
                                String time = jsonObject.getString("timeStamp");
                                Date convertedDate = new Date();
                                try {
                                    convertedDate = dateFormat.parse(time);
                                }catch (ParseException pe) {
                                    pe.printStackTrace();
                                }
                                MessagePacket msg = new MessagePacket(
                                        jsonObject.getInt("msgId"),
                                        jsonObject.getInt("source"),
                                        jsonObject.getInt("conversationID"),
                                        jsonObject.getString("destinataires"),
                                        jsonObject.getString("message"),
                                        convertedDate,
                                        jsonObject.getString("attachement"));
                                msgList.add(msg);
                                if(lDb != null){
                                    if(msg.getId_msg() > lastMessageID) {
                                        lastMessageID = msg.getId_msg();
                                        lDb.writeMessage(msg);
                                        lDb.setLastMsgIndex(userId, lastMessageID);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return  msgList;
    }

    /*
    *  Méthode qui envoi un sondage au réseau commun.
    *       Reçoit un SurveyGroup
    *       Ne retourne rien
    */
    public void sendSurvey(SurveyGroup  sGrp){
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "createSurveyForm"));
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date tStmp = sGrp.getDateLimite();
        String dueDate = sdf.format(tStmp);
        pairs.add(new BasicNameValuePair("dateLimite", dueDate));
        String ligneResult = sendRequest(pairs);

        if(ligneResult != null) {
            parser = new JSONParser(ligneResult);
            String status = parser.getStatus();
            if (!status.isEmpty()) {
                if (status.equalsIgnoreCase("Success")) {
                    int srvId = parser.getIndex();

                    List<SurveyObject> qList = sGrp.getQuestions();
                    Iterator<SurveyObject> iter = qList.iterator();
                    while(iter.hasNext()){
                        SurveyObject sObj = iter.next();
                        pairs = new ArrayList<NameValuePair>();
                        pairs.add(new BasicNameValuePair("action", "sendSurveyQuestion"));
                        pairs.add(new BasicNameValuePair("id_survey", ""+srvId));
                        pairs.add(new BasicNameValuePair("question", ""+sObj.getQuestionTexte()));
                        pairs.add(new BasicNameValuePair("type", ""+sObj.getIntType()));
                        ArrayList<String> reps  = sObj.getChoixReponse();
                        String listeReponses = reps.get(0);
                        for(int r=1; r<reps.size(); r++)
                            listeReponses += ","+reps.get(r);
                        pairs.add(new BasicNameValuePair("listeReponses", listeReponses));
                        String s = sendRequest(pairs);
                    }
                }
            }
        }
    }
    /*
    * Méthode qui lis un sondage
    *       Reçoit l'identifiant su sondage à lire
    *       Retourne un SurveyGroup contenant la liste des questions
    */
    public SurveyGroup readSurvey(int surveyID){
        SurveyGroup surveyGrp = null;

        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "readSurvey"));
        pairs.add(new BasicNameValuePair("surveyID", "" + surveyID));
        String ligneResult = sendRequest(pairs);

        if(ligneResult != null) {
            parser = new JSONParser(ligneResult);
            String status = parser.getStatus();
            if (!status.isEmpty()) {
                if (status.equalsIgnoreCase("Success")) {
                    JSONObject jsonObject = parser.getJSONObject("survey");
                    Date convertedDate = new Date();
                    int srvId = -1;
                    try {
                        srvId = jsonObject.getInt("id");
                        String time = jsonObject.getString("dueTime");
                        convertedDate = dateFormat.parse(time);
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }catch (ParseException pe) {
                        pe.printStackTrace();
                    }
                    if(srvId >0)
                        surveyGrp = new SurveyGroup( srvId, convertedDate );
                    if(surveyGrp != null){
                        try{
                            SurveyObject qObj = null;
                            JSONArray jArray = jsonObject.getJSONArray("questions");
                            for (int idx = 0; idx < jArray.length(); idx++) {
                                JSONObject questionObject = jArray.getJSONObject(idx);
                                int type = questionObject.getInt("type");
                                switch(type) {
                                    case 0:
                                        qObj = new SurveyYesNo(
                                                questionObject.getInt("id"),
                                                questionObject.getString("question"));
                                        break;
                                    case 1:
                                        String choix = questionObject.getString("choix");
                                        String [] chList = choix.split(",");
                                        qObj = new SurveyMultiple(
                                                questionObject.getInt("id"),
                                                questionObject.getString("question"),
                                                chList.length,
                                                chList);
                                        break;
                                    case 2:
                                        qObj = new SurveyText(
                                                questionObject.getInt("id"),
                                                questionObject.getString("question"));
                                        break;
                                    default:
                                        qObj=null;
                                        break;
                                }
                                if(qObj != null)
                                    surveyGrp.addQuestion(qObj);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return surveyGrp;
    }

    /*
    * Méthode qui lis la listes de sondages. Cette liste ne contient que des coquilles
    * c'est à qu'elle ne contient que les ID et les dates de péremption pour chaque sondage
    *       Reçoit : rien.
    *       Retourne un ArrayList contenant la liste des identifiants.
    */
    public ArrayList<SurveyGroup> getSurveyList() {
        ArrayList<SurveyGroup> list = null;
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "getListSurvey"));
        String ligneResult = sendRequest(pairs);
        if (ligneResult != null) {
            parser = new JSONParser(ligneResult);
            String status = parser.getStatus();
            if (!status.isEmpty()) {
                if (status.equalsIgnoreCase("Success")) {
                    JSONArray jArray = parser.getList("surveyList");
                    if (jArray != null) {
                        list = new ArrayList<SurveyGroup>();
                        for (int idx = 0; idx < jArray.length(); idx++) {
                            try {
                                JSONObject jObj = jArray.getJSONObject(idx);
                                if (jObj != null) {
                                    int id = jObj.getInt("id");
                                    String time = jObj.getString("date");
                                    Date convertedDate = new Date();
                                    try {
                                        convertedDate = dateFormat.parse(time);
                                    } catch (ParseException pe) {
                                        pe.printStackTrace();
                                    }
                                    SurveyGroup sGrp = new SurveyGroup(id, convertedDate);
                                    list.add(sGrp);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    /*
    * Méthode qui lis la listes des sondages non lu.
    *       Reçoit l'identifiant du dernier sondage déjà lu.
    *       Retourne un tableau contenant la liste des identifiants non-lu
    */
    public Integer [] readSurveyList(int lastSurveyID){
        Integer[] surveylist = null;
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "readSurveyList"));
        pairs.add(new BasicNameValuePair("surveyID", "" + lastSurveyID));
        String ligneResult = sendRequest(pairs);

        if(ligneResult != null) {
            parser = new JSONParser(ligneResult);
            String status = parser.getStatus();
            if (!status.isEmpty()) {
                if (status.equalsIgnoreCase("Success")) {
                    JSONArray jArray = parser.getList("surveyList");
                    if (jArray != null) {
                        surveylist = new Integer[jArray.length()];
                        for (int idx = 0; idx < jArray.length(); idx++) {
                            try {
                                surveylist[idx] = jArray.getInt(idx);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return surveylist;
    }

    /*
    * Méthode qui envois la réponse à une question de sondage sur les serveur
    *       Reçoit l'identifiant de l'usager ainsi qu'un SurveyAnswer contenant la réponse
    *       Ne retourne rien
    */
    public void answerSurveyQuestion(int UserID, SurveyAnswer sQuestion){
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "answerSurveyQuestion"));
        pairs.add(new BasicNameValuePair("questionID", ""+sQuestion.getQuestionId()));
        pairs.add(new BasicNameValuePair("sourceID", ""+UserID));
        pairs.add(new BasicNameValuePair("reponseInt", ""+sQuestion.getReponseInt()));
        pairs.add(new BasicNameValuePair("reponseText", sQuestion.getReponseTexte()));
        sendRequestNoResponse(pairs);
    }

    /*
    * Méthode poutr lire les résultats de sondage pour une question donnée.
    *       Reçoit l'identifiant de la question.
    *       Retourne un object surveyAnswerObject
    */
    public ArrayList<SurveyObjectResults> readSurveyResults(int surveyId){
        ArrayList<SurveyObjectResults> srvAnswrObj = null;
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "readSurveyResults"));
        pairs.add(new BasicNameValuePair("surveyID", "" + surveyId));
        String ligneResult = sendRequest(pairs);

        if(ligneResult != null) {
            parser = new JSONParser(ligneResult);

            String status = parser.getStatus();
            if (!status.isEmpty()) {
                if (status.equalsIgnoreCase("Success")) {
                    JSONArray jArray = parser.getList("questions");
                    if(jArray != null){
                        srvAnswrObj = new ArrayList<>();
                        for (int idx = 0; idx < jArray.length(); idx++){
                            try {
                                JSONObject srvy = jArray.getJSONObject(idx);
                                ArrayList<SurveyPair> answersAndHit = new ArrayList<>();
                                JSONArray respArray = srvy.getJSONArray("reponses");
                                int hit=0, ttlHit=0;
                                for(int jdx=0; jdx<respArray.length(); jdx++){
                                    JSONObject joPesp = respArray.getJSONObject(jdx);
                                    hit = joPesp.getInt("hit");
                                    ttlHit += hit;
                                    SurveyPair pair = new SurveyPair(joPesp.getString("label"), ""+hit);
                                    answersAndHit.add(pair);
                                }
                                SurveyObjectResults sor = new SurveyObjectResults(
                                        srvy.getString("question"), ttlHit, answersAndHit );
                                srvAnswrObj.add(sor);

                            }catch(JSONException  je){

                            }
                        }
                    }
                }
            }
        }

        return srvAnswrObj;
    }

}
