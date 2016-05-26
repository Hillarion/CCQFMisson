package com.devel.ccqf.ccqfmisson;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import com.devel.ccqf.ccqfmisson.LoginObjects.Login;
import com.devel.ccqf.ccqfmisson.Utilitairies.Verify;

public class MainActivity extends AppCompatActivity {
    ActionMenuView amvMenu;
    private Button btnDoc;
    private Button btnProg;
    private Button btnAgenda;
    private Button btnFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialogLogin();

        Toolbar toolbar = (Toolbar) findViewById(R.id.tToolbar);
        amvMenu = (ActionMenuView) toolbar.findViewById(R.id.amvMenu);
        amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });
        setSupportActionBar(toolbar);
        Drawable logo = null;
        logo = getResources().getDrawable(R.mipmap.ccqf_logo);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setLogo(logo);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View child = toolbar.getChildAt(i);
            if (child != null)
                if (child.getClass() == ImageView.class) {
                    ImageView iv2 = (ImageView) child;
                    if (iv2.getDrawable() == logo) {
                        iv2.setAdjustViewBounds(true);
                    }
                }
        }

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
                Intent i = new Intent(MainActivity.this, Feed.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main,  amvMenu.getMenu());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Options réservés au compte admin
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings://Nouveau Survey
                Intent i = new Intent(MainActivity.this, SurveyCreate.class);
                startActivity(i);
                break;
            case R.id.action_result://Consulter résulat du survey
                Intent i2 = new Intent(MainActivity.this, SurveyResults.class);
                startActivity(i2);
                break;
            case R.id.action_newB2b://Nouveau B2B (formulaire quie crée un object Event)
                Intent b2b = new Intent(MainActivity.this, NewB2B.class);
                startActivity(b2b);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void dialogLogin(){

        final Dialog d = new Dialog(MainActivity.this);
        d.setContentView(R.layout.dialog_login);
        d.setTitle("Enregistrez-Vous");
        //d.setCancelable(false);
        final EditText txtPrenom = (EditText)d.findViewById(R.id.txtLoginName);
        final EditText txtNom = (EditText)d.findViewById(R.id.txtLoginLastName);
        final EditText txtEmail = (EditText)d.findViewById(R.id.txtLoginLastName);
        final EditText txtSurnom = (EditText)d.findViewById(R.id.txtLoginScreenName);
        Button btnOK = (Button)d.findViewById(R.id.btnLoginOk);
        d.show();

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prenom = txtPrenom.getText().toString();
                String nom = txtNom.getText().toString();
                String email = txtEmail.getText().toString();
                String surnom = txtSurnom.getText().toString();
                Verify verify = new Verify();

                if(verify.isValidName(prenom)){

                     if(verify.isValidName(nom)) {
                         
                         if(!surnom.isEmpty()){

                             if (verify.isValidEmail(email)){
                                 d.dismiss(); //a vérifier

                             } else if(!verify.isValidEmail(email)) {
                                 Toast.makeText(MainActivity.this, "Email Invalide",
                                         Toast.LENGTH_SHORT).show();
                             }

                         } else{
                             Toast.makeText(MainActivity.this, "Surnom vide", Toast.LENGTH_SHORT).
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


}
