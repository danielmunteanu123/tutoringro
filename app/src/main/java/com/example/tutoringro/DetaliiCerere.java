package com.example.tutoringro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class DetaliiCerere extends AppCompatActivity {

    private TextView materiiTV, oraTV, dataTV, locTV;
    private ManagerStudenti receivedStudent;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private DatabaseReference CereriRequestsRef, SedinteRef;
    private String sender,reciever,CURRENT_STATE, numeProfesor;
    private Button accepta_btn, respinge_btn,profil_btn;
    private ImageView profileImage;
    AppCompatImageButton back_btn;
    ProgressDialog progressDialog;




    @SuppressLint("WrongViewCast")
    private void initializeWidgets(){
        materiiTV = findViewById(R.id.materiiTV);
        oraTV = findViewById(R.id.oraTV);
        dataTV = findViewById(R.id.dataTV);
        locTV = findViewById(R.id.locTV);
        accepta_btn = findViewById(R.id.accepta_button);
        respinge_btn = findViewById(R.id.respinge_btn);
        profileImage = findViewById(R.id.profile_imageView);
        back_btn = findViewById(R.id.back_btn);
        profil_btn = findViewById(R.id.profil_btn);


        CURRENT_STATE = "cereri_null";
        CereriRequestsRef = FirebaseDatabase.getInstance().getReference().child("CereriRequests");
        SedinteRef = FirebaseDatabase.getInstance().getReference().child("Sedinte");
        final ManagerProfesori profesor = ManagerProfesori.getProfesor();
        numeProfesor = profesor.getNume();
        reciever = profesor.getUsername();
        receivedStudent= Utils.receiveStudent(getIntent(), DetaliiCerere.this);
        sender = receivedStudent.getUsername();

        StorageReference profileRef = FirebaseStorage.getInstance().getReference().child("pozeProfil").child(receivedStudent.getUsername() + ".jpg");

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CautareCereri.class));
            }
        });

        profil_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activity = "cerere";
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
    private void receiveAndShowData(){
        receivedStudent= Utils.receiveStudent(getIntent(), DetaliiCerere.this);
        String username_prof = ManagerProfesori.getProfesor().getUsername();

        CereriRequestsRef.child(username_prof).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //get cereri si populam Arraylist-ul
                        ManagerCereri cerere = ds.getValue(ManagerCereri.class);
                        if (cerere != null && cerere.getUsername_student().equals(receivedStudent.getUsername())) {

                            materiiTV.setText(cerere.getMaterie());
                            oraTV.setText(String.valueOf(cerere.getOra()));
                            locTV.setText(cerere.getLoc());
                            dataTV.setText(cerere.getData());
                            final ManagerCereri cerereFinal = ManagerCereri.getCerere();
                            cerereFinal.setMaterie(String.valueOf(cerere.getMaterie()));
                            cerereFinal.setOra(cerere.getOra());
                            cerereFinal.setLoc(cerere.getLoc());
                            cerereFinal.setData(cerere.getData());
                            cerereFinal.setUsername_profesor(username_prof);
                            cerereFinal.setUsername_student(receivedStudent.getUsername());
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
        setContentView(R.layout.activity_detalii_cerere);

        initializeWidgets();
        receiveAndShowData();
        accepta_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetaliiCerere.this)
            .setMessage("Sunteti sigur?")
                        .setCancelable(false)
                        .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DetaliiCerere.super.onBackPressed();
                                AcceptaCerere();
                                stergeCerere();

                            }
                        })
                        .setNegativeButton("Nu", null)
                        .show();

            }
        });

        respinge_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetaliiCerere.this)
                        .setMessage("Sunteti sigur?")
                        .setCancelable(false)
                        .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DetaliiCerere.super.onBackPressed();
                                RespingeCerere();



                            }
                        })
                        .setNegativeButton("Nu", null)
                        .show();
            }
        });

    }

    private void stergeCerere()
    {
        final ManagerCereri cerere = ManagerCereri.getCerere();
        CereriRequestsRef.child(sender).child(reciever).child("cerere_tip").setValue("cerere_acceptata").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {

                    SedinteRef.child(reciever).child(sender).setValue(cerere);
                    CereriRequestsRef.child(reciever).child(sender).child("cerere_tip").setValue("cerere_acceptata").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {

                                CereriRequestsRef.child(sender).child(reciever).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    CereriRequestsRef.child(reciever).child(sender).removeValue()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        CURRENT_STATE = "cerere_acceptata";

                                                                        CautareCereri.CereriStudenti.clear();






                                                                    }
                                                                }
                                                            });

                                                }
                                            }
                                        });
                            }
                        }
                    });
                }

            }
        });
    }

    private void AcceptaCerere() {

        final ManagerCereri cerere = ManagerCereri.getCerere();
        SedinteRef.child(sender).child(reciever).setValue(cerere).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    SedinteRef.child(reciever).child(sender).setValue(cerere).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                CereriRequestsRef.child(sender).child(reciever).child("cerere_tip").setValue("cerere_acceptata").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            CereriRequestsRef.child(reciever).child(sender).child("cerere_tip").setValue("cerere_acceptata").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    trimiteEmailAcceptat();
                                                    Toast.makeText(DetaliiCerere.this, "Cererea a fost acceptată!", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), Fereastra_Profesor.class));
                                                }
                                            });

                                        }
                                    }
                                });
                            }


                        }
                    });




                }
            }
        });

    }



    private void RespingeCerere() {
        final ManagerCereri cerere = ManagerCereri.getCerere();

        CereriRequestsRef.child(sender).child(reciever).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    CereriRequestsRef.child(reciever).child(sender).removeValue().
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        respinge_btn.setEnabled(false);
                                        accepta_btn.setEnabled(false);
                                        trimiteEmailRespins();
                                        startActivity(new Intent(getApplicationContext(), Fereastra_Profesor.class));
                                        Toast.makeText(DetaliiCerere.this, "Cererea a fost respinsa!", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }

            }
            });
        }

    private void trimiteEmailAcceptat() {
        final ManagerCereri cerere = ManagerCereri.getCerere();
        final ManagerProfesori profesor = ManagerProfesori.getProfesor();
        String mail = receivedStudent.getEmail();
        String message = "Cererea a fost acceptata de către:"+profesor.getNume()+"\n"+"Detalii cerere:"+"\n"+"Materie:"+cerere.getMaterie()+"\n"+"Data:"+cerere.getData()+"\n"
                +"Ora:"+cerere.getOra()+"\n"+"Adresă:"+cerere.getLoc()  +"\n\n"+"O zi buna!"+"\n TutoringRO";;
        String subject = "Cererea a fost acceptata!";


        //Send Mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,message);

        javaMailAPI.execute();


    }
    private void trimiteEmailRespins() {
        final ManagerCereri cerere = ManagerCereri.getCerere();
        final ManagerProfesori profesor = ManagerProfesori.getProfesor();
        String mail = receivedStudent.getEmail();
        String message = "Din păcate cererea a fost respinsă de către:"+profesor.getNume()+"\n"+"Detalii cerere:"+"\n"+"Materie:"+cerere.getMaterie()+"\n"+"Data:"+cerere.getData()+"\n"
                +"Ora:"+cerere.getOra()+"\n"+"Adresă:"+cerere.getLoc()  +"\n\n"+"O zi buna!"+"\n TutoringRO";;
        String subject = "Cererea a fost respinsă.";

        //Send Mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,message);
        javaMailAPI.execute();


    }


}

