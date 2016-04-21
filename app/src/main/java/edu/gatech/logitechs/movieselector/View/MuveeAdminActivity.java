package edu.gatech.logitechs.movieselector.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.logitechs.movieselector.Controller.UserManager;
import edu.gatech.logitechs.movieselector.Model.User;
import edu.gatech.logitechs.movieselector.R;

public class MuveeAdminActivity extends AppCompatActivity {

    /**
     * The list of user's visible to the admin
     */
    private List<User> users = new ArrayList<>();
    /**
     * Adapter admins and their users
     */
    private final RVAdminAdapter adapter = new RVAdminAdapter(users);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muvee_admin_activity);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final RecyclerView rv = (RecyclerView) findViewById(R.id.users_recycler_view);
        rv.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        initListOfUsers();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Method to initialize the list of visible user's for the admin
     */
    private void initListOfUsers() {
        UserManager.populateUserList(new Runnable() {
            @Override
            public void run() {
                adapter.updateUserList(UserManager.getUserList());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        if (id == R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        logoutAction();
    }

    /**
     * Perform logout
     */
    private void logoutAction() {
        new AlertDialog.Builder(MuveeAdminActivity.this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.prompt_logout)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @TargetApi(11)
                            public void onClick(DialogInterface dialog, int id) {
                                final Intent myIntent = new Intent(MuveeAdminActivity.this, MuveeLogin.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
                                startActivity(myIntent);
                                LoginManager.getInstance().logOut();
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
