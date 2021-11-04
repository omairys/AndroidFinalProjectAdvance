package com.omug.androidlabtest2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {
    private List<Location> listLocations;
    private GoogleMap mMap = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bundleListObjects();//  obteniendo la data para el mapa

        View view = inflater.inflate(R.layout.fragment_map, container, false); //inicializar vista

        SupportMapFragment supportMapFragment = (SupportMapFragment) //inicializar fragmento del mapa
                getChildFragmentManager().findFragmentById(R.id.google_map);

        //sincronizar el mapa
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) { //cuando el mapa este cargado
                mMap = googleMap;
                addMarkersToMap();
                //mMap.setOnMarkerClickListener(this);
            }
        });
        return view;
    }

    private void bundleListObjects() {
        Bundle listLocationsArgs = getArguments();
        listLocations = (List<Location>) listLocationsArgs.getSerializable("locations");
    }

    private void addMarkersToMap() {
        for (Location location : listLocations) {
            mMap.addMarker(new MarkerOptions()
                    .position(location.getLocation())
                    .title(location.getTitle())
                    .snippet(location.getSubtitle())
            );
        }
    }
}