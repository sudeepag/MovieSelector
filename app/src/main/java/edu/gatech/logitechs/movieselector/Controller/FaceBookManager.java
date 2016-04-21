package edu.gatech.logitechs.movieselector.Controller;

import com.facebook.Profile;

/**
 * Created by jiangshen on 4/20/16.
 */
public class FaceBookManager {

    public static Profile profile;

    public static Profile getProfile() {
        return profile;
    }

    public static void setProfile(Profile p) {
        profile = p;
    }
}
