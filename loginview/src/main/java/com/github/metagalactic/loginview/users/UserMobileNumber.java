package com.github.metagalactic.loginview.users;

import android.os.Parcelable;

import com.github.metagalactic.loginview.common.StringId;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UserMobileNumber implements StringId, Parcelable {

    public static UserMobileNumber create(String mobileNumber) {
        return new AutoValue_UserMobileNumber(mobileNumber);
    }
}
