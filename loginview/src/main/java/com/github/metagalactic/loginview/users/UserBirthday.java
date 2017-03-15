package com.github.metagalactic.loginview.users;

import android.os.Parcelable;

import com.github.metagalactic.loginview.common.StringId;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class UserBirthday implements StringId, Parcelable {

    public static UserBirthday create(String birthday) {
        return new AutoValue_UserBirthday(birthday);
    }
}
