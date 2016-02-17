package edu.gatech.logitechs.movieselector;

/**
 * Created by akhilesh on 2/7/16.
 */
public class User {
    private String email;
    private String password;
    private String major;
    private String description;

    /**
     * construr for user
     */
    public User() {
        super();
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
