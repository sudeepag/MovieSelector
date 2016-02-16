package edu.gatech.logitechs.movieselector;

/**
 * Created by akhilesh on 2/7/16.
 */
public class User {
    private String email;
    private String password;
    private String major;
    private String description;

    public User() {
        super();
    }
    public User(String email, String password, String major, String description) {
        this.email = email;
        this.password = password;
        this.major = major;
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getMajor() {
        return major;
    }

    public String getDescription() {
        return description;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
