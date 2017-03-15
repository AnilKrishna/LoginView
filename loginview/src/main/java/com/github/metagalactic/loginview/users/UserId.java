package com.github.metagalactic.loginview.users;

import android.os.Parcelable;

import com.github.metagalactic.loginview.common.StringId;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UserId implements StringId, Parcelable {

    public static UserId create(String userId) {
        return new AutoValue_UserId(userId);
    }
}
