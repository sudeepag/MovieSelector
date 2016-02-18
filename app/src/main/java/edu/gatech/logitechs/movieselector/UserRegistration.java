package edu.gatech.logitechs.movieselector;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class UserRegistration extends AppCompatActivity implements LoaderCallbacks<Cursor>{
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    //private static final int REQUEST_READ_CONTACTS = 0;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private EditText mDescriptionView;
    private Spinner mMajorsView;
    private View mProgressView;
    private View mSignupFormView;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        //custom fonts for welcome text
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/akaDora.ttf");
        TextView tv = (TextView) findViewById(R.id.signup_text);
        tv.setTypeface(tf);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        //populateAutoComplete();
        mPasswordView = (EditText) findViewById(R.id.password);
        mConfirmPasswordView = (EditText) findViewById(R.id.confirm_password);
        mDescriptionView = (EditText) findViewById(R.id.profile_description);
        mDescriptionView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_ACTION_DONE) {
                    System.out.println("asd");
                    return true;
                }
                return false;
            }
        });

        mMajorsView = (Spinner)findViewById(R.id.drop_majors);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.majors_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMajorsView.setAdapter(adapter);

        //sign up button
        Button mEmailSignUpButton = (Button) findViewById(R.id.email_sign_up_button);
        mEmailSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptLogin();
                attemptSignUp();
            }
        });

        final TextView linkCancel = (TextView) findViewById(R.id.login_link);
        linkCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(UserRegistration.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        linkCancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // change the background color
                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        linkCancel.setTextColor(Color.rgb(189, 189, 189));
                        break;
                    case MotionEvent.ACTION_UP:
                        linkCancel.setTextColor(Color.rgb(224, 224, 224));
                        break;
                }
                return false;
            }
        });

        mSignupFormView = findViewById(R.id.signup_form);
        mProgressView = findViewById(R.id.signup_progress);
    }

    /**
     * Attempts to sign up.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptSignUp() {
        if (mAuthTask != null) {
            return;
        }
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirmPassword = mConfirmPasswordView.getText().toString();
        String major = mMajorsView.getSelectedItem().toString();
        String description = mDescriptionView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        TextView majorErrorText = (TextView)mMajorsView.getSelectedView();
        majorErrorText.setError(null);

//        //checks if description was written
//        if (description.length() > 0) {
//
//        }

        //checks if major was picked
        if (!isMajorValid(major)) {
            majorErrorText.setError("You must choose a major");
            majorErrorText.setText("You must choose a major");
            focusView = mMajorsView;
            cancel = true;
        }

        //checks for valid password/confirm password
         if (TextUtils.isEmpty(password)) {
            //if password is empty
            mPasswordView.setError(getString(R.string.error_empty_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (TextUtils.isEmpty(confirmPassword)) {
            //if password is empty
            mConfirmPasswordView.setError(getString(R.string.error_empty_password));
            focusView = mConfirmPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(confirmPassword)) {
             mConfirmPasswordView.setError(getString(R.string.error_invalid_password));
             focusView = mConfirmPasswordView;
             cancel = true;
         } else if (!password.equals(confirmPassword)) {
             mConfirmPasswordView.setError(getString(R.string.error_password_mismatch));
             focusView = mConfirmPasswordView;
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
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            //transition to loading view
            showProgress(true);

            //start authentication
            UserManager manager = new UserManager();
            manager.addUser(new User(email, password, major, description), UserRegistration.this, new Runnable() {
                @Override
                public void run() {
                    showProgress(false);
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
            });
//            mAuthTask = new UserLoginTask(email, password, major, description);
//            mAuthTask.execute((Void) null);
        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Validate EMAIL
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        //TODO: Validate PASSWORD
        return password.length() > 4;
    }

    private boolean isMajorValid(String major) {
        return !major.equals("Choose a Major");
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        //isLoading = show;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_longAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignupFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mSignupFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

   @Override
   public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
       return new CursorLoader(this,
               // Retrieve data rows for the device user's 'profile' contact.
               Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                       ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

               // Select only email addresses.
               ContactsContract.Contacts.Data.MIMETYPE +
                       " = ?", new String[]{ContactsContract.CommonDataKinds.Email
               .CONTENT_ITEM_TYPE},

               // Show primary email addresses first. Note that there won't be
               // a primary email address if the user hasn't specified one.
               ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
   }
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(UserRegistration.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    public void transition() {
        Intent myIntent = new Intent(this,MainActivity.class);
        this.startActivity(myIntent);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mMajor;
        private final String mDescription;

        /**
         * The parameters for the userlogintask
         * @param email String of the users email
         * @param password String of the password of the user
         * @param major String of the user's major
         * @param descrption String of a short description of the user
         */
        UserLoginTask(String email, String password, String major, String descrption) {
            mEmail = email;
            mPassword = password;
            mMajor = major;
            mDescription = descrption;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            UserManager manager = new UserManager();
            // Create the user

            manager.addUser(new User(mEmail, mPassword, mMajor, mDescription), UserRegistration.this, new Runnable() {
                @Override
                public void run() {
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
            });
            // Authenticate the user
            return true;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                //go onto next activity if suceess

            } else {
                new AlertDialog.Builder(UserRegistration.this)
                        .setTitle(R.string.app_name)
                        .setMessage(R.string.prompt_registration_error)
                        .setPositiveButton("Okay",
                                new DialogInterface.OnClickListener() {
                                    @TargetApi(11)
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .show();
                mEmailView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
