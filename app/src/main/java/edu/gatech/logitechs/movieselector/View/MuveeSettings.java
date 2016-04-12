package edu.gatech.logitechs.movieselector.View;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.logitechs.movieselector.Controller.UserManager;
import edu.gatech.logitechs.movieselector.Model.Consumer;
import edu.gatech.logitechs.movieselector.Model.User;
import edu.gatech.logitechs.movieselector.R;

/**
 * A link that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class MuveeSettings extends AppCompatPreferenceActivity {

    /**
     * The instance of UserManager to interact with backend
     */
    private static UserManager manager;
    /**
     * keeps track of the current user
     */
    private static User currUser;
    /**
     * map to convert the int input from user to a string major
     */
    private static Map<Integer, String> intToMajor;
    /**
     * the new password for the user
     */
    private static String newPass;
    /**
     * the new email for the user
     */
    private static String newEmail;
    /**
     * identifier for change email field
     */
    private static final String CHANGE_EMAIL = "change_email";
    /**
     * identifier for change password field
     */
    private static final String CHANGE_PASSWORD = "change_password";
    /**
     * identifier for change major field
     */
    private static final String CHANGE_MAJOR = "change_major";
    /**
     * identifier for change description field
     */
    private static final String CHANGE_DESCRIPTION = "change_description";
    /**
     * identifier for number of majors
     */
    private static final int NUM_MAJORS = 8;

//    /**
//     * The preferences for all of the view files
//     */
//    private static SharedPreferences copiedSp;
//    /**
//     * The preferences for all of the edit fields in view files
//     */
//    private static SharedPreferences.Editor copiedEditor;


    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            final String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                final ListPreference listPreference = (ListPreference) preference;
                final int index = listPreference.findIndexOfValue(stringValue);
                updateUserProfileServer(preference.getKey(), String.valueOf(index));
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
            } else if (preference instanceof EditTextPreference) {
                updateUserProfileServer(preference.getKey(), stringValue);
                preference.setSummary(stringValue);
            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Updates the user whenever there is a value change
     * @param key       the preference key
     * @param value     the preference value
     */
    public static void updateUserProfileServer(String key, String value) {
        switch (key) {
            case CHANGE_EMAIL:
                newEmail = value;
                break;
            case CHANGE_PASSWORD:
                newPass = value;
                break;
            case CHANGE_MAJOR:
                currUser.setMajor(intToMajor.get(Integer.valueOf(value)));
                break;
            case CHANGE_DESCRIPTION:
                currUser.setDescription(value);
                break;
        }
    }

    /**
     * Returns the new password
     * @return String representation of new password
     */
    public String getNewPass() {
        return newPass;
    }

    /**
     * Returns the new email
     * @return String representation of the new email
     */
    public String getNewEmail() {
        return newEmail;
    }

    /**
     * Returns the current user
     * @return User object representation of the user
     */
    public User getCurrUser() {
        return currUser;
    }

    @Override
    protected void onDestroy() {
//        TODO update the current User to the server here
        if(newPass != null || newEmail != null){
            if (newPass != null) {
                manager.changePassword(currUser, newPass, new Runnable() {
                    @Override
                    public void run() {
                        // success
                    }
                }, new Consumer() {
                    @Override
                    public void consume(String input) {
                    }
                });
            }
            if (newEmail != null) {
                manager.changeEmail(currUser, newEmail, new Runnable() {
                    @Override
                    public void run() {
                        // success
                    }}, new Consumer() {
                    @Override
                    public void consume(String input) {
                        //put error scenario for change email
                    }
                });
            }
        } else {
            manager.updateCurrentUser(currUser);
        }
        super.onDestroy();
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     * @param context the context of the screen being used
     * @return true if the device has an extra-large screen
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     *
     * @param preference new preference of the user
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String test = "test";
        super.onCreate(savedInstanceState);
        setupActionBar();

        updateUserProfileServerS(test, test);

        manager = new UserManager();
        currUser = manager.getCurrentUser();

        final String majors[] = getResources().getStringArray(R.array.pref_example_list_titles);
        final Map<String, Integer> majorToInt = new HashMap<>();
        intToMajor = new HashMap<>();

        for (int i = 0; i < majors.length; i++) {
            majorToInt.put(majors[i], i);
            intToMajor.put(i, majors[i]);
        }

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MuveeSettings.this);
        final SharedPreferences.Editor editor = sp.edit();
        editor.putString(CHANGE_EMAIL, newEmail == null ? currUser.getEmail() : newEmail);
        editor.putString(CHANGE_PASSWORD, newPass == null ? currUser.getPassword() : newPass);
        editor.putString(CHANGE_MAJOR, String.valueOf(majorToInt.get(currUser.getMajor())));
        editor.putString(CHANGE_DESCRIPTION, currUser.getDescription());
        editor.commit();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        final int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     *
     * @param fragmentName the fragment being confirmed
     * @return true if the fragment is valid
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || EditProfilePreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * Updates the user whenever there is a value change
     * @param key       the preference key
     * @param value     the preference value
     */
    public static void updateUserProfileServerS(String key, String value) {
        currUser = new User();
        final String majors[] = new String[NUM_MAJORS];
        int q = 0;
        majors[q]= "Computer Science";
        majors[++q]= "Electrical Engineering";
        majors[++q]= "Mechanical Engineering";
        majors[++q]= "Industrial and Systems Engineering";
        majors[++q]= "Math";
        majors[++q]= "Physics";
        majors[++q]= "Chemistry";
        majors[++q]= "Chemical Engineering";

        final Map<String, Integer > majToInt = new HashMap<>();
        final Map<Integer, String> intToMaj = new HashMap<>();

        for (int i = 0; i < majors.length; i++) {
            majToInt.put(majors[i], i);
            intToMaj.put(i, majors[i]);
        }

        switch (key) {
            case CHANGE_EMAIL:
                newEmail = value;
                break;
            case CHANGE_PASSWORD:
                newPass = value;
                break;
            case CHANGE_MAJOR:
                currUser.setMajor(intToMaj.get(Integer.valueOf(value)));
                break;
            case CHANGE_DESCRIPTION:
                currUser.setDescription(value);
                break;
        }
    }

    public static class EditProfilePreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_edit_account);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference(CHANGE_EMAIL));
            bindPreferenceSummaryToValue(findPreference(CHANGE_PASSWORD));
            bindPreferenceSummaryToValue(findPreference(CHANGE_MAJOR));
            bindPreferenceSummaryToValue(findPreference(CHANGE_DESCRIPTION));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {final int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), MuveeSettings.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


}
