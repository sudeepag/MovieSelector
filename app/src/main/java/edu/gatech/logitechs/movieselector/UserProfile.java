package edu.gatech.logitechs.movieselector;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class UserProfile extends AppCompatPreferenceActivity {

    private static UserManager manager;
    private static User currUser;

    private static Map<String, Integer> majorToInt;
    private static Map<Integer, String> intToMajor;
    String majors[];

    SharedPreferences sp;
    SharedPreferences.Editor editor;


    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                updateUserProfileServer(preference.getKey(), String.valueOf(index));

                // Set the summary to reflect the new value.
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
    private static void updateUserProfileServer(String key, String value) {
        if (key.equals("change_email")) {
            currUser.setEmail(value);
        } else if (key.equals("change_password")) {
            currUser.setPassword(value);
        } else if (key.equals("change_major")) {
            currUser.setMajor(intToMajor.get(Integer.valueOf(value)));
        } else if (key.equals("change_description")) {
            currUser.setDescription(value);
        }
    }

    @Override
    protected void onDestroy() {
//        TODO update the current User to the server here
        System.out.println();
        manager.updateCurrentUser(currUser);
        super.onDestroy();
    }

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
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
        super.onCreate(savedInstanceState);
        setupActionBar();

        manager = new UserManager();
        currUser = manager.getCurrentUser();

        majors = getResources().getStringArray(R.array.pref_example_list_titles);
        majorToInt = new HashMap<>();
        intToMajor = new HashMap<>();

        for (int i = 0; i < majors.length; i++) {
            majorToInt.put(majors[i],i);
            intToMajor.put(i, majors[i]);
        }

        sp = PreferenceManager.getDefaultSharedPreferences(UserProfile.this);
        editor = sp.edit();
        editor.putString("change_email", currUser.getEmail());
        editor.putString("change_password", currUser.getPassword());
        editor.putString("change_major", String.valueOf(majorToInt.get(currUser.getMajor())));
        editor.putString("change_description", currUser.getDescription());
        editor.commit();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
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
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || EditProfilePreferenceFragment.class.getName().equals(fragmentName);
    }

    public static class EditProfilePreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_edit_profile);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("change_email"));
            bindPreferenceSummaryToValue(findPreference("change_password"));
            bindPreferenceSummaryToValue(findPreference("change_major"));
            bindPreferenceSummaryToValue(findPreference("change_description"));

//            SharedPreferences sp = getPreferenceScreen().getSharedPreferences();

//            EditTextPreference change_email_pref = (EditTextPreference) findPreference("change_email");
//            change_email_pref.setSummary(sp.getString("change_email", "FERRK UUUU"));

//            EditText edit = ((EditTextPreference) findPreference("change_password")).getEditText();
//            String pref = edit.getTransformationMethod().getTransformation(objValue.toString(), edit).toString();
//            prePreference.setSummary(pref);

        }

        @Override
         public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), UserProfile.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
