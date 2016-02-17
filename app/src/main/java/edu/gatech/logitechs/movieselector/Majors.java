package edu.gatech.logitechs.movieselector;

/**
 * Created by matth_000 on 2/15/2016.
 */
public enum Majors {
    CS("CS"), EE("EE"), ME("ME"), ISYE("ISYE"), MATH("Math"), PHYS("Phys"), CHEM("Chem"), CHEME("ChemE");

    private String majorString;

    /**
     * Constructor for Major enum; provides alternative string representation to the name
     * @param major  String interpretation of a major
     */
    Majors(String major) {
        this.majorString = major;
    }

    /**
     * @return String interpretation of a major
     */
    public String getMajorString() {
        return majorString;
    }
}
