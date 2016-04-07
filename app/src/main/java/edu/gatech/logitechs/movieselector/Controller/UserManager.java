package edu.gatech.logitechs.movieselector.Controller;

/**
 * Created by matth_000 on 2/7/2016.
 */

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.logitechs.movieselector.Model.Consumer;
import edu.gatech.logitechs.movieselector.Model.User;
import edu.gatech.logitechs.movieselector.View.MuveeRegistration;

public class UserManager {

    /**
     * REF is base reference to target of base firebase url
     */
    private static final Firebase REF = new Firebase("https://muvee.firebaseio.com/");
    /**
     * currentUser is the user currently using the app
     */
    private static User currentUser;
    /**
     * userList is a master list of all users in the system
     */
    private static List<User> userList;
    /**
     * DATASTRING is simply a hardcoded reference to a string containing "data"
     */
    private static final String DATASTRING = "data";
    /**
     * USERSTRING is simply a hardcoded reference to a string containing "users"
     */
    private static final String USERSTRING = "users";

    /**
     * EVENTLISTENER is a reference to the default event listener used in almost all cases
     */
    private static final ChildEventListener EVENTLISTENER = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            currentUser = dataSnapshot.getValue(User.class);
        }
        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            currentUser = dataSnapshot.getValue(User.class);
        }
        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            currentUser = dataSnapshot.getValue(User.class);
        }
        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            currentUser = dataSnapshot.getValue(User.class);
        }
        @Override
        public void onCancelled(FirebaseError firebaseError) {}
    };

    /**
     * Method to add user to database of users
     *
     * @return true is email is unique and was added properly, false otherwise
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * getter for userList
     *
     * @return the userList
     */
    public static List<User> getUserList() {
        return  userList;
    }

    /**
    * Essentially a setter for current user
    *
    * @param user  the new current user
     */
    public static void updateCurrentUser(User user) {
        final Map<String, Object> map = new HashMap<>();
        map.put(DATASTRING, user);
        REF.child(USERSTRING).child(user.getUid()).setValue(map);
        Firebase userRef =REF.child(USERSTRING);
        userRef = userRef.child(currentUser.getUid());
        userRef.addChildEventListener(EVENTLISTENER);

    }

    /**
    * Changes password for the input user
    *
    * @param  user  the user who's password is being changed
    * @param  password  The new password for the user
    * @param  runnable  The Runnable to execute upon completion of the synchronous method
    * @param  consumer  the consumer to handle error messages from firebase
     */
    public static void changePassword(final User user, final String password, final Runnable runnable, final Consumer consumer) {
        REF.changePassword(user.getEmail(), user.getPassword(), password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                user.setPassword(password);
                updateCurrentUser(user);
                runnable.run();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                consumer.consume(firebaseError.getMessage());
            }
        });
    }

    /**
    * Changes password for the input user
    *
    * @param  user  the user who's password is being changed
    * @param  email the new email for the user
    * @param  runnable  The Runnable to execute upon completion of the synchronous method
    * @param  consumer  the consumer to handle error messages from firebase
     */
    public static void changeEmail(final User user, final String email, final Runnable runnable, final Consumer consumer) {
        REF.changeEmail(user.getEmail(), user.getPassword(), email, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                user.setEmail(email);
                updateCurrentUser(user);
                runnable.run();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                consumer.consume(firebaseError.getMessage());
            }
        });
    }

    /**
     * Method to add a user to the local app as well as the server
     *
     * @param user  the user to add
     * @param context  the context in which the user is being added
     * @param runnable  the runnable to run after the server call has been resolved
     */
    public void addUser(final User user, final MuveeRegistration context, final Runnable runnable) {

        final Firebase userRef = REF.child("User");
        userRef.createUser(user.getEmail(), user.getPassword(), new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                REF.authWithPassword(user.getEmail(), user.getPassword(),
                        new Firebase.AuthResultHandler() {
                            @Override
                            public void onAuthenticated(AuthData authData) {
                                onAuthenticatedHelper(user, context, authData);
                            }

                            @Override
                            public void onAuthenticationError(FirebaseError error) {
                                runnable.run();
                            }
                        });
                currentUser = user;
                user.setUid((String) result.get("uid"));
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                runnable.run();
            }
        });
    }

    /**
     * Helper method to authenticate user during adding
     * @param user the user that is being authenticated
     * @param context the context in which the authentication call is being run; completion handler
     * @param authData The data which is being used to authenticate the new user
     */
    public void onAuthenticatedHelper(User user, MuveeRegistration context, AuthData authData) {
        final Map<String, Object> map = new HashMap<>();
        map.put(DATASTRING, user);
        REF.child(USERSTRING).child(authData.getUid()).setValue(map);
        context.transition();
        context.finish();
    }

    /**
     * Authenticate users under the assumption that no duplicate user-names are
     * allowed (enforced in the addUser method above)
     *
     * @param email the user's email
     * @param pass  the user's password
     * @param consumer  Consumer object that acts as completion handler
     */
    public void authenticateUser(String email, String pass, final Consumer consumer) {
        REF.child("User").authWithPassword(email, pass, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(final AuthData authData) {
                REF.child(USERSTRING).child(authData.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (final DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            currentUser = dataSnapshot.getValue(User.class);
                        }
                        consumer.consume("valid");
                        REF.child(USERSTRING).child(authData.getUid()).removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                consumer.consume(firebaseError.getMessage());
            }
        });
    }

    /**
     * Method to lock a user's account by modifying
     *
     * @param id the ID of the user who's account is being locked
     */
    public static void lockUser(String id) {
        final Firebase userRef = REF.child(USERSTRING).child(id).child(DATASTRING).child("locked");
        userRef.setValue(true);
    }

    /**
     * Method to unlock a user's account
     * @param id the ID of the user who's account is being locked
     */
    public static void unlockUser(String id) {
        final Firebase userRef = REF.child(USERSTRING).child(id).child(DATASTRING).child("locked");
        userRef.setValue(false);
    }

    /**
     * method to ban a user
     * @param id the ID of the user being banned
     */
    public static void banUser(String id) {
        final Firebase userRef = REF.child(USERSTRING).child(id).child(DATASTRING).child("banned");
        userRef.setValue(true);
    }

    /**
     * method to unban a user
     * @param id ID of the user being unbanned
     */
    public static void unbanUser(String id) {
        final Firebase userRef = REF.child(USERSTRING).child(id).child(DATASTRING).child("banned");
        userRef.setValue(false);
    }

    /**
     * Method to update the use list from firebase
     *
     * @param runnable runnable to be run on completion of the retrieval to synchronize async call
     */
    public static void populateUserList(final Runnable runnable) {
        userList = new ArrayList<>();
        final Firebase userRef = REF.child(USERSTRING);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (final DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final User user = dataSnapshot.child(DATASTRING).getValue(User.class);
                    if(!user.isAdmin()) {
                        userList.add(dataSnapshot.child(DATASTRING).getValue(User.class));
                    }
                }
                userRef.removeEventListener(this);
                runnable.run();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }
}
