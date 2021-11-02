package com.omug.androidfinalprojectadvance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class GenderActivity extends AppCompatActivity implements GenderAdapter.OnGenderItemClick{
    private RecyclerView recyclerView;
    private ArrayList<String> genderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gender_list);
        recyclerView = findViewById(R.id.gender_recycler_view);
        genderList = new ArrayList<>();

        setCountryList();
        setAdapter();
    }

    private void setAdapter() {
        GenderAdapter adapter = new GenderAdapter(genderList, GenderActivity.this);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setCountryList() {
        genderList.add("Female");
        genderList.add("Male");
    }

    private void setResult(String gender, int flag) {
        Log.e("result ", "setResult: GenderActivity " + flag +" "+ gender);
        setResult(flag, new Intent().putExtra("gender", gender));
        finish();
    }

    @Override
    public void onGenderClick(int pos) { setResult(genderList.get(pos), 2);}
}