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
        assertTrue(muveeLogin.isPasswordValid("ćçßš*()-={}8kŁ;Ÿ"));
    }
}
