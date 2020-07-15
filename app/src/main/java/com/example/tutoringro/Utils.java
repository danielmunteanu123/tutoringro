package com.example.tutoringro;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


public class Utils {


    public static ArrayList<ManagerProfesori> Profesori =new ArrayList<>();
    public static ArrayList<ManagerStudenti> Studenti =new ArrayList<>();
    public static final String EMAIL = "tutoringro@gmail.com";
    public static final String PASSWORD = "bggigvsgbszqipuk";
    public static ArrayList<ManagerProfesori> SedinteStudenti  =new ArrayList<>();
    public static String searchString = "";
    public static final String UNICODE_FORMAT = "UTF-8";

    public static void show(Context c,String message){
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }

    public static void openActivity(Context c,Class clazz){
        Intent intent = new Intent(c, clazz);
        c.startActivity(intent);
    }

    //Trimitem obiectul serializat catre activity specificata de noi

    public static void sendProfesorToActivity(Context c, ManagerProfesori profesor,
                                               Class clazz){
        Intent i=new Intent(c,clazz);
        i.putExtra("PROFESOR_KEY",profesor);
        c.startActivity(i);
    }

    public static void sendStudentToActivity(Context c, ManagerStudenti student,
                                              Class clazz){
        Intent i=new Intent(c,clazz);
        i.putExtra("STUDENT_KEY",(Serializable) student);
        c.startActivity(i);
    }


    // Primim obiect obiectul serializat, il deserializam si il returnam

    public  static ManagerProfesori receiveProfesor(Intent intent, Context c){
        try {
            return (ManagerProfesori) intent.getSerializableExtra("PROFESOR_KEY");
        }catch (Exception e){
            e.printStackTrace();
            show(c,"RECEIVING-PROFESOR ERROR: "+e.getMessage());
        }
        return null;
    }


    public  static ManagerStudenti receiveStudent(Intent intent, Context c){
        try {
            return (ManagerStudenti) intent.getSerializableExtra("STUDENT_KEY");
        }catch (Exception e){
            e.printStackTrace();
            show(c,"RECEIVING-STUDENT ERROR: "+e.getMessage());
        }
        return null;
    }

    public static void showProgressBar(ProgressBar pb){
        pb.setVisibility(View.VISIBLE);
    }
    public static void hideProgressBar(ProgressBar pb){
        pb.setVisibility(View.GONE);
    }

    public static DatabaseReference getDatabaseRefence() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static void search(final AppCompatActivity a, DatabaseReference db,
                              final ProgressBar pb,
                              Admin_AdapterProfesori adapter, String searchTerm) {
        if(searchTerm != null && searchTerm.length()>0){
            char[] letters=searchTerm.toCharArray();
            String firstLetter = String.valueOf(letters[0]).toUpperCase();
            String remainingLetters = searchTerm.substring(1);
            searchTerm=firstLetter+remainingLetters;
        }

        Utils.showProgressBar(pb);

        Query firebaseSearchQuery = db.child("Users").child("Profesori").orderByChild("nume").startAt(searchTerm)
                .endAt(searchTerm + "\uf8ff");


        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profesori.clear();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //get Obiectele Profesorilor si populam Arraylist-ul
                        ManagerProfesori profesor = ds.getValue(ManagerProfesori.class);
                        profesor.setKey(ds.getKey());
                        Profesori.add(profesor);

                    }
                    adapter.notifyDataSetChanged();
                }else {
                    Utils.show(a,"N-am gasit nici-un profesor.");
                }
                Utils.hideProgressBar(pb);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FIREBASE CRUD", databaseError.getMessage());
                Utils.hideProgressBar(pb);
                Utils.show(a,databaseError.getMessage());
            }
        });
    }

    public static void search_studenti(final AppCompatActivity a, DatabaseReference db,
                              final ProgressBar pb,
                              Admin_AdapterStudenti adapter, String searchTerm) {
        if(searchTerm != null && searchTerm.length()>0){
            char[] letters=searchTerm.toCharArray();
            String firstLetter = String.valueOf(letters[0]).toUpperCase();
            String remainingLetters = searchTerm.substring(1);
            searchTerm=firstLetter+remainingLetters;
        }

        Utils.showProgressBar(pb);

        Query firebaseSearchQuery = db.child("Users").child("Studenti").orderByChild("nume").startAt(searchTerm)
                .endAt(searchTerm + "\uf8ff");


        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Studenti.clear();
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //get Obiectele Profesorilor si populam Arraylist-ul
                        ManagerStudenti student = ds.getValue(ManagerStudenti.class);
                        student.setKey(ds.getKey());
                        Studenti.add(student);

                    }
                    adapter.notifyDataSetChanged();
                }else {
                    Utils.show(a,"N-am gasit nici-un student.");
                }
                Utils.hideProgressBar(pb);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FIREBASE CRUD", databaseError.getMessage());
                Utils.hideProgressBar(pb);
                Utils.show(a,databaseError.getMessage());
            }
        });
    }
    public static String cipher(String parola) {
        String word = "";
        for (int i = 0; i < parola.length(); i++) {
            char code = Character.toLowerCase(parola.charAt(i));
            switch (code) {
                case 'e':
                    word += "!";
                    break;
                case 't':
                    word += "@";
                    break;
                case 'a':
                    word += "#";
                    break;
                case 'o':
                    word += "$";
                    break;
                case 'i':
                    word += "%";
                    break;
                case 'n':
                    word += "*";
                    break;
                case 's':
                    word += "(";
                    break;
                case 'r':
                    word += ")";
                    break;
                case 'p':
                    word += "-";
                    break;

                case 'b':
                    word += "=";
                    break;
                case 'c':
                    word += "+";
                    break;
                case 'd':
                    word += "^";
                    break;
                case 'f':
                    word += "~";
                    break;
                case 'g':
                    word += "`";
                    break;
                case 'h':
                    word += "”";
                    break;
                case 'm':
                    word += "„";
                    break;
                case 'j':
                    word += "|";
                    break;
                case 'k':
                    word += "/";
                    break;
                default:
                    word += parola.charAt(i);
                    break;
            }
        }

        return word;

    }

    public static String deCipher(String parola) {
        String word = "";
        for (int i = 0; i < parola.length(); i++) {
            char code = Character.toLowerCase(parola.charAt(i));
            switch (code) {

                    case '!':
                        word += "e";
                        break;
                    case '@':
                        word += "t";
                        break;
                    case '#':
                        word += "a";
                        break;
                    case '$':
                        word += "o";
                        break;
                    case '%':
                        word += "i";
                        break;
                    case '*':
                        word += "n";
                        break;
                    case '(':
                        word += "s";
                        break;
                    case ')':
                        word += "r";
                        break;
                    case '-':
                        word += "p";
                        break;

                    case '=':
                        word += "b";
                        break;
                    case '+':
                        word += "c";
                        break;
                    case '^':
                        word += "d";
                        break;
                    case '~':
                        word += "f";
                        break;
                    case '`':
                        word += "g";
                        break;
                    case '”':
                        word += "h";
                        break;
                    case '„':
                        word += "m";
                        break;
                    case '|':
                        word += "j";
                        break;
                    case '/':
                        word += "k";
                        break;

                    default:
                        word += parola.charAt(i);
                        break;
            }
        }

        return word;

    }



}
//end
