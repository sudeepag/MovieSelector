package edu.gatech.logitechs.movieselector.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.logitechs.movieselector.Controller.MovieManager;
import edu.gatech.logitechs.movieselector.Controller.UserManager;
import edu.gatech.logitechs.movieselector.Model.Movie;
import edu.gatech.logitechs.movieselector.R;

public class MuveeMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * mainlayout coordinates layout for main page
     */
    private CoordinatorLayout mainLayout;
    /**
     * navigation view is the sideview that helps with navigation
     */
    private NavigationView navigationView;
    /**
     * recycler view for the movies
     */
    private RecyclerView rv;
    /**
     * view for the about dialogue
     */
    private View aboutDialogLayout;
    /**
     * menu item for searching
     */
    private MenuItem searchMenuItem;
    /**
     * list of movies to display
     */
    private List<Movie> movies = new ArrayList<>();
    /**
     * adapter for the recycler view
     */
    private final RVMainAdapter adapter = new RVMainAdapter(movies);

    /**
     * the number five hundred
     */
    private static final int FIVE_HUNDRED = 500;
    /**
     * the number eleven
     */
    private static final int ELEVEN = 11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muvee_main_activity);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainLayout = (CoordinatorLayout) findViewById(R.id.main_layout);

        //Setting up RecyclerView
        rv = (RecyclerView) findViewById(R.id.recycler_view);
        rv.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(
            new RecyclerItemClickListener(MuveeMainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (view instanceof LinearLayout) {
                        //User touched an item
                        final Intent myIntent = new Intent(MuveeMainActivity.this, MuveeDetails.class);
                        myIntent.putExtra("title", movies.get(position).getTitle());
                        myIntent.putExtra("description", movies.get(position).getDescription());
                        myIntent.putExtra("thumbnail", movies.get(position).getThumbnail());
                        MovieManager.queryMovieRating(movies.get(position), new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                        startActivity(myIntent);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        //TODO consider using startActivityForResult for the main screen to stay the same when user hits back
                    }
                }
            })
        );

        getDVD();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getRecomendation();
            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Get the elements from Navigation Header
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        View headerView = navigationView.inflateHeaderView(R.layout.muvee_main_nav_header);

        TextView emailHeader = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_email);
        emailHeader.setText(UserManager.getCurrentUser().getEmail());

        TextView textHeader = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_text);
        ProfilePictureView prof = (ProfilePictureView) navigationView.getHeaderView(0).findViewById(R.id.profilePicture);
        prof.setPresetSize(ProfilePictureView.SMALL);

        if (Profile.getCurrentProfile() != null) {
            textHeader.setText(getResources().getString(R.string.app_name) + " | Online | " + Profile.getCurrentProfile().getName());
            prof.setProfileId(Profile.getCurrentProfile().getId());
        } else {
            String name = UserManager.getCurrentUser().getEmail().split("@")[0];
            textHeader.setText(getResources().getString(R.string.app_name) + " | Local | " + name);

        }
    }

    /**
     * listener for the searchview
     */
    private SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            MenuItemCompat.collapseActionView(searchMenuItem);
            MovieManager.searchTitles(query, MuveeMainActivity.this, new Runnable() {
                @Override
                public void run() {
                    movies = MovieManager.getMovieList();
                    adapter.updateMovieList(movies);
                }
            });
            navigationView.getMenu().getItem(0).setChecked(false);
            navigationView.getMenu().getItem(1).setChecked(false);
            return false;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };


    @Override
    public void onBackPressed() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            logoutAction();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        final SearchView mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(searchListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        } else if (id == R.id.action_about) {
            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            aboutDialogLayout = inflater.inflate(R.layout.muvee_about_dialog_view, null, false);
            final Snackbar snackbar = Snackbar
                        .make(mainLayout, "Made with ❤️ from CS 2340", Snackbar.LENGTH_LONG)
                        .setAction("MORE INFO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final AlertDialog dialog = new AlertDialog.Builder(MuveeMainActivity.this)
                                        .setTitle("About us")
                                        .setView(aboutDialogLayout)
                                        .setPositiveButton("THANKS", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        }).create();
                                dialog.show();
                            }
                        });
            snackbar.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();

        if (id == R.id.nav_dvd_library) {
            getDVD();
        } else if (id == R.id.nav_new_release) {
            getRecent();
        } else if (id == R.id.nav_manage) {
            final Intent myIntent = new Intent(MuveeMainActivity.this, MuveeSettings.class);
            //DO not show headers when loading preference
            myIntent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, MuveeSettings.class);
            myIntent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);
            startActivity(myIntent);
        } else if (id == R.id.nav_log_out) {
            logoutAction();
        }

        //Close Drawer
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Communicate with MovieManager to update DVD list
     */
    private void getDVD() {
        rvDisappear();
        MovieManager.getDVD(this, new Runnable() {
            @Override
            public void run() {
                movies = MovieManager.getMovieList();
//                rvAnimate(false);
                adapter.updateMovieList(movies);
//                rvAnimate(true);
            }
        });
        adapter.updateMovieList(movies);
        rvAppear();
    }

    /**
     * Communicate with MovieManager to update recent list
     */
    private void getRecent() {
        rvDisappear();
        MovieManager.getRecent(MuveeMainActivity.this, new Runnable() {
            @Override
            public void run() {
                movies = MovieManager.getMovieList();
                adapter.updateMovieList(movies);

            }
        });
        adapter.updateMovieList(movies);
        rvAppear();
    }

    /**
     * Communicate with MovieManager to update recent list
     */
    private void getRecomendation() {
        rvDisappear();
        MovieManager.getRankedMovies(MuveeMainActivity.this, new Runnable() {
            @Override
            public void run() {
                movies = MovieManager.getMovieList();
                adapter.updateMovieList(movies);
            }
        }, UserManager.getCurrentUser().getMajor());
//        MovieManager.getMoviesFromIds(MuveeMainActivity.this, new Runnable() {
//            @Override
//            public void run() {
//                movies = MovieManager.getMovieList();
//                adapter.updateMovieList(movies);
//            }
//        }, "770739679");
        adapter.updateMovieList(movies);
        rvAppear();
    }

    /**
     * Animation for RecyclerView to fade in
     */
    private void rvAppear() {
        final AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FIVE_HUNDRED);
        rv.startAnimation(anim);
    }

    /**
     * Animation for RecyclerView to fade out
     */
    private void rvDisappear() {
        final AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(FIVE_HUNDRED);
        rv.startAnimation(anim);
    }

    @Override
    protected void onStop() {
        //Log out of Facebook when user forces app to quit
        LoginManager.getInstance().logOut();
        super.onStop();
    }

    /**
     * Perform logout
     */
    private void logoutAction() {
        new AlertDialog.Builder(MuveeMainActivity.this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.prompt_logout)
                .setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @TargetApi(ELEVEN)
                    public void onClick(DialogInterface dialog, int id) {
                        final Intent myIntent = new Intent(MuveeMainActivity.this, MuveeLogin.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
                        startActivity(myIntent);
                        LoginManager.getInstance().logOut();
                        finish();
                    }
                })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
            @TargetApi(ELEVEN)
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).show();
    }
}
