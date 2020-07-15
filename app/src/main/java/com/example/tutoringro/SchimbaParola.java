package com.example.tutoringro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.internal.Util;

public class SchimbaParola extends AppCompatActivity {
    TextInputLayout old_password, password, confirm;
    Button ok_btn;
    DatabaseReference reference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_schimba_parola);

        old_password = findViewById(R.id.old_password);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        ok_btn = findViewById(R.id.ok_button);

        Intent intent = getIntent();
        String parolaFromDB = intent.getStringExtra("parola");
        String username = intent.getStringExtra("username");
        String tip = intent.getStringExtra("user_tip");






        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!old_password(Utils.cipher(old_password.getEditText().getText().toString()), parolaFromDB))
                {
                    old_password.setError("Parola greșită");
                    old_password.requestFocus();
                }
                else if (!validatePassword())
                {

                    password.requestFocus();
                }

                else if(!validateConfirm(password.getEditText().getText().toString(), confirm.getEditText().getText().toString()))
                {
                    confirm.setError("Parolele nu sunt la fel.");
                    confirm.requestFocus();
                }
                else if(!same_password(old_password.getEditText().getText().toString(),password.getEditText().getText().toString()))
                {
                    password.setError("Parola nu poate fi aceiasi ca parola veche!");
                    password.requestFocus();
                }

                else {
                    old_password.setErrorEnabled(false);
                    password.setErrorEnabled(false);
                    confirm.setErrorEnabled(false);
                    if (tip.equals("student")) {
                        reference = FirebaseDatabase.getInstance().getReference("Users").child("Studenti");
                        reference.child(username).child("parola").setValue(Utils.cipher(password.getEditText().getText().toString()));
                        Toast.makeText(SchimbaParola.this, "Parola a fost schimbată cu succes!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), EditStudent.class));

                    }
                    else
                    {
                        reference = FirebaseDatabase.getInstance().getReference("Users").child("Profesori");
                        reference.child(username).child("parola").setValue(Utils.cipher(password.getEditText().getText().toString()));
                        Toast.makeText(SchimbaParola.this, "Parola a fost schimbată cu succes!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), EditProfesor.class));
                    }


                }



            }
        });


    }

    private boolean old_password(String old_password, String real_password) {

        if (real_password.equals(old_password))
        {
            return true;
        }
        else
        {
            return false;
        }


    }

    private boolean same_password(String old_password, String password) {

        if (password.equals(old_password))
        {
            return false;
        }
        else
        {
            return true;
        }


    }

    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
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
            password.setError("Parola nu poate conține caractere speciale.");
            return false;
        }
        else if (val.isEmpty()) {
            password.setError("Compleatati spatiile libere.");
            return false;
        } else if (!val.matches(passwordVal)) {
            password.setError("Parola e prea slaba.Parola trebuie sa aiba cel putin 4 caractere, sa contina cel putin 1 cifra, 1 minuscula si 1 majuscula");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateConfirm(String password, String confirm)
    {
        if (password.equals(confirm))
        {
            return true;
        }
        else
        {
            return false;
        }

    }
}
