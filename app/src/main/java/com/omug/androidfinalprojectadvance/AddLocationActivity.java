package com.omug.androidfinalprojectadvance;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.sql.Date;
import java.util.Calendar;

public class AddLocationActivity extends AppCompatActivity {
    private EditText et_title, et_latitude, et_longitude;
    private TextView tv_country, tv_birtdate, tv_gender;
    private ImageView iv_image;
    private Bitmap bitmap;
    DatePickerDialog.OnDateSetListener setListener;
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
        tv_country = findViewById(R.id.inputCountry);
        et_latitude = findViewById(R.id.inputLatitude);
        et_longitude = findViewById(R.id.intputLongitude);
        tv_gender = findViewById(R.id.intputGender);
        tv_birtdate = findViewById(R.id.inputBdate);
        iv_image = findViewById(R.id.userPhotoSelected);
        Button button_choseImage = findViewById(R.id.btn_choseImage);
        Button button_save = findViewById(R.id.button_edit);
        Button button_delete = findViewById(R.id.button_delete);
        button_delete.setVisibility(View.INVISIBLE);


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        //pickBirtdate
        tv_birtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddLocationActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = year+"-"+month+"-"+day;
                tv_birtdate.setText(date);
            }
        };

        //Pick Country
        tv_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(AddLocationActivity.this,
                                CountryActivity.class), 100);
            }
        });

        //Pick Country
        tv_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(AddLocationActivity.this,
                                GenderActivity.class), 100);
            }
        });

        button_choseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImagesFromGallery();
            }
        });

        locationDatabase = LocationDatabase.getDatabase(AddLocationActivity.this);
        if ((location = (Location) getIntent().getSerializableExtra("location")) != null) {
            //getSupportActionBar().setTitle("Update Location");
            update = true;
            button_save.setText("Update");
            button_delete.setVisibility(View.VISIBLE);
            et_title.setText(location.getTitle());
            tv_country.setText(location.getSubtitle());
            et_latitude.setText(Double.toString(location.getLatitude()));
            et_longitude.setText(Double.toString(location.getLongitude()));
            tv_birtdate.setText(location.getBirtdate().toString());
            tv_gender.setText(location.getGender());
            iv_image.setImageBitmap(Converters.StringToBitMap(location.getImage()));
        }

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BitmapDrawable drawable = (BitmapDrawable) iv_image.getDrawable();
                Bitmap bitmapToString = drawable.getBitmap();
                String image = Converters.BitMapToString(bitmapToString);
                Log.e("image ", "image: " + image);

                if (update) {
                    location.setTitle(et_title.getText().toString());
                    location.setSubtitle(tv_country.getText().toString());
                    location.setLatitude(Double.parseDouble(et_latitude.getText().toString()));
                    location.setLongitude(Double.parseDouble(et_longitude.getText().toString()));
                    location.setGender(tv_gender.getText().toString());
                    Date date = Date.valueOf(tv_birtdate.getText().toString());
                    location.setBirtdate(date);
                    location.setImage(image);
                    locationDatabase.personDao().update(location);
                    setResult(location, 2);
                } else {
                    location = new Location(et_title.getText().toString(), tv_country.getText().toString(), Double.parseDouble(et_latitude.getText().toString()), Double.parseDouble(et_longitude.getText().toString()), tv_gender.getText().toString(), Date.valueOf(tv_birtdate.getText().toString()), image);
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
        Log.e("result ", "setResult: " + flag + " " + location.toString());
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
            long j = activityReference.get().locationDatabase.personDao().insertLocation(location);
            location.setId(j);
            Log.e("ID ", "doInBackground: " + j);
            return true;
        }

        // onPostExecute runs on main thread
        @Override
        protected void onPostExecute(Boolean bool) {
            Log.e("Bool ", "onPostExecute: " + bool);
            if (bool) {
                activityReference.get().setResult(location, 1);
                activityReference.get().finish();
            }
        }
    }

    private void loadImagesFromGallery() {
        if (ActivityCompat.checkSelfPermission(AddLocationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //getting permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("getting Results ", "onActivityResult: AddLocationActivity " + resultCode +" "+ requestCode);

        if ( resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            try {
                iv_image = findViewById(R.id.userPhotoSelected);

                InputStream is = getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(is);
                iv_image.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 100 && resultCode > 0) {
            if (resultCode == 11) {
                tv_country.setText((String) data.getSerializableExtra("country"));
            }
            if (resultCode == 22) {
                tv_gender.setText((String) data.getSerializableExtra("gender"));
            }
        }
    }
}

