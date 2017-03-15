package com.github.metagalactic.loginview.users;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.net.URI;

@AutoValue
public abstract class UserProfileInfo implements Parcelable {

    public static UserProfileInfo.Builder create(UserName userName) {
        return new $AutoValue_UserProfileInfo.Builder()
                .setUserName(userName);
    }

    public static UserProfileInfo.Builder create(UserEmail userEmail) {
        return new $AutoValue_UserProfileInfo.Builder()
                .setUserEmail(userEmail);
    }


    public abstract UserId getUserId();

    @Nullable
    public abstract UserName getUserName();

    public abstract UserPassword getPassword();

    public abstract UserFirstName getFirstName();

    public abstract UserLastName getLastName();

    @Nullable
    public abstract UserEmail getUserEmail();

    public abstract UserBirthday getBirthday();

    @Gender
    public abstract String getGender();

    public abstract URI getProfileImage();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setUserId(UserId userId);

        public abstract Builder setUserName(@Nullable UserName userId);

        public abstract Builder setPassword(UserPassword userId);

        public abstract Builder setFirstName(UserFirstName userId);

        public abstract Builder setLastName(UserLastName userId);

        public abstract Builder setUserEmail(@Nullable UserEmail userId);

        public abstract Builder setBirthday(UserBirthday userId);

        public abstract Builder setGender(@Gender String gender);

        public abstract Builder setProfileImage(URI userId);

        public abstract UserProfileInfo build();
    }
}
