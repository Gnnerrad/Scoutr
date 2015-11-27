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
    private GoogleMap mMap;
    private GPSTracker gps;
    private Uri imageUri;
    private HashMap<String, String> popupImgMap = new HashMap<>();
    private int imageCount;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent loadingScreen = new Intent(this, LoadingScreenActivity.class);
//        startActivity(loadingScreen);

//        ScoutrDBHelper dbHelper = new ScoutrDBHelper(getApplicationContext());
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(ScoutrDBContract.Table.LATITUDE, "234234534");
//        values.put(ScoutrDBContract.Table.LONGITUDE, "23545645");
//
//        long newRowId;
//        newRowId = db.insert(
//                ScoutrDBContract.Table.TABLE_NAME,
//                null,
//                values
//        );


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
                TextView lat = (TextView) v.findViewById(R.id.latlng);
                lat.setText(imageUri.toString());
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
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "A menu", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private View.OnClickListener camListener = new View.OnClickListener() {
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
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        String photoName = Double.toString(gps.getLongitude()) +
                "_" + Double.toString(gps.getLatitude()) + "_" + imageCount + ".jpg";


        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), photoName);

        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 1);

        popupImgMap.put("m" + imageCount, photoName);
        Log.d(Integer.toString(imageCount), "SPOCK");
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

                addMarkerForPicture(wcBool, wifiBool, powerBool, accesBool, sunBool);
            }
        }
    }


    public void onMapReady(GoogleMap googleMap) {
    }

    protected void addMarkerForPicture(boolean wcBool, boolean wifiBool, boolean powerBool, boolean accessBool, boolean sunBool) {
        if (gps.canGetLocation) {
            LatLng currentLocation = new LatLng(gps.getLatitude(), gps.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            mMap.moveCamera(CameraUpdateFactory.zoomIn());
            Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(currentLocation)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                            .snippet(wcBool + " " + wifiBool + " " + powerBool + " " + accessBool + " " + sunBool)
            );

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Log.d(popupImgMap.get(marker.getId()), marker.getId());

                    String photoName = popupImgMap.get(marker.getId());
                    File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), photoName);
                    imageUri = Uri.fromFile(photo);
                    marker.showInfoWindow();
                    return true;
                }
            });
            imageCount++;
        }
    }
}

