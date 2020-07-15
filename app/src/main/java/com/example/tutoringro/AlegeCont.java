package com.example.tutoringro;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class AlegeCont extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alege_cont);
    }

    public void btnStudent(View view) {
        String tip = "student";
        Intent intent = new Intent(getApplicationContext(), formular_inregistrare.class);
        intent.putExtra("tip",tip);
        startActivity(intent);

    }

    public void btnProfesor(View view){
        String tip = "profesor";
        Intent intent = new Intent(getApplicationContext(), formular_inregistrare.class);
        intent.putExtra("tip",tip);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
