package com.example.tutoringro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Fereastra_Student extends AppCompatActivity {
    TextView fullNameLabel, usernameLabel;
    String _USERNAME, _NUME;
    Button edit_btn;

    ImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student);


        fullNameLabel = findViewById(R.id.nume_field);
        usernameLabel = findViewById(R.id.username_field);
        profileImage = findViewById(R.id.profile_imageView);
        edit_btn = findViewById(R.id.edit_btn);

        final ManagerStudenti student = ManagerStudenti.getStudent();

        _USERNAME = student.getUsername();
        _NUME = student.getNume();

        fullNameLabel.setText(_NUME);
        usernameLabel.setText("username:" + _USERNAME);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.homeFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.searchFragment){
                    startActivity(new Intent(getApplicationContext(),formular_cerere.class));
                    finish();
                    overridePendingTransition(0,0);
                    return true;
                }
                if(menuItem.getItemId() == R.id.notificationsFragment){
                    startActivity(new Intent(getApplicationContext(), Sedinte_Student.class));
                    finish();
                    overridePendingTransition(0,0);
                    return true;
                }
                else {return false;}
            }
        });


        StorageReference profileRef = FirebaseStorage.getInstance().getReference().child("pozeProfil").child(_USERNAME + ".jpg");

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });



        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),EditStudent.class));
            }
        });
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Sunteți sigur ca vreți să plecați?")
                .setCancelable(false)
                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Fereastra_Student.super.onBackPressed();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                })
                .setNegativeButton("Nu", null)
                .show();
    }

}
