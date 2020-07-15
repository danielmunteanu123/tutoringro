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
import com.sun.mail.iap.ByteArray;

import java.util.List;

public class Admin_EditStudent extends AppCompatActivity {
    TextView fullNameLabel, usernameLabel;
    TextInputLayout nume, email, telefon, parola,categorii,subcategorii,materii,domiciliu,varsta;
    Spinner spinner_judet;
    String _USERNAME;
    String _NUME;
    String _EMAIL;
    String _TELEFON;
    String _PAROLA;
    String _CATEGORII;
    String _SUBCATEGORII;
    String _MATERII;
    String _VARSTA;
    String _DOMICILIU;
    String _JUDET;
    DatabaseReference reference;
    private ManagerStudenti receivedStudent;
    String index = "edit";
    ImageView profileImage;
    Button adauga_poza,schimbaParola,update_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_student);
        spinner_judet = findViewById(R.id.spinner_judet);

        fullNameLabel = findViewById(R.id.nume_field);
        usernameLabel = findViewById(R.id.username_field);
        nume = findViewById(R.id.nume_profile);
        email = findViewById(R.id.email_profile);
        telefon = findViewById(R.id.telefon_profile);
        adauga_poza = findViewById(R.id.adauaImagine_btn);
        categorii = findViewById(R.id.categorii_profile);
        profileImage = findViewById(R.id.profile_imageView);
        subcategorii = findViewById(R.id.subcategorii_profile);
        materii = findViewById(R.id.materii_profile);
        varsta = findViewById(R.id.varsta_profile);
        domiciliu = findViewById(R.id.domiciliu_profile);
        update_btn = findViewById(R.id.update_btn);
        schimbaParola = findViewById(R.id.schimbaParola_btn);
        reference = FirebaseDatabase.getInstance().getReference("Users").child("Studenti");


        String[] judete = getResources().getStringArray(R.array.judete);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,judete);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_judet.setAdapter(arrayAdapter);

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
                String tip = "student";
                Intent intent = new Intent(getApplicationContext(),SchimbaParola.class);
               // intent.putExtra("parola",_PAROLA);
                intent.putExtra("username",_USERNAME);
                intent.putExtra("user_tip",tip);
                startActivity(intent);
            }
        });

        adauga_poza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tip = "student";
                Intent intent = new Intent(getApplicationContext(),AdaugaImagine.class);
                intent.putExtra("user_tip",tip);
                startActivity(intent);
            }
        });




    }

    private void showAllUserData() {
        final ManagerStudenti student = ManagerStudenti.getStudent();
        receivedStudent= Utils.receiveStudent(getIntent(), Admin_EditStudent.this);
        _USERNAME = receivedStudent.getUsername();
        _NUME = receivedStudent.getNume();
        _EMAIL = receivedStudent.getEmail();
        _TELEFON = receivedStudent.getTelefon();
        _PAROLA = receivedStudent.getParola();
        _CATEGORII = receivedStudent.getCategorii();
        _SUBCATEGORII = receivedStudent.getSubcategorii();
        _MATERII = receivedStudent.getMaterii();
        _VARSTA = String.valueOf(receivedStudent.getVarsta());
        _DOMICILIU = receivedStudent.getDomiciliu();
        _JUDET = receivedStudent.getJudet();

        student.setUsername( _USERNAME );
        student.setNume(_NUME);
        student.setEmail(_EMAIL);
        student.setTelefon(_TELEFON);
        student.setParola(_PAROLA);
        student.setCategorii(_CATEGORII);
        student.setSubcategorii(_SUBCATEGORII);
        student.setMaterii(_MATERII);
        student.setDomiciliu(_DOMICILIU);
        student.setVarsta(_VARSTA);
        student.setJudet(_JUDET);
        student.setParola(receivedStudent.getParola());



        nume.getEditText().setText(_NUME);
        email.getEditText().setText(_EMAIL);
        telefon.getEditText().setText("(+373) " + _TELEFON);

        categorii.getEditText().setText(_CATEGORII);
        subcategorii.getEditText().setText(_SUBCATEGORII);
        materii.getEditText().setText(_MATERII);
        domiciliu.getEditText().setText(_DOMICILIU);
        varsta.getEditText().setText(_VARSTA);
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
        String tip = "student";
        Intent intent = new Intent(getApplicationContext(),Categorii.class);
        intent.putExtra("index",index);
        intent.putExtra("user_tip", tip);
        startActivity(intent);

    }


    public void update(View view) {
        boolean schimbat = false;

        final ManagerStudenti student = ManagerStudenti.getStudent();

        if(!validateNume() || !validateEmail() || !validateVarsta() || !validateDomiciliu())
        {
            return;
        }
        else if(validateNume() || validateEmail() || validateVarsta() || validateDomiciliu()){

            if (schimbaNume()) {
                Toast.makeText(this, "Datele au fost modificate cu succes!", Toast.LENGTH_LONG).show();
                student.setNume(_NUME);
                schimbat = true;

            }


            if (schimbaDomiciliu()) {
                Toast.makeText(this, "Datele au fost modificate cu succes!", Toast.LENGTH_LONG).show();
                student.setDomiciliu(_DOMICILIU);
                schimbat = true;
            }

            if (schimbaEmail()) {
                Toast.makeText(this, "Datele au fost modificate cu succes!", Toast.LENGTH_LONG).show();
                student.setEmail(_EMAIL);
                schimbat = true;
            }
            if (schimbaJudet()) {
                Toast.makeText(this, "Datele au fost modificate cu succes!", Toast.LENGTH_LONG).show();
                student.setJudet(_JUDET);
                schimbat = true;
            }
            if (schimbaVarsta()) {
                Toast.makeText(this, "Datele au fost modificate cu succes!", Toast.LENGTH_LONG).show();
                student.setVarsta(_VARSTA);
                schimbat = true;
            }
            if(schimbat == false){
                Toast.makeText(this, "Nu ati facut nicio modificare", Toast.LENGTH_SHORT).show();
            }
        }

    }



    private boolean schimbaNume() {

        if (!_NUME.equals(nume.getEditText().getText().toString())) {
            reference.child(_USERNAME).child("nume").setValue(nume.getEditText().getText().toString());
            _NUME = nume.getEditText().getText().toString();
            return true;
        }
        else
        {
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
    private boolean schimbaVarsta() {

        if (!_VARSTA.equals(varsta.getEditText().getText().toString())) {
            reference.child(_USERNAME).child("varsta").setValue(varsta.getEditText().getText().toString());
            _VARSTA = varsta.getEditText().getText().toString();
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
    private boolean schimbaJudet() {
        if (!_JUDET.equals(spinner_judet.getSelectedItem().toString())) {
            reference.child(_USERNAME).child("judet").setValue(spinner_judet.getSelectedItem().toString());
            _JUDET = spinner_judet.getSelectedItem().toString();
            return true;
        } else {
            return false;
        }
    }

    private Boolean validateDomiciliu() {
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
    private Boolean validateVarsta() {
        String val = varsta.getEditText().getText().toString();

        if (val.isEmpty()) {
            varsta.setError("Completati spatiile libere");
            return false;
        }
        else if (Integer.parseInt(val) > 100)
        {
            varsta.setError("Varsta trebuie sa fie mai mica ca 100");
            return false;
        }
        else if (Integer.parseInt(val) < 10)
        {
            varsta.setError("Trebuie sa aveti cel putin 10 ani pentru a folosi aplicatia");
            return false;
        }else {
            varsta.setError(null);
            varsta.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateNume() {
        String val = nume.getEditText().getText().toString();

        if (val.isEmpty()) {
            nume.setError("Completati spatiile libere");
            return false;
        }
        else if(val.length() > 40){
            nume.setError("Nume prea lung.");
            return false;
        }
        else if(val.length() < 7){
            nume.setError("Nume prea mic.");
            return false;
        }
        else {
            nume.setError(null);
            nume.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
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

}
