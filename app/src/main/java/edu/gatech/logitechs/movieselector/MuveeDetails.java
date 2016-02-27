package edu.gatech.logitechs.movieselector;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MuveeDetails extends AppCompatActivity {

    String movieTitle;
    Bitmap movieThumbnail;

    Toolbar dToolbar;
    TextView detailsTitle;
    TextView detailsDescription;
    FloatingActionButton fabWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muvee_details_activity);

        //Get data from Intent
        Intent intent = getIntent();
        this.movieTitle = intent.getStringExtra("title");
        Bundle extras = intent.getExtras();
        this.movieThumbnail = (Bitmap) extras.get("thumbnail");

        dToolbar = (Toolbar) findViewById(R.id.details_toolbar);
        setSupportActionBar(dToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        detailsTitle = (TextView) findViewById(R.id.details_title);
        detailsTitle.setText(movieTitle);
        detailsDescription = (TextView) findViewById(R.id.details_description);

        fabWrite = (FloatingActionButton) findViewById(R.id.fab);
        fabWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Write a Review :)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
