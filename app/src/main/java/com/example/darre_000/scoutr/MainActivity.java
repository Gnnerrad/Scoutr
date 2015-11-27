package com.example.darre_000.scoutr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private GPSTracker gps;
    private Uri imageUri;
    Bitmap bitmap;
    ScoutrDBHelper ScoutrDb;
    String currentLocationPhotoName;
    SearchView searchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScoutrDb = new ScoutrDBHelper(this);

        searchBar = (SearchView) findViewById(R.id.searchView);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Cursor res = ScoutrDb.getAllData();

                while(res.moveToNext()){
                    if(res.getString(1).equals(query)){
                        double lat = Double.parseDouble(res.getString(2));
                        double lng = Double.parseDouble(res.getString(3));
                        CameraPosition camPos = new CameraPosition.Builder()
                                .target(new LatLng(lat, lng))
                                .zoom(18)
                                .build();
                        CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);
                        mMap.animateCamera(camUpd3);
                        break;
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onQueryTextSubmit(newText);
                return false;
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMap = mapFragment.getMap();
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            //taken from http://stackoverflow.com/questions/15090148/custom-info-window-adapter-with-custom-data-in-map-v2
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                String booleans[] = marker.getSnippet().split(" ");
                View v = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                if (booleans[0].equals("true")) {
                    ImageView wc = (ImageView) v.findViewById(R.id.popupTolietIcon);
                    wc.setImageURI(Uri.parse("android.resource://com.example.darre_000.scoutr/drawable/wc_icon"));
                }
                if (booleans[1].equals("true")) {
                    ImageView wifi = (ImageView) v.findViewById(R.id.popupWifiIcon);
                    wifi.setImageURI(Uri.parse("android.resource://com.example.darre_000.scoutr/drawable/wifi_icon"));
                }
                if (booleans[2].equals("true")) {
                    ImageView power = (ImageView) v.findViewById(R.id.popupPowerIcon);
                    power.setImageURI(Uri.parse("android.resource://com.example.darre_000.scoutr/drawable/power_icon"));
                }
                if (booleans[3].equals("true")) {
                    ImageView access = (ImageView) v.findViewById(R.id.popupAccesibilityIcon);
                    access.setImageURI(Uri.parse("android.resource://com.example.darre_000.scoutr/drawable/accessibility_icon"));
                }
                if (booleans[4].equals("true")) {
                    ImageView sun = (ImageView) v.findViewById(R.id.popupSunIcon);
                    sun.setImageURI(Uri.parse("android.resource://com.example.darre_000.scoutr/drawable/weather_icon"));
                }
                ImageView locationPhoto = (ImageView) v.findViewById(R.id.locationPhoto);
                try {
                    getContentResolver().notifyChange(imageUri, null);
                    ContentResolver cr = getContentResolver();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(cr, imageUri);
                    Bitmap resized = Bitmap.createScaledBitmap(bitmap, 1920, 1080, true);
                    locationPhoto.setImageBitmap(resized);
                } catch (Exception e) {
                    locationPhoto.setImageURI(imageUri);
                }
                return v;
            }
        });
        mapFragment.getMapAsync(this);

        ImageButton cameraButton = (ImageButton) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(camListener);

        ImageButton favourites = (ImageButton) findViewById(R.id.favourites);
        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This links to favourite locations", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button scoutrButton = (Button) findViewById(R.id.scoutr_social_network);
        scoutrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This links to Scoutr news feed", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ImageButton menu = (ImageButton) findViewById(R.id.bottomMenuButton);
        menu.setOnClickListener(settingListener);
    }

    private View.OnClickListener settingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settings);
        }
    };

    private View.OnClickListener camListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            gps = new GPSTracker(MainActivity.this);
            if (!gps.canGetLocation) {
                DialogInterface.OnClickListener enableGpsClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent gpsOptionsIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(gpsOptionsIntent);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("No pin will be dropped as your location services cannot be accessed.")
                                        .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Location services must be enabled to drop a new pin.\nWould you like to enable location services now?")
                        .setPositiveButton("Yes", enableGpsClickListener)
                        .setNegativeButton("No", enableGpsClickListener).show();
            }
            takeLocationPhoto(v);
        }
    };

    private void takeLocationPhoto(View v) {
        gps = new GPSTracker(MainActivity.this);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        currentLocationPhotoName = Double.toString(gps.getLongitude()) +
                "_" + Double.toString(gps.getLatitude())+ ".jpg";


        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), currentLocationPhotoName);

        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(resultCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri chosenImage = imageUri;
                getContentResolver().notifyChange(chosenImage, null);

                ImageView imageView = (ImageView) findViewById(R.id.image_camera);
                ContentResolver cr = getContentResolver();
                Bitmap bitmap;

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(cr, imageUri);
                    imageView.setImageBitmap(bitmap);
                } catch (Exception e) {
                }

                Intent photoCheckbox = new Intent(this, CheckBoxActivity.class);
                photoCheckbox.putExtra("imageUri", imageUri.toString());
                startActivityForResult(photoCheckbox, 2);
            } else if (requestCode == 2) {
                boolean wcBool = intent.getExtras().getBoolean("wcChk"),
                        wifiBool = intent.getExtras().getBoolean("wifiChk"),
                        powerBool = intent.getExtras().getBoolean("powerChk"),
                        accesBool = intent.getExtras().getBoolean("accessChk"),
                        sunBool = intent.getExtras().getBoolean("sunChk");

                if (intent.getExtras().getString("popUpTitle") == null){
                    Toast.makeText(MainActivity.this, "null", Toast.LENGTH_LONG).show();
                }
                else if(intent.getExtras().getString("popUpTitle").equals("")) {
                    Toast.makeText(MainActivity.this, "result Empty", Toast.LENGTH_LONG).show();
                    Intent photoCheckbox = new Intent(this, CheckBoxActivity.class);
                    photoCheckbox.putExtra("imageUri", imageUri.toString());
                    startActivityForResult(photoCheckbox, 2);
                } else {
                      String markerTitle = intent.getExtras().getString("popUpTitle");
                    Toast.makeText(MainActivity.this, markerTitle, Toast.LENGTH_SHORT).show();
                    addMarkerForPicture(wcBool, wifiBool, powerBool, accesBool, sunBool, markerTitle);
                }
            }
        }
    }


    public void onMapReady(GoogleMap googleMap) {
    }

    protected void addMarkerForPicture(boolean wcBool, boolean wifiBool, boolean powerBool, boolean accessBool, boolean sunBool, String markerTitle) {
        if (gps.canGetLocation) {
            LatLng currentLocation = new LatLng(gps.getLatitude(), gps.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            mMap.moveCamera(CameraUpdateFactory.zoomIn());
            Marker marker = mMap.addMarker(new MarkerOptions()
                            .title(markerTitle)
                            .position(currentLocation)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                            .snippet(wcBool + " " + wifiBool + " " + powerBool + " " + accessBool + " " + sunBool)
            );
            //marker.showInfoWindow();
            boolean isInserted = ScoutrDb.insertData(
                    marker.getPosition().toString(),
                    markerTitle,
                    Double.toString(currentLocation.latitude),
                    Double.toString(currentLocation.longitude),
                    Boolean.toString(wcBool),
                    Boolean.toString(wifiBool),
                    Boolean.toString(powerBool),
                    Boolean.toString(accessBool),
                    Boolean.toString(sunBool),
                    currentLocationPhotoName);

            if(isInserted)
                Toast.makeText(this,"Data inserted", Toast.LENGTH_LONG).show();
            Cursor res = ScoutrDb.getAllData();
            StringBuffer buffer = new StringBuffer();
            while(res.moveToNext()){
                for (int i=0;i<10;i++){
                    buffer.append(res.getString(i) + "\n");
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("ALL DATA");
            builder.setMessage(buffer.toString());
            builder.show();
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Cursor res = ScoutrDb.getAllData();
                    marker.getPosition();

                    while(res.moveToNext()){
                        if(res.getString(0).equals(marker.getPosition().toString())){
                            Toast.makeText(MainActivity.this, res.getString(1), Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    String photoName = res.getString(9);

                    File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), photoName);
                    imageUri = Uri.fromFile(photo);

                    marker.showInfoWindow();
                    return true;
                }
            });
        } else {
            //The alert popup idea comes from the second response of http://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-in-android
            DialogInterface.OnClickListener enableGpsClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            Intent gpsOptionsIntent = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(gpsOptionsIntent);
                            //addMarkerForPicture();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Location services must be enabled to drop a new pin.\nWould you like to enable location services now?")
                    .setPositiveButton("Yes", enableGpsClickListener)
                    .setNegativeButton("No", enableGpsClickListener).show();
        }
    }
}