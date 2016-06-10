package com.devel.ccqf.ccqfmisson.Pub;

/**
 * Created by thierry on 09/06/16.
 */
public class Commanditaire {
    String filePath;
    String url;

    public Commanditaire(String file, String url){
        filePath = file;
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString(){
        return filePath + ", " + url;
    }
}
