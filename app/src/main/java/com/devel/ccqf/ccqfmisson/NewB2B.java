package com.devel.ccqf.ccqfmisson;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.devel.ccqf.ccqfmisson.AgendaObjects.Event;
import com.devel.ccqf.ccqfmisson.Utilitairies.Verify;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NewB2B extends AppCompatActivity {
    private EditText txtDestinataire;
    private EditText btnDebut;
    private TextView txtDebut;
    private TextView txtFin;
    private EditText txtCompanie;
    private EditText txtNom;
    private EditText txtPoste;
    private EditText txtTelephone;
    private EditText txtBatiment;
    private EditText txtEmail;
    private Spinner spinner;
    private Button btnDone;

    private String destinataire;
    private String heureDebut;
    private String heureFin;
    private String companie;
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

        ArrayList<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Batiment Extérieur Faux");
        spinnerArray.add("Batiment Extérieur Vrai");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destinataire = txtDestinataire.getText().toString();
                heureDebut = txtDebut.getText().toString();
                heureFin = txtFin.getText().toString();
                companie = txtCompanie.getText().toString();
                nom = txtNom.getText().toString();
                poste = txtPoste.getText().toString();
                telephnone = txtTelephone.getText().toString();
                email = txtEmail.getText().toString();
                batiment = txtBatiment.getText().toString();
                if(spinner.getSelectedItem().toString().equals("Batiment Extérieur Vrai")){
                    ext = true;
                }
                else {
                    ext = false;
                }
                //Validation
                Verify ve = new Verify();
                if(!heureDebut.equals("")){
                    if(!heureFin.equals("")){
                        if(!nom.equals("")){
                            if(!poste.equals("")){
                                if(ve.isValidPhone(telephnone)){
                                    if(ve.isValidEmail(email)){
                                        if(!batiment.equals("")){
                                            Event e = new Event
                                                    (destinataire, heureDebut, heureFin, nom, poste,
                                                            telephnone, email, batiment, ext);
                                            Toast.makeText(NewB2B.this, ""+e, Toast.LENGTH_SHORT)
                                                    .show();
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
            }
        });

    }
    public String timePicker(){
        String date = "";
        final Dialog d = new Dialog(NewB2B.this);
        d.setContentView(R.layout.popup_time_picker_layout);
        final TimePicker timePicker = (TimePicker)d.findViewById(R.id.timePicker);
        Button ok = (Button)d.findViewById(R.id.btnOkTimePIcker);
        d.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour =  timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                if(minute < 10){
                    temp = hour+"h0"+minute;
                }else{
                    temp = hour+"h"+minute;
                }
                d.dismiss();
            }
        });
        date = temp;
        return date;


    }
    public void initialize(){
        txtDestinataire = (EditText)findViewById(R.id.txtB2BDestinataire);
        txtDebut = (TextView)findViewById(R.id.txtB2bHeureDebut);
        txtFin = (TextView)findViewById(R.id.txtB2bHeureFin);
        txtCompanie = (EditText)findViewById(R.id.editTextB2bCompanie);
        txtNom = (EditText)findViewById(R.id.editTextB2bNom);
        txtPoste = (EditText)findViewById(R.id.editTextB2bPoste);
        txtTelephone = (EditText)findViewById(R.id.editTextB2bPhone);
        txtBatiment = (EditText)findViewById(R.id.editTextB2bBatiment);
        txtEmail = (EditText)findViewById(R.id.editTextB2bEmail);
        spinner = (Spinner)findViewById(R.id.spinnerBatiementExterieur);
        btnDone = (Button)findViewById(R.id.btnB2bOk);
    }

}