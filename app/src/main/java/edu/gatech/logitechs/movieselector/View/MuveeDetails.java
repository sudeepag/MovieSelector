package edu.gatech.logitechs.movieselector.View;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import edu.gatech.logitechs.movieselector.Controller.FaceBookManager;
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

    /**
     * textview to store user review;
     */
    private TextView tvReview;
    private Button fbPostButton;

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

        tvReview = (TextView) findViewById(R.id.details_review);
        if (ratingMovie.getReview() != null && !ratingMovie.getReview().equals("")) {
            tvReview.setText(ratingMovie.getReview());
        }

        final FloatingActionButton fabWrite = (FloatingActionButton) findViewById(R.id.fab);
        fabWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Write a Review :)", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                postReviewDialog();
            }
        });

        fbPostButton = (Button) findViewById(R.id.button_post_fb);

        if (FaceBookManager.getProfile() != null) {
            if (ratingMovie.getReview() != null && !ratingMovie.getReview().equals("")) {
                fbPostButton.setVisibility(View.VISIBLE);
            }
        } else {
            fbPostButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        updateRatings();
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    @Override
    protected void onStop() {
        //Log out of Facebook when user forces app to quit
        LoginManager.getInstance().logOut();
        super.onStop();
    }

    private void postReviewDialog() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View postReviewDialogView = inflater.inflate(R.layout.muvee_post_review_dialog_view, null, false);
        final EditText reviewText = (EditText) postReviewDialogView.findViewById(R.id.review_text);
        final TextView charCount = (TextView) postReviewDialogView.findViewById(R.id.char_count);

        if (ratingMovie.getReview() != null && !ratingMovie.getReview().equals("")) {
            reviewText.setText(ratingMovie.getReview());
            charCount.setText(String.valueOf(ratingMovie.getReview().length()));
            charCount.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a TextView to the current length

                int len = s.length();
                int color = 0;
                if (len > 0 || len < 255) {
                    color = getResources().getColor(R.color.colorPrimary);
                } else {
                    color = getResources().getColor(R.color.colorAlternateAccent);
                }
                charCount.setTextColor(color);
                charCount.setText(String.valueOf(len));

            }

            public void afterTextChanged(Editable s) {
            }
        };

        reviewText.addTextChangedListener(mTextEditorWatcher);

        final AlertDialog dialog = new AlertDialog.Builder(MuveeDetails.this).setView(postReviewDialogView)
                .setTitle("Post a review")
                .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {
                        postReview(reviewText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create();

        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAlternateAccent));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAlternateAccent));

            }
        });
        dialog.show();
    }

    /**
     * Posts review to reviews section
     * @param s the review string
     */
    private void postReview(String s) {
        ratingMovie.setReview(s);
        if (!ratingMovie.getReview().equals("")) {
            tvReview.setText(ratingMovie.getReview());
            fbPostButton.setVisibility(View.VISIBLE);
        } else {
            fbPostButton.setVisibility(View.GONE);
        }
    }

    /**
     * Share review to facebook
     * @param view  the parent view
     */
    public void shareReview(View view) {
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentTitle("Müveé Reviews: " + MovieManager.getCurrentMovie().getTitle())
                .setContentUrl(Uri.parse("http://www.rottentomatoes.com/m/the_revenant_2015/"))
                .setContentDescription(tvReview.getText().toString())
                .build();

        ShareDialog.show(MuveeDetails.this, content);
    }

    /**
     * Transition to maps activity
     * @param view  the parent view
     */
    public void goToMap(View view) {
        final Intent myIntent = new Intent(MuveeDetails.this, MuveeMaps.class);
        startActivity(myIntent);
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