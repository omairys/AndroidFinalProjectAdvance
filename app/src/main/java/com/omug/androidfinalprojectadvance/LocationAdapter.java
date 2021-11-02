package com.omug.androidfinalprojectadvance;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.BeanHolder>{
    private List<Location> list;
    private OnLocationItemClick onLocationItemClick;

    public LocationAdapter(List<Location> list, OnLocationItemClick onLocationItemClick) {
        this.list = list;
        this.onLocationItemClick = onLocationItemClick;
    }

    @Override
    public BeanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //This is how I get the custom cell
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list_item, parent, false);
        return new BeanHolder(view);
    }

    @Override
    public void onBindViewHolder(BeanHolder holder, int position) {
        Log.e("bind", "onBindViewHolder: " + list.get(position));
        holder.textViewTitulo.setText(list.get(position).getTitle());
        holder.textViewSubtitulo.setText(list.get(position).getSubtitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewTitulo;
        TextView textViewSubtitulo;


        public BeanHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewTitulo = itemView.findViewById(R.id.tv_gender_item);
            textViewSubtitulo = itemView.findViewById(R.id.tv_subtitulo);
        }

        @Override
        public void onClick(View view) {
            onLocationItemClick.onLocationClick(getAdapterPosition());
        }
    }

    public interface OnLocationItemClick {
        void onLocationClick(int pos);
    }
}