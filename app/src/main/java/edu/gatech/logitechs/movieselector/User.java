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
     * User
     * @param email
     * @param password
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

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

    public Majors getMajor() {
        return major;
    }

    public String getMajorString() {
        return major.getMajorString();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
