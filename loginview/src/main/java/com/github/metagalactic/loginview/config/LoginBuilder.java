package com.github.metagalactic.loginview.config;

import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.github.metagalactic.loginview.R;
import com.google.auto.value.AutoValue;

import java.util.ArrayList;

@AutoValue
public abstract class LoginBuilder implements Parcelable {

    public static Builder create() {
        return new $AutoValue_LoginBuilder.Builder()
                .setAppLogo(R.drawable.com_facebook_button_icon_white)
                .setIsCustomLoginEnabled(true)
                .setIsGoogleLoginEnabled(false)
                .setIsFacebookLoginEnabled(false)
                .setFacebookPermissions(getDefaultFacebookPermissions());
    }

    @DrawableRes
    public abstract int getAppLogo();

    @StringRes
    public abstract int getActionBarTitle();

    public abstract boolean getIsCustomLoginEnabled();

    public abstract boolean getIsGoogleLoginEnabled();

    public abstract boolean getIsFacebookLoginEnabled();

    @Nullable
    public abstract String getFacebookAppId();

    @Nullable
    public abstract ArrayList<String> getFacebookPermissions();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder setAppLogo(@DrawableRes int appLogoRes);

        public abstract Builder setActionBarTitle(@StringRes int titleRes);

        public abstract Builder setIsCustomLoginEnabled(boolean customLoginEnabled);

        public abstract Builder setIsGoogleLoginEnabled(boolean googleLoginEnabled);

        public abstract Builder setIsFacebookLoginEnabled(boolean facebookLoginEnabled);

        public abstract Builder setFacebookAppId(@Nullable String facebookAppId);

        public abstract Builder setFacebookPermissions(@Nullable ArrayList<String> facebookPermissions);

        public abstract LoginBuilder build();
    }

    private static ArrayList<String> getDefaultFacebookPermissions() {
        ArrayList<String> defaultPermissions = new ArrayList<>();
        defaultPermissions.add("public_profile");
        defaultPermissions.add("email");
        defaultPermissions.add("user_birthday");
        return defaultPermissions;
    }

}
