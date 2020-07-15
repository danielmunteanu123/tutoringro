package com.example.tutoringro;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class AfiseazaRecenzii extends AppCompatActivity {


    private RecyclerView rv;
    public ProgressBar mProgressBar;
    static ImageView info;
    private LinearLayoutManager layoutManager;
    AdapterRecenzii adapterRecenzii;
    public static TextView mHeader2Txt,empty;
    public static ArrayList<ManagerRecenzii> Recenzii  =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_afiseaza_recenzii);
        initializeViews();
        bindData();



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
        rv = findViewById(R.id.lista_cereri_rv);
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(),
                layoutManager.getOrientation());

        rv.addItemDecoration(dividerItemDecoration);
        adapterRecenzii = new AdapterRecenzii(Recenzii);
        rv.setAdapter(adapterRecenzii);


    }




    private void bindData(){
        CautareRecenzii(this,Utils.getDatabaseRefence(),mProgressBar,rv, adapterRecenzii);


    }
    public void CautareRecenzii(final AppCompatActivity a, DatabaseReference db,
                                final ProgressBar pb,
                                final RecyclerView rv, AdapterRecenzii adapterRecenzii) {
        Utils.showProgressBar(pb);

       Intent intent = getIntent();
       String username_prof = intent.getStringExtra("username_prof");


        db.child("Recenzii").child(username_prof).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Recenzii.clear();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0 ) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //get Recenzii si populam Arraylist-ul
                        ManagerRecenzii recenzii = ds.getValue(ManagerRecenzii.class);
                        Recenzii.add(recenzii);
                    }
                    }

                adapterRecenzii.notifyDataSetChanged();
                    if(adapterRecenzii.getItemCount() < 1)
                    {
                        empty.setVisibility(View.VISIBLE);
                        info.setVisibility(View.VISIBLE);
                        mHeader2Txt.setVisibility(View.GONE);
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

}
