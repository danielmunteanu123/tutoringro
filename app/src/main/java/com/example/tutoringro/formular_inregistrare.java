package com.example.tutoringro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.common.primitives.Bytes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sun.mail.iap.ByteArray;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class formular_inregistrare extends AppCompatActivity {
    static TextInputLayout regNume, regUsername, regEmail, regTelefon, regParola;
    Button regBtn;
    FirebaseDatabase rootNode;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Ascunde status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        Intent intent = getIntent();
        String tip = intent.getStringExtra("tip");
        //Firebase Initializare
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Users");

        //Hooks pentru toate elementle
        regNume = findViewById(R.id.reg_nume);
        regUsername = findViewById(R.id.reg_username);
        regEmail = findViewById(R.id.reg_email);
        regTelefon = findViewById(R.id.reg_telefon);
        regParola = findViewById(R.id.reg_parola);
    }

    private Boolean validateName() {
        String val = regNume.getEditText().getText().toString();
        Pattern pattern = Pattern.compile("^[a-zA-Z_ ]*$");
        Matcher matcher = pattern.matcher(val);

        if (!matcher.matches()) {
            regNume.setError("Format incorect. (Exemplu: Mihai Munteanu)");
            return false;
        }
        if (val.isEmpty()) {
            regNume.setError("Completati spatiile libere");
            return false;
        } else if (val.length() > 40) {
            regNume.setError("Nume prea lung.");
            return false;
        } else if (val.length() < 7) {
            regNume.setError("Nume prea mic.");
            return false;
        } else {
            regNume.setError(null);
            regNume.setErrorEnabled(false);
            return true;
        }
    }

    private static Boolean validateUsername() {
        final ManagerProfesori profesor = ManagerProfesori.getProfesor();
        String val = regUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            regUsername.setError("Completati spatiile libere!");
            return false;
        } else if (val.length() >= 20) {
            regUsername.setError("Username prea lung");
            return false;
        } else if (val.length() <= 4) {
            regUsername.setError("Username prea mic");
            return false;

        } else if (!val.matches(noWhiteSpace)) {
            regUsername.setError("Format incorect");
            return false;
        } else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }


    private Boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            regEmail.setError("Completati spatiile libere!");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("Adresa de email incorecta.");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = regTelefon.getEditText().getText().toString();

        if (val.isEmpty()) {
            regTelefon.setError("Completati spatiile libere!");
            return false;

        } else if (val.length() < 8) {
            regTelefon.setError("Format incorect,numar prea mic! (Ex:12345678)");
            return false;
        } else if (val.length() > 8) {
            regTelefon.setError("Format incorect,numar prea mare!(Ex:12345678)");
            return false;
        } else {
            regTelefon.setError(null);
            regTelefon.setErrorEnabled(false);
            return true;
        }
    }


    private Boolean validatePassword() {
        String val = regParola.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                // "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
        Matcher matcher = pattern.matcher(val);

        if (!matcher.matches()) {
            regParola.setError("Parola nu poate conține caractere speciale sau spații libere.");
            return false;
        }
        if (val.isEmpty()) {
            regParola.setError("Competati spatiile libere!");
            return false;
        } else if (!val.matches(passwordVal)) {
            regParola.setError("Parola e prea slaba.Parola trebuie sa aiba cel putin 4 caractere, sa contina cel putin 1 cifra, 1 minuscula si 1 majuscula");
            return false;
        } else {
            regParola.setError(null);
            regParola.setErrorEnabled(false);
            return true;
        }
    }

    public void checkTelefonProfesori(String telefon, String username, String email) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Profesori");
        final Query checkUser = reference.orderByChild("telefon").equalTo(telefon);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    regTelefon.setError("Numar de telefon ocupat");
                    regTelefon.requestFocus();
                } else {
                    checkTelefonStudenti(telefon, username, email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkTelefonStudenti(String telefon, String username, String email) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Profesori");
        final Query checkUser = reference.orderByChild("telefon").equalTo(telefon);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    regTelefon.setError("Numar de telefon ocupat");
                    regTelefon.requestFocus();
                } else {
                    regTelefon.setError(null);
                    regTelefon.setErrorEnabled(false);
                    checkEmailProfesori(username, email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkEmailProfesori(String username, String email) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Profesori");
        final Query checkUser = reference.orderByChild("email").equalTo(email);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    regEmail.setError("Email ocupat");
                    regEmail.requestFocus();
                } else {
                    checkEmailStudenti(username, email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void checkEmailStudenti(String username, String email) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Studenti");
        final Query checkUser = reference.orderByChild("email").equalTo(email);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    regEmail.setError("Email ocupat");
                    regEmail.requestFocus();
                } else {
                    regEmail.setError(null);
                    regEmail.setErrorEnabled(false);
                    checkUsernameProfesori(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void checkUsernameProfesori(String username) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Profesori");
        final Query checkUser = reference.orderByChild("username").equalTo(username);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    regUsername.setError("Username ocupat");
                    regUsername.requestFocus();
                } else {
                    checkUsernameStudenti(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkUsernameStudenti(String username) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Studenti");
        final Query checkUser = reference.orderByChild("username").equalTo(username);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    regUsername.setError("Username ocupat");
                    regUsername.requestFocus();
                } else {
                    regUsername.setError(null);
                    regUsername.setErrorEnabled(false);


                    String nume = regNume.getEditText().getText().toString();
                    String username = regUsername.getEditText().getText().toString();
                    String email = regEmail.getEditText().getText().toString();
                    String telefon = regTelefon.getEditText().getText().toString();
                    String parola = regParola.getEditText().getText().toString();
                    Intent intent = getIntent();
                    String tip = intent.getStringExtra("tip");

                    if (tip.equals("profesor")) {


                        final ManagerProfesori profesor = ManagerProfesori.getProfesor();
                        profesor.setNume(nume);
                        profesor.setUsername(username);
                        profesor.setEmail(email);
                        profesor.setTelefon(telefon);
                        profesor.setParola(Utils.cipher(parola));

                        startActivity(new Intent(getApplicationContext(), formular_profesor.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                    } else {
                        final ManagerStudenti student = ManagerStudenti.getStudent();
                        student.setNume(nume);
                        student.setUsername(username);
                        student.setEmail(email);
                        student.setTelefon(telefon);
                        student.setParola(Utils.cipher(parola));


                        startActivity(new Intent(getApplicationContext(), formular_student.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    //functia va fi executata cand se va da click pe butonul de inregistrare"OK"
    public void registerUser(View view) {

        //Validari
        if(!validateName() | !validatePassword() | !validatePhoneNo() | !validateEmail() | !validateUsername()){
            return;
        }

        else {
            String nume = regNume.getEditText().getText().toString();
            String username = regUsername.getEditText().getText().toString();
            String email = regEmail.getEditText().getText().toString();
            String telefon = regTelefon.getEditText().getText().toString();
            String parola = regParola.getEditText().getText().toString();
            checkTelefonProfesori(telefon,username,email);

        }
        }


}


