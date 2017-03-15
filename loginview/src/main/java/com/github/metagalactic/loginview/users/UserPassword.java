package com.github.metagalactic.loginview.users;

import android.os.Parcelable;

import com.github.metagalactic.loginview.common.StringId;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UserPassword implements StringId, Parcelable {

    public static UserPassword create(String password) {
        return new AutoValue_UserPassword(password);
    }
}
