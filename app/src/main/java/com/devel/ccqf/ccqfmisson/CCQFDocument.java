package com.devel.ccqf.ccqfmisson;

/**
 * Created by thierry on 16-06-03.
 */
public class CCQFDocument {
    String titre;
    String fileName;
    String path;

    public CCQFDocument(){
        this("", "", "");
    }

    public CCQFDocument(String titre, String path, String fName){
        this.titre=titre;
        this.path = path;
        this.fileName = fName;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setPath(String path){
        this.path = path;
    }

    public String getPath(){
        return path;
    }

    public String getCompletePath(){
        return path+"/"+fileName;
    }
}
