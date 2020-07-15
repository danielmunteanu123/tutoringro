package com.example.tutoringro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CautareCereri extends AppCompatActivity{

    private RecyclerView rv;
    public ProgressBar mProgressBar;
    private LinearLayoutManager layoutManager;
    AdapterStudenti adapterStudenti;
    static ImageView info;
    public static TextView empty,mHeader2Txt;
    AppCompatImageButton refresh_btn;
    public static ArrayList<String> studentiArray  = new ArrayList<>();
    //public static ArrayList<ManagerCereri> Cereri =new ArrayList<>();
    public static ArrayList<ManagerStudenti> CereriStudenti =new ArrayList<>();


    @SuppressLint("WrongViewCast")
    private void initializeViews(){
        mProgressBar = findViewById(R.id.mProgressBarLoad);
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);
        mHeader2Txt = findViewById(R.id.mHeader2Txt);
        empty = findViewById(R.id.empty);
        info = findViewById(R.id.info_icon);
        refresh_btn = findViewById(R.id.refresh_btn);
        rv = findViewById(R.id.lista_cereri_rv);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(),
                layoutManager.getOrientation());

        rv.addItemDecoration(dividerItemDecoration);
        adapterStudenti =new AdapterStudenti(CereriStudenti);
        rv.setAdapter(adapterStudenti);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_afiseaza_cereri);


        initializeViews();
        selectStudenti(this,Utils.getDatabaseRefence(),mProgressBar,rv, adapterStudenti);



        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.cereriFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.homeFragment){
                    startActivity(new Intent(getApplicationContext(), Fereastra_Profesor.class));
                    finish();
                    overridePendingTransition(0,0);
                    return true;
                }
                if(menuItem.getItemId() == R.id.notificationsFragment){
                    startActivity(new Intent(getApplicationContext(), Sedinte_Profesor.class));
                    finish();
                    overridePendingTransition(0,0);
                    return true;
                }
                else {return false;}
            }
        });

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CautareCereri.class));
                finish();
                overridePendingTransition(0,0);
            }
        });


    }



    public static void selectStudenti(final AppCompatActivity a, DatabaseReference db,
                                      final ProgressBar pb,
                                      final RecyclerView rv, AdapterStudenti adapterStudenti) {
        Utils.showProgressBar(pb);
        studentiArray.clear();
        CereriStudenti.clear();

        final ManagerProfesori profesor = ManagerProfesori.getProfesor();
        String prof_username =  profesor.getUsername();
        db.child("CereriRequests").child(prof_username).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0 ){
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ManagerCereri cerere = ds.getValue(ManagerCereri.class);
                        String username_student = cerere.getUsername_student();
                        studentiArray.add(username_student);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        db.child("Users").child("Studenti").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0 ) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //get Studenti si populam Arraylist-ul
                        ManagerStudenti student = ds.getValue(ManagerStudenti.class);

                        for(int i =0; i < studentiArray.size(); i++)
                            if(student.getUsername().equals(studentiArray.get(i))){
                                student.setKey(ds.getKey());

                                CereriStudenti.add(student);
                            }
                    }

                    adapterStudenti.notifyDataSetChanged();
                    if(adapterStudenti.getItemCount() < 1)
                    {
                        empty.setVisibility(View.VISIBLE);
                        info.setVisibility(View.VISIBLE);
                        mHeader2Txt.setVisibility(View.GONE);
                    }
                }else {
                    Utils.show(a,"N-am gasit nici o cerere");
                }
                Utils.hideProgressBar(pb);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log.d("FIREBASE CRUD", databaseError.getMessage());
                Utils.hideProgressBar(pb);
                Utils.show(a,databaseError.getMessage());
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
                        CautareCereri.super.onBackPressed();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                })
                .setNegativeButton("Nu", null)
                .show();
    }
}