package com.example.tutoringro;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sun.mail.iap.ByteArray;

import androidx.annotation.NonNull;

import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import okhttp3.internal.Util;

public class Login extends AppCompatActivity{

    Button callSignUp, login_btn;
    ImageView image;
    TextView logoText, sloganText;
    TextInputLayout username, parola;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ascunde status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);


        // Hooks pentru toate elementele
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        callSignUp = findViewById(R.id.signup_screen);
        image = findViewById(R.id.logo_image);
        logoText = findViewById(R.id.logo_name);
        sloganText = findViewById(R.id.slogan_name);
        username = findViewById(R.id.username_login);
        parola = findViewById(R.id.parola_login);
        login_btn = findViewById(R.id.login_btn);


    }

    private Boolean validateUsername() {
        String val = username.getEditText().getText().toString();
        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = parola.getEditText().getText().toString();
        if (val.isEmpty()) {
            parola.setError("Field cannot be empty");
            return false;
        } else {
            parola.setError(null);
            parola.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser(View view) {
        //Validate Login Info
        if (!validateUsername() | !validatePassword()) {
            return;
        } else {
            isUser();
        }
    }

    private void isUser() {
        progressBar.setVisibility(View.VISIBLE);
        //Get users
        final String userEnteredUsername = username.getEditText().getText().toString().trim();
        final String userEnteredPassword = parola.getEditText().getText().toString().trim();
        //Set Firebase Root reference
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Profesori");
        final Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    username.setError(null);
                    username.setErrorEnabled(false);
                    String parolaFromDB = dataSnapshot.child(userEnteredUsername).child("parola").getValue(String.class);
                    if (parolaFromDB.equals(Utils.cipher(userEnteredPassword))) {
                        username.setError(null);
                        username.setErrorEnabled(false);


                        String numeFromDB = dataSnapshot.child(userEnteredUsername).child("nume").getValue(String.class);
                        String usernameFromDB = dataSnapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        String telefonFromDB = dataSnapshot.child(userEnteredUsername).child("telefon").getValue(String.class);
                        String emailFromDB = dataSnapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        String categoriiFromDB = dataSnapshot.child(userEnteredUsername).child("categorii").getValue(String.class);
                        String subcategoriiFromDB = dataSnapshot.child(userEnteredUsername).child("subcategorii").getValue(String.class);
                        String materiiFromDB = dataSnapshot.child(userEnteredUsername).child("materii").getValue(String.class);
                        String distantaFromDB = dataSnapshot.child(userEnteredUsername).child("distanta").getValue(String.class);
                        String domiciliuFromDB = dataSnapshot.child(userEnteredUsername).child("domiciliu").getValue(String.class);
                        String pretFromDB = dataSnapshot.child(userEnteredUsername).child("pret").getValue(String.class);
                        String judetFromDB = dataSnapshot.child(userEnteredUsername).child("judet").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(), Fereastra_Profesor.class);



                        final ManagerProfesori profesor = ManagerProfesori.getProfesor();
                        profesor.setNume(numeFromDB);
                        profesor.setParola(parolaFromDB);
                        profesor.setUsername(usernameFromDB);
                        profesor.setTelefon(telefonFromDB);
                        profesor.setEmail(emailFromDB);
                        profesor.setCategorii(categoriiFromDB);
                        profesor.setSubcategorii(subcategoriiFromDB);
                        profesor.setMaterii(materiiFromDB);
                        profesor.setDistanta(distantaFromDB);
                        profesor.setDomiciliu(domiciliuFromDB);
                        profesor.setPret(pretFromDB);
                        profesor.setJudet(judetFromDB);

                        startActivity(intent);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        parola.setError("Parola gresita");
                        parola.requestFocus();
                    }
                } else {
                   checkUser();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkUser(){
        final String userEnteredUsername = username.getEditText().getText().toString().trim();
        final String userEnteredPassword = parola.getEditText().getText().toString().trim();
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child("Studenti");
        Query checkUser2 = reference2.orderByChild("username").equalTo(userEnteredUsername);
        checkUser2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    username.setError(null);
                    username.setErrorEnabled(false);
                    String parolaFromDB = dataSnapshot.child(userEnteredUsername).child("parola").getValue(String.class);



                        if (parolaFromDB.equals(Utils.cipher(userEnteredPassword))) {
                            username.setError(null);
                            username.setErrorEnabled(false);


                            String numeFromDB = dataSnapshot.child(userEnteredUsername).child("nume").getValue(String.class);
                            String usernameFromDB = dataSnapshot.child(userEnteredUsername).child("username").getValue(String.class);
                            String telefonFromDB = dataSnapshot.child(userEnteredUsername).child("telefon").getValue(String.class);
                            String emailFromDB = dataSnapshot.child(userEnteredUsername).child("email").getValue(String.class);
                            String categoriiFromDB = dataSnapshot.child(userEnteredUsername).child("categorii").getValue(String.class);
                            String subcategoriiFromDB = dataSnapshot.child(userEnteredUsername).child("subcategorii").getValue(String.class);
                            String domiciliuFromDB = dataSnapshot.child(userEnteredUsername).child("domiciliu").getValue(String.class);
                            String materiiFromDB = dataSnapshot.child(userEnteredUsername).child("materii").getValue(String.class);
                            String varstaFromDB = dataSnapshot.child(userEnteredUsername).child("varsta").getValue(String.class);
                            String judetFromDB = dataSnapshot.child(userEnteredUsername).child("judet").getValue(String.class);

                            Intent intent = new Intent(getApplicationContext(), Fereastra_Student.class);

                            final ManagerStudenti student = ManagerStudenti.getStudent();
                            student.setUsername(usernameFromDB);
                            student.setNume(numeFromDB);
                            student.setEmail(emailFromDB);
                            student.setTelefon(telefonFromDB);
                            student.setParola(parolaFromDB);
                            student.setCategorii(categoriiFromDB);
                            student.setSubcategorii(subcategoriiFromDB);
                            student.setMaterii(materiiFromDB);
                            student.setDomiciliu(domiciliuFromDB);
                            student.setVarsta(varstaFromDB);
                            student.setJudet(judetFromDB);

                            startActivity(intent);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            parola.setError("Parola gresita");
                            parola.requestFocus();
                        }



                } else {
                    checkAdmin();


                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkAdmin() {
        final String userEnteredUsername = username.getEditText().getText().toString().trim();
        final String userEnteredPassword = parola.getEditText().getText().toString().trim();
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Users").child("Administrator");
        Query checkUser2 = reference2;
        checkUser2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    username.setError(null);
                    username.setErrorEnabled(false);
                    String parolaFromDB = dataSnapshot.child("parola").getValue(String.class);
                    if (parolaFromDB.equals(userEnteredPassword)) {
                        username.setError(null);
                        username.setErrorEnabled(false);


                        Intent intent = new Intent(getApplicationContext(), Administrator.class);
                        startActivity(intent);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        username.setError("Utilizator cu acest username nu exista");
                        username.requestFocus();

                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //Call SignUp Screen
    public void callSignUpScreen(View view) {

        Intent intent = new Intent(Login.this, AlegeCont.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);




    }

}
