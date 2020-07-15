package com.example.tutoringro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Upload_PDF extends AppCompatActivity {

    EditText editPDFName;
    Button btn_upload,view_btn, sterge_btn;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.upload_pdf);

        btn_upload = findViewById(R.id.adauga_btn);
        view_btn = findViewById(R.id.view_btn);
        sterge_btn = findViewById(R.id.sterge_btn);
        editPDFName = findViewById(R.id.txt_pdfName);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("certificate");

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String val = editPDFName.getText().toString();

                if (val.isEmpty()) {
                    editPDFName.setError("Completati spatiile libere");

                } else {
                    selectPDFFile();
                }
            }
        });

        view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tip = "profesor";
                Intent intent = new Intent(getApplicationContext(), View_PDF.class);
                intent.putExtra("activity", tip);
                startActivity(intent);
            }
        });


        sterge_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stergeCertificate();
            }
        });

    }

    private void stergeCertificate() {
        ManagerProfesori profesor = ManagerProfesori.getProfesor();
        String username = profesor.getUsername();
        databaseReference = FirebaseDatabase.getInstance().getReference("certificate");
        Query query = databaseReference.orderByChild("username").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    databaseReference.child(username).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Upload_PDF.this, "Certificatele au fost șterse cu succes.", Toast.LENGTH_SHORT).show();


                                    }

                                }
                            });

                }
                else
                {
                    Toast.makeText(Upload_PDF.this, " Nu există certificate disponibile.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void selectPDFFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF File"), 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null)

            uploadPDFFile(data.getData());
    }

    private void uploadPDFFile(Uri data) {


        ManagerProfesori profesor = ManagerProfesori.getProfesor();
        String username = profesor.getUsername();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        StorageReference reference = storageReference.child("certificate").child(username+"_"+System.currentTimeMillis()+".pdf");
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                            Uri url = uri.getResult();
                            Manager_PDF uploadPDF = new Manager_PDF(editPDFName.getText().toString(), url.toString(), username);
                            databaseReference.child(username).setValue(uploadPDF);
                        Toast.makeText(Upload_PDF.this, "Fisierul a fost incarcat cu succes!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();

                progressDialog.setMessage("Uploaded: " + progress +"%");
            }
        });
    }
}
