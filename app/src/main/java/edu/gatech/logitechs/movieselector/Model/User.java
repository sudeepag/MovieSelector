package edu.gatech.logitechs.movieselector.Model;

/**
 * Created by akhilesh on 2/7/16.
 */
public class User {
    /** email for a given user */
    private String email;
    /** password for a given user */
    private String password;
    /** description for a given user */
    private String description;
    /** major for a given user */
    private String major;
    /** firebase id for a given user */
    private String uid;
    /** admin status for a given user */
    private boolean admin;
    /** locked status for a given user */
    private boolean locked;
    /** banned status for a given user */
    private boolean banned;

    /**
     * User constructor. Only here because required for use by FireBase
     */
    public User() {
        super();
    }

    /**
     * Chained constructor for user containing only essential user information
     * @param email String of user email
     * @param password String of user password
     */
    public User(String email, String password) {
        this(email, password, null, null);
    }

    /**
     * constructor for user
     * @param email String of user email
     * @param password String of user password
     * @param major String of user major
     * @param description String of user description
     */
    public User(String email, String password, String major, String description) {
        this.email = email;
        this.password = password;
        this.major = major;
        this.description = description;
    }



    /**
     * getter for boolean isAdmin
     *
     * @return value for isAdmin
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Setter for the admin value
     * @param value the new admin boolean
     */
    public void setAdmin(boolean value) {
        admin = value;
    }

    /**
     * getter for the locked boolean
     * @return the locked boolean
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * setter for the locken boolean
     * @param value the new value for the locked variable
     */
    public void setLocked(boolean value) {
        locked = value;
    }

    /**
     * getter for the banned boolean
     * @return the value of the banned variable
     */
    public boolean isBanned() {
        return banned;
    }

    /**
     * Setter for the banned variable
     * @param value the new value for the banned boolean
     */
    public void setBanned(boolean value) {
        banned = value;
    }
    /**
     * get the email of the user
     * @return String representation of user email
     */
    public String getEmail() {
        return email;
    }


     /**
     * get the password of the user
     * @return String representation of user password
     */
    public String getPassword() {
        return password;
    }

    /**
     * getter for uid
     * @return the userID for the user
     */
    public String getUid() {
        return uid;
    }

    /**
     * setter for uid
     * @param uid the UserID of the user
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * get the major of the user
     * @return String representation of the user major
     */
    public String getMajor() {
        return major;
    }

    /**
     * get the description of the user
     * @return String representation of the user description
     */
    public String getDescription() {
        return description;
    }

    /**
     * set the password of the user
     * @param password String of user password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * set the email of the user
     * @param email String of user email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * set the major of the user
     * @param major String of the major
     */
    public void setMajor(String major) {
        this.major = major;
    }

    /**
     * set the description of the user
     * @param description String of the description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
