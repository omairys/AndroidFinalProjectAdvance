package com.omug.androidfinalprojectadvance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {
    private ArrayList<String> countryList;
    private OnCountryItemClick onCountryItemClick;

    public CountryAdapter(ArrayList<String> countryList, OnCountryItemClick onCountryItemClick)
    {
        this.countryList = countryList;
        this.onCountryItemClick = onCountryItemClick;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView countryTxt;

        public MyViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            countryTxt = view.findViewById(R.id.tv_title);
        }

        @Override
        public void onClick(View v) {
            onCountryItemClick.onCountryClick(getAdapterPosition());
        }
    }
    @NonNull
    @Override
    public CountryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryAdapter.MyViewHolder holder, int position) {
        String country = countryList.get(position);
        holder.countryTxt.setText(country);
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public interface OnCountryItemClick {
        void onCountryClick(int pos);
    }
}
