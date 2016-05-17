package com.devel.ccqf.ccqfmisson.AgendaObjects;

import java.util.Date;

/**
 * Created by jo on 5/16/16.
 */
public class Event {
    protected String destinataire;
    protected String DTStart;
    protected String DTEnd;
    protected String nom;
    protected String poste;
    protected String telephone;
    protected String nomBatiment;
    protected boolean autreBatiment;




    public Event( String title, String time) {
        this.nom = title;
        this.DTStart = time;
    }

    public Event(String destinataire, String DTStart, String DTEnd, String nom, String poste,
                 String telephone, String nomBatiment, boolean autreBatiment) {
        this.destinataire = destinataire;
        this.DTStart = DTStart;
        this.DTEnd = DTEnd;
        this.nom = nom;
        this.poste = poste;
        this.telephone = telephone;
        this.nomBatiment = nomBatiment;
        this.autreBatiment = autreBatiment;
    }

    @Override
    public String toString() {
        return "Event{" +
                "destinataire='" + destinataire + '\'' +
                ", DTStart='" + DTStart + '\'' +
                ", DTEnd='" + DTEnd + '\'' +
                ", nom='" + nom + '\'' +
                ", poste='" + poste + '\'' +
                ", telephone='" + telephone + '\'' +
                ", nomBatiment='" + nomBatiment + '\'' +
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

    public String getNomBatiment() {
        return nomBatiment;
    }

    public void setNomBatiment(String nomBatiment) {
        this.nomBatiment = nomBatiment;
    }

    public boolean isAutreBatiment() {
        return autreBatiment;
    }

    public void setAutreBatiment(boolean autreBatiment) {
        this.autreBatiment = autreBatiment;
    }
}