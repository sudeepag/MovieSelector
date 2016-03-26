package edu.gatech.logitechs.movieselector;

/**
 * Created by akhilesh on 2/7/16.
 */
public class User {
    private String email;
    private String password;
    private String description;
   // private Majors majorEnum;
    private String major;
    private String UID;
    private boolean admin;
    private boolean locked;
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
     * Setter for the major of a given user
     *
     * @param name  takes in UPPERCASE, abbreviated name of major
     * @return true if the major was added correctly, false if major was not member of enum
     */
//    public boolean setMajorEnum(String name) {
//        try {
//            if (Majors.valueOf(name) instanceof Majors) {
//                if (Majors.CS == Majors.valueOf(name)) {
//                    this.majorEnum = Majors.CS;
//                } else if (Majors.EE == Majors.valueOf(name)) {
//                    this.majorEnum = Majors.EE;
//                } else if (Majors.ISYE == Majors.valueOf(name)) {
//                    this.majorEnum = Majors.ISYE;
//                } else if (Majors.MATH == Majors.valueOf(name)) {
//                    this.majorEnum = Majors.MATH;
//                } else if (Majors.PHYS == Majors.valueOf(name)) {
//                    this.majorEnum = Majors.PHYS;
//                } else if (Majors.CHEM == Majors.valueOf(name)) {
//                    this.majorEnum = Majors.CHEM;
//                } else if (Majors.CHEME == Majors.valueOf(name)) {
//                    this.majorEnum = Majors.CHEME;
//                } else {
//                    return false;
//                }
//            }
//        } catch (Exception e) {
//            return false;
//        }
//        return true;
//    }

    /**
     * @return User's major as a string
     */
 //   public String getMajorString() {
//        return majorEnum.getMajorString();
//    }

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
     * getter for UID
     * @return the userID for the user
     */
    public String getUID() {
        return UID;
    }

    /**
     * setter for UID
     * @param UID the UserID of the user
     */
    public void setUID(String UID) {
        this.UID = UID;
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
