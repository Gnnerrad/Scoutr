package com.example.darre_000.scoutr;

//This is a comment

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    //charles has been here
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
                ImageView locationPhoto = (ImageView) v.findViewById(R.id.locationPhoto);
                locationPhoto.setImageURI(imageUri);
                return v;
            }
        });
        mapFragment.getMapAsync(this);

        View topBar = findViewById(R.id.topBarLayout);

        ImageButton cameraButton = (ImageButton) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(camListener);

        SearchView searchView = (SearchView) findViewById(R.id.searchView);

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
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "A menu or somthing....", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void onMapReady(GoogleMap googleMap) {

    }

    private View.OnClickListener camListener =  new View.OnClickListener(){
        public void onClick(View v){
            takeLocationPhoto(v);
        }
    };

    private void takeLocationPhoto(View v){
        gps = new GPSTracker(MainActivity.this);
        Intent intent  = new Intent("android.media.action.IMAGE_CAPTURE");

        //asdjfl

        String photoName = Double.toString(gps.getLongitude()) +
                     "_" + Double.toString(gps.getLatitude() ) + ".jpg";

        File photo = new File(Environment.getExternalStorageDirectory(),photoName);
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PICTURE_TAKE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(resultCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK){
            Uri chosenImage = imageUri;
            getContentResolver().notifyChange(chosenImage,null);

            ImageView imageView = (ImageView)findViewById(R.id.image_camera);
            ContentResolver cr = getContentResolver();
            Bitmap bitmap;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(cr, imageUri);
                imageView.setImageBitmap(bitmap);
            } catch(Exception e) {

            }
        }
        addMarkerForPicture();
    }


    protected void addMarkerForPicture() {

        if (gps.canGetLocation) {
            LatLng currentLocation = new LatLng(gps.getLatitude(), gps.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            mMap.moveCamera(CameraUpdateFactory.zoomIn());
            Marker marker = mMap.addMarker(new MarkerOptions()
                            .title("King Charles")
                            .position(currentLocation)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
            );
            marker.showInfoWindow();
        }
//End Map Stuff

    }
}

