package edu.gatech.logitechs.movieselector.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

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
        muveeSettings.updateUserProfileServerS(key, value);
        assertEquals(value, muveeSettings.getNewEmail());
        key = "change_password";
        value = "cookies";
        muveeSettings.updateUserProfileServerS(key, value);
        assertEquals(value, muveeSettings.getNewPass());
        key = "change_major";
        value = "0";

        muveeSettings.updateUserProfileServerS(key, value);
        assertEquals("Computer Science", muveeSettings.getCurrUser().getMajor());
        key = "change_description";
        value = "I love android studio";
        muveeSettings.updateUserProfileServerS(key, value);
        assertEquals(value, muveeSettings.getCurrUser().getDescription());
    }
}
