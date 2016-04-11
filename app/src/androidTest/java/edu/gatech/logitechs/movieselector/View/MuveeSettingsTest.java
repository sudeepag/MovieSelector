package edu.gatech.logitechs.movieselector.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import edu.gatech.logitechs.movieselector.Model.User;
import edu.gatech.logitechs.movieselector.View.MuveeSettings;

/**
 * Created by Marcus on 4/10/2016.
 */
public class MuveeSettingsTest {
    private MuveeSettings muveeSettings = new MuveeSettings();
    private User user = new User();

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
        muveeSettings.updateUserProfileServer(key, value);
        assertEquals("Computer Science", user.getMajor());
        key = "change_description";
        value = "I love android studio";
        muveeSettings.updateUserProfileServer(key, value);
        assertEquals(value, user.getDescription());
    }
}
