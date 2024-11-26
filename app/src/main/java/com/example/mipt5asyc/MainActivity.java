package com.example.mipt5asyc;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private EditText filterInput;
    private CurrencyAdapter adapter;
    private List<Currency> currencyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "onCreate called");

        listView = findViewById(R.id.currency_list);
        filterInput = findViewById(R.id.filter_input);

        Log.d("MainActivity", "Initializing DataLoader");

        // Load data using the new DataLoader
        DataLoader dataLoader = new DataLoader(data -> {
            Log.d("MainActivity", "DataLoader callback called. Data size: " + data.size());
            currencyList = data;

            runOnUiThread(() -> {
                adapter = new CurrencyAdapter(MainActivity.this, currencyList);
                listView.setAdapter(adapter);
                Log.d("MainActivity", "Adapter set with data. ListView updated.");
            });
        });

        dataLoader.fetchData("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");

        // Add filter functionality
        filterInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    Log.d("MainActivity", "Filter applied: " + s.toString());
                    adapter.filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
