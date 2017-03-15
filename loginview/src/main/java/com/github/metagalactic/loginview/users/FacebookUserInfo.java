package com.github.metagalactic.loginview.users;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.net.URI;

@AutoValue
public abstract class FacebookUserInfo implements Parcelable {

    public static FacebookUserInfo.Builder create() {
        return new $AutoValue_FacebookUserInfo.Builder();
    }

    public abstract AccessToken getAccessToken();

    @Nullable
    public abstract UserId getUserId();

    @Nullable
    public abstract UserDisplayName getUserDisplayName();

    @Nullable
    public abstract UserName getUserName();

    @Nullable
    public abstract UserPassword getPassword();

    @Nullable
    public abstract UserFirstName getFirstName();

    @Nullable
    public abstract UserLastName getLastName();

    @Nullable
    public abstract UserEmail getEmail();

    @Nullable
    public abstract UserBirthday getBirthday();

    @Gender
    public abstract String getGender();

    @Nullable
    public abstract URI getProfileImage();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setAccessToken(AccessToken accessToken);

        public abstract Builder setUserId(@Nullable UserId userId);

        public abstract Builder setUserName(@Nullable UserName userId);

        public abstract Builder setUserDisplayName(@Nullable UserDisplayName userDisplayName);

        public abstract Builder setPassword(@Nullable UserPassword userId);

        public abstract Builder setFirstName(@Nullable UserFirstName userId);

        public abstract Builder setLastName(@Nullable UserLastName userId);

        public abstract Builder setEmail(@Nullable UserEmail userId);

        public abstract Builder setBirthday(@Nullable UserBirthday userId);

        public abstract Builder setGender(@Gender String gender);

        public abstract Builder setProfileImage(@Nullable URI userId);

        public abstract FacebookUserInfo build();
    }
}
