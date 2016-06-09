package com.devel.ccqf.ccqfmisson;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.devel.ccqf.ccqfmisson.Database.InterfaceDB;
import com.devel.ccqf.ccqfmisson.Database.LocaleDB;
import com.devel.ccqf.ccqfmisson.LoginObjects.Login;
import com.devel.ccqf.ccqfmisson.Pub.Commenditaire;
import com.devel.ccqf.ccqfmisson.Pub.DialogRep;
import com.devel.ccqf.ccqfmisson.Utilitairies.FileDownLoader;
import com.devel.ccqf.ccqfmisson.Utilitairies.FontsOverride;
import com.devel.ccqf.ccqfmisson.Utilitairies.Verify;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends CCQFBaseActivity {
    private Button btnDoc;
    private Button btnProg;
    private Button btnAgenda;
    private Button btnFeed;
    private InterfaceDB iDb;
    final static String dirUrl = "http://thierrystpierre.ddns.net:81/CCQFMission/Commanditaires";
    private static String baseApplicationFilesPath;
    private ArrayList<Commenditaire> menuPage =  null;
    private ArrayList<Commenditaire> menuBanniere =  null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iDb = new InterfaceDB(MainActivity.this);
        baseApplicationFilesPath = "" + Environment.getExternalStorageDirectory() + "/" +
                getPackageName() + "/Commanditaires";

        String [] params = {dirUrl, baseApplicationFilesPath, "menu.csv"};

        FontsOverride.setDefaultFont(MainActivity.this, "MONOSPACE", "fonts/Myriad Web Bold.otf");//<- changer la font ici
        DialogRep dr = new DialogRep(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        int user = -1;
        InterfaceDB iDb = new InterfaceDB(MainActivity.this);
        new getCommenditairesAsyncTask().execute(params);

        if(iDb.isUserEmpty() == 0){
            dialogLogin();
        }
        else {
            user = iDb.getCurrentUserID();
/*            if(menuPage != null)
                dr.setPub(menuPage[iDb.getCurrentPageIndex()]);*/
            dr.dialogPub(MainActivity.this);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Survey.class);
                startActivity(i);
            }
        });

        btnAgenda = (Button)findViewById(R.id.myAgendaActivityBtn);
        btnAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MonAgenda.class);
                startActivity(i);
            }
        });

        btnDoc = (Button)findViewById(R.id.docActivityBtn);
        btnDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Doc.class);
                startActivity(i);
            }
        });

        btnProg = (Button)findViewById(R.id.programActivityBtn);
        btnProg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProgramDays.class);
                startActivity(i);
            }
        });

        btnFeed = (Button)findViewById(R.id.feedActivityBtn);
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, FeedListActivity.class);
                startActivity(i);
            }
        });
    }

    public void dialogLogin(){

        final Dialog d = new Dialog(MainActivity.this);
        d.setContentView(R.layout.dialog_login);
        d.setTitle("Enregistrez-Vous");
        d.setCancelable(false);
        final EditText txtPrenom = (EditText)d.findViewById(R.id.txtLoginName);
        final EditText txtNom = (EditText)d.findViewById(R.id.txtLoginLastName);
        final EditText txtSurnom = (EditText)d.findViewById(R.id.txtLoginScreenName);
        Button btnOK = (Button)d.findViewById(R.id.btnLoginOk);
        d.show();

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prenom = txtPrenom.getText().toString();
                String nom = txtNom.getText().toString();
                String compagnie = txtSurnom.getText().toString();
                Verify verify = new Verify();

                if(verify.isValidName(prenom)){

                     if(verify.isValidName(nom)) {
                         
                         if(!compagnie.isEmpty()){
//   pour référence
//                       int  userID =  iDb.registerUser(String nom, String prenom, String compagnie) ;
                             new SendLoginAsyncTask().execute(nom, prenom, compagnie);
                             Toast.makeText
                                     (MainActivity.this, ""+nom +"_"+prenom+"@"+compagnie,
                                             Toast.LENGTH_SHORT).show();

                             InterfaceDB iDb = new InterfaceDB(MainActivity.this);
                             LocaleDB lDb = new LocaleDB(MainActivity.this);
                             iDb.registerUser(nom, prenom, compagnie);
//                             lDb.setUser(1, nom, prenom, compagnie);

                             d.dismiss();
                         } else{
                             Toast.makeText(MainActivity.this, "Companie vide", Toast.LENGTH_SHORT).
                                     show();
                         }

                     } else{
                         Toast.makeText(MainActivity.this, "Nom Invalide", Toast.LENGTH_SHORT).
                                 show();
                     }

                } else{
                    Toast.makeText(MainActivity.this, "Prenom Invalide", Toast.LENGTH_SHORT).show();
                }
                
                
                
             /*   Login login = new Login(prenom, nom, email, surnom);
                d.dismiss();*/
            }
        });
    }

    private class SendLoginAsyncTask extends AsyncTask<String, Void, Login>{

        @Override
        protected Login doInBackground(String... login) {

            if(iDb != null)
                iDb.registerUser(login[0],  login[1], login[2]);

            return null;
        }

        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }

        @Override
        protected void onProgressUpdate(Void... text) {
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }
    }

    private class getCommenditairesAsyncTask extends AsyncTask<String, Void, Void>{
        private boolean uploadFile = false;

        @Override
        protected Void doInBackground(String... params) {
            long date = 0;
            if (iDb.isNetAccessible()) {
                try {
                    FileDownLoader fd = new FileDownLoader(params[2], params[1], params[0]);
                    if (!fd.isUptodate())
                        fd.getFileFromServer();

                    menuPage = new ArrayList<Commenditaire>();
                    menuBanniere = new ArrayList<Commenditaire>();

                    Scanner fSc = new Scanner(fd.getFileHandle());
                    fSc.useDelimiter("\n");
                    do {
                        String line = fSc.next();
                        String[] linetbl = line.split(";");

                        FileDownLoader fgl = new FileDownLoader(linetbl[2], params[1]+"/"+linetbl[0], params[0]+"/"+linetbl[0]);
                        if(!fgl.isUptodate())
                            fgl.getFileFromServer();
                        if(linetbl[0].equalsIgnoreCase("Pages"))
                            menuPage.add(new Commenditaire(params[1]+"/"+linetbl[0], linetbl[1]));
                        else if(linetbl[0].equalsIgnoreCase("Banners"))
                            menuBanniere.add(new Commenditaire(params[1]+"/"+linetbl[0], linetbl[1]));
                    } while (fSc.hasNext());
                    fSc.close();
                    iDb.initCommenditaires(menuPage.size(), menuBanniere.size());
                } catch (FileNotFoundException fnfe) {
                    fnfe.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

}
