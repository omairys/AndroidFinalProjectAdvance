package com.omug.androidfinalprojectadvance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GenderAdapter extends RecyclerView.Adapter<GenderAdapter.MyViewHolder> {
    private ArrayList<String> genderList;
    private OnGenderItemClick onGenderItemClick;

    public GenderAdapter(ArrayList<String> genderList, OnGenderItemClick onGenderItemClick) {
        this.genderList = genderList;
        this.onGenderItemClick = onGenderItemClick;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView genderTxt;

        public MyViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            genderTxt = view.findViewById(R.id.tv_title);
        }

        @Override
        public void onClick(View v) {
            onGenderItemClick.onGenderClick(getAdapterPosition());
        }
    }
    @NonNull
    @Override
    public GenderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gender_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GenderAdapter.MyViewHolder holder, int position) {
        String country = genderList.get(position);
        holder.genderTxt.setText(country);

    }

    @Override
    public int getItemCount() {
        return genderList.size();
    }

    public interface OnGenderItemClick {
        void onGenderClick(int pos);
    }
}
