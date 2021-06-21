package com.example.weatherservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class dbActivity extends AppCompatActivity {

    private DBSetupHelper dbSetupHelper;
    private RecyclerView recyclerView;
    private ValueAdapter valueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        // setub the db
        dbSetupHelper = new DBSetupHelper(dbActivity.this);

        ArrayList<Weatherbean> dbValues = new ArrayList<Weatherbean>();
        dbValues = dbSetupHelper.getValues(dbValues);

        recyclerView = findViewById(R.id.showDbValues);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        valueAdapter = new ValueAdapter(dbValues);

        recyclerView.setAdapter(valueAdapter);
    }
    public void gotoView(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void deleteEverything(View view) {
        dbSetupHelper.deleteEverythingFromTable();
    }
}