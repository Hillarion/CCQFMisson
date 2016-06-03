package com.devel.ccqf.ccqfmisson;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.devel.ccqf.ccqfmisson.LoginObjects.Login;
import com.devel.ccqf.ccqfmisson.Pub.DialogRep;
import com.devel.ccqf.ccqfmisson.Utilitairies.FontsOverride;
import com.devel.ccqf.ccqfmisson.Utilitairies.Verify;

public class MainActivity extends CCQFBaseActivity {
    private Button btnDoc;
    private Button btnProg;
    private Button btnAgenda;
    private Button btnFeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FontsOverride.setDefaultFont(MainActivity.this, "MONOSPACE", "fonts/Cronus Round.otf");//<- changer la font ici
        DialogRep dr = new DialogRep();

        dr.dialogPub(MainActivity.this);
        dialogLogin();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
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
        //d.setCancelable(false);
        final EditText txtPrenom = (EditText)d.findViewById(R.id.txtLoginName);
        final EditText txtNom = (EditText)d.findViewById(R.id.txtLoginLastName);
      //  final EditText txtEmail = (EditText)d.findViewById(R.id.txtLoginEmail);
        final EditText txtSurnom = (EditText)d.findViewById(R.id.txtLoginScreenName);
        Button btnOK = (Button)d.findViewById(R.id.btnLoginOk);
        d.show();

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prenom = txtPrenom.getText().toString();
                String nom = txtNom.getText().toString();
               // String email = txtEmail.getText().toString();
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

                            Login l = new Login(nom, prenom, compagnie);

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
            InterfaceDB iDb = new InterfaceDB(MainActivity.this);

            if(iDb != null){
                int id = iDb.registerUser(login[0],  login[1], login[2]);
                System.out.print("CCQF SendLoginAsyncTask user Id = " + id + "\n\n");
                System.out.flush();

            }return null;
        }
        protected void onPostExecute(Void...unused){

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

}
