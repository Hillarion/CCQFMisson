package com.devel.ccqf.ccqfmisson;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thierry on 16-06-06.
 */
public class CCQFDocument implements Parcelable {
    private String titre;
    private String nomFichier;
    private String filePath;

    public CCQFDocument(){
        this("", "");
    }

    public CCQFDocument(String titre, String nom){
        this.titre = titre;
        this.nomFichier = nom;
        filePath = "";
    }

    public CCQFDocument(Parcel in){
        titre = in.readString();
        nomFichier = in.readString();
        filePath = in.readString();
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    void setPath(String path){
        filePath = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titre);
        dest.writeString(nomFichier);
        dest.writeString(filePath);
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
        return "CCQFDocument " + titre + ", " + nomFichier + ", path = " + filePath;
    }
}
