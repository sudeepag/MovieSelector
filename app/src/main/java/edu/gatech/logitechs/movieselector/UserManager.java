package edu.gatech.logitechs.movieselector;

/**
 * Created by matth_000 on 2/7/2016.
 */
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
        userList.put("bob@email.com", new User("bob@email.com", "12345"));
        Firebase userRef = ref.child("User");
        userRef.addChildEventListener(new ChildEventListener() {
            // Retrieve new posts as they are added to the database
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                User newUser = snapshot.getValue(User.class);
                userList.put(newUser.getEmail(), newUser);

            }
            public void onChildRemoved(DataSnapshot snapshot) {
                User newUser = snapshot.getValue(User.class);
                userList.remove(newUser.getEmail());
            }
            public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {
                User newUser = snapshot.getValue(User.class);
                userList.put(newUser.getEmail(), newUser);
            }
            public void onChildMoved(DataSnapshot snapshot, String previousChildKey) {
                User newUser = snapshot.getValue(User.class);
                userList.put(newUser.getEmail(), newUser);
            }
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
            //... ChildEventListener also defines onChildChanged, onChildRemoved,
            //    onChildMoved and onCanceled, covered in later sections.
        });
    }

    public Map<String, User> getUserList() {
        return userList;
    }
    /**
     * Method to add user to database of users
     *
     * @return true is email is unique and was added properly, false otherwise
     */
    public boolean addUser(User user) {
        String newEmail = user.getEmail();
        for (User curr : userList.values()) {
            if (curr.getEmail().equals(newEmail)) {
                return false;
            }
        }
        Firebase userRef = ref.child("User").child(""+newEmail.hashCode());
        userRef.setValue(user);
        return true;
    }

    /**
     * Authenticate users under the assumption that no duplacte usernames are
     * allowed (enforced in the addUser method above)
     *
     * @return true if the input email and password match, false otherwise
     */
    public boolean authenticateUser(String email, String pass) {
        User curr = userList.get(email);
        if (curr == null) {
            return false;
        }
        return curr.getPassword().equals(pass);
    }
}
