package edu.gatech.logitechs.movieselector;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.CoordinatorLayout;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MuveeMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    UserManager manager;
    User currUser;

    CoordinatorLayout mainLayout;
    NavigationView navigationView;
    RecyclerView rv;
    TextView navHeader;
    TextView navHeaderEmail;

    View aboutDialogLayout;

    SearchView mSearchView;
    MenuItem searchMenuItem;

    private List<Movie> movies = new ArrayList<>();
    public final RVAdapter adapter = new RVAdapter(movies);

    boolean isAtMainPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainLayout = (CoordinatorLayout) findViewById(R.id.main_layout);

        isAtMainPage = false;

        //Setting up RecyclerView
        rv = (RecyclerView)findViewById(R.id.recycler_view);
        rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        getDVD();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //get view from layout XML
//                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                final View searchFieldLayout = inflater.inflate(R.layout.about_dialog_view,
//                        null, false);
//                final EditText input = (EditText) searchFieldLayout.findViewById(R.id.search_field);
//                // create the AlertDialog as final
//                final AlertDialog dialog = new AlertDialog.Builder(MuveeMainActivity.this)
//                        .setTitle("Find Movie")
//                        .setView(searchFieldLayout)
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//                                MovieManager.searchTitles(input.getText().toString(), MuveeMainActivity.this, new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        movies = MovieManager.getMovieList();
//                                        adapter.updateMovieList(movies);
//                                    }
//                                });
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        })
//                        .create();
//                // set the focus change listener of the EditText
//                // this part will make the soft keyboard automatically visible
//                input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                    @Override
//                    public void onFocusChange(View v, boolean hasFocus) {
//                        if (hasFocus) {
//                            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//                        }
//                    }
//                });
//                dialog.show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navHeader = (TextView) findViewById(R.id.nav_header_text);
        navHeaderEmail = (TextView) findViewById(R.id.nav_header_email);
    }

    SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            MenuItemCompat.collapseActionView(searchMenuItem);
//            rvDisappear();
            MovieManager.searchTitles(query, MuveeMainActivity.this, new Runnable() {
                @Override
                public void run() {
                    movies = MovieManager.getMovieList();
                    adapter.updateMovieList(movies);
                }
            });
            navigationView.getMenu().getItem(0).setChecked(false);
            navigationView.getMenu().getItem(1).setChecked(false);
//            rvAppear();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            // newText is text entered by user to SearchView
            return false;
        }
    };

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
        searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(searchListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        } else if (id == R.id.action_about) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            aboutDialogLayout = inflater.inflate(R.layout.about_dialog_view, null, false);
            Snackbar snackbar = Snackbar
                        .make(mainLayout, "Made with ❤️ from CS 2340", Snackbar.LENGTH_LONG)
                        .setAction("MORE INFO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog dialog = new AlertDialog.Builder(MuveeMainActivity.this)
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
        int id = item.getItemId();

        if (id == R.id.nav_dvd_library) {
            getDVD();
        } else if (id == R.id.nav_new_release) {
            getRecent();
        } else if (id == R.id.nav_manage) {
            Intent myIntent = new Intent(MuveeMainActivity.this, MuveeSettings.class);
            //DO not show headers when loading preference
            myIntent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, MuveeSettings.class);
            myIntent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);
            startActivity(myIntent);
        } else if (id == R.id.nav_log_out) {
            logoutAction();
        }

        //Close Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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

    private void rvAppear() {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        rv.startAnimation(anim);
    }

    private void rvDisappear() {
        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(500);
        rv.startAnimation(anim);
    }

    private void logoutAction() {
        new AlertDialog.Builder(MuveeMainActivity.this)
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

                        Intent myIntent = new Intent(MuveeMainActivity.this, MuveeLogin.class);
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
