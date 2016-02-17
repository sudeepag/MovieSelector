package edu.gatech.logitechs.movieselector;

/**
 * Created by matth_000 on 2/15/2016.
 */
public enum Majors {
    CS("CS"), EE("EE"), ME("ME"), ISYE("ISYE"), MATH("Math"), PHYS("Phys"), CHEM("Chem"), CHEME("ChemE");

    private String majorString;

    Majors(String major) {
        this.majorString = major;
    }

    public String getMajorString() {
        return majorString;
    }
}
