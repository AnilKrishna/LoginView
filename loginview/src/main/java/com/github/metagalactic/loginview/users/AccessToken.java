package com.github.metagalactic.loginview.users;

import android.os.Parcelable;

import com.github.metagalactic.loginview.common.StringId;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AccessToken implements StringId, Parcelable {

    public static AccessToken create(String accessToken) {
        return new AutoValue_AccessToken(accessToken);
    }
}
