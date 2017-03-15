package com.github.metagalactic.loginview.users;

import android.os.Parcelable;

import com.github.metagalactic.loginview.common.StringId;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UserDisplayName implements StringId, Parcelable {

    public static UserDisplayName create(String displayName) {
        return new AutoValue_UserDisplayName(displayName);
    }
}
