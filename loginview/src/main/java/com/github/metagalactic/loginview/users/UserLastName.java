package com.github.metagalactic.loginview.users;

import android.os.Parcelable;

import com.github.metagalactic.loginview.common.StringId;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UserLastName implements StringId, Parcelable {

    public static UserLastName create(String lastName) {
        return new AutoValue_UserLastName(lastName);
    }
}
