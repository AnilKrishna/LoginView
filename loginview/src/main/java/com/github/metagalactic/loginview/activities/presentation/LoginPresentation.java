package com.github.metagalactic.loginview.activities.presentation;

import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import java.util.List;

public interface LoginPresentation {

    void showAppLogo(@DrawableRes int appLogo);

    void setActionBarTitle(@StringRes int actionBarTitle);

    void setDisplayHomeAsUpEnabled(boolean enable);

    void hideAppLogo();

    void showCustomLoginButton();

    void hideCustomLoginButton();

    void initializeFacebookSdk();

    void setFacebookAppId(String facebookAppId);

    void setFacebookLogInWithReadPermissions(List<String> permissions);

    void showFacebookLoginButton();

    void performFacebookLoginClick();

    void hideFacebookLoginButton();

    void showGoogleLoginButton();

    void showGoogleLoginFailed();

    void hideGoogleLoginButton();

    void showLoginProgress();

    void hideLoginProgress();

    void killCurrentActivity();

    void showCustomLoginUserNameError(@StringRes int error);

    void showCustomLoginUserPasswordError(@StringRes int error);

    void onStartActivityForResult(Intent intent, int requestCode);
}
