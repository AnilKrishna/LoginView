package com.github.metagalactic.loginview.users;

import android.os.Parcel;
import android.os.Parcelable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SmartFacebookUser extends SmartUser implements Parcelable {
    private String profileName;

    public SmartFacebookUser() {
    }

    protected SmartFacebookUser(Parcel in) {
        super(in);
        profileName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(profileName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SmartFacebookUser> CREATOR = new Creator<SmartFacebookUser>() {
        @Override
        public SmartFacebookUser createFromParcel(Parcel in) {
            return new SmartFacebookUser(in);
        }

        @Override
        public SmartFacebookUser[] newArray(int size) {
            return new SmartFacebookUser[size];
        }
    };

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }
}
