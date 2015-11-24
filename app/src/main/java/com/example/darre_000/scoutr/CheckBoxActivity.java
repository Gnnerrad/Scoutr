package com.example.darre_000.scoutr;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

public class CheckBoxActivity extends AppCompatActivity{

    private CheckBox chk1, chk2, chk3, chk4, chk5;
    private Button btnDisplay;
    String imageUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_information_checklist_activity);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("imageUri") && extras.getString("imageUri") != null) {
            try {
                imageUri = extras.getString("imageUri");
                ImageView imageView = (ImageView) findViewById(R.id.checklistPhoto);
                imageView.setImageURI(Uri.parse(imageUri));
                addListenerOnButton();
            } catch (Exception e) {
            }
        }
    }

    public void addListenerOnButton() {

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
                intent.putExtra("accessCheck", chk4.isChecked());
                intent.putExtra("sunCheck", chk5.isChecked());
                setResult(Activity.RESULT_OK, intent);
                if (intent.getExtras() == null) {
                    Toast.makeText(CheckBoxActivity.this, "Intent is empty even before return", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }
}
