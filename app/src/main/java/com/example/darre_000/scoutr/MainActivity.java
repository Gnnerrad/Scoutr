package com.example.darre_000.scoutr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    //charles has been here
    private GoogleMap mMap;
    private GPSTracker gps;
    //    private Uri imageUri;
    private HashMap<String, Bitmap> popupImgMap;
    private int imageCount;
    Bitmap bitmap;
//    private CheckBox chkIos, chkAndroid, chkWindows;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupImgMap = new HashMap<>();
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
//                LatLng latLng = marker.getPosition();
                TextView lat = (TextView) v.findViewById(R.id.latlng);
//                lat.setText(imageUri.toString());
                popupImgMap.put(marker.getSnippet(), bitmap);
                ImageView locationPhoto = (ImageView) v.findViewById(R.id.locationPhoto);
                locationPhoto.setImageBitmap(popupImgMap.get(marker.getSnippet()));
                return v;
            }
        });
        mapFragment.getMapAsync(this);

//        View topBar = findViewById(R.id.topBarLayout);

        ImageButton cameraButton = (ImageButton) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(camListener);

//        SearchView searchView = (SearchView) findViewById(R.id.searchView);

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
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 1);

//            gps = new GPSTracker(MainActivity.this);
//            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//            Long tsLong = System.currentTimeMillis() / 1000;
//            String ts = tsLong.toString();
//
//            String photoName = Double.toString(gps.getLongitude()) +
//                    "_" + Double.toString(gps.getLatitude()) + "_" + imageCount + ".jpg";
//
//            File photo = new File(Environment.getExternalStorageDirectory(), photoName);
//            Uri photoUri = Uri.fromFile(photo);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri.toString());
//            startActivityForResult(intent, 1);
//            Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
//            File file = new File ("sd/Scoutr/", "image.jpg");
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//            startActivityForResult(intent, 0);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(resultCode, resultCode, intent);
        if (requestCode == 1 && resultCode== RESULT_OK && intent != null){
            Bundle extras = intent.getExtras();
            Bitmap bitMap = (Bitmap) extras.get("data");
            FileOutputStream out = null;
            try {
                out = new FileOutputStream("TESTIMAGE");
                bitMap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            Intent imageChecklistIntent = new Intent(this, CheckBoxActivity.class);
//            imageChecklistIntent.putExtra("imageUri", Uri.);
//            startActivity(imageChecklistIntent); //change to forResult.
//            addMarkerForPicture();

        }
//        if (resultCode == Activity.RESULT_OK) {
//            Uri chosenImage =  Uri.parse(intent.getExtras().getString(MediaStore.EXTRA_OUTPUT));;
//            getContentResolver().notifyChange(chosenImage, null);
//
//            ImageView imageView = (ImageView) findViewById(R.id.image_camera);
//            ContentResolver cr = getContentResolver();
////            Bitmap bitmap;
//
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(cr, chosenImage);
//                imageView.setImageBitmap(bitmap);
//                Intent imageChecklistIntent = new Intent(this, CheckBoxActivity.class);
//                imageChecklistIntent.putExtra("imageUri", chosenImage.toString());
//                startActivity(imageChecklistIntent); //change to forResult.
//                addMarkerForPicture();
//            } catch (Exception e) {
//            }
//        }
    }

    public void onMapReady(GoogleMap googleMap) {
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
            marker.showInfoWindow();
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

