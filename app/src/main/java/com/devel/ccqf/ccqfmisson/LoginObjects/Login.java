package com.devel.ccqf.ccqfmisson.LoginObjects;

/**
 * Created by jo on 5/24/16.
 */
public class Login {
    String nom;
    String prenom;
   // String email;
    String companie;

    public Login(String nom, String prenom, String companie) {
        this.nom = nom;
        this.prenom = prenom;
        this.companie = companie;
    }

    @Override
    public String toString() {
        return "Login{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", companie='" + companie + '\'' +
                '}';
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

    public String getCompanie(){
        return companie;
    }

    public void setCompanie(String companie){
        this.companie = companie;
    }

}
