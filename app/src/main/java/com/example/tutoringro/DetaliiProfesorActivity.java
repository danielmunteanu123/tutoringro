package com.example.tutoringro;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.view.View;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

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

public class DetaliiProfesorActivity extends AppCompatActivity {

    private TextView numeTV, domiciliuTV, categoriiTV, subcategoriiTV, materiiTV, pretTV, distantaTV, judetTV;
    private ManagerProfesori receivedProfesor;
    private DatabaseReference CereriRequestsRef;
    private String sender,reciever,CURRENT_STATE;
    private Button trimite_cerere_btn,certificate_btn, recenzii_btn;
    ImageView profileImage;
    ProgressDialog progressDialog;



    private void initializeWidgets(){
        numeTV= findViewById(R.id.nume_field);
        domiciliuTV= findViewById(R.id.domiciliuTV);
        judetTV = findViewById(R.id.judetTV);
        pretTV= findViewById(R.id.pretTV);
        profileImage = findViewById(R.id.profile_imageView);
        distantaTV= findViewById(R.id.distantaTV);
        categoriiTV= findViewById(R.id.categoriiTV);
        subcategoriiTV= findViewById(R.id.subcategoriiTV);
        materiiTV = findViewById(R.id.materiiTV);
        trimite_cerere_btn = findViewById(R.id.trimite_cerere_btn);
        certificate_btn = findViewById(R.id.certificate_btn);
        recenzii_btn = findViewById(R.id.recenzii_btn);



        CURRENT_STATE = "cereri_null";
        CereriRequestsRef = FirebaseDatabase.getInstance().getReference().child("CereriRequests");
        final ManagerStudenti student = ManagerStudenti.getStudent();
        sender = student.getUsername();
        receivedProfesor= Utils.receiveProfesor(getIntent(), DetaliiProfesorActivity.this);
        reciever = receivedProfesor.getUsername();

        final ManagerCereri cerere = ManagerCereri.getCerere();
        cerere.setUsername_profesor(reciever);

    }
    private void receiveAndShowData(){
        receivedProfesor= Utils.receiveProfesor(getIntent(), DetaliiProfesorActivity.this);

        if(receivedProfesor != null){
            numeTV.setText(receivedProfesor.getNume());
            domiciliuTV.setText(receivedProfesor.getDomiciliu());
            judetTV.setText(receivedProfesor.getJudet());
            pretTV.setText(String.valueOf(receivedProfesor.getPret())+" LEI" );
            distantaTV.setText(String.valueOf(receivedProfesor.getDistanta())+" km");
            categoriiTV.setText(receivedProfesor.getCategorii());
            subcategoriiTV.setText(receivedProfesor.getSubcategorii());
            materiiTV.setText(receivedProfesor.getMaterii());

        }
        StorageReference profileRef = FirebaseStorage.getInstance().getReference().child("pozeProfil").child(receivedProfesor.getUsername() + ".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_detalii_profesor);

        initializeWidgets();
        receiveAndShowData();

        trimite_cerere_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trimite_cerere_btn.setEnabled(false);

                if(CURRENT_STATE.equals("cereri_null")){
                    TrimiteCerere();
                }
                if (CURRENT_STATE.equals("cerere_trimisa")){
                    anulareCerere();
                }
            }
        });
        assistButtons();

        certificate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tip = "student";
                Intent intent = new Intent(getApplicationContext(),View_PDF.class);
                intent.putExtra("activity",tip);
                intent.putExtra("username_prof",receivedProfesor.getUsername());
                startActivity(intent);
            }
        });

        recenzii_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_prof = receivedProfesor.getUsername();
                Intent intent = new Intent(getApplicationContext(), AfiseazaRecenzii.class);
                intent.putExtra("username_prof",username_prof);
                startActivity(intent);
            }
        });

    }

    private void anulareCerere() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Asteptați...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        CereriRequestsRef.child(sender).child(reciever).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            CereriRequestsRef.child(reciever).child(sender).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                trimite_cerere_btn.setEnabled(true);
                                                CURRENT_STATE = "cerere_null";
                                                trimite_cerere_btn.setText("Trimite cerere");
                                                progressDialog.dismiss();
                                                Toast.makeText(DetaliiProfesorActivity.this, "Cererea a fost anulata", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void TrimiteCerere(){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Asteptați...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        //CereriRequestsRef.child(sender).child(reciever).setValue(cerere);
        CereriRequestsRef.child(sender).child(reciever).child("cerere_tip").setValue("cerere_trimisa")

                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            final ManagerCereri cerere = ManagerCereri.getCerere();
                            CereriRequestsRef.child(reciever).child(sender).setValue(cerere);
                            CereriRequestsRef.child(reciever).child(sender).child("cerere_tip").setValue("cerere_primita")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                trimite_cerere_btn.setEnabled(true);
                                                CURRENT_STATE = "cerere_trimisa";
                                                trimite_cerere_btn.setText("Anulare trimitere cerere");
                                                trimiteEmail();
                                                progressDialog.dismiss();
                                                Toast.makeText(DetaliiProfesorActivity.this, "Cererea a fost trimisă cu succes!", Toast.LENGTH_SHORT).show();


                                            }

                                        }
                                    });

                        }
                    }
                });

    }

    public void assistButtons(){
        CereriRequestsRef.child(sender).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(reciever))
                {
                    String cerere_tip = dataSnapshot.child(reciever).child("cerere_tip").getValue().toString();

                    if (cerere_tip.equals("cerere_trimisa"))
                    {
                        CURRENT_STATE = "cerere_trimisa";
                        trimite_cerere_btn.setText("Anulare trimitere cerere");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

    }

    private void trimiteEmail()
    {
        final ManagerCereri cerere = ManagerCereri.getCerere();
        final ManagerStudenti student = ManagerStudenti.getStudent();
        String mail = receivedProfesor.getEmail();
        String message = "Ati primit o noua cerere de la:"+student.getNume()+"\n"+"Detalii cerere:"+"\n"+"Materie:"+cerere.getMaterie()+"\n"+"Data:"+cerere.getData()+"\n"
                +"Ora:"+cerere.getOra()+"\n"+"Adresă:"+cerere.getLoc()  +"\n\n"+"O zi buna!"+"\n TutoringRO";;
        String subject = "Ati primit o cerere!";


        //Send Mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,message);

        javaMailAPI.execute();
    }

}
//end

