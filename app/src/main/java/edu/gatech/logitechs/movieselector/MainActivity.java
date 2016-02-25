package edu.gatech.logitechs.movieselector;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    UserManager manager;
    User currUser;

    TextView navHeader;
    TextView navHeaderEmail;

    private List<Movie> movies;

    private void initializeData() {
        movies = new ArrayList<>();
        movies.add(new Movie("Movie", 2000, 100, "best movie", "bob", "bobert"));
        movies.add(new Movie("Movie", 2000, 100, "best movie", "bob", "bobert"));
        movies.add(new Movie("Movie", 2000, 100, "best movie", "bob", "bobert"));
        movies.add(new Movie("Movie", 2000, 100, "best movie", "bob", "bobert"));
        movies.add(new Movie("Movie", 2000, 100, "best movie", "bob", "bobert"));
        movies.add(new Movie("Movie", 2000, 100, "best movie", "bob", "bobert"));
        movies.add(new Movie("Movie", 2000, 100, "best movie", "bob", "bobert"));
        movies.add(new Movie("Movie", 2000, 100, "best movie", "bob", "bobert"));
        movies.add(new Movie("Movie", 2000, 100, "best movie", "bob", "bobert"));
        movies.add(new Movie("Movie", 2000, 100, "best movie", "bob", "bobert"));
        movies.add(new Movie("a", 2, 1, "best movie", "bob", "bobert"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //Setting up RecyclerView
        RecyclerView rv = (RecyclerView)findViewById(R.id.recycler_view);
        rv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        initializeData();
        System.out.println("Data initialized");

        final RVAdapter adapter = new RVAdapter(movies);
        rv.setAdapter(adapter);

        MovieManager.getDVD(this, new Runnable() {
            @Override
            public void run() {
                movies = MovieManager.getMovieList();
                adapter.updateMovieList(movies);
                System.out.println("Changed");
            }
        });

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // creating the EditText widget programatically
//                final EditText input = new EditText(MainActivity.this);
//                TableLayout.LayoutParams params = new TableLayout.LayoutParams();
//                params.setMarginStart(16);
//                params.setMarginEnd(16);
//                input.setLayoutParams(params);

                //get view from layout XML
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View searchFieldLayout = inflater.inflate(R.layout.search_box_view,
                        null, false);
                final EditText input = (EditText) searchFieldLayout.findViewById(R.id.search_field);

                // create the AlertDialog as final
                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Enter a movie title")
                        .setTitle("Search")
                        .setView(searchFieldLayout)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                MovieManager.searchTitles(input.getText().toString(), MainActivity.this, new Runnable() {
                                    @Override
                                    public void run() {
                                        movies = MovieManager.getMovieList();
                                        adapter.updateMovieList(movies);
//                                        System.out.println("Changed");
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .create();

                // set the focus change listener of the EditText
                // this part will make the soft keyboard automaticall visible
                            input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        }
                    }
                });

                dialog.show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navHeader = (TextView) findViewById(R.id.nav_header_text);
        navHeaderEmail = (TextView) findViewById(R.id.nav_header_email);
    }

    protected void onStop() {
//        update navigation headers
//        manager = new UserManager();
//        currUser = manager.getCurrentUser();
//        String[] names = currUser.getEmail().split("@");
//
//        navHeader.setText(currUser.getEmail().split("@")[0]);
//        navHeaderEmail.setText(currUser.getEmail());
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_library) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Intent myIntent = new Intent(MainActivity.this, UserProfile.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_log_out) {
            logoutAction();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutAction() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.prompt_logout)
        .setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @TargetApi(11)
                    public void onClick(DialogInterface dialog, int id) {

//                        User user = UserManager.getCurrentUser();
//                        UserManager.currentUser = null;
//                        user.setMajor("chicken");
//                        UserManager.updatedCurrentUser(user);
//                        user = null;
//                        user = UserManager.getCurrentUser();

                        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
                        startActivity(myIntent);
                        finish();
                    }
                })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            @TargetApi(11)
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        }).show();
    }

}
