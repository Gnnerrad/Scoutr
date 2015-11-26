package com.example.darre_000.scoutr;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
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
import java.util.HashMap;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    //charles has been here
    private GoogleMap mMap;
    private GPSTracker gps;
    private Uri imageUri;
    private HashMap<String, File> popupImgMap;
    private int imageCount;
    Bitmap bitmap;
//    private CheckBox chkIos, chkAndroid, chkWindows;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMap = mapFragment.getMap();
        imageCount = 0;
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            //taken from http://stackoverflow.com/questions/15090148/custom-info-window-adapter-with-custom-data-in-map-v2
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.custom_info_window, null);
//              LatLng latLng = marker.getPosition();
                TextView lat = (TextView) v.findViewById(R.id.latlng);
                lat.setText(imageUri.toString());
                ImageView locationPhoto = (ImageView) v.findViewById(R.id.locationPhoto);
                locationPhoto.setImageURI(imageUri);
                return v;
            }
        });
        mapFragment.getMapAsync(this);

//       View topBar = findViewById(R.id.topBarLayout);

        ImageButton cameraButton = (ImageButton) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(camListener);

//      SearchView searchView = (SearchView) findViewById(R.id.searchView);

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

    private View.OnClickListener camListener = new View.OnClickListener() {
        public void onClick(View v) {
            takeLocationPhoto(v);
        }
    };

    private void takeLocationPhoto(View v) {
        gps = new GPSTracker(MainActivity.this);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        String photoName = Double.toString(gps.getLongitude()) +
                "_" + Double.toString(gps.getLatitude()) + "_" + imageCount + ".jpg";


        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), photoName);

        //File photo = new File(Environment.getExternalStorageDirectory(), photoName);
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
                } catch (Exception e) {}

                Intent photoCheckbox = new Intent(this, CheckBoxActivity.class);
                photoCheckbox.putExtra("imageUri", imageUri.toString());
                startActivityForResult(photoCheckbox, 2);
            }
            else if(requestCode == 2) {
                if (intent.getExtras() != null) {
                    if(intent.getExtras().getBoolean("wcChk")){
                        ImageView wc = (ImageView) findViewById(R.id.popupTolietIcon);
                        wc.setBackgroundResource(R.drawable.wc_icon);
                    }
                    if(intent.getExtras().getBoolean("wifiChk")){
                        ImageView wifi = (ImageView) findViewById(R.id.popupWifiIcon);
                        wifi.setBackgroundResource(R.drawable.wifi_icon);
                    }
                    if(intent.getExtras().getBoolean("powerChk")){
                        ImageView power = (ImageView) findViewById(R.id.popupPowerIcon);
                        power.setBackgroundResource(R.drawable.power_icon);
                    }
                    if(intent.getExtras().getBoolean("accessCheck")){
                        ImageView access = (ImageView) findViewById(R.id.popupAccesibilityIcon);
                        access.setBackgroundResource(R.drawable.accessibility_icon);
                    }
                    if(intent.getExtras().getBoolean("sunCheck")){
                        ImageView sun = (ImageView) findViewById(R.id.popupSunIcon);
                        sun.setBackgroundResource(R.drawable.weather_icon);
                    }
                    addMarkerForPicture();
                }
                else{
                    Toast.makeText(MainActivity.this, "checkBox Intent is null", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void onMapReady(GoogleMap googleMap) {
//        Marker marker = mMap.addMarker(new MarkerOptions()
//                        .title("King Charles")
//                        .position(new LatLng(53.3860,-6.0660))
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
//                        .snippet(Integer.toHexString(imageCount))
//        );
//
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
                            .snippet(Integer.toHexString(imageCount))
            );
            //marker.showInfoWindow();
            imageCount++;
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
                            addMarkerForPicture();
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

