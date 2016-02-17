package edu.gatech.logitechs.movieselector;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.junit.Test;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Test()
    public void firebaseeTest() {
        UserManager mngr = new UserManager();
        mngr.addUser(new User("robert@email.com", "12345"));
        assertTrue(mngr.authenticateUser("robert@email.com", "12345"));
    }

}