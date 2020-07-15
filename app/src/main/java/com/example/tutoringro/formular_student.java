package com.example.tutoringro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Float.parseFloat;

public class formular_student extends AppCompatActivity {
    TextInputLayout regDomiciliu, regVarsta;
    Button regBtn;
    Spinner spinner_judet;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Ascunde status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_formular_student);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Studenti");

        spinner_judet = findViewById(R.id.spinner_judet);

        regBtn = findViewById(R.id.reg_btn);
        regDomiciliu = findViewById(R.id.reg_domiciliu);
        regVarsta = findViewById(R.id.reg_varsta);

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


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(v);
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
            }else {
                regDomiciliu.setError(null);
                regDomiciliu.setErrorEnabled(false);
                return true;
            }
        }
        private Boolean validateVarsta() {
            String val = regVarsta.getEditText().getText().toString();

            if (val.isEmpty()) {
                regVarsta.setError("Completati spatiile libere");
                return false;
            }
            else if (Integer.parseInt(val) > 100)
            {
                regVarsta.setError("Varsta trebuie sa fie mai mica ca 100");
                return false;
            }
            else if (Integer.parseInt(val) < 10)
            {
                regVarsta.setError("Trebuie sa aveti macar 10 ani pentru a folosi aplicatia");
                return false;
            }else {
                regVarsta.setError(null);
                regVarsta.setErrorEnabled(false);
                return true;
            }
        }

    public void registerUser(View view) {


        if(!validateVarsta() | !validateDomiciliu()){
            return;
        }
        else {


            String judet = spinner_judet.getSelectedItem().toString();
            String domiciliu = regDomiciliu.getEditText().getText().toString();
            String varsta = regVarsta.getEditText().getText().toString();

            final ManagerStudenti student = ManagerStudenti.getStudent();
            student.setDomiciliu(domiciliu);
            student.setVarsta(varsta);
            student.setJudet(judet);

            String index = "student";
            String signUP = "signup";
            Intent intent = new Intent(getApplicationContext(), Categorii.class);

            intent.putExtra("user_tip", index);
            intent.putExtra("index", signUP);
            startActivity(intent);
        }


    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    }

