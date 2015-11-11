package com.example.darre_000.scoutr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GPSTracker gps;
    private static int PICTURE_TAKE = 0;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMap=mapFragment.getMap();
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            //taken from http://stackoverflow.com/questions/15090148/custom-info-window-adapter-with-custom-data-in-map-v2

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                LatLng latLng = marker.getPosition();
                TextView tvLat = (TextView) v.findViewById(R.id.infoWindowTitle);
                TextView tvLng = (TextView) v.findViewById(R.id.InfoWindowSnippet);
                ImageView locationPhoto = (ImageView) v.findViewById(R.id.locationPhoto);

                        //setImageURI(Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)));
                tvLat.setText("Latitude:" + latLng.latitude);
                tvLng.setText("Longitude:"+ latLng.longitude);
                return v;
            }

//            public void setLocationPhoto(Uri photoUri){
//                locationPhoto.setImageURI(photoUri);
//            }
        });
        mapFragment.getMapAsync(this);


//Top Bar Stuff
        View topBar = findViewById(R.id.topBarLayout);

//Camera Button
        ImageButton cameraButton = (ImageButton) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(camListener);

//Search Bar
        SearchView searchView = (SearchView) findViewById(R.id.searchView);

//End Top Bar Stuff


//Bottom Bar Stuff
//LeftButton
        ImageButton favourites = (ImageButton) findViewById(R.id.favourites);
        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "I couldn't remember what this button was ment to be so I called it favourites", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//Middle button
        Button scoutrButton = (Button) findViewById(R.id.scoutr_social_network);
        scoutrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "I couldn't remember what this button was ment to be so I called it scoutrButton", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//Menu button
        ImageButton menu = (ImageButton) findViewById(R.id.bottomMenuButton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "A menu or somthing....", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

//End Bottom Bar

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera);
//        Button cameraButton  = (Button)findViewById(R.id.button_camera);
//        cameraButton.setOnClickListener(camListener);
//    }

    private View.OnClickListener camListener =  new View.OnClickListener(){
        public void onClick(View v){
            takeLocationPhoto(v);
        }
    };

    private void takeLocationPhoto(View v){
        Intent intent  = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"location_picture.jpg");
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PICTURE_TAKE);
        addMarkerForPicture(imageUri);
    }



    //Map Stuff
    public void onMapReady(GoogleMap googleMap) {
    }

    public void recieveCurrentImageLocation(Uri imageUri){
        Toast.makeText(MainActivity.this, imageUri.toString(), Toast.LENGTH_LONG).show();
    }

    protected void addMarkerForPicture(Uri imageUri) {
        GPSTracker gps;
        gps = new GPSTracker(MainActivity.this);

        if (gps.canGetLocation) {
            LatLng currentLocation = new LatLng(gps.getLatitude(), gps.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            mMap.moveCamera(CameraUpdateFactory.zoomIn());
            Marker marker = mMap.addMarker(new MarkerOptions()

                            .position(currentLocation)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
            );

            marker.showInfoWindow();
        }
//End Map Stuff

    }

    private void loadImageFromStorage(String path)
    {
        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)findViewById(R.id.locationPhoto);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}

