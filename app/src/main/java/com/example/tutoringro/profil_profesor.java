package com.example.tutoringro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class profil_profesor extends AppCompatActivity {
    TextView fullNameLabel, usernameLabel;
    TextInputLayout nume, email, telefon, parola,categorii,subcategorii,materii,distanta,domiciliu,pret,judet;
    String _USERNAME, _NUME, _EMAIL, _TELEFON, _CATEGORII, _SUBCATEGORII, _MATERII, _DISTANTA, _DOMICILIU, _PRET, _JUDET;
    AppCompatImageButton back_btn;
    ImageView profileImage;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profil_profesor);

        fullNameLabel = findViewById(R.id.nume_field);
        usernameLabel = findViewById(R.id.username_field);
        nume = findViewById(R.id.nume_profile);
        email = findViewById(R.id.email_profile);
        telefon = findViewById(R.id.telefon_profile);
        back_btn = findViewById(R.id.back_btn);
        profileImage = findViewById(R.id.profile_imageView);
        categorii = findViewById(R.id.categorii_profile);
        subcategorii = findViewById(R.id.subcategorii_profile);
        materii = findViewById(R.id.materii_profile);
        distanta = findViewById(R.id.distanta_profile);
        domiciliu = findViewById(R.id.domiciliu_profile);
        pret = findViewById(R.id.pret_profile);
        judet = findViewById(R.id.judet_profile);

        showAllUserData();

        StorageReference profileRef = FirebaseStorage.getInstance().getReference().child("pozeProfil").child(_USERNAME+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String activity = intent.getStringExtra("activity");
                if(activity.equals("cerere")){
                    startActivity(new Intent(getApplicationContext(),CautareProfesori.class));
                }
                else if(activity.equals("sedinta"));
                {
                    startActivity(new Intent(getApplicationContext(), Sedinte_Student.class));
                }
            }
        });


    }
    private void showAllUserData() {
        Intent intent = getIntent();

        _USERNAME = intent.getStringExtra("username");
        _NUME = intent.getStringExtra("nume");
        _EMAIL = intent.getStringExtra("email");
        _TELEFON = intent.getStringExtra("telefon");
        _CATEGORII = intent.getStringExtra("categorii");
        _SUBCATEGORII = intent.getStringExtra("subcategorii");
        _MATERII = intent.getStringExtra("materii");
        _PRET =intent.getStringExtra("pret");
        _DOMICILIU = intent.getStringExtra("domiciliu");
        _JUDET = intent.getStringExtra("judet");
        _DISTANTA = intent.getStringExtra("distanta");

        nume.getEditText().setText(_NUME);
        email.getEditText().setText(_EMAIL);
        telefon.getEditText().setText("(+373) " + _TELEFON);
        categorii.getEditText().setText(_CATEGORII);
        subcategorii.getEditText().setText(_SUBCATEGORII);
        materii.getEditText().setText(_MATERII);
        distanta.getEditText().setText(_DISTANTA);
        domiciliu.getEditText().setText(_DOMICILIU);
        pret.getEditText().setText(_PRET);
        judet.getEditText().setText(_JUDET);
        fullNameLabel.setText(_NUME);
        usernameLabel.setText("username:"+_USERNAME);


    }
}
