package com.example.darre_000.scoutr;

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
                addListenerOnChkIos();
                addListenerOnButton();
            }catch(Exception e){}
        }
        else{
            Toast.makeText(CheckBoxActivity.this, "YOU DONE FUCKED UP",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void addListenerOnChkIos() {

        chk1 = (CheckBox) findViewById(R.id.checklistAccessibility);

        chk1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chk1 checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(CheckBoxActivity.this,
                            "WHAT IS GOING ON?????", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void addListenerOnButton() {

        chk1 = (CheckBox) findViewById(R.id.checklistAccessibility);
        chk2 = (CheckBox) findViewById(R.id.checklistPower);
        chk3 = (CheckBox) findViewById(R.id.checklistSun);
        chk4 = (CheckBox) findViewById(R.id.checklistWc);
        chk5 = (CheckBox) findViewById(R.id.checklistWifi);
        btnDisplay = (Button) findViewById(R.id.btnDisplay);

        btnDisplay.setOnClickListener(new OnClickListener() {

            //Run when button is clicked
            @Override
            public void onClick(View v) {

                StringBuffer result = new StringBuffer();
                result.append("1 : ").append(chk1.isChecked());
                result.append("\n2 : ").append(chk2.isChecked());
                result.append("\n3 :").append(chk3.isChecked());
                result.append("\n4 : ").append(chk2.isChecked());
                result.append("\n5 :").append(chk3.isChecked());
                result.append("\n"+imageUri.toString());

                Toast.makeText(CheckBoxActivity.this, result.toString(),
                        Toast.LENGTH_LONG).show();

            }
        });

    }
}
