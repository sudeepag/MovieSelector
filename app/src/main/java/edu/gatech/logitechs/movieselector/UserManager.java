package edu.gatech.logitechs.movieselector;

/**
 * Created by matth_000 on 2/7/2016.
 */
import java.util.HashMap;
import java.util.Map;

public class UserManager {

    static Map<String, User> userList = new HashMap<>();

    public UserManager() {
        userList.put("Bob", new User("Bob", "1234"));
    }
    public Map<String, User> getUserList() {
        return userList;
    }

    /**
     * Method to add user to database of users
     * ab
     * @return true is email is unique and was added properly, false otherwise
     */
    public boolean addUser(User user) {
        String newEmail = user.getEmail();
        for (User curr : userList.values()) {
            if (curr.getEmail().equals(newEmail)) {
                return false;
            }
        }
        userList.put(newEmail, user);
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