package com.example.tutoringro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.Edits;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Materii extends AppCompatActivity {

    ListView listview;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    String tip, index;
    String  categorii,subcategorii, materii = "";

    SparseBooleanArray sparseBooleanArray;
    String[] Algebra, AnalizaMatematica, Geometrie, StiinteEconomice, DigitalMarketing, POO, MachineLearning, ElectroMagnetism, Mecanica;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_materii);
         Algebra = getResources().getStringArray(R.array.algebra_array);
         AnalizaMatematica = getResources().getStringArray(R.array.analiza_matematica);
        Geometrie = getResources().getStringArray(R.array.geometrie_array);

      StiinteEconomice = getResources().getStringArray(R.array.materii_economy);


        DigitalMarketing = getResources().getStringArray(R.array.digital_marketing);
         POO = getResources().getStringArray(R.array.POO_array);
         MachineLearning = getResources().getStringArray(R.array.machine_learning);


         ElectroMagnetism = getResources().getStringArray(R.array.electromagnetism_array);
         Mecanica = getResources().getStringArray(R.array.mecanica_array);


        Intent intent = getIntent();
        tip = intent.getStringExtra("user_tip");
        index = intent.getStringExtra("index");

        firebaseAuth = FirebaseAuth.getInstance();
        categorii = getIntent().getStringExtra("categorii");
        subcategorii = getIntent().getStringExtra("subcategorii");

        listview = (ListView) findViewById(R.id.listView);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        setAdapter();






        findViewById(R.id.btnInainte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sparseBooleanArray = listview.getCheckedItemPositions();
                int i = 0;
                while (i < sparseBooleanArray.size()) {

                    if (categorii.contains("Matematica")) {

                        if (subcategorii.contains("Algebra")) {

                            if (sparseBooleanArray.valueAt(i) && (!materii.contains(Algebra[sparseBooleanArray.keyAt(i)]))) {

                                if (i == 0) {
                                    materii += Algebra[sparseBooleanArray.keyAt(i)];
                                } else {
                                    materii += "," + Algebra[sparseBooleanArray.keyAt(i)];
                                }
                            }

                            i++;
                        } else if (subcategorii.contains("AnalizaMatematica")) {

                            if (sparseBooleanArray.valueAt(i) && (!materii.contains(AnalizaMatematica[sparseBooleanArray.keyAt(i)]))) {

                                if (i == 0) {
                                    materii += AnalizaMatematica[sparseBooleanArray.keyAt(i)];
                                } else {
                                    materii += "," + AnalizaMatematica[sparseBooleanArray.keyAt(i)];
                                }
                            }

                            i++;
                        }
                        else if (subcategorii.contains("Geometrie")) {

                            if (sparseBooleanArray.valueAt(i) && (!materii.contains(Geometrie[sparseBooleanArray.keyAt(i)]))) {

                                if (i == 0) {
                                    materii += Geometrie[sparseBooleanArray.keyAt(i)];
                                } else {
                                    materii += "," + Geometrie[sparseBooleanArray.keyAt(i)];
                                }
                            }
                            i++;
                        } else {

                            if (sparseBooleanArray.valueAt(i) && (!materii.contains(AnalizaMatematica[sparseBooleanArray.keyAt(i)]))) {

                                if (i == 0) {
                                    materii += AnalizaMatematica[sparseBooleanArray.keyAt(i)];
                                } else {
                                    materii += "," + AnalizaMatematica[sparseBooleanArray.keyAt(i)];
                                }
                            }
                            i++;


                        }

                    }
                    else if (categorii.contains("Economie")) {

                        if (sparseBooleanArray.valueAt(i) && (!materii.contains(StiinteEconomice[sparseBooleanArray.keyAt(i)]))) {

                                if (i == 0) {
                                    materii += StiinteEconomice[sparseBooleanArray.keyAt(i)];
                                } else {
                                    materii += "," + StiinteEconomice[sparseBooleanArray.keyAt(i)];
                                }
                            }

                            i++;





                    } else if (categorii.contains("Informatica")) {
                        if (subcategorii.contains("Digital Marketing")) {

                            if (sparseBooleanArray.valueAt(i) && (!materii.contains(DigitalMarketing[sparseBooleanArray.keyAt(i)]))) {

                                if (i == 0) {
                                    materii += DigitalMarketing[sparseBooleanArray.keyAt(i)];
                                } else {
                                    materii += "," + DigitalMarketing[sparseBooleanArray.keyAt(i)];
                                }
                            }

                            i++;
                        }


                        else if (subcategorii.contains("POO")) {

                            if (sparseBooleanArray.valueAt(i) && (!materii.contains(POO[sparseBooleanArray.keyAt(i)]))) {

                                if (i == 0) {
                                    materii += POO[sparseBooleanArray.keyAt(i)];
                                } else {
                                    materii += "," + POO[sparseBooleanArray.keyAt(i)];
                                }
                            }

                            i++;
                        }
                        else if (subcategorii.contains("Machine Learning")) {

                            if (sparseBooleanArray.valueAt(i) && (!materii.contains(MachineLearning[sparseBooleanArray.keyAt(i)]))) {

                                if (i == 0) {
                                    materii += MachineLearning[sparseBooleanArray.keyAt(i)];
                                } else {
                                    materii += "," + MachineLearning[sparseBooleanArray.keyAt(i)];
                                }
                            }

                            i++;
                        }
                        else
                        {
                            if (sparseBooleanArray.valueAt(i) && (!materii.contains(POO[sparseBooleanArray.keyAt(i)]))) {

                                if (i == 0) {
                                    materii += POO[sparseBooleanArray.keyAt(i)];
                                } else {
                                    materii += "," + POO[sparseBooleanArray.keyAt(i)];
                                }
                            }

                            i++;

                        }
                    } else if (categorii.contains("Fizica")) {
                        if (subcategorii.contains("ElectroMagnetism")) {

                            if (sparseBooleanArray.valueAt(i) && (!materii.contains(ElectroMagnetism[sparseBooleanArray.keyAt(i)]))) {

                                if (i == 0) {
                                    materii += ElectroMagnetism[sparseBooleanArray.keyAt(i)];
                                } else {
                                    materii += "," + ElectroMagnetism[sparseBooleanArray.keyAt(i)];
                                }
                            }

                            i++;
                        }
                       else if (subcategorii.contains("Mecanica")) {

                            if (sparseBooleanArray.valueAt(i) && (!materii.contains(Mecanica[sparseBooleanArray.keyAt(i)]))) {

                                if (i == 0) {
                                    materii += Mecanica[sparseBooleanArray.keyAt(i)];
                                } else {
                                    materii += "," + Mecanica[sparseBooleanArray.keyAt(i)];
                                }
                            }

                            i++;
                        }
                       else
                        if (sparseBooleanArray.valueAt(i) && (!materii.contains(Mecanica[sparseBooleanArray.keyAt(i)]))) {

                            if (i == 0) {
                                materii += Mecanica[sparseBooleanArray.keyAt(i)];
                            } else {
                                materii += "," + Mecanica[sparseBooleanArray.keyAt(i)];
                            }
                        }

                        i++;
                    }
                    }



                if (listview.getCheckedItemCount() < 2) {
                    Toast.makeText(Materii.this, "Alegeti cel putin 2 materii!", Toast.LENGTH_SHORT).show();

                } else {

                    if (tip.equals("profesor")) {
                        final ManagerProfesori profesor = ManagerProfesori.getProfesor();
                        profesor.setMaterii(materii);
                    } else {
                        final ManagerStudenti student = ManagerStudenti.getStudent();
                        student.setMaterii(materii);
                    }
                    if (index.equals("signup")) {

                        if (tip.equals("profesor")) {
                            startActivity(new Intent(getApplicationContext(), VerificareTelProfesori.class));
                        } else {
                            startActivity(new Intent(getApplicationContext(), VerificareTelStudenti.class));
                        }
                    } else if (index.equals("edit")) {
                        if (tip.equals("profesor")) {
                            final ManagerProfesori profesor = ManagerProfesori.getProfesor();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child("Profesori");
                            reference.child(profesor.getUsername()).child("categorii").setValue(profesor.getCategorii());
                            reference.child(profesor.getUsername()).child("subcategorii").setValue(profesor.getSubcategorii());
                            reference.child(profesor.getUsername()).child("materii").setValue(profesor.getMaterii());
                            Toast.makeText(Materii.this, "Datele au fost modificate cu succes!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), EditProfesor.class));

                        } else {
                            final ManagerStudenti student = ManagerStudenti.getStudent();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child("Studenti");
                            reference.child(student.getUsername()).child("categorii").setValue(student.getCategorii());
                            reference.child(student.getUsername()).child("subcategorii").setValue(student.getSubcategorii());
                            reference.child(student.getUsername()).child("materii").setValue(student.getMaterii());
                            Toast.makeText(Materii.this, "Datele au fost modificate cu succes!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), EditStudent.class));
                        }
                    }
                }
            }
        });

    }
    public void setAdapter() {
        if (categorii.contains("Matematica")) {
            if (subcategorii.contains("Algebra")) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (Materii.this,
                                android.R.layout.simple_list_item_single_choice,
                                android.R.id.text1, Algebra);
                listview.setAdapter(adapter);
            } else if (subcategorii.contains("AnalizaMatematica")) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (Materii.this,
                                android.R.layout.simple_list_item_single_choice,
                                android.R.id.text1, AnalizaMatematica);


                listview.setAdapter(adapter);

            } else if (subcategorii.contains("Geometrie")) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (Materii.this,
                                android.R.layout.simple_list_item_single_choice,
                                android.R.id.text1, Geometrie);
                listview.setAdapter(adapter);
            } else {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (Materii.this,
                                android.R.layout.simple_list_item_single_choice,
                                android.R.id.text1, AnalizaMatematica);
                listview.setAdapter(adapter);
            }

        }
        else if(categorii.contains("Economie")){


                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (Materii.this,
                                android.R.layout.simple_list_item_single_choice,
                                android.R.id.text1, StiinteEconomice);



                listview.setAdapter(adapter);
            }

        else if (categorii.contains("Informatica")) {

            if (subcategorii.contains("Digital Marketing")) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (Materii.this,
                                android.R.layout.simple_list_item_single_choice,
                                android.R.id.text1, DigitalMarketing);
                listview.setAdapter(adapter);
            } else if (subcategorii.contains("POO")) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (Materii.this,
                                android.R.layout.simple_list_item_single_choice,
                                android.R.id.text1, POO);


                listview.setAdapter(adapter);

            } else if (subcategorii.contains("Machine Learning")) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (Materii.this,
                                android.R.layout.simple_list_item_single_choice,
                                android.R.id.text1, MachineLearning);
                listview.setAdapter(adapter);
            } else {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (Materii.this,
                                android.R.layout.simple_list_item_single_choice,
                                android.R.id.text1, POO);


                listview.setAdapter(adapter);
            }
        }else if (categorii.contains("Fizica")) {
            if (subcategorii.contains("ElectroMagnetism")) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (Materii.this,
                                android.R.layout.simple_list_item_single_choice,
                                android.R.id.text1, ElectroMagnetism);


                listview.setAdapter(adapter);

            } else {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (Materii.this,
                                android.R.layout.simple_list_item_single_choice,
                                android.R.id.text1, Mecanica);


                listview.setAdapter(adapter);
            }
        }

    }




        @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {

                       subcategorii = "";
                       materii = "";
                        Intent intent = new Intent(getApplicationContext(), Subcategorii.class);
                        intent.putExtra("user_tip", tip);
                        intent.putExtra("index", index);
                        intent.putExtra("categorii",categorii);
                        startActivity(intent);

    }
}