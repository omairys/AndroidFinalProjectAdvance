package com.omug.androidfinalprojectadvance;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Location> list;
    private OnLocationItemClick onLocationItemClick;

    final int VIEW_RESULTS = 1;
    final int VIEW_NO_RESULTS = 2;

    public LocationAdapter(List<Location> list, OnLocationItemClick onLocationItemClick) {
        this.list = list;
        this.onLocationItemClick = onLocationItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //This is how I get the custom cell
        View view = null;
        Log.e("viewType", "onCreateViewHolder: " + viewType);
        switch (viewType)
        {
            case VIEW_RESULTS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list_item, parent, false);
                return new ResultsBeanHolder(view);

            case VIEW_NO_RESULTS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_list_noitem, parent, false);
                return new NoResultBeanHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final int itemType = getItemViewType(position);
        Log.e("onBindViewHolder", "itemType " + itemType);

        if(itemType == VIEW_RESULTS){
            if (holder instanceof ResultsBeanHolder) {
                ((ResultsBeanHolder) holder).bindData(list.get(position));
            }
        }
        if(itemType == VIEW_NO_RESULTS){
            if (holder instanceof NoResultBeanHolder) {
                ((NoResultBeanHolder) holder).bindData();
            }
        }
    }


    @Override
    public int getItemCount() {
        Log.e("getItemCount", "list.size() " + list.size());
        return (list == null || list.isEmpty()) ? 1 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.size() == 0)
            return VIEW_NO_RESULTS;
        else
            return VIEW_RESULTS;
    }

    //custom cell 1
    public class ResultsBeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewTitulo, textViewSubtitulo, textViewGender, textViewBday;
        ImageView imageView;

        public ResultsBeanHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewTitulo = itemView.findViewById(R.id.tv_title);
            textViewSubtitulo = itemView.findViewById(R.id.tv_subtitulo);
            imageView = itemView.findViewById(R.id.imageViewUser);
            textViewGender = itemView.findViewById(R.id.tv_gender);
            textViewBday = itemView.findViewById(R.id.tv_birtday);

        }

        public void bindData(Location list) {
            textViewTitulo.setText(list.getTitle());
            textViewSubtitulo.setText(list.getSubtitle());
            imageView.setImageBitmap(Converters.StringToBitMap(list.getImage()));
            textViewGender.setText(list.getGender());
            textViewBday.setText(list.getBirtdate().toString());


        }

        @Override
        public void onClick(View view) {
            onLocationItemClick.onLocationClick(getAdapterPosition());
        }
    }

    //custom cell 2
    public class NoResultBeanHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;

        public NoResultBeanHolder(View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.tv_noItem);
        }
        public void bindData() {
            textViewMessage.setText("No Data");

        }
    }

    public interface OnLocationItemClick {
        void onLocationClick(int pos);
    }
}