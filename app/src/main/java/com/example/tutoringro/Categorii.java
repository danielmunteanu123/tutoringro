package com.example.tutoringro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import android.util.SparseBooleanArray;

import android.widget.Toast;

public class Categorii extends AppCompatActivity {
    ListView listview ;
    Button next;
    String categorii = "" ;


    String[] ListViewItems = new String[] {
            "Matematica",
            "Informatica",
            "Economie",
            "Fizica",
    };

    SparseBooleanArray sparseBooleanArray ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_categorii);

        next = findViewById(R.id.btnInainte);
        listview = (ListView)findViewById(R.id.listView);

        Intent intent = getIntent();
        String tip = intent.getStringExtra("user_tip");
        String index = intent.getStringExtra("index");
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (Categorii.this,
                        android.R.layout.simple_list_item_multiple_choice,
                        android.R.id.text1, ListViewItems );

        listview.setAdapter(adapter);






        next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sparseBooleanArray = listview.getCheckedItemPositions();
                    int i = 0;
                    while (i < sparseBooleanArray.size()) {

                        if (sparseBooleanArray.valueAt(i) && (!categorii.contains(ListViewItems[sparseBooleanArray.keyAt(i)]))) {
                            if (i == 0) {
                                categorii += ListViewItems[sparseBooleanArray.keyAt(i)];
                            } else {
                                categorii += "," + ListViewItems[sparseBooleanArray.keyAt(i)];
                            }

                        }
                        i++;
                    }


                    if (listview.getCheckedItemCount() < 1) {
                        Toast.makeText(Categorii.this, "Alegeti cel putin o categorie!", Toast.LENGTH_SHORT).show();

                    } else {

                        if(tip.equals("profesor")) {
                            final ManagerProfesori profesor = ManagerProfesori.getProfesor();
                            profesor.setCategorii(categorii);

                        }else
                        {
                            final ManagerStudenti student = ManagerStudenti.getStudent();
                            student.setCategorii(categorii);
                        }


                        Intent intent = new Intent(getApplicationContext(), Subcategorii.class);
                        intent.putExtra("user_tip", tip);
                        intent.putExtra("categorii", categorii);
                        intent.putExtra("index", index);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        System.out.println(categorii);

                    }
                }
            });




    }

    public void onBackPressed() {
        categorii = "";



    }
}
