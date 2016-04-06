package edu.gatech.logitechs.movieselector.View;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import edu.gatech.logitechs.movieselector.Model.User;
import edu.gatech.logitechs.movieselector.R;

public class MuveeAdminUserPriv extends AppCompatActivity {

    private TextView banTextView;
    private TextView lockTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muvee_admin_user_priv_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        banTextView = (TextView)findViewById(R.id.ban_text);
        banTextView.setVisibility(View.INVISIBLE);

        lockTextView = (TextView)findViewById(R.id.ban_text);
        lockTextView.setVisibility(View.INVISIBLE);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void changeBanState(View view) {
        boolean checked = ((ToggleButton)view).isChecked();
        if (checked) {
            banTextView.setText("unban");
            banTextView.setVisibility(View.VISIBLE);
        } else {
            banTextView.setText("ban");
        }
    }

    public void changeLockState(View view) {
        boolean checked = ((ToggleButton)view).isChecked();
        if (checked) {
            lockTextView.setText("unlock");
            lockTextView.setVisibility(View.VISIBLE);
        } else {
            lockTextView.setText("lock");
        }
    }

}
