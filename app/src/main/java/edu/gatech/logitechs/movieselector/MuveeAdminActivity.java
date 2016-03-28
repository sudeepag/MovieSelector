package edu.gatech.logitechs.movieselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

public class MuveeAdminActivity extends AppCompatActivity {

    RecyclerView rv;

    private List<User> users = new ArrayList<>();
    public final RVAdminAdapter adapter = new RVAdminAdapter(users);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muvee_admin_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rv = (RecyclerView) findViewById(R.id.users_recycler_view);
        rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        initListOfUsers();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initListOfUsers() {
        UserManager.poppulateUserList(this, new Runnable() {
            @Override
            public void run() {
                adapter.updateUserList(UserManager.getUserList());
            }
        });
    }
}
