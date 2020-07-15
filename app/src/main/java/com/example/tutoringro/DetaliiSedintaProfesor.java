package com.example.tutoringro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetaliiSedintaProfesor extends AppCompatActivity {

    private TextView materiiTV, oraTV, dataTV, locTV, numeTV;
    private ManagerStudenti receivedStudent;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private DatabaseReference  SedinteRef;
    private String sender,reciever;
    private Button anuleaza_btn,profil_btn,recenzie_btn,certificate_btn;
    ImageView profile_image;



    private void initializeWidgets(){
        materiiTV = findViewById(R.id.materiiTV);
        oraTV = findViewById(R.id.oraTV);
        dataTV = findViewById(R.id.dataTV);
        locTV = findViewById(R.id.locTV);
        anuleaza_btn = findViewById(R.id.anuleaza_btn);
        numeTV = findViewById(R.id.nume_field);
        profile_image = findViewById(R.id.profile_imageView);
        profil_btn = findViewById(R.id.profil_btn);
        recenzie_btn = findViewById(R.id.recenzie_btn);
        recenzie_btn.setVisibility(View.GONE);
        certificate_btn =findViewById(R.id.certificate_btn);
        certificate_btn.setVisibility(View.GONE);




        SedinteRef = FirebaseDatabase.getInstance().getReference().child("Sedinte");
        final ManagerProfesori profesor = ManagerProfesori.getProfesor();
        reciever = profesor.getUsername();
        receivedStudent= Utils.receiveStudent(getIntent(), DetaliiSedintaProfesor.this);
        sender = receivedStudent.getUsername();

    }
    private void receiveAndShowData(){
        receivedStudent= Utils.receiveStudent(getIntent(), DetaliiSedintaProfesor.this);
        String username_prof = ManagerProfesori.getProfesor().getUsername();

        SedinteRef.child(username_prof).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //get Sedinte si populam Arraylist-ul

                        ManagerCereri cerere = ds.getValue(ManagerCereri.class);
                        if (cerere != null && cerere.getUsername_student().equals(receivedStudent.getUsername())) {

                            numeTV.setText(receivedStudent.getNume());
                            materiiTV.setText(cerere.getMaterie());
                            oraTV.setText(String.valueOf(cerere.getOra()));
                            locTV.setText(cerere.getLoc());
                            dataTV.setText(cerere.getData());
                            final ManagerCereri cerereFinal = ManagerCereri.getCerere();
                            cerereFinal.setMaterie(String.valueOf(cerere.getMaterie()));
                            cerereFinal.setOra(cerere.getOra());
                            cerereFinal.setLoc(cerere.getLoc());
                            cerereFinal.setData(cerere.getData());

                            StorageReference profileRef = FirebaseStorage.getInstance().getReference().child("pozeProfil").child(receivedStudent.getUsername() + ".jpg");

                            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Picasso.get().load(uri).into(profile_image);
                                }
                            });

                        }
                    }
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_detalii_sedinte);

        initializeWidgets();
        receiveAndShowData();
        anuleaza_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ManagerCereri cerere = ManagerCereri.getCerere();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date dateToday = new Date();
                String date = cerere.getData();
                int i = date.compareTo(sdf.format(dateToday));

                if (i > 0) {
                    Toast.makeText(DetaliiSedintaProfesor.this, "Nu mai puteți anula ședința.", Toast.LENGTH_LONG).show();
                } else {

                    AnuleazaSedinta();
                }
            }
        });

        profil_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activity = "sedinta";
                Intent intent = new Intent(getApplicationContext(),profil_student.class);
                intent.putExtra("username",receivedStudent.getUsername());
                intent.putExtra("nume",receivedStudent.getNume());
                intent.putExtra("email",receivedStudent.getEmail());
                intent.putExtra("telefon",receivedStudent.getTelefon());
                intent.putExtra("categorii",receivedStudent.getCategorii());
                intent.putExtra("subcategorii",receivedStudent.getSubcategorii());
                intent.putExtra("materii",receivedStudent.getMaterii());
                intent.putExtra("varsta",receivedStudent.getVarsta());
                intent.putExtra("domiciliu",receivedStudent.getDomiciliu());
                intent.putExtra("judet",receivedStudent.getJudet());
                intent.putExtra("activity",activity);
                startActivity(intent);
            }
        });
    }

    private void AnuleazaSedinta() {
       SedinteRef.child(sender).child(reciever).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            SedinteRef.child(reciever).child(sender).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                anuleaza_btn.setEnabled(false);
                                                Toast.makeText(DetaliiSedintaProfesor.this, "Sedinta a fost anulata!", Toast.LENGTH_SHORT).show();
                                                trimiteEmailAnulare();
                                                Intent intent = new Intent(getApplicationContext(), Fereastra_Profesor.class);
                                                startActivity(intent);

                                            }

                                        }
                                    });

                        }
                    }
                });


    }

    private void trimiteEmailAnulare() {
        final ManagerCereri cerere = ManagerCereri.getCerere();
        final ManagerProfesori profesor = ManagerProfesori.getProfesor();
        String mail = receivedStudent.getEmail();
        String message = "Din păcate ședința a fost anulată de către:"+profesor.getNume()+"\n"+"Detalii ședință:"+"\n"+"Materie:"+cerere.getMaterie()+"\n"+"Data:"+cerere.getData()+"\n"
                +"Ora:"+cerere.getOra()+"\n"+"Adresă:"+cerere.getLoc()  +"\n\n"+"O zi buna!"+"\n TutoringRO";
        String subject = "Ședința a fost anulată.";


        //Send Mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,message);

        javaMailAPI.execute();
    }


}
