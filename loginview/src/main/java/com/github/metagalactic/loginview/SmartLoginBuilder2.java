package com.github.metagalactic.loginview;

import android.os.Parcelable;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import java.util.ArrayList;

@AutoValue
public abstract class SmartLoginBuilder2 implements Parcelable {

    public static Builder create() {
        return new AutoValue_SmartLoginBuilder2.Builder()
                .setIsGoogleLoginEnabled(false)
                .setIsFacebookLoginEnabled(false);
    }

    @IntegerRes
    public abstract int getAppLogo();

    public abstract boolean getIsCustomLoginEnabled();

    public abstract boolean getIsGoogleLoginEnabled();

    public abstract boolean getIsFacebookLoginEnabled();

    @Nullable
    public abstract String getFacebookAppId();

    @Nullable
    public abstract ArrayList<String> getFacebookPermissions();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setAppLogo(@IntegerRes int appLogoRes);

        public abstract Builder setIsCustomLoginEnabled(boolean customLoginEnabled);

        public abstract Builder setIsGoogleLoginEnabled(boolean googleLoginEnabled);

        public abstract Builder setIsFacebookLoginEnabled(boolean facebookLoginEnabled);

        public abstract Builder setFacebookAppId(@Nullable String facebookAppId);

        public abstract Builder setFacebookPermissions(@Nullable ArrayList<String> facebookPermissions);

        public abstract SmartLoginBuilder2 build();
    }

}
