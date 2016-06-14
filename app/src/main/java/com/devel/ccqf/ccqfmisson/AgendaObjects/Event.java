package com.devel.ccqf.ccqfmisson.AgendaObjects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by jo on 5/16/16.
 */
public class Event implements Parcelable {
    protected String destinataire;
    protected String DTStart;
    protected String DTEnd;
    protected String compagnie;
    protected String nom;
    protected String poste;
    protected String telephone;
    protected String email;
    protected String adresse;
    protected boolean autreBatiment;

    public Event( String title, String timeStart, String timeEnd) {
        this.nom = title;
        this.DTStart = timeStart;
        this.DTEnd = timeEnd;
    }

    public Event( String title, String timeStart, String timeEnd, String compagnie) { //constructeur pour liste b2b
        this.nom = title;
        this.DTStart = timeStart;
        this.DTEnd = timeEnd;
        this.compagnie = compagnie;
    }

    public Event( String title, String timeStart) {
        this.nom = title;
        this.DTStart = timeStart;
    }

    public Event(String destinataire, String DTStart, String DTEnd,String compagnie, String nom, String poste,
                 String telephone,String email,  String adresse, boolean autreBatiment) {
        this.destinataire = destinataire;
        this.DTStart = DTStart;
        this.DTEnd = DTEnd;
        this.compagnie = compagnie;
        this.nom = nom;
        this.poste = poste;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
        this.autreBatiment = autreBatiment;
    }

    protected Event(Parcel in) {
        destinataire = in.readString();
        DTStart = in.readString();
        DTEnd = in.readString();
        compagnie = in.readString();
        nom = in.readString();
        poste = in.readString();
        telephone = in.readString();
        email = in.readString();
        adresse = in.readString();
        autreBatiment = in.readByte() != 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public String toString() {
        return "Event{" +
                "destinataire='" + destinataire + '\'' +
                ", DTStart='" + DTStart + '\'' +
                ", DTEnd='" + DTEnd + '\'' +
                ", compagnie='" + compagnie + '\'' +
                ", nom='" + nom + '\'' +
                ", poste='" + poste + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email +'\''+
                ", adresse='" + adresse + '\'' +
                ", autreBatiment=" + autreBatiment +
                '}';
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public String getDTStart() {
        return DTStart;
    }

    public void setDTStart(String DTStart) {
        this.DTStart = DTStart;
    }

    public String getDTEnd() {
        return DTEnd;
    }

    public void setDTEnd(String DTEnd) {
        this.DTEnd = DTEnd;
    }

    public String getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(String compagnie) {
        this.compagnie = compagnie;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public boolean isAutreBatiment() {
        return autreBatiment;
    }

    public void setAutreBatiment(boolean autreBatiment) {
        this.autreBatiment = autreBatiment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(destinataire);
        dest.writeString(DTStart);
        dest.writeString(DTEnd);
        dest.writeString(compagnie);
        dest.writeString(nom);
        dest.writeString(poste);
        dest.writeString(telephone);
        dest.writeString(email);
        dest.writeString(adresse);
        dest.writeByte((byte) (autreBatiment ? 1 : 0));

    }


}