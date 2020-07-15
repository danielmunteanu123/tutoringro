package com.example.tutoringro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Admin_EditProfesor extends AppCompatActivity {

    TextView fullNameLabel, usernameLabel;
    TextInputLayout nume, email, telefon, parola,categorii,subcategorii,materii,distanta,domiciliu,pret;
    String _USERNAME, _NUME, _EMAIL, _TELEFON, _PAROLA, _CATEGORII, _SUBCATEGORII, _MATERII, _DISTANTA, _DOMICILIU, _PRET, _JUDET;
    DatabaseReference reference;
    private ManagerProfesori receivedProfesor;
    Button adauga_poza, adauga_certificate,schimbaParola;
    ImageView profileImage;
    String index = "edit";
    Spinner spinner_judet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_edit_profesor);

        spinner_judet = findViewById(R.id.spinner_judet);

        fullNameLabel = findViewById(R.id.nume_field);
        usernameLabel = findViewById(R.id.username_field);
        nume = findViewById(R.id.nume_profile);
        email = findViewById(R.id.email_profile);
        telefon = findViewById(R.id.telefon_profile);
        adauga_poza = findViewById(R.id.adauaImagine_btn);
        profileImage = findViewById(R.id.profile_imageView);
        categorii = findViewById(R.id.categorii_profile);
        subcategorii = findViewById(R.id.subcategorii_profile);
        materii = findViewById(R.id.materii_profile);
        distanta = findViewById(R.id.distanta_profile);
        domiciliu = findViewById(R.id.domiciliu_profile);
        pret = findViewById(R.id.pret_profile);
        schimbaParola = findViewById(R.id.schimbaParola_btn);
        adauga_certificate = findViewById(R.id.adauaCertificate_btn);


        String[] judete = getResources().getStringArray(R.array.judete);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,judete);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_judet.setAdapter(arrayAdapter);
        // fullNameLabel = findViewById(R.id.nume_field);
        // usernameLabel = findViewById(R.id.username_field);

        reference = FirebaseDatabase.getInstance().getReference("Users").child("Profesori");
        showAllUserData();

        StorageReference profileRef = FirebaseStorage.getInstance().getReference().child("pozeProfil").child(_USERNAME+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        schimbaParola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SchimbaParola.class);
                String tip = "profesor";
                intent.putExtra("parola",_PAROLA);
                intent.putExtra("username",_USERNAME);
                intent.putExtra("user_tip",tip);
                startActivity(intent);
            }
        });

        adauga_poza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tip = "profesor";
                Intent intent = new Intent(getApplicationContext(),AdaugaImagine.class);
                intent.putExtra("user_tip",tip);
                startActivity(intent);
            }
        });
        adauga_certificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Upload_PDF.class);
                intent.putExtra("username",receivedProfesor.getUsername());
            }
        });


    }

    private void showAllUserData() {
        final ManagerProfesori profesor = ManagerProfesori.getProfesor();
        receivedProfesor= Utils.receiveProfesor(getIntent(), Admin_EditProfesor.this);
        _USERNAME = receivedProfesor.getUsername();
        _NUME = receivedProfesor.getNume();
        _EMAIL = receivedProfesor.getEmail();
        _TELEFON = receivedProfesor.getTelefon();
        _PAROLA = receivedProfesor.getParola();
        _CATEGORII = receivedProfesor.getCategorii();
        _SUBCATEGORII = receivedProfesor.getSubcategorii();
        _MATERII = receivedProfesor.getMaterii();
        _DISTANTA = String.valueOf(receivedProfesor.getDistanta());
        _DOMICILIU = receivedProfesor.getDomiciliu();
        _PRET = String.valueOf(receivedProfesor.getPret());
        _JUDET = receivedProfesor.getJudet();

       profesor.setUsername(_USERNAME);
        profesor.setNume(_NUME);
        profesor.setParola(_PAROLA);
        profesor.setTelefon(_TELEFON);
        profesor.setEmail(_EMAIL);
        profesor.setCategorii(_CATEGORII);
        profesor.setSubcategorii(_SUBCATEGORII);
        profesor.setMaterii(_MATERII);
        profesor.setDistanta(_DISTANTA);
        profesor.setDomiciliu(_DOMICILIU);
        profesor.setPret(_PRET);
        profesor.setJudet(_JUDET);


        nume.getEditText().setText(_NUME);
        email.getEditText().setText(_EMAIL);
        telefon.getEditText().setText("(+373) " + _TELEFON);
        categorii.getEditText().setText(_CATEGORII);
        subcategorii.getEditText().setText(_SUBCATEGORII);
        materii.getEditText().setText(_MATERII);
        distanta.getEditText().setText(_DISTANTA);
        domiciliu.getEditText().setText(_DOMICILIU);
        pret.getEditText().setText(_PRET);


        setSpinText(spinner_judet,_JUDET);
        fullNameLabel.setText(_NUME);
        usernameLabel.setText("username:"+_USERNAME);


    }
    public void setSpinText(Spinner spinner_judet, String text)
    {
        for(int i= 0; i < spinner_judet.getAdapter().getCount(); i++)
        {
            if(spinner_judet.getAdapter().getItem(i).toString().contains(text))
            {
                spinner_judet.setSelection(i);
            }
        }

    }

    public void editMaterii(View view)
    {
        String tip = "profesor";
        Intent intent = new Intent(getApplicationContext(),Categorii.class);
        intent.putExtra("index",index);
        intent.putExtra("user_tip", tip);
        startActivity(intent);


    }

    public void update(View view) {
        final ManagerProfesori profesor = ManagerProfesori.getProfesor();
        boolean schimbat = false;

        if(!validateNume() || !validateEmail() || !validatePret() || !validateDomiciliu() || !validateDistanta())
        {
            return;
        }
        else if(validateNume() || validateEmail() || validatePret() || validateDomiciliu() || validateDistanta()) {

            if (schimbaNume()) {
                Toast.makeText(this, "Datele au fost modificate cu succes!", Toast.LENGTH_LONG).show();
                profesor.setNume(_NUME);
                schimbat = true;
            }

            if (schimbaDistanta()) {
                Toast.makeText(this, "Datele au fost modificate cu succes!", Toast.LENGTH_LONG).show();
                profesor.setDistanta(_DISTANTA);
                schimbat = true;
            }
            if (schimbaDomiciliu()) {
                Toast.makeText(this, "Datele au fost modificate cu succes!", Toast.LENGTH_LONG).show();
                profesor.setDomiciliu(_DOMICILIU);
                schimbat = true;
            }
            if (schimbaPret()) {
                Toast.makeText(this, "Datele au fost modificate cu succes!", Toast.LENGTH_LONG).show();
                profesor.setPret(_PRET);
                schimbat = true;
            }
            if (schimbaEmail()) {
                Toast.makeText(this, "Datele au fost modificate cu succes!", Toast.LENGTH_LONG).show();
                profesor.setEmail(_EMAIL);
                schimbat = true;
            }
            if (schimbaJudet()) {
                Toast.makeText(this, "Datele au fost modificate cu succes!", Toast.LENGTH_LONG).show();
                profesor.setJudet(_JUDET);
                schimbat = true;
            }
            if (schimbat == false) {
                Toast.makeText(this, "Nu ati facut nicio modificare", Toast.LENGTH_SHORT).show();
            }
        }


    }




    private boolean schimbaJudet() {
        if (!_JUDET.equals(spinner_judet.getSelectedItem().toString())) {
            reference.child(_USERNAME).child("judet").setValue(spinner_judet.getSelectedItem().toString());
            _JUDET = spinner_judet.getSelectedItem().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean schimbaNume() {

        if (!_NUME.equals(nume.getEditText().getText().toString())) {
            reference.child(_USERNAME).child("nume").setValue(nume.getEditText().getText().toString());
            _NUME = nume.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }

    }


    private boolean schimbaDistanta() {

        if (!_DISTANTA.equals(distanta.getEditText().getText().toString())) {
            reference.child(_USERNAME).child("distanta").setValue(distanta.getEditText().getText().toString());
            _DISTANTA = distanta.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }

    }


    private boolean schimbaDomiciliu() {

        if (!_DOMICILIU.equals(domiciliu.getEditText().getText().toString())) {
            reference.child(_USERNAME).child("domiciliu").setValue(domiciliu.getEditText().getText().toString());
            _DOMICILIU = domiciliu.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }

    }
    private boolean schimbaPret() {

        if (!_PRET.equals(pret.getEditText().getText().toString())) {
            reference.child(_USERNAME).child("pret").setValue(pret.getEditText().getText().toString());
            _PRET = pret.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }

    }
    private boolean schimbaEmail() {

        if (!_EMAIL.equals(email.getEditText().getText().toString())) {
            reference.child(_USERNAME).child("email").setValue(email.getEditText().getText().toString());
            _EMAIL = email.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private Boolean validateDomiciliu () {
        String val = domiciliu.getEditText().getText().toString();

        if (val.isEmpty()) {
            domiciliu.setError("Completati spatiile libere");
            return false;
        } else {
            domiciliu.setError(null);
            domiciliu.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateNume () {
        String val = nume.getEditText().getText().toString();
        String numePattern = "^\\p{L}+[\\p{L}\\p{Z}\\p{P}]{0,}";

        if (val.isEmpty()) {
            nume.setError("Completati spatiile libere");
            return false;
        } else if (val.length() > 40) {
            nume.setError("Nume prea lung.");
            return false;
        } else if (val.length() < 7) {
            nume.setError("Nume prea mic.");
            return false;
        }
        else if (!val.matches(numePattern))
        {
            nume.setError("Format incorect.");
            return false;
        }else {
            nume.setError(null);
            nume.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail () {
        String val = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateDistanta () {
        String val = distanta.getEditText().getText().toString();

        if (val.isEmpty()) {
            distanta.setError("Completati spatiile libere");
            return false;
        } else if (Integer.parseInt(val) < 1) {
            distanta.setError("Distanta de deplasare trebuie sa fie mai mare ca 1");
            return false;
        } else if (Integer.parseInt(val) > 100) {
            distanta.setError("Distanta de deplasare trebuie sa fie mai mica ca 100");
            return false;
        } else {
            distanta.setError(null);
            distanta.setErrorEnabled(false);
            return true;
        }
    }


    private Boolean validatePret () {
        String val = pret.getEditText().getText().toString();

        if (val.isEmpty()) {
            pret.setError("Field cannot be empty");
            return false;
        } else if (Integer.parseInt(val) > 400) {
            pret.setError("Pretul trebuie sa fie mai mic ca 400");
            return false;
        } else if (Integer.parseInt(val) < 10) {
            pret.setError("Pretul trebuie sa fie mai mare ca 10");
            return false;
        } else {
            pret.setError(null);
            pret.setErrorEnabled(false);
            return true;
        }


    }





}