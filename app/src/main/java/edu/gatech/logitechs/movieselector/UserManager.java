package edu.gatech.logitechs.movieselector;

/**
 * Created by matth_000 on 2/7/2016.
 */

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private static Firebase ref = new Firebase("https://muvee.firebaseio.com/");
    public static User currentUser;
    private static ChildEventListener eventListener = new ChildEventListener() {

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
        public void onCancelled(FirebaseError firebaseError) {
            System.out.println("The read failed: " + firebaseError.getMessage());
        }
    };


    /**
     * constructor for user manager
     */
    public UserManager() {}

    /**
     * Method to add user to database of users
     *
     * @return true is email is unique and was added properly, false otherwise
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /*
    * Essentially a setter for current user
    *
    * @param user  the new current user
     */
    public static void updateCurrentUser(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", user);
        ref.child("users").child(user.getUID()).setValue(map);
        Firebase userRef = ref.child("users");
        userRef = userRef.child(currentUser.getUID());
        userRef.addChildEventListener(eventListener);

    }

    /*
    * Changes password for the input user
    *
    * @param  user  the user who's password is being changed
    * @param  password  The new password for the user
    * @param  runnable  The Runnable to execute upon completion of the synchronous method
    * @param  consumer  the consumer to handle error messages from firebase
     */
    public static void changePassword(final User user, final String password, final Runnable runnable, final Consumer consumer) {
        ref.changePassword(user.getEmail(), user.getPassword(), password, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                user.setPassword(password);
                updateCurrentUser(user);
                System.out.println("changed password");
                runnable.run();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                System.out.println("failed changed password");
                System.out.println(firebaseError.getMessage());
                consumer.consume(firebaseError.getMessage());
            }
        });
    }

    /*
    * Changes password for the input user
    *
    * @param  user  the user who's password is being changed
    * @param  password  The new password for the user
    * @param  runnable  The Runnable to execute upon completion of the synchronous method
    * @param  consumer  the consumer to handle error messages from firebase
     */
    public static void changeEmail(final User user, final String email, final Runnable runnable,final Consumer consumer) {
        ref.changeEmail(user.getEmail(), user.getPassword(), email, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                user.setEmail(email);
                updateCurrentUser(user);
                System.out.println("changed email");
                runnable.run();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                System.out.println(firebaseError.getCode());
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
        String newEmail = user.getEmail();

        Firebase userRef = ref.child("User");
        userRef.createUser(user.getEmail(), user.getPassword(), new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                ref.authWithPassword(user.getEmail(), user.getPassword(),
                        new Firebase.AuthResultHandler() {
                            @Override
                            public void onAuthenticated(AuthData authData) {
                                // Authentication just completed successfully :)
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("data", user);
                                ref.child("users").child(authData.getUid()).setValue(map);
                                context.transition();
                                context.finish();
                            }

                            @Override
                            public void onAuthenticationError(FirebaseError error) {
                                runnable.run();
                                // Something went wrong :(
                            }
                        });
                currentUser = user;
                user.setUID((String) result.get("uid"));
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                System.out.println(firebaseError.getMessage());
                System.out.println("Error2");
                runnable.run();
                // there was an error
            }
        });
    }

    /**
     * Authenticate users under the assumption that no duplacte usernames are
     * allowed (enforced in the addUser method above)
     *
     * @return true if the input email and password match, false otherwise
     */
    public void authenticateUser(String email, String pass, final MuveeLogin context, final Runnable runnable) {
        Firebase userRef = ref.child("User");
        userRef.authWithPassword(email, pass, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Firebase userRef = ref.child("users");
                userRef = userRef.child(authData.getUid());
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                            currentUser = dataSnapshot.getValue(User.class);
                        context.transition();
                        context.finish();
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });

            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                runnable.run();
                // there was an error
            }
        });
    }
}
