package com.github.metagalactic.loginview.users;

import android.os.Parcelable;

import com.github.metagalactic.loginview.common.StringId;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UserEmail implements StringId, Parcelable {

    public static UserEmail create(String userEmail) {
        return new AutoValue_UserEmail(userEmail);
    }
}
