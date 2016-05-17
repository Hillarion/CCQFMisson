package com.devel.ccqf.ccqfmisson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NewB2B extends AppCompatActivity {
    private EditText txtDestinataire;
    private Button btnDebut;
    private Button btnFin;
    private EditText txtCompanie;
    private EditText txtNom;
    private EditText txtPoste;
    private EditText txtTelephone;
    private EditText txtBatiment;
    private EditText txtEmail;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_b2_b);

        txtDestinataire = (EditText)findViewById(R.id.txtB2BDestinataire);
        btnDebut = (Button)findViewById(R.id.btnB2bHeureDebut);
        btnFin = (Button)findViewById(R.id.btnB2bHeureFin);
        txtCompanie = (EditText)findViewById(R.id.editTextB2bCompanie);
        txtNom = (EditText)findViewById(R.id.editTextB2bNom);
        txtPoste = (EditText)findViewById(R.id.editTextB2bPoste);
        txtTelephone = (EditText)findViewById(R.id.editTextB2bPhone);
        txtBatiment = (EditText)findViewById(R.id.editTextB2bBatiment);
        txtEmail = (EditText)findViewById(R.id.editTextB2bEmail);
        spinner = (Spinner)findViewById(R.id.spinnerBatiementExterieur);

        ArrayList<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Batiment Extérieur Vrai");
        spinnerArray.add("Batiment Extérieur Faux");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }
}
