package edu.gatech.logitechs.movieselector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.gatech.logitechs.movieselector.View.MuveeLogin;

import static org.junit.Assert.*;

/**
 * Created by jiangshen on 4/11/16.
 */
public class MuveeLoginTest {

    private MuveeLogin muveeLogin = new MuveeLogin();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testValidPassword() throws Exception {
        assertTrue(muveeLogin.isPasswordValid("abcdefg"));
        assertFalse(muveeLogin.isPasswordValid(""));
        assertFalse(muveeLogin.isPasswordValid("abc"));
        assertFalse(muveeLogin.isPasswordValid("holacomoestasbonjourcommentcava"));
        assertFalse(muveeLogin.isPasswordValid("mnsnej$sms"));
        assertFalse(muveeLogin.isPasswordValid("ta&istois"));
        assertFalse(muveeLogin.isPasswordValid("m#er#dE#"));
    }

    @Test
    public void testValidEmail() throws Exception {
        assertTrue(muveeLogin.isEmailValid("email@email.com"));
        String longString = "";
        for (int i = 0; i < 255; i++) {
            longString += "c";
        }
        assertFalse(muveeLogin.isEmailValid(longString));
        assertFalse(muveeLogin.isEmailValid("email.com"));
        assertFalse(muveeLogin.isEmailValid("email@email"));
        assertFalse(muveeLogin.isEmailValid("email"));
        assertFalse(muveeLogin.isEmailValid("email@email.xyz"));
        String invalidChars = "()<>,;:\\/\"[]{}";
        for (int j = 0; j < invalidChars.length(); j++) {
            char c = invalidChars.charAt(j);
            String testEmail = "email" + c + "email.com";
            assertFalse(muveeLogin.isEmailValid(testEmail));
        }
    }
}
