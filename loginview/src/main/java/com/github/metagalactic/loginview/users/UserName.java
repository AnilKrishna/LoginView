package com.github.metagalactic.loginview.users;

import android.os.Parcelable;

import com.github.metagalactic.loginview.common.StringId;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UserName implements StringId, Parcelable {

    public static UserName create(String userName) {
        return new AutoValue_UserName(userName);
    }
}
