package edu.gatech.logitechs.movieselector;

import android.os.Bundle;
import android.app.Activity;

public class MuveeAdminUserPrivileges extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muvee_admin_user_privileges);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
