package com.example.tutoringro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import io.grpc.Context;

public class AdaugaImagine extends AppCompatActivity {
    private StorageReference UserProfileImageRef;
    ImageView imagineProfil;
    Button adauga;

    private ProgressDialog loadingBar;
    StorageReference reference;
    static String tip;
    ProgressDialog progressDialog;


    static String username;
    int TAKE_IMAGE_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_adauga_imagine);
        imagineProfil = findViewById(R.id.profile_imageView);
        adauga = findViewById(R.id.adauga_btn);


        Intent intent = getIntent();
       tip = intent.getStringExtra("user_tip");


        if(tip.equals("student")){
            final ManagerStudenti student = ManagerStudenti.getStudent().getStudent();
            username = student.getUsername();

        }
        else{
            final ManagerProfesori profesor = ManagerProfesori.getProfesor();
            username = profesor.getUsername();

        }
        reference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = reference.child("pozeProfil").child(username+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imagineProfil);
            }
        });


        adauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                imagineProfil.setImageURI(imageUri);
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Asteptați...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference fileRef = reference.child("pozeProfil").child(username+".jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdaugaImagine.this, "Imaginea a fost adaugata cu succes!", Toast.LENGTH_SHORT).show();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imagineProfil);
                        if(tip.equals("student"))
                        {
                            progressDialog.dismiss();
                                startActivity(new Intent(getApplicationContext(),EditStudent.class));
                        }
                        else
                            {
                                progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(),EditProfesor.class));
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdaugaImagine.this, "Eroare."+e, Toast.LENGTH_LONG).show();
            }
        });

    }
}