package edu.gatech.logitechs.movieselector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

import edu.gatech.logitechs.movieselector.Model.User;
import edu.gatech.logitechs.movieselector.R;
import edu.gatech.logitechs.movieselector.View.MuveeSettings;

/**
 * Created by Marcus on 4/10/2016.
 */
public class MuveeSettingsTest {
    private MuveeSettings muveeSettings = new MuveeSettings();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testUpdateUserProfileServer() throws Exception {
        String key;
        String value;
        key = "change_email";
        value = "apple@email.com";
        muveeSettings.updateUserProfileServer(key, value);
        assertEquals(value, muveeSettings.getNewEmail());
        key = "change_password";
        value = "cookies";
        muveeSettings.updateUserProfileServer(key, value);
        assertEquals(value, muveeSettings.getNewPass());
        key = "change_major";
        value = "0";

        String majors[] = new String[8];
        majors[0]= "Computer Science";
        majors[1]= "Electrical Engineering";
        majors[2]= "Mechanical Engineering";
        majors[3]= "Industrial and Systems Engineering";
        majors[4]= "Math";
        majors[5]= "Physics";
        majors[6]= "Chemistry";
        majors[7]= "Chemical Engineering";

        Map<String, Integer > majorToInt = new HashMap<>();
        Map intToMajor = new HashMap<>();

        for (int i = 0; i < majors.length; i++) {
            majorToInt.put(majors[i], i);
            intToMajor.put(i, majors[i]);
        }
        muveeSettings.updateUserProfileServer(key, value);
        assertEquals("Computer Science", muveeSettings.getCurrUser().getMajor());
        key = "change_description";
        value = "I love android studio";
        muveeSettings.updateUserProfileServer(key, value);
        assertEquals(value, muveeSettings.getCurrUser().getDescription());
    }
}
