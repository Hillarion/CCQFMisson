package com.devel.ccqf.ccqfmisson.Utilitairies;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by thierry on 09/06/16.
 */
public class FileDownLoader {
    private String nomFichier;
    private String filePath;
    private String baseURL;
    private static final int  MEGABYTE = 1024 * 1024;
    private File fichier;


    public FileDownLoader (String nom, String path, String url){
        nomFichier = nom;
        filePath = path;
        baseURL = url;
        fichier = new File(filePath, nomFichier);
    }

    public boolean isUptodate() throws IOException {
        boolean upToDate = false;
        long date = 0;

        URL url = new URL(baseURL + "/" + nomFichier);
        File docBaseDir = new File(filePath);
        File fichier = new File(filePath, nomFichier);

        if (docBaseDir.exists()) {
            if (fichier.exists()) {
                HttpURLConnection.setFollowRedirects(false);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                date = con.getLastModified();
                long dateLocal = fichier.lastModified();
                if (dateLocal >= date)
                    upToDate = true;
            }
        }

        return upToDate;
    }

    public void getFileFromServer() throws IOException{
        URL url = new URL(baseURL + "/" + nomFichier);
        File docBaseDir = new File(filePath);
        File fichier = new File(filePath, nomFichier);
        if (!docBaseDir.exists())
            docBaseDir.mkdirs();

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoOutput(true);
        urlConnection.connect();
        InputStream inputStream = urlConnection.getInputStream();
        fichier.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(fichier);
        int totalSize = urlConnection.getContentLength();
        byte[] buffer = new byte[Math.min(totalSize + 1, MEGABYTE)];
        int bufferLength = 0;
        while ((bufferLength = inputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, bufferLength);
        }
        fileOutputStream.close();
    }


    public File getFileHandle(){
        return fichier;
    }
}
