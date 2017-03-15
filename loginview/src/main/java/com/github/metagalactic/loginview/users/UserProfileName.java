package com.github.metagalactic.loginview.users;

import android.os.Parcelable;

import com.github.metagalactic.loginview.common.StringId;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UserProfileName implements StringId, Parcelable {

    public static UserProfileName create(String profileName) {
        return new AutoValue_UserProfileName(profileName);
    }
}
