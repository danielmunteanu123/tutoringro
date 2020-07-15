package com.example.tutoringro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Sedinte_Student extends AppCompatActivity {


    private RecyclerView rv;
    public ProgressBar mProgressBar;
    static ImageView info;
    private LinearLayoutManager layoutManager;
    AdapterSedinteStudent adapterStudenti;
    AppCompatImageButton refresh_btn;
    public static TextView mHeader2Txt,empty;
    public static ArrayList<String> profesoriArray  = new ArrayList<>();
    public static ArrayList<ManagerCereri> Cereri =new ArrayList<>();
    public static ArrayList<ManagerProfesori> SedinteStudenti  =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sedinte__student);





        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.notificationsFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.homeFragment){
                    startActivity(new Intent(getApplicationContext(), Fereastra_Student.class));
                    finish();
                    overridePendingTransition(0,0);
                    return true;
                }
                if(menuItem.getItemId() == R.id.searchFragment){
                    startActivity(new Intent(getApplicationContext(),formular_cerere.class));
                    finish();
                    overridePendingTransition(0,0);
                    return true;
                }
                else {return false;}
            }
        });

        initializeViews();
        bindData();

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Sedinte_Student.class));
                finish();
                overridePendingTransition(0,0);
            }
        });



    }

    @SuppressLint("WrongViewCast")
    private void initializeViews() {
        mProgressBar = findViewById(R.id.mProgressBarLoad);
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);
        empty = findViewById(R.id.empty);
        empty.setVisibility(View.GONE);
        info = findViewById(R.id.info_icon);
        info.setVisibility(View.GONE);
        mHeader2Txt = findViewById(R.id.mHeader2Txt);
        refresh_btn = findViewById(R.id.refresh_btn);
        rv = findViewById(R.id.lista_cereri_rv);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(),
                layoutManager.getOrientation());

        rv.addItemDecoration(dividerItemDecoration);
        adapterStudenti = new AdapterSedinteStudent(SedinteStudenti);
        rv.setAdapter(adapterStudenti);


    }




    private void bindData(){
        CautareSedinteStudent(this,Utils.getDatabaseRefence(),mProgressBar,rv, adapterStudenti);


    }
    public static void CautareSedinteStudent(final AppCompatActivity a, DatabaseReference db,
                                             final ProgressBar pb,
                                             final RecyclerView rv, AdapterSedinteStudent adapterProfesori) {
        Utils.showProgressBar(pb);

        final ManagerStudenti student = ManagerStudenti.getStudent();
        String student_username =  student.getUsername();

        db.child("Sedinte").child(student_username).addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profesoriArray.clear();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0 ){
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ManagerCereri cerere = ds.getValue(ManagerCereri.class);
                        cerere.setKey(ds.getKey());
                        Cereri.add(cerere);
                        String username_profesor = cerere.getUsername_profesor();
                        profesoriArray.add(username_profesor);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        db.child("Users").child("Profesori").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SedinteStudenti.clear();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0 ) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //get Profesori si populam Arraylist-ul
                        ManagerProfesori profesor = ds.getValue(ManagerProfesori.class);

                        for(int i =0; i < profesoriArray.size(); i++)
                            if(profesor.getUsername().equals(profesoriArray.get(i))){
                                profesor.setKey(ds.getKey());

                                SedinteStudenti.add(profesor);


                            }
                    }

                    adapterProfesori.notifyDataSetChanged();
                    if(adapterProfesori.getItemCount() < 1)
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
                        Sedinte_Student.super.onBackPressed();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                })
                .setNegativeButton("Nu", null)
                .show();
    }


}
