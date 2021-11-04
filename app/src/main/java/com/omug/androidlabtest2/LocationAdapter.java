package com.omug.androidlabtest2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.omug.androidlabtest2.R;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.BeanHolder>{
    private List<Location> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnNoteItemClick onNoteItemClick;

    public LocationAdapter(List<Location> list, Context context) {
        layoutInflater = layoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.onNoteItemClick = (OnNoteItemClick) context;
    }

    @Override
    public BeanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //This is how I get the custom cell
        View view = layoutInflater.inflate(R.layout.note_list_item, parent, false);
        return new BeanHolder(view);
    }

    @Override
    public void onBindViewHolder(BeanHolder holder, int position) {
        Log.e("bind", "onBindViewHolder: " + list.get(position));
        holder.textViewName.setText(list.get(position).getTitle());
        holder.textViewAge.setText(String.valueOf(list.get(position).getSubtitle()));
        holder.textViewTuition.setText(String.valueOf(list.get(position).getLatitude()));
        holder.textViewStartDate.setText(String.valueOf(list.get(position).getLongitude()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewName;
        TextView textViewAge;
        TextView textViewTuition;
        TextView textViewStartDate;

        public BeanHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewName = itemView.findViewById(R.id.tv_title);
            textViewAge = itemView.findViewById(R.id.tv_subtitle);
            textViewTuition = itemView.findViewById(R.id.tv_longitude);
            textViewStartDate = itemView.findViewById(R.id.tv_latitude);
        }

        @Override
        public void onClick(View view) {
            onNoteItemClick.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteItemClick {
        void onNoteClick(int pos);
    }
}
