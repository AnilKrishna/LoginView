package com.github.metagalactic.loginview.users;

import android.os.Parcelable;

import com.github.metagalactic.loginview.common.StringId;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UserFirstName implements StringId, Parcelable {

    public static UserFirstName create(String firstName) {
        return new AutoValue_UserFirstName(firstName);
    }
}
