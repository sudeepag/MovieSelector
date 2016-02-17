package edu.gatech.logitechs.movieselector;

/**
 * Created by akhilesh on 2/7/16.
 */
public class User {
    private String email;
    private String password;
    private String description;
    private Majors major;

    /**
     * User constructor. Only here because requried for use by FireBase
     */
    public User() {
        super();
    }

    /**
     * User constructor: requires only minimum information
     * @param email  user's email; also used for username
     * @param password user's password
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Setter for description
     * @param description  user's description of themselves
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Setter for the major of a given user
     *
     * @param name  takes in UPPERCASE, abbreviated name of major
     * @return true if the major was added correctly, false if major was not member of enum
     */
    public boolean setMajor(String name) {
        try {
            if (Majors.valueOf(name) instanceof Majors) {
                if (Majors.CS == Majors.valueOf(name)) {
                    this.major = Majors.CS;
                } else if (Majors.EE == Majors.valueOf(name)) {
                    this.major = Majors.EE;
                } else if (Majors.ISYE == Majors.valueOf(name)) {
                    this.major = Majors.ISYE;
                } else if (Majors.MATH == Majors.valueOf(name)) {
                    this.major = Majors.MATH;
                } else if (Majors.PHYS == Majors.valueOf(name)) {
                    this.major = Majors.PHYS;
                } else if (Majors.CHEM == Majors.valueOf(name)) {
                    this.major = Majors.CHEM;
                } else if (Majors.CHEME == Majors.valueOf(name)) {
                    this.major = Majors.CHEME;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * @return User's major as a member of the Majors enuum
     */
    public Majors getMajor() {
        return major;
    }

    /**
     * @return User's major as a string
     */
    public String getMajorString() {
        return major.getMajorString();
    }

    /**
     * @return User's email as a string
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return User's password as a string
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password User's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param email User's email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
