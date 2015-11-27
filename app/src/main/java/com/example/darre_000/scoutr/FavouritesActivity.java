//Basic Layout for Favourites for future development

package com.example.darre_000.scoutr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class FavouritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourites_activity);

        Button favourites = (Button) findViewById(R.id.favButton);
        favourites.setOnClickListener(returnListener);
    }

    private View.OnClickListener returnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(FavouritesActivity.this);
            builder.setMessage("As the database is local photos must be save to the phone so this checkBox is purly astectic.")
                    .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    };
}
