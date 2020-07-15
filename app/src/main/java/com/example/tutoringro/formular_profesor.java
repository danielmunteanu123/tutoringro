package com.example.tutoringro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class formular_profesor extends AppCompatActivity {

    //Variables
    TextInputLayout regDomiciliu, regDistanta, regPret;
    Spinner spinner_judet;
    FirebaseDatabase rootNode;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Ascunde status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_formular_profesor);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Profesori");

        spinner_judet = findViewById(R.id.spinner_judet);
        regDomiciliu = findViewById(R.id.reg_domiciliu);
        regDistanta = findViewById(R.id.reg_distanta);
        regPret = findViewById(R.id.reg_pret);

        String[] judete = getResources().getStringArray(R.array.judete);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,judete);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_judet.setAdapter(arrayAdapter);
        spinner_judet.setSelection(1);
        spinner_judet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_judet.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private Boolean validateDomiciliu() {
        String val = regDomiciliu.getEditText().getText().toString();
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_ ]*$");
        Matcher matcher = pattern.matcher(val);

        if (!matcher.matches()) {
            regDomiciliu.setError("Adresa nu poate conține caractere speciale.");
            return false;
        }

        if (val.isEmpty()) {
            regDomiciliu.setError("Completati spatiile libere");
            return false;
        }
            else if(val.length() < 7){
            regDomiciliu.setError("Adresă prea mică.");
                return false;
            } else {
            regDomiciliu.setError(null);
            regDomiciliu.setErrorEnabled(false);
            return true;
        }
    }


    private Boolean validateDistanta() {
        String val = regDistanta.getEditText().getText().toString();

        if (val.isEmpty()) {
            regDistanta.setError("Completati spatiile libere");
            return false;
        }
        else if (Integer.parseInt(val) < 1)
        {
            regDistanta.setError("Distanta de deplasare trebuie sa fie mai mare ca 1");
            return false;
        }
        else if (Integer.parseInt(val) > 100)
        {
            regDistanta.setError("Distanta de deplasare trebuie sa fie mai mica ca 100");
            return false;
        }
            else {
            regDistanta.setError(null);
            regDistanta.setErrorEnabled(false);
            return true;
        }
    }


    private Boolean validatePret() {
        String val = regPret.getEditText().getText().toString();

        if (val.isEmpty()) {
            regPret.setError("Completati spatiile libere!");
            return false;
        }
        else if (Integer.parseInt(val) > 400)
        {
            regPret.setError("Pretul trebuie sa fie mai mic ca 400");
            return false;
        }
        else if (Integer.parseInt(val) < 10)
        {
            regPret.setError("Pretul trebuie sa fie mai mare ca 10");
            return false;
        }else {
            regPret.setError(null);
            regPret.setErrorEnabled(false);
            return true;
        }
    }

    //functia va fi executata cand se va da click pe butonul de inregistrare"OK"
    public void registerUser(View view) {

        //Validari
        if (!validateDomiciliu() | !validateDistanta() | !validatePret()) {
            return;
        } else {
            String judet = spinner_judet.getSelectedItem().toString();
            String domiciliu = regDomiciliu.getEditText().getText().toString();
            String distanta = regDistanta.getEditText().getText().toString();
            String pret = regPret.getEditText().getText().toString();


            final ManagerProfesori profesor = ManagerProfesori.getProfesor();
            profesor.setDomiciliu(domiciliu);
            profesor.setDistanta(distanta);
            profesor.setPret(pret);
            profesor.setJudet(judet);

            String i = "signup";
            String index = "profesor";
            Intent intent = new Intent(getApplicationContext(), Categorii.class);
            intent.putExtra("user_tip", index);
            intent.putExtra("index", i);
            startActivity(intent);


        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}