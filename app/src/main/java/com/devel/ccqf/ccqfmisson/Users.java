package com.devel.ccqf.ccqfmisson;

/**
 * Created by thierry on 16-05-27.
 */
public class Users {
    private int user_id;
    private String nom;
    private String prenom;
    private String compagnie;
    private String privilege;

    public Users(){
        this(-1, "", "", "", "");
    }

    public Users(int uId, String nom, String prenom, String compagnie, String privilege){
        user_id = uId;
        this.nom = nom;
        this.prenom = prenom;
        this.compagnie = compagnie;
        this.privilege = privilege;
    }

    public int getUserID() {
        return user_id;
    }

    public String getUID() {
        return ""+user_id;
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
        return prenom + "_" + nom + "@" + compagnie;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String toString(){
        return "" + user_id + " : " + getUserName();
    }
}
