package com.omug.androidlabtest2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.omug.androidlabtest2.R;

import java.lang.ref.WeakReference;

public class AddLocationActivity extends AppCompatActivity {
    private TextView et_title, et_subtitle, et_latitude, et_longitude;
    private LocationDatabase locationDatabase;
    private Location location;
    private boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initUI();
    }

    private void initUI() {
        // Associate variables with view elements
        et_title = findViewById(R.id.inputTitle);
        et_subtitle = findViewById(R.id.inputSubtitle);
        et_latitude = findViewById(R.id.inputLatitude);
        et_longitude = findViewById(R.id.intputLongitude);

        locationDatabase = LocationDatabase.getDatabase(AddLocationActivity.this);
        Button button_save = findViewById(R.id.button_edit);
        Button button_delete = findViewById(R.id.button_delete);
        button_delete.setVisibility(View.INVISIBLE);

        if ((location = (Location) getIntent().getSerializableExtra("location")) != null) {
            //getSupportActionBar().setTitle("Update Location");
            update = true;
            button_save.setText("Update");
            button_delete.setVisibility(View.VISIBLE);
            et_title.setText(location.getTitle());
            et_subtitle.setText(location.getSubtitle());
            et_latitude.setText(Double.toString(location.getLatitude()));
            et_longitude.setText(Double.toString(location.getLongitude()));
        }


        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (update) {
                    location.setTitle(et_title.getText().toString());
                    location.setSubtitle(et_subtitle.getText().toString());
                    location.setLatitude(Double.parseDouble(et_latitude.getText().toString()));
                    location.setLongitude(Double.parseDouble(et_longitude.getText().toString()));
                    locationDatabase.personDao().update(location);
                    setResult(location, 2);
                } else {
                    location = new Location(et_title.getText().toString(), et_subtitle.getText().toString(), Double.parseDouble(et_latitude.getText().toString()), Double.parseDouble(et_longitude.getText().toString()));
                    new InsertTask(AddLocationActivity.this, location).execute();
                }
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (update) {
                    locationDatabase.personDao().delete(location);
                    setResult(location, 3);
                }
            }
        });
    }

    private void setResult(Location location, int flag) {
        setResult(flag, new Intent().putExtra("location", location));
        finish();
    }


    private static class InsertTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<AddLocationActivity> activityReference;
        private Location location;

        // only retain a weak reference to the activity
        InsertTask(AddLocationActivity context, Location location) {
            activityReference = new WeakReference<>(context);
            this.location = location;
        }

        // doInBackground methods runs on a worker thread
        @Override
        protected Boolean doInBackground(Void... objs) {
            // retrieve auto incremented person id
            long j = activityReference.get().locationDatabase.personDao().insertPerson(location);
            location.setId(j);
            Log.e("ID ", "doInBackground: " + j);
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool) {
                activityReference.get().setResult(location, 1);
                activityReference.get().finish();
            }
        }
    }
}
