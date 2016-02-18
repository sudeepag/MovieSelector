package edu.gatech.logitechs.movieselector;

/**
 * Created by matth_000 on 2/7/2016.
 */
import android.content.Context;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private static Map<String, User> userList = new HashMap<>();
    private static Firebase ref = new Firebase("https://muvee.firebaseio.com/");


    /**
     * cunstructor for user manager
     * creates listener for added changed or deleted user values that updates list of users
     */
    public UserManager() {
//       Firebase userRef = ref.child("User");
//        userRef.addChildEventListener(new ChildEventListener() {
//            // Retrieve new posts as they are added to the database
//            @Override
//            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
//                User newUser = snapshot.getValue(User.class);
//                userList.put(newUser.getEmail(), newUser);
//
//            }
//            public void onChildRemoved(DataSnapshot snapshot) {
//                User newUser = snapshot.getValue(User.class);
//                userList.remove(newUser.getEmail());
//            }
//            public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {
//                User newUser = snapshot.getValue(User.class);
//                userList.put(newUser.getEmail(), newUser);
//            }
//            public void onChildMoved(DataSnapshot snapshot, String previousChildKey) {
//                User newUser = snapshot.getValue(User.class);
//                userList.put(newUser.getEmail(), newUser);
//            }
//            public void onCancelled(FirebaseError firebaseError) {
//                System.out.println("The read failed: " + firebaseError.getMessage());
//            }
//            //... ChildEventListener also defines onChildChanged, onChildRemoved,
//            //    onChildMoved and onCanceled, covered in later sections.
//        });
    }

    public Map<String, User> getUserList() {
        return userList;
    }
    /**
     * Method to add user to database of users
     *
     * @return true is email is unique and was added properly, false otherwise
     */
    public void addUser(final User user, final UserRegistration context, final Runnable runnable) {
        String newEmail = user.getEmail();

        Firebase userRef = ref.child("User");
//        userRef.setValue(user);
//        return true;
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
                System.out.println("Successfully created user account with uid: " + result.get("uid"));

            }
            @Override
            public void onError(FirebaseError firebaseError) {
                System.out.println(firebaseError.getMessage());
                System.out.println("Error2");
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
    public void authenticateUser(String email, String pass, final LoginActivity context, final Runnable runnable) {
        Firebase userRef = ref.child("User");
        userRef.authWithPassword(email, pass, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                context.transition();
                context.finish();

            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                runnable.run();
                // there was an error
            }
        });
//        User curr = userList.get(email);
//        if (curr == null) {
//            return false;
//        }
//        return curr.getPassword().equals(pass);
    }
}
