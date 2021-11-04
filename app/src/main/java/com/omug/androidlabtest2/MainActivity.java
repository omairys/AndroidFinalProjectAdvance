package com.omug.androidlabtest2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationAdapter.OnNoteItemClick{
    private Toolbar mToolbar;
    private LocationDatabase locationDatabase;
    private List<Location> locations;
    private Fragment fragment = new MapFragment();//inicializar fragmento
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVies();
        initializeMapsFragment();
        displayLocationList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void displayLocationList() {
        locationDatabase = LocationDatabase.getDatabase(MainActivity.this);
        new RetrieveTask(this).execute();
    }
    //este metodo carga la informacion de las localizaciones en la lista
    private static class RetrieveTask extends AsyncTask<Void, Void, List<Location>> {
        private WeakReference<MainActivity> activityReference;
        // only retain a weak reference to the activity
        RetrieveTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected List<Location> doInBackground(Void... voids) {
            if (activityReference.get() != null)
                return activityReference.get().locationDatabase.personDao().getAll();
            else
                return null;
        }

        @Override
        protected void onPostExecute(List<Location> locations) {
            if (locations != null && locations.size() > 0) {
                activityReference.get().locations.clear();
                activityReference.get().locations.addAll(locations);

                //activityReference.get().locationAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onNoteClick(int pos) {
        MainActivity.this.pos = pos;
        startActivityForResult(
                new Intent(MainActivity.this,
                        AddLocationActivity.class).putExtra("location", locations.get(pos)), 100);
    }

    private void initializeMapsFragment() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("locations", (Serializable) locations);
        fragment.setArguments(bundle);
        //abrir fragmento
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }

    private void initializeVies() {
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(listener);
        locations = new ArrayList<>();
    }

    private Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.menu_add:
                    startActivityForResult(new Intent(MainActivity.this, AddLocationActivity.class), 100);
                    break;
                default:
            }
            return false;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode > 0) {
            if (resultCode == 1) {
                locations.add((Location) data.getSerializableExtra("location"));
            } else if (resultCode == 2) {
                locations.set(pos, (Location) data.getSerializableExtra("location"));
            } else if (resultCode == 3) {
            locations.remove(pos);
            }
            //locationAdapter.notifyDataSetChanged();
        }
    }
}