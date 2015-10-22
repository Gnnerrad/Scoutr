package com.example.darre_000.scoutr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GPSTracker gps;
    private static int PICTURE_TAKE = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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

    }


//Map Stuff
public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    GPSTracker gps;
    gps = new GPSTracker(MainActivity.this);

    if(gps.canGetLocation) {
        LatLng currentLocation = new LatLng(gps.getLatitude(), gps.getLongitude());
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("current location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
    }
}
//End Map Stuff

    }

