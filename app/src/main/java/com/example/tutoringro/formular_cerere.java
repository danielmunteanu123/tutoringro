package com.example.tutoringro;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class formular_cerere extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    TextInputLayout locInput;
    Spinner spinner,spinner_judete;
    static TextInputEditText oraInput, dataInput;
    Button cautare_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_formular_cerere);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.searchFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.homeFragment){
                    startActivity(new Intent(getApplicationContext(), Fereastra_Student.class));
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

        spinner_judete = findViewById(R.id.spinner_judete);
        spinner = findViewById(R.id.spinner);
        dataInput = findViewById(R.id.data_EditText);
        oraInput = findViewById(R.id.ora_EditText);
        locInput = findViewById(R.id.loc_TextField);
        cautare_btn = findViewById(R.id.cauta_btn);

        String[] judete = getResources().getStringArray(R.array.judete);
        String[] materii = getResources().getStringArray(R.array.materii);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,materii);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> arrayadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,judete);
        arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_judete.setAdapter(arrayadapter);
        spinner_judete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_judete.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        oraInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment  timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");

            }
        });

        dataInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

    cautare_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cautareProfesori();
        }
    });

    }


    public void cautareProfesori(){
        String judet = spinner_judete.getSelectedItem().toString();
        String materie = spinner.getSelectedItem().toString();
        String loc = locInput.getEditText().getText().toString();

       if (!validateData() | (!validateLoc()) | (!validateOra())){

            return;
        }
        else{

            final ManagerCereri cerere = ManagerCereri.getCerere();
        cerere.setMaterie(materie);
        cerere.setLoc(loc);
        cerere.setJudet(judet);
        cerere.setUsername_student(ManagerStudenti.getStudent().getUsername());
        startActivity(new Intent(getApplicationContext(), CautareProfesori.class));



    }}

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextInputEditText oraInput = findViewById(R.id.ora_EditText);
        oraInput.setText(hourOfDay+ ":" + minute);
        String ora = oraInput.getText().toString();
        final ManagerCereri cerere = ManagerCereri.getCerere();
        cerere.setOra(ora);

    }

    private void showDatePickerDialog()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH
                        ));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 86400000 );
                datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month +=1;

        dataInput.setText(+dayOfMonth+"/"+month+"/"+year);
        String data = dataInput.getText().toString();

        LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());
        int an = currentDate.getYear();
        int luna = currentDate.getMonth().getValue();
        int zi = currentDate.getDayOfMonth();
        System.out.println(currentDate);
        System.out.println(an+"/"+luna+"/"+zi);
        if(year<an)
        {
            Toast.makeText(formular_cerere.this, "Data eronata!", Toast.LENGTH_SHORT).show();
        }
        if(year == an && (month+1<luna))
        {
            Toast.makeText(formular_cerere.this, "Data eronata!", Toast.LENGTH_SHORT).show();
        }
        if(year == an && month == luna && dayOfMonth < zi){
            Toast.makeText(formular_cerere.this, "Data eronata!", Toast.LENGTH_SHORT).show();

        }

        else
        {
            final ManagerCereri cerere = ManagerCereri.getCerere();
            cerere.setData(data);
        }

        }

    private Boolean validateLoc() {
        String val = locInput.getEditText().getText().toString();
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_ ]*$");
        Matcher matcher = pattern.matcher(val);

        if (!matcher.matches()) {
            locInput.setError("Adresa nu poate conține caractere speciale.");
            return false;
        }
        if (val.isEmpty()) {
            locInput.setError("Completati spatiile libere");
            return false;
        }
        else if(val.length() < 7)
        {
            locInput.setError("Adresa prea mica.");
            return false;
        }
        else {
            locInput.setError(null);
            locInput.setErrorEnabled(false);
            return true;
        }
    }


    private Boolean validateData() {
        String val = dataInput.getText().toString();

        if (val.isEmpty()) {
            dataInput.setError("Completati spatiile libere");
            return false;
        }
        else {
            dataInput.setError(null);
            return true;
        }
    }
    private Boolean validateOra() {
        String val = oraInput.getText().toString();

        if (val.isEmpty()) {
            oraInput.setError("Completati spatiile libere");
            return false;
        }
        else {
            oraInput.setError(null);
            return true;
        }
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Sunteți sigur ca vreți să plecați?")
                .setCancelable(false)
                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        formular_cerere.super.onBackPressed();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                })
                .setNegativeButton("Nu", null)
                .show();
    }
    }

