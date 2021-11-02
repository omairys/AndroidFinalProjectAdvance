package com.omug.androidfinalprojectadvance.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.omug.androidfinalprojectadvance.AddLocationActivity;
import com.omug.androidfinalprojectadvance.Location;
import com.omug.androidfinalprojectadvance.LocationAdapter;
import com.omug.androidfinalprojectadvance.LocationDatabase;
import com.omug.androidfinalprojectadvance.R;
import com.omug.androidfinalprojectadvance.ui.map.MapFragment;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements LocationAdapter.OnLocationItemClick {
    private LocationDatabase locationDatabase;
    private RecyclerView recyclerView;
    private List<Location> locations;
    private LocationAdapter locationAdapter;
    private int pos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list, container, false);
        setHasOptionsMenu(true);
        displayLocationList();

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        locations = new ArrayList<>();
        locationAdapter = new LocationAdapter(locations, ListFragment.this);
        recyclerView.setAdapter(locationAdapter);

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.searchView_MenuMain);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here!");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new ListFragment.RetrieveOneTask(searchView, query).execute();
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                new ListFragment.RetrieveOneTask(searchView, newText).execute();
                return false;
            }
        });
    }

    private void displayLocationList() {
        locationDatabase = LocationDatabase.getDatabase(getActivity());
        new ListFragment.RetrieveTask().execute();
    }

    //este metodo carga la informacion de las personas en la lista
    private class RetrieveTask extends AsyncTask<Void, Void, List<Location>> {
        @Override
        protected List<Location> doInBackground(Void... voids) {
            List<Location> locationList = locationDatabase.personDao().getAll();
            return locationList;
        }

        @Override
        protected void onPostExecute(List<Location> locationList) {
            if (locationList != null && locationList.size() > 0) {
                locations.clear();
                locations.addAll(locationList);
                locationAdapter.notifyDataSetChanged();
            }
        }
    }

    private class RetrieveOneTask extends AsyncTask<Void, Void, List<Location>> {
        private SearchView searchView;
        private String strQuery;


        RetrieveOneTask(SearchView searchView, String strQuery) {
            this.searchView = searchView;
            this.strQuery = strQuery;
        }

        @Override
        protected List<Location> doInBackground(Void... voids) {
            locationDatabase = LocationDatabase.getDatabase(getActivity());
            List<Location> locationList;
            if(strQuery.isEmpty()){
                locationList = locationDatabase.personDao().getAll();
            }else{
                locationList = locationDatabase.personDao().getOne(strQuery);
            }
            return locationList;
        }

        @Override
        protected void onPostExecute(List<Location> locationList) {
            if (locationList != null && locationList.size() > 0) {
                locations.clear();
                locations.addAll(locationList);
                locationAdapter.notifyDataSetChanged();
            }else{
                locations.clear();
                Toast toast = Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public void onLocationClick(int pos) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Select Options")
                .setItems(new String[]{"Delete", "Update"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                locationDatabase.personDao().delete(locations.get(pos));
                                locations.remove(pos);
                                locationAdapter.notifyDataSetChanged();
                                break;
                            case 1:
                                ListFragment.this.pos = pos;
                                startActivityForResult(
                                        new Intent(getActivity(),
                                                AddLocationActivity.class).putExtra("location", locations.get(pos)), 100);
                                break;
                        }
                    }
                }).show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("resultCode ", "onActivityResult: ListFragment " + resultCode +" "+ requestCode);

        if (requestCode == 100 && resultCode > 0) {
            if (resultCode == 1) {
                locations.add((Location) data.getSerializableExtra("location"));
            } else if (resultCode == 2) {
                locations.set(pos, (Location) data.getSerializableExtra("location"));
            }
            locationAdapter.notifyDataSetChanged();
        }
    }
}