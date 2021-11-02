package com.omug.androidfinalprojectadvance.ui.map;

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

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.omug.androidfinalprojectadvance.AddLocationActivity;
import com.omug.androidfinalprojectadvance.Location;
import com.omug.androidfinalprojectadvance.LocationDatabase;
import com.omug.androidfinalprojectadvance.MainActivity;
import com.omug.androidfinalprojectadvance.R;
import com.omug.androidfinalprojectadvance.ui.home.ListFragment;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback,  GoogleMap.OnMarkerClickListener {
    private LocationDatabase locationDatabase;
    private List<Location> locations;
    private GoogleMap mMap;
    private int pos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        setHasOptionsMenu(true);
        locations = new ArrayList<>();
        displayLocationList();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.map, fragment);
        transaction.commit();

        fragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.searchView_MenuMain);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here2!");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new MapFragment.RetrieveOneTask(searchView, query).execute();
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                new MapFragment.RetrieveOneTask(searchView, newText).execute();
                return false;
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for (Location location : locations) {

            Marker currentMark = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title(location.getTitle())
                    .snippet(location.getSubtitle())
            );
            Log.w("Click", "is "+marker.getTitle().equalsIgnoreCase(currentMark.getTitle()));
            if(marker.getTitle().equalsIgnoreCase(currentMark.getTitle())){

                startActivityForResult(
                        new Intent(getContext(), AddLocationActivity.class).putExtra("location", location), 100);
                return true;
            }
        }
        return false;
    }

    private void displayLocationList() {
        new RetrieveTask().execute();
    }

    private class RetrieveTask extends AsyncTask<Void, Void, List<Location>> {
        @Override
        protected List<Location> doInBackground(Void... voids) {
            locationDatabase = LocationDatabase.getDatabase(getActivity());
            List<Location> locationList = locationDatabase.personDao().getAll();
            return locationList;
        }

        @Override
        protected void onPostExecute(List<Location> locationList) {
            if (locationList != null && locationList.size() > 0) {
                locations.clear();
                locations.addAll(locationList);
                onResetMap();
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
                onResetMap();
            }else{
                clearMap();
                Toast toast = Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addMarkersToMap();
        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
    }

    public void onResetMap() {
        // Clear the map because we don't want duplicates of the markers.
        if (mMap != null){
            clearMap();
            addMarkersToMap();
        }
    }

    public void clearMap() {
        // Clear the map because we don't want duplicates of the markers.
        if (mMap != null){
            mMap.clear();
        }
    }

    private void addMarkersToMap() {
        Log.w("locations ", "locations " + locations.size() );
        if (locations != null && locations.size() > 0) {
            for (Location location : locations) {
                Log.w("locations ", "locations " + location.toString());
                     mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .title(location.getTitle())
                    .snippet(location.getSubtitle())
                );
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.w("resultCode ", "onActivityResult: MapFragment " + resultCode +" "+ requestCode);
        if (requestCode == 100 && resultCode > 0) {
            if (resultCode == 1) {
                locations.add((Location) data.getSerializableExtra("location"));
            } else if (resultCode == 2) {
                locations.set(pos, (Location) data.getSerializableExtra("location"));
            } else if (resultCode == 3) {
                locations.remove(pos);
            }
            onResetMap();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroy() {
        locationDatabase.cleanUp();
        super.onDestroy();
    }
}



