package edu.gatech.logitechs.movieselector.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.logitechs.movieselector.Controller.UserManager;
import edu.gatech.logitechs.movieselector.Model.User;
import edu.gatech.logitechs.movieselector.R;

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
        UserManager.populateUserList(new Runnable() {
            @Override
            public void run() {
                adapter.updateUserList(UserManager.getUserList());
            }
        });
    }
}
