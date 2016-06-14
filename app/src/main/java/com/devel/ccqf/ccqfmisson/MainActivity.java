package com.devel.ccqf.ccqfmisson;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.devel.ccqf.ccqfmisson.Database.InterfaceDB;
import com.devel.ccqf.ccqfmisson.Database.LocaleDB;
import com.devel.ccqf.ccqfmisson.LoginObjects.Login;
import com.devel.ccqf.ccqfmisson.Pub.Commanditaire;
import com.devel.ccqf.ccqfmisson.Pub.DialogRep;
import com.devel.ccqf.ccqfmisson.Utilitairies.FeedAlarmReceiver;
import com.devel.ccqf.ccqfmisson.Utilitairies.FileDownLoader;
import com.devel.ccqf.ccqfmisson.Utilitairies.FontsOverride;
import com.devel.ccqf.ccqfmisson.Utilitairies.Verify;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends CCQFBaseActivity {
    private Button btnDoc;
    private Button btnProg;
    private Button btnAgenda;
    private Button btnFeed;
    private InterfaceDB iDb;
    private ImageView ibBanner;
    final static String dirUrl = "http://thierrystpierre.ddns.net:81/CCQFMission/Commanditaires";
    private static String baseApplicationFilesPath;
    private ArrayList<Commanditaire> menuPage =  null;
    private ArrayList<Commanditaire> menuBanniere =  null;
    private Commanditaire currentBanner = null;
    private LocaleDB lDb;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iDb = new InterfaceDB(MainActivity.this);
        baseApplicationFilesPath = "" + Environment.getDataDirectory().getPath() + "/data/" +
                getPackageName() + "/Files/Commanditaires";

        String [] params = {dirUrl, baseApplicationFilesPath, "menu.csv"};
        int user = -1;
        InterfaceDB iDb = new InterfaceDB(MainActivity.this);
        new getCommanditairesAsyncTask().execute(params);

        FontsOverride.setDefaultFont(MainActivity.this, "MONOSPACE", "fonts/Myriad Web Bold.otf");//<- changer la font ici
        DialogRep dr = new DialogRep(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Survey.class);
                startActivity(i);
            }
        });

        ibBanner = (ImageView)findViewById(R.id.imageButton);
        ibBanner.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(currentBanner != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(currentBanner.getUrl()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }
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
        if(iDb.isUserEmpty() == 0){
            dialogLogin();
        }
        else {
            SystemClock.sleep(1500);
            user = iDb.getCurrentUserID();
            if(menuPage != null) {
                int cmdtIdx = iDb.getCurrentPageIndex();
                if (cmdtIdx < menuPage.size()) {
                    Commanditaire cmdt = menuPage.get(cmdtIdx);
                    dr.setPub(cmdt);
                }
            }
            dr.dialogPub(MainActivity.this);
        }

        InterfaceDB.privilegeType privilege = iDb.getPrivilege(iDb.getCurrentUserID());
        if(privilege == InterfaceDB.privilegeType.ADMIN)
            enableMenu = true;

        if ((iDb != null) && (user > 0)){
            Intent intent = new Intent(getApplicationContext(), FeedAlarmReceiver.class);
            final PendingIntent pIntent= PendingIntent.getBroadcast(this, FeedAlarmReceiver.REQUEST_CODE,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            long firstMillis = System.currentTimeMillis(); // alarm is set right away
            AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                    15 * 1000, pIntent); // tout les 15 secondes
        }
        if(menuBanniere != null){
            int idx = iDb.getCurrentBannerIndex();
            System.out.print("CCQF MainActiviti onCreate Banner Area idx = "+idx +" menuBanniere="+menuBanniere.size()+"\n\n");
            System.out.flush();
            if((idx >= 0) && (idx<menuBanniere.size())) {
                currentBanner = menuBanniere.get(idx);
                Drawable drawable = Drawable.createFromPath(currentBanner.getFilePath());
                ibBanner.setImageDrawable(drawable);
            }
        }


        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                new getSurveyAsyncTask().execute();
                System.out.print("CCQF MainActivity TimerTask querying survey");
                System.out.flush();
                // Implement the task you want to perform periodically
            }
        };
        Timer timer = new Timer();
        //schedule your timer to execute perodically
        timer.scheduleAtFixedRate(task, 1000, 30 /* 60 */* 1000);

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

    private class getCommanditairesAsyncTask extends AsyncTask<String, Void, Void>{
        private boolean uploadFile = false;

        @Override
        protected Void doInBackground(String... params) {
            long date = 0;
            boolean updatedFromServer = false;
            FileDownLoader fd = new FileDownLoader(params[2], params[1], params[0]);
            if (iDb.isNetAccessible()) {
                try {
                    if (!fd.isUptodate()) {
                        fd.getFileFromServer();
                        updatedFromServer = true;
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            menuPage = new ArrayList<Commanditaire>();
            menuBanniere = new ArrayList<Commanditaire>();

            File file = fd.getFileHandle();
            if (file.exists()) {
                try {
                    Scanner fSc = new Scanner(fd.getFileHandle());
                    fSc.useDelimiter("\n");
                    do {
                        String line = fSc.next();
                        String[] linetbl = line.split(";");

                        FileDownLoader fgl = new FileDownLoader(linetbl[2], params[1] + "/" + linetbl[0], params[0] + "/" + linetbl[0]);
                        if (!fgl.isUptodate())
                            fgl.getFileFromServer();
                        if (linetbl[0].equalsIgnoreCase("Pages"))
                            menuPage.add(new Commanditaire(params[1] + "/" + linetbl[0] + "/" + linetbl[2], linetbl[1]));
                        else if (linetbl[0].equalsIgnoreCase("Banners"))
                            menuBanniere.add(new Commanditaire(params[1] + "/" + linetbl[0] + "/" + linetbl[2], linetbl[1]));
                    } while (fSc.hasNext());
                    fSc.close();
//                    if(updatedFromServer)
                        iDb.initCommanditaires(menuPage.size(), menuBanniere.size());
                } catch (FileNotFoundException fnfe) {
                    fnfe.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    private class getSurveyAsyncTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            int [] lst = iDb.readSurveyList();
            System.out.print("CCQF MainActivity getSurveyAsyncTask doInBackground lst = "+ lst + "\n\n");
            System.out.flush();
            return  new Integer((lst == null)?0:1);
        }
        @Override
        protected void onPostExecute(Integer  val) {
            if(val != 0)
                fab.setVisibility(View.VISIBLE);
            else
                fab.setVisibility(View.INVISIBLE);
        }

    }
}
