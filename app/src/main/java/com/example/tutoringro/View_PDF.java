package com.example.tutoringro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class View_PDF extends AppCompatActivity {
   static TextView empty;
   ListView myPDFListView;
    static List<Manager_PDF> uploadPDFs;
    DatabaseReference databaseReference;
    static ImageView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_view__pdf);

        empty = findViewById(R.id.empty);
        myPDFListView = findViewById(R.id.myListView);
        info = findViewById(R.id.info_icon);
        info.setVisibility(View.GONE);
        uploadPDFs = new ArrayList<>();

        viewAllFiles();



        myPDFListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Manager_PDF uploadPDF = uploadPDFs.get(position);

                Intent intent = new Intent();
                intent.setData(Uri.parse(uploadPDF.getUrl()));
                startActivity(intent);
            }
        });
    }

    private void viewAllFiles() {
        String username;
        Intent intent = getIntent();
        String tip = intent.getStringExtra("activity");

        if(tip.equals("student"))
        {
            username = intent.getStringExtra("username_prof");
        }
        else
        {
            ManagerProfesori profesor = ManagerProfesori.getProfesor();
            username = profesor.getUsername();
        }



        databaseReference = FirebaseDatabase.getInstance().getReference("certificate");
        Query checkUser = databaseReference.orderByChild("username").equalTo(username);
        uploadPDFs.clear();
        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Manager_PDF uploadPDF = postSnapshot.getValue(Manager_PDF.class);
                        uploadPDFs.add(uploadPDF);

                    }

                    String[] uploads = new String[uploadPDFs.size()];
                    for (int i = 0; i < uploads.length; i++) {
                        uploads[i] = uploadPDFs.get(i).getName();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads);
                        myPDFListView.setAdapter(adapter);


                    }

                    if(uploadPDFs.size() < 1)
                    {
                        info.setVisibility(View.VISIBLE);
                        empty.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
