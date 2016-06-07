package com.devel.ccqf.ccqfmisson;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.devel.ccqf.ccqfmisson.AgendaObjects.Event;
import com.devel.ccqf.ccqfmisson.Database.InterfaceDB;
import com.devel.ccqf.ccqfmisson.Database.RemoteDB;
import com.devel.ccqf.ccqfmisson.LoginObjects.Login;
import com.devel.ccqf.ccqfmisson.Utilitairies.Verify;

import java.util.ArrayList;

public class NewB2B extends CCQFBaseActivity {
    private Spinner spnDestinataire;
    private EditText btnDebut;
    private TextView txtDebut;
    private TextView txtFin;
    private EditText txtCompanie;
    private EditText txtNom;
    private EditText txtPoste;
    private EditText txtTelephone;
    private EditText txtAdresse;
    private EditText txtEmail;
    private Spinner spinner;
    private Button btnDone;

    private String destinataire;
    private String heureDebut;
    private String heureFin;
    private String compagnie;
    private String nom;
    private String poste;
    private String telephnone;
    private String batiment;
    private String email;
    private boolean ext = false;
    String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_b2_b);

        initialize();
        fillBatimentSpinner();
        //get Login adresse
     /*   ArrayList<String> alTempLoginList = new ArrayList<>();
        alTempLoginList.add("jonathan_bleau@derp.com");
        alTempLoginList.add("jacinthe_desrochers@derp.com");
        alTempLoginList.add("simon_petit@derp.com");*/
        fillDestinataireSpinner(loginAdressList());



        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destinataire = spnDestinataire.getSelectedItem().toString();
                heureDebut = txtDebut.getText().toString();
                heureFin = txtFin.getText().toString();
                compagnie = txtCompanie.getText().toString();
                nom = txtNom.getText().toString();
                poste = txtPoste.getText().toString();
                telephnone = txtTelephone.getText().toString();
                email = txtEmail.getText().toString();
                batiment = txtAdresse.getText().toString();
                if(spinner.getSelectedItem().toString().equals("Batiment Extérieur Vrai")){
                    ext = true;
                }
                else {
                    ext = false;
                }
                //Validation
                Verify ve = new Verify();
                if (!compagnie.equals("")) {
                    if(!heureDebut.equals("")){
                        if(!heureFin.equals("")){
                            if(!nom.equals("")){
                                if(!poste.equals("")){
                                    if(ve.isValidPhone(telephnone)){
                                        if(ve.isValidEmail(email)){
                                            if(!batiment.equals("")){
                                                Event e = new Event(destinataire, heureDebut,
                                                        heureFin, compagnie, nom, poste, telephnone,
                                                        email, batiment, ext);
                                                new SendB2BAsyncTask().execute(e);

                                            }
                                            else{
                                                Toast.makeText
                                                        (NewB2B.this, "Batiment invalide",
                                                                Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else {
                                            Toast.makeText
                                                    (NewB2B.this, "Email invalide", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }
                                    else {
                                        Toast.makeText
                                                (NewB2B.this, "Numero invalide", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }
                                else{Toast.makeText
                                        (NewB2B.this, "Poste vide", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(NewB2B.this, "Nom invalide", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(NewB2B.this, "Heure invalide", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(NewB2B.this, "Heure invalide", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(NewB2B.this, "Companie Invalide", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void fillBatimentSpinner(){
        ArrayList<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Batiment Extérieur Faux");
        spinnerArray.add("Batiment Extérieur Vrai");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }
    public void initialize(){
        spnDestinataire = (Spinner)findViewById(R.id.spnB2BDestinataire);
        txtDebut = (TextView)findViewById(R.id.txtB2bHeureDebut);
        txtFin = (TextView)findViewById(R.id.txtB2bHeureFin);
        txtCompanie = (EditText)findViewById(R.id.editTextB2bCompanie);
        txtNom = (EditText)findViewById(R.id.editTextB2bNom);
        txtPoste = (EditText)findViewById(R.id.editTextB2bPoste);
        txtTelephone = (EditText)findViewById(R.id.editTextB2bPhone);
        txtAdresse = (EditText)findViewById(R.id.editTextB2bBatiment);
        txtEmail = (EditText)findViewById(R.id.editTextB2bEmail);
        spinner = (Spinner)findViewById(R.id.spinnerBatiementExterieur);
        btnDone = (Button)findViewById(R.id.btnB2bOk);
    }

    public void fillDestinataireSpinner(ArrayList<String> alLoginAdress){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, alLoginAdress);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDestinataire.setAdapter(adapter);
    }

    public ArrayList<String> loginAdressList(){
        ArrayList<String> alLoginAdress;
        alLoginAdress = new ArrayList<>();
        InterfaceDB iDb = new InterfaceDB(NewB2B.this);
        ArrayList<Users> alUser = iDb.getUserList();
        for(int i = 0; i<alUser.size(); i++){
            alLoginAdress.add(alUser.get(i).getUserName());
        }
        return alLoginAdress;
    }

    public void clean(){
        txtDebut.setText("");
        txtFin.setText("");
        txtCompanie.setText("");
        txtPoste.setText("");
        txtTelephone.setText("");
        txtEmail.setText("");
        txtAdresse.setText("");

    }

    private class SendB2BAsyncTask extends AsyncTask<Event, Void, Event>{

        @Override
        protected Event doInBackground(Event... event) {
            InterfaceDB iDb = new InterfaceDB(NewB2B.this);

            if(iDb != null){
                iDb.registerEvent(event[0]);

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