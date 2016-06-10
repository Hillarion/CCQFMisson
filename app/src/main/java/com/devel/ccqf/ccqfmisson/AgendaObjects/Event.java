package com.devel.ccqf.ccqfmisson.AgendaObjects;

import java.util.Date;

/**
 * Created by jo on 5/16/16.
 */
public class Event {
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
}