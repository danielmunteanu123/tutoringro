package com.example.tutoringro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class Subcategorii extends AppCompatActivity {

    ListView listview;
    Button next;
    String categorii, subcategorii = "";
    String tip,index;

    String[] Matematica = new String[]{
            "Algebră",
            "Analiză matematică",
            "Geometrie",
            "Aritmetică",
            "Logică matematică",
            "Matematică aplicată",
            "Probabilitate și statistici",
    };
    String[] Economie = new String[]{
            "Stiinte Economice",
            "Marketing",
            "Management",
            "Relatii internationale",
    };
    String[] Informatica = new String[]{
            "Digital Marketing",
            "Programare orientată pe obiecte",
            "Machine Learning",
            "Securitate Cibernetică",
    };
    String[] Fizica = new String[]{
            "Biofizică",
            "Electromagnetism",
            "Electronică",
            "Electrostatică",
            "Fizica atomului",
            "Mecanică",
    };


    SparseBooleanArray sparseBooleanArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_subcategorii);
        Intent intent = getIntent();

         tip = intent.getStringExtra("user_tip");
         index = intent.getStringExtra("index");
        categorii = getIntent().getStringExtra("categorii");
        listview = (ListView) findViewById(R.id.listView);
        next = findViewById(R.id.btnInainte);

        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        if (categorii.contains("Economie")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (Subcategorii.this,
                            android.R.layout.simple_list_item_single_choice,
                            android.R.id.text1, Economie);
            listview.setAdapter(adapter);
        } else if (categorii.contains("Informatica")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (Subcategorii.this,
                            android.R.layout.simple_list_item_single_choice,
                            android.R.id.text1, Informatica);


            listview.setAdapter(adapter);

        } else if (categorii.contains("Fizica")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (Subcategorii.this,
                            android.R.layout.simple_list_item_single_choice,
                            android.R.id.text1, Fizica);


            listview.setAdapter(adapter);

        } else if (categorii.contains("Matematica")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (Subcategorii.this,
                            android.R.layout.simple_list_item_single_choice,
                            android.R.id.text1, Matematica);


            listview.setAdapter(adapter);

        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sparseBooleanArray = listview.getCheckedItemPositions();
                int i = 0;

                while (i < sparseBooleanArray.size()) {
                    if (categorii.contains("Economie")) {
                        if (sparseBooleanArray.valueAt(i) && (!subcategorii.contains(Economie[sparseBooleanArray.keyAt(i)]))) {

                                subcategorii += Economie[sparseBooleanArray.keyAt(i)];



                        }
                        i++;
                    } else if (categorii.contains("Informatica")) {
                        if (sparseBooleanArray.valueAt(i) && (!subcategorii.contains(Informatica[sparseBooleanArray.keyAt(i)]))) {


                                subcategorii += Informatica[sparseBooleanArray.keyAt(i)];



                        }

                        i++;
                    } else if (categorii.contains("Matematica")) {
                        if (sparseBooleanArray.valueAt(i) && (!subcategorii.contains(Matematica[sparseBooleanArray.keyAt(i)]))) {


                                subcategorii += Matematica[sparseBooleanArray.keyAt(i)];

                        }

                        i++;
                    } else if (categorii.contains("Fizica")) {
                        if (sparseBooleanArray.valueAt(i) && (!subcategorii.contains(Fizica[sparseBooleanArray.keyAt(i)]))) {


                                subcategorii += Fizica[sparseBooleanArray.keyAt(i)];

                        }

                        i++;
                    }

                }


                if (listview.getCheckedItemCount() < 1) {
                    Toast.makeText(Subcategorii.this, "Alegeti cel putin 1 subcategorie!", Toast.LENGTH_SHORT).show();

                } else {


                    if (tip.equals("profesor")) {
                        System.out.println(subcategorii);
                        final ManagerProfesori profesor = ManagerProfesori.getProfesor();
                        profesor.setSubcategorii(subcategorii);
                    } else {
                        final ManagerStudenti student = ManagerStudenti.getStudent();
                        student.setSubcategorii(subcategorii);
                    }
                    Intent intent = new Intent(getApplicationContext(), Materii.class);
                    intent.putExtra("user_tip", tip);
                    intent.putExtra("index", index);
                    intent.putExtra("categorii",categorii);
                    intent.putExtra("subcategorii",subcategorii);
                    startActivity(intent);
                    System.out.println(subcategorii);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {

                        categorii = "";
                        subcategorii = "";
                        Intent intent = new Intent(getApplicationContext(), Categorii.class);
                        intent.putExtra("user_tip", tip);
                        intent.putExtra("index", index);
                        startActivity(intent);

    }


}
