package edu.gatech.logitechs.movieselector;



import android.content.Context;
import android.test.ServiceTestCase;

import com.firebase.client.Firebase;

import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Method;

import edu.gatech.logitechs.movieselector.Controller.UserManager;
import edu.gatech.logitechs.movieselector.Model.User;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void firebaseeTest() {
        Firebase.setAndroidContext(getTestContext());
        UserManager mngr = new UserManager();
        mngr.addUser(new User("robert@email.com", "12345"));
        assertTrue(mngr.authenticateUser("robert@email.com", "12345"));
    }

    private Context getTestContext()
    {
        try
        {
            Method getTestContext = ServiceTestCase.class.getMethod("getTestContext");
            return (Context) getTestContext.invoke(this);
        }
        catch (final Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
    }
}