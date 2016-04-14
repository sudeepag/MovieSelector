package edu.gatech.logitechs.movieselector.View;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import edu.gatech.logitechs.movieselector.Controller.MovieManager;
import edu.gatech.logitechs.movieselector.Controller.UserManager;
import edu.gatech.logitechs.movieselector.Model.RatingData;
import edu.gatech.logitechs.movieselector.R;

public class MuveeDetails extends AppCompatActivity {
    /**
     * rating bar to rate a movie
     */
    private RatingBar ratingBar;

    /**
     * rating data to store the movies rating
     */
    private RatingData ratingMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muvee_details_activity);

        //Get data from Intent
        final Intent intent = getIntent();
        final String movieTitle = intent.getStringExtra("title");
        final String movieDescription = intent.getStringExtra("description");

        final Toolbar dToolbar = (Toolbar) findViewById(R.id.details_toolbar);
        setSupportActionBar(dToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final TextView detailsTitle = (TextView) findViewById(R.id.details_title);
        detailsTitle.setText(movieTitle);
        final TextView detailsDescription = (TextView) findViewById(R.id.details_description);
        detailsDescription.setText(movieDescription);

        ratingBar = (RatingBar) findViewById(R.id.rating_bar);

        ratingMovie = MovieManager.getCurrentMovie();
        ratingBar.setRating((float) ratingMovie.calculateRating(UserManager.getCurrentUser().getMajor()));

        final FloatingActionButton fabWrite = (FloatingActionButton) findViewById(R.id.fab);
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
        if (ratingBar.getRating() != 0.0f) {
            updateRatings();
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @Override
    protected void onStop() {
        //Log out of Facebook when user forces app to quit
        LoginManager.getInstance().logOut();
        super.onStop();
    }

    /**
     * Updates the ratings via the Movie and User manager classes
     */
    public void updateRatings() {
        ratingMovie.addRating(UserManager.getCurrentUser().getMajor(), ratingBar.getRating());
        MovieManager.updateMovie(ratingMovie);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
