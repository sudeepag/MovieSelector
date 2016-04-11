package edu.gatech.logitechs.movieselector.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;

import android.view.View.OnTouchListener;

import edu.gatech.logitechs.movieselector.Controller.UserManager;
import edu.gatech.logitechs.movieselector.Model.Consumer;
import edu.gatech.logitechs.movieselector.R;


/**
 * Müveé Login Screen
 */
public class MuveeLogin extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    /**
     * TextView for email
     */
    private AutoCompleteTextView mEmailView;
    /**
     * text field for password
     */
    private EditText mPasswordView;
    /**
     * view for progress(indicate waitng screen)
     */
    private View mProgressView;
    /**
     * view for the entire login form
     */
    private View mLoginFormView;

    /**
     * variable to keep track of if the screen should be frozen when loading
     */
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muvee_login_activity);

        //Linking UI to Code

        //Custom fonts for welcome text
        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/akaDora.ttf");
        final TextView tv = (TextView) findViewById(R.id.welcome_text);
        tv.setTypeface(tf);

        final int color1 = 160;
        final int color2 = 189;
        final int color3 = 255;
        final int color4 = 224;
        final TextView linkSignup = (TextView) findViewById(R.id.signup_link);
        //Change the text color when the user touches it
        linkSignup.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        linkSignup.setTextColor(Color.argb(color1, color2, color2, color2));
                        break;
                    case MotionEvent.ACTION_UP:
                        linkSignup.setTextColor(Color.argb(color3, color4, color4, color4));
                        break;
                }
                return false;
            }
        });
        linkSignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                final Intent intent = new Intent(getApplicationContext(), MuveeRegistration.class);
                //startActivityForResult(intent, REQUEST_SIGNUP);
                MuveeLogin.this.startActivity(intent);
            }
        });

        //Set up Firebase
        Firebase.setAndroidContext(this);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        final Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        //Code Initializers
        isLoading = false;
    }

    @Override
    public void onBackPressed() {
        //first bring down the keyboard
        final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        final int defaultAPI = 11;
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        if (isLoading) {
            //system loading pressed back button -> do cancel async task action;
            mAuthTask.cancel(true);
        } else {
            new AlertDialog.Builder(MuveeLogin.this)
                    .setTitle(R.string.app_name)
                    .setMessage(R.string.prompt_exit)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @TargetApi(defaultAPI)
                                public void onClick(DialogInterface dialog, int id) {
                                    System.exit(0);
                                }
                            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @TargetApi(defaultAPI)
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).show();
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            //if password is empty
            mPasswordView.setError(getString(R.string.error_empty_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            //Force bring down the keyboard
            // Check if no view has focus:
            final View view = this.getCurrentFocus();
            if (view != null) {
                final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            //transition to loading view
            showProgress(true);

            //start authentication
            final UserManager manager = new UserManager();
            manager.authenticateUser(email, password, new Consumer() {

                @Override
                public void consume(String message) {
                    showProgress(false);
                    if ("valid".equals(message)) {
                        transition();
                    } else {
                        mPasswordView.setError(message);
                        mPasswordView.requestFocus();
                    }
                }
            });
        }
    }

    /**
     * Simple Email Validation when user inputs on the UI
     * @param email     the email to validate
     * @return          a boolean that tells whether the email is in properly formatted
     */
    public boolean isEmailValid(String email) {

        if (email.length() > 254) {
            return false;
        }
        if (!email.contains("@")) {
            return false;
        }
        if (!email.contains(".")) {
            return false;
        }
        String invalidChars = "()<>,;:\\/\"[]{}";
        for (int i = 0; i < email.length(); i++) {
            if (invalidChars.contains(String.valueOf(email.charAt(i)))) {
                return false;
            }
        }
        String[] elements = email.split("\\.");
        String tld = elements[elements.length - 1];
        final String[] validTlds = {"com", "edu", "org", "gov"};
        boolean isValid = false;
        for (int j = 0; j < validTlds.length; j++) {
            if (validTlds[j].equals(tld)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Simple Email Validation when user inputs on the UI
     * @param password      the password to validate
     * @return              a boolean that tells whether the password is long enough
     */
    public boolean isPasswordValid(String password) {
        final int minpasslength = 4;
        final int maxpasslength = 20;

        final char dollar = '$';
        final char hex = '#';
        final char and = '&';

        if (password.length() < minpasslength || password.length() > maxpasslength) {
            return false;
        }

        for (int i = 0;i < password.length(); i++) {
            final char c = password.charAt(i);
            if (c == dollar) {
                return false;
            } else if (c == hex) {
                return false;
            } else if (c == and) {
                return false;
            }
        }
        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     * @param show object containing what is currently visible to the user
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        isLoading = show;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            final int shortAnimTime = getResources().getInteger(android.R.integer.config_longAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Transition to the main Movies page after login
     */
    public void transition() {
        Intent myIntent;
        if (UserManager.getCurrentUser().isAdmin()) {
            myIntent = new Intent(this,MuveeAdminActivity.class);
            this.startActivity(myIntent);
        } else {
            if (UserManager.getCurrentUser().isBanned()) {
                showProgress(false);
                mPasswordView.setError("User is Banned");
                mPasswordView.requestFocus();
            } else if (UserManager.getCurrentUser().isLocked()) {
                showProgress(false);
                mPasswordView.setError("User is Locked");
                mPasswordView.requestFocus();
            } else {
                myIntent = new Intent(this,MuveeMainActivity.class);
                this.startActivity(myIntent);
            }
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        /**
         * The string representation of the user's email
         */
        private final String mEmail;
        /**
         * The string representation of the user's password
         */
        private final String mPassword;

        /**
         * Initializes a new UserLoginTask
         * @param email         user's email
         * @param password      user's password
         */
        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final int sleeplength = 1000;
            try {
                Thread.sleep(sleeplength);
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                //TODO change this to
                //if (user.isAdmin())
                if ("admin@admin.com".equals(mEmail) && "admin".equals(mPassword)) {
                    finish();
                    final Intent myIntent = new Intent(MuveeLogin.this,MuveeAdminActivity.class);
                    MuveeLogin.this.startActivity(myIntent);
                } else {
                    finish();
                    final Intent myIntent = new Intent(MuveeLogin.this,MuveeMainActivity.class);
                    MuveeLogin.this.startActivity(myIntent);
                }
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
