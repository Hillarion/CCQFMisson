package com.devel.ccqf.ccqfmisson;

/**
 * Created by thierry on 16-05-27.
 */
public class Users {
    private int user_id;
    private String nom;
    private String prenom;
    private String userName;
    private String courriel;
    private String privilege;

    public Users(){
        this(-1, "", "", "", "", "");
    }

    public Users(int uId, String nom, String prenom, String userName, String courriel, String privilege){
        user_id = uId;
        this.nom = nom;
        this.prenom = prenom;
        this.userName = userName;
        this.courriel = courriel;
        this.privilege = privilege;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCourriel() {
        return courriel;
    }

    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }
}
