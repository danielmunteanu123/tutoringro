package com.example.tutoringro;

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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Admin_Cautare_Profesori  extends AppCompatActivity implements
        SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private RecyclerView rv;
    public ProgressBar mProgressBar;
    private LinearLayoutManager layoutManager;
    Admin_AdapterProfesori adapter;
    public TextView empty, headerText;
    ImageView info;


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
        adapter = new Admin_AdapterProfesori(Utils.Profesori);
        int count = adapter.getItemCount();
        System.out.println("---------------"+count);
        rv.setAdapter(adapter);


    }

    private void bindData() {
        String query = "";
        Utils.search(this, Utils.getDatabaseRefence(), mProgressBar, adapter, query);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Populeaza meniul cu optiuni din res/menu/cautare_menu.xml file.
        // Adauga optiuni
        getMenuInflater().inflate(R.menu.cautare_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(true);
        searchView.setQueryHint("Search");
        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Utils.searchString = query;
        Utils.search(this, Utils.getDatabaseRefence(), mProgressBar, adapter, query);
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cautare_profesori);

        initializeViews();
        bindData();


    }
}


