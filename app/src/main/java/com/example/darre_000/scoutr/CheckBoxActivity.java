package com.example.darre_000.scoutr;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckBoxActivity extends AppCompatActivity {

    private CheckBox chk1, chk2, chk3, chk4, chk5;
    private Button btnDisplay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_information_checklist_activity);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("imageUri") && extras.getString("imageUri") != null) {
            try {
                String imageUriString = extras.getString("imageUri");
                Uri chosenImage = Uri.parse(imageUriString);
                getContentResolver().notifyChange(chosenImage, null);
                ImageView imageView = (ImageView) findViewById(R.id.checklistPhoto);
                ContentResolver cr = getContentResolver();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(cr, chosenImage);
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 1920, 1080, true);
                imageView.setImageBitmap(resized);
                addListenerOnButton();
            } catch (Exception e) {}
        }
    }

    public void addListenerOnButton() {

        TextView textBox = (TextView) findViewById(R.id.checkBoxTextField);
        chk1 = (CheckBox) findViewById(R.id.checklistWc);
        chk2 = (CheckBox) findViewById(R.id.checklistWifi);
        chk3 = (CheckBox) findViewById(R.id.checklistPower);
        chk4 = (CheckBox) findViewById(R.id.checklistAccessibility);
        chk5 = (CheckBox) findViewById(R.id.checklistSun);

        btnDisplay = (Button) findViewById(R.id.btnDisplay);

        btnDisplay.setOnClickListener(new OnClickListener() {

            //Run when button is clicked
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("wcChk", chk1.isChecked());
                intent.putExtra("wifiChk", chk2.isChecked());
                intent.putExtra("powerChk", chk3.isChecked());
                intent.putExtra("accessChk", chk4.isChecked());
                intent.putExtra("sunChk", chk5.isChecked());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}
