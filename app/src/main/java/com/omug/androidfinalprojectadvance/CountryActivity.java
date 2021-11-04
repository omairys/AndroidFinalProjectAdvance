package com.omug.androidfinalprojectadvance;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;



public class CountryActivity extends AppCompatActivity implements CountryAdapter.OnCountryItemClick {
    private RecyclerView recyclerView;
    private ArrayList<String> countryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_list);
        recyclerView = findViewById(R.id.country_recycler_view);
        countryList = new ArrayList<>();

        setCountryList();
        setAdapter();
    }

    private void setAdapter() {
        CountryAdapter adapter = new CountryAdapter(countryList, CountryActivity.this);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setCountryList() {
        countryList.add("Venezuela");
        countryList.add("Canada");
        countryList.add("Argentina");
        countryList.add("Colombia");
        countryList.add("Chile");
        countryList.add("Panama");
    }

    private void setResult(String country, int flag) {
        Log.e("result ", "setResult: CountryActivity " + flag +" "+ country);
        setResult(flag, new Intent().putExtra("country", country));
        finish();
    }

    @Override
    public void onCountryClick(int pos) {
        setResult(countryList.get(pos), 11);
    }
}