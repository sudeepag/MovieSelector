package edu.gatech.logitechs.movieselector.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
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

import edu.gatech.logitechs.movieselector.Controller.UserManager;
import edu.gatech.logitechs.movieselector.Model.User;
import edu.gatech.logitechs.movieselector.R;

public class MuveeRegistration extends AppCompatActivity{

    /**
     * Autocomplete text view for the email
     */
    private AutoCompleteTextView mEmailView;
    /**
     * text view for password
     */
    private EditText mPasswordView;
    /**
     * text view to confirm password
     */
    private EditText mConfirmPasswordView;
    /**
     * description text view
     */
    private EditText mDescriptionView;
    /**
     * majot spinner
     */
    private Spinner mMajorsView;
    /**
     * view for progress
     */
    private View mProgressView;
    /**
     * view for the signup form
     */
    private View mSignupFormView;

    /**
     * color for links
     */
    private static final int LINK_COLOR = Color.argb(160, 189, 189, 189);

    /**
     * color for canceling link
     */
    private static final int LINK_CANCEL_COLOR =Color.argb(255, 224, 224, 224);

    /**
     * target api level
     */
    private static final int API_LEVEL = 11;
    /**
     * minimum password length
     */
    private static final int MINIMUM_PASSWORD_LENGTH = 11;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muvee_registration_activity);

        //custom fonts for welcome text
        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/akaDora.ttf");
        final TextView tv = (TextView) findViewById(R.id.signup_text);
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
                    return true;
                }
                return false;
            }
        });

        mMajorsView = (Spinner)findViewById(R.id.drop_majors);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.majors_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMajorsView.setAdapter(adapter);

        //sign up button
        final Button mEmailSignUpButton = (Button) findViewById(R.id.email_sign_up_button);
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
                final Intent intent = new Intent(MuveeRegistration.this, MuveeLogin.class);
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
                        linkCancel.setTextColor(LINK_COLOR);
                        break;
                    case MotionEvent.ACTION_UP:
                        linkCancel.setTextColor(LINK_CANCEL_COLOR);
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
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String confirmPassword = mConfirmPasswordView.getText().toString();
        final String major = mMajorsView.getSelectedItem().toString();
        final String description = mDescriptionView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        final TextView majorErrorText = (TextView)mMajorsView.getSelectedView();
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
        } else if (isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else {
            mConfirmPasswordView.setError(isPasswordValid(confirmPassword) ? getString(R.string.error_invalid_password) : getString(R.string.error_password_mismatch));
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
            final View view = this.getCurrentFocus();
            if (view != null) {
                final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            //transition to loading view
            showProgress(true);

            //start authentication
            final UserManager manager = new UserManager();
            manager.addUser(new User(email, password, major, description), MuveeRegistration.this, new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(MuveeRegistration.this)
                            .setTitle(R.string.app_name)
                            .setMessage(R.string.prompt_registration_error)
                            .setPositiveButton("Okay",
                                    new DialogInterface.OnClickListener() {
                                        @TargetApi(API_LEVEL)
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    })
                            .show();
                    mEmailView.requestFocus();
                    showProgress(false);
                }
            });
        }
    }

    /**
     * Validates the user's input for the email field
     * @param email the input email to check
     * @return true if it is a valid email
     */
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * Validator the user's input password
     * @param password the user's input password
     * @return true if the password meets requirements
     */
    private boolean isPasswordValid(String password) {
        return password.length() <= MINIMUM_PASSWORD_LENGTH;
    }

    /**
     * validator for the major field
     * @param major the user's input major
     * @return true if the input is a valid major
     */
    private boolean isMajorValid(String major) {
        return !("Choose a Major").equals(major);
    }

    /**
     * Shows the progress UI and hides the login form.
     * @param show the object containing what is currently visible to the user
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        //isLoading = show;
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

    /**
     * Transitions to the main movie activity
     */
    public void transition() {
        final Intent myIntent = new Intent(this,MuveeMainActivity.class);
        this.startActivity(myIntent);
    }
}
