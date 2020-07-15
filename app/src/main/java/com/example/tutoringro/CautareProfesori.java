package com.example.tutoringro;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CautareProfesori extends AppCompatActivity {

    private RecyclerView rv;
    public ProgressBar mProgressBar;
    private LinearLayoutManager layoutManager;
    AdapterProfesori adapter;
    public static TextView empty, headerText;
    static ImageView info;

    public static ArrayList<ManagerProfesori> Profesori =new ArrayList<>();


    @SuppressLint("WrongViewCast")
    private void initializeViews() {
        mProgressBar = findViewById(R.id.mProgressBarLoad);
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);
        headerText = findViewById(R.id.mHeader2Txt);
        info = findViewById(R.id.info_icon);
        info.setVisibility(View.GONE);
        empty = findViewById(R.id.empty);
        empty.setVisibility(View.GONE);

        rv = findViewById(R.id.mRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(),
                layoutManager.getOrientation());

        rv.addItemDecoration(dividerItemDecoration);
        adapter = new AdapterProfesori(Profesori);
        int count = adapter.getItemCount();
        System.out.println("---------------"+count);
        rv.setAdapter(adapter);



    }

    private void bindData() {
        final ManagerCereri cerere = new ManagerCereri().getCerere();

        select(this, Utils.getDatabaseRefence(), mProgressBar, rv, adapter, cerere);

        }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cautare_profesori);


        initializeViews();
        bindData();






    }

    public static void select(final AppCompatActivity a, DatabaseReference db,
                              final ProgressBar pb,
                              final RecyclerView rv, AdapterProfesori adapter, ManagerCereri cerere) {
        Utils.showProgressBar(pb);


        db.child("Users").child("Profesori").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profesori.clear();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0 ) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //get Profesori si populam Arraylist-ul

                        ManagerProfesori profesor = ds.getValue(ManagerProfesori.class);

                        profesor.setKey(ds.getKey());
                        if (cerere.getJudet().equals("Nu conteaza")){
                            if(profesor.getMaterii().contains(cerere.getMaterie())){
                                Profesori.add(profesor);

                            }
                        } else {
                            if (profesor.getMaterii().contains(cerere.getMaterie()) && profesor.getJudet().equals(cerere.getJudet())) {
                                Profesori.add(profesor);
                            }
                        }

                    }

                    adapter.notifyDataSetChanged();

                    if(adapter.getItemCount() < 1)
                    {
                        empty.setVisibility(View.VISIBLE);
                        info.setVisibility(View.VISIBLE);
                        headerText.setVisibility(View.GONE);
                    }
                }else {
                    Utils.show(a,"N-am gasit nici-un profesor");
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
                .setMessage("Sunteti sigur?")
                .setCancelable(false)
                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CautareProfesori.super.onBackPressed();
                        final ManagerStudenti student = ManagerStudenti.getStudent();
                        Intent intent = new Intent(getApplicationContext(), Fereastra_Student.class);
                        intent.putExtra("nume", student.getNume());
                        intent.putExtra("username", student.getUsername());
                        intent.putExtra("email", student.getEmail());
                        intent.putExtra("telefon", student.getTelefon());
                        intent.putExtra("categorii", student.getCategorii());
                        intent.putExtra("subcategorii", student.getSubcategorii());
                        intent.putExtra("materii", student.getMaterii());
                        intent.putExtra("domiciliu", student.getDomiciliu());
                        intent.putExtra("varsta", student.getVarsta());
                        startActivity(intent);

                    }
                })
                .setNegativeButton("Nu", null)
                .show();

    }
}
//end
