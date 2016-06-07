package com.devel.ccqf.ccqfmisson;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by thierry on 16-06-06.
 */


public class CCQFCategorie implements Parcelable{
    private String nom;
    private ArrayList<CCQFDocument> itemsList;


    public CCQFCategorie() {
        this("");
    }

    public CCQFCategorie(String nom){
        this.nom = nom;
        itemsList = new ArrayList<CCQFDocument>();
    }

    public CCQFCategorie(Parcel in){
        nom = in.readString();
        itemsList = new ArrayList<CCQFDocument>();
        in.readList(itemsList, CCQFDocument.class.getClassLoader());
//        in.readTypedList(itemsList, CCQFDocument.CREATOR);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setItemsList(ArrayList<CCQFDocument> itemsList) {
        this.itemsList = itemsList;
    }

    public void addDocument(CCQFDocument  doc){
        itemsList.add(doc);
    }

    public ArrayList<CCQFDocument> getItemsList(){
        return itemsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
//        dest.writeTypedList(itemsList);
        dest.writeList(itemsList);

    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CCQFDocument createFromParcel(Parcel in) {
            return new CCQFDocument(in);
        }

        public CCQFDocument[] newArray(int size) {
            return new CCQFDocument[0];
        }
    };

    public String toString(){
        return "CCQFCategorie : " + nom + "[" + itemsList + "]";
    }
}
