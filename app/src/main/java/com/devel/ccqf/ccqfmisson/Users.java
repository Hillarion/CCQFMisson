package com.devel.ccqf.ccqfmisson;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thierry on 16-05-27.
 */
public class Users implements Parcelable {
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

    public Users(Parcel in){
        user_id = in.readInt();
        this.nom = in.readString();
        this.prenom = in.readString();
        this.compagnie = in.readString();
        this.privilege = in.readString();
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

    public String getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(String compagnie) {
        this.compagnie = compagnie;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(user_id);
        dest.writeString(nom);
        dest.writeString(prenom);
        dest.writeString(compagnie);
        dest.writeString(privilege);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        public Users[] newArray(int size) {
            return new Users[0];
        }
    };


}
