package com.example.tutoringro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Realizare_Recenzie extends AppCompatActivity {

    EditText descriere_editText, recomandare_editText;
    float nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_realizare__recenzie);

        final RatingBar ratingBar = findViewById(R.id.rating_bar);
        Button btnOk = findViewById(R.id.trimite_btn);
        descriere_editText = findViewById(R.id.descriere_editText);
        recomandare_editText = findViewById(R.id.recomandare_editText);


        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ManagerRecenzii recenzie = new ManagerRecenzii();
                String descriere = descriere_editText.getText().toString();
                String recomandare = recomandare_editText.getText().toString();
                final ManagerStudenti student = ManagerStudenti.getStudent();
                nota = ratingBar.getRating();

                recenzie.setDescriere(descriere);
                recenzie.setRecomandare(recomandare);
                recenzie.setNota(nota);
                recenzie.setNume_student(student.getNume());
                trimiteRecenzie();

                Intent intent = getIntent();
                String username_prof = intent.getStringExtra("username_prof");


                DatabaseReference Recenzii = FirebaseDatabase.getInstance().getReference().child("Recenzii");

                Recenzii.child(username_prof).child(student.getUsername()).setValue(recenzie)

                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {


                                    Toast.makeText(Realizare_Recenzie.this, "Recenzia a fost trimisă cu succes. Mulțumim!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), Fereastra_Student.class));



                                }

                            }
                        });

            }

        });
    }


    private void trimiteRecenzie() {
        Intent intent = getIntent();
        String email_prof = intent.getStringExtra("email_prof");
        String nume_student = intent.getStringExtra("nume_student");


        final ManagerStudenti student = ManagerStudenti.getStudent();

        String message = "Un student v-a trimis o recenzie!"+"\n Nume Student:"+nume_student+"\n"+"Cum s-a desfășurat ședința? \n"+descriere_editText.getText().toString()+"\n"+"Puteți recomanda acest profesor?\n"+recomandare_editText.getText().toString()+"\n"+"Nota: \n"+nota+"\n"
                +"\n\n"+"O zi buna!"+"\n TutoringRO";
        String subject = "Ați primit o recenzie!";


        //Send Mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,email_prof,subject,message);

        javaMailAPI.execute();
    }
}
