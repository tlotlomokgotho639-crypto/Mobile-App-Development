package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "smart_pantry_prefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Spinner unitSpinner = findViewById(R.id.unitPreferenceSpinner);
        Switch alertSwitch = findViewById(R.id.expiringSoonSwitch);

        String[] units = {"items", "cups", "grams", "ml", "tbsp", "whole"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedUnit = prefs.getString("unit_preference", "items");
        boolean alertEnabled = prefs.getBoolean("expiring_alerts", true);

        int position = 0;
        for (int i = 0; i < units.length; i++) {
            if (units[i].equalsIgnoreCase(savedUnit)) {
                position = i;
                break;
            }
        }
        unitSpinner.setSelection(position);
        alertSwitch.setChecked(alertEnabled);

        unitSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                prefs.edit().putString("unit_preference", units[position]).apply();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        alertSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean("expiring_alerts", isChecked).apply());
    }
}
