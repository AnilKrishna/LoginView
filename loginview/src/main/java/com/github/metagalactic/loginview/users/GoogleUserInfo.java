package com.github.metagalactic.loginview.users;

import android.net.Uri;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class GoogleUserInfo implements Parcelable {

    public static GoogleUserInfo.Builder create() {
        return new $AutoValue_GoogleUserInfo.Builder();
    }

    public abstract AccessToken getAccessToken();

    public abstract UserId getUserId();

    public abstract UserDisplayName getUserDisplayName();

    public abstract UserName getUserName();

    public abstract UserPassword getPassword();

    public abstract UserFirstName getFirstName();

    public abstract UserLastName getLastName();

    public abstract UserEmail getEmail();

    public abstract UserBirthday getBirthday();

    @Gender
    public abstract String getGender();

    public abstract Uri getProfileImage();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setAccessToken(AccessToken accessToken);

        public abstract Builder setUserId(UserId userId);

        public abstract Builder setUserName(UserName userId);

        public abstract Builder setUserDisplayName(UserDisplayName userDisplayName);

        public abstract Builder setPassword(UserPassword userId);

        public abstract Builder setFirstName(UserFirstName userId);

        public abstract Builder setLastName(UserLastName userId);

        public abstract Builder setEmail(UserEmail userId);

        public abstract Builder setBirthday(UserBirthday userId);

        public abstract Builder setGender(@Gender String gender);

        public abstract Builder setProfileImage(Uri userId);

        public abstract GoogleUserInfo build();
    }
}
