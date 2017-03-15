package com.github.metagalactic.loginview.activities.presenter;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.github.metagalactic.loginview.R;
import com.github.metagalactic.loginview.activities.presentation.LoginPresentation;
import com.github.metagalactic.loginview.config.LoginBuilder;
import com.github.metagalactic.loginview.users.FacebookUserInfo;
import com.github.metagalactic.loginview.users.GoogleUserInfo;
import com.github.metagalactic.loginview.users.UserName;
import com.github.metagalactic.loginview.users.UserPassword;
import com.github.metagalactic.loginview.users.UserProfileInfo;
import com.github.metagalactic.loginview.utils.UserInfoUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import javax.annotation.ParametersAreNonnullByDefault;

import zeta.android.utils.lang.StringUtils;

@ParametersAreNonnullByDefault
public class LoginPresenter {

    private static final int RC_SIGN_IN = 105;

    private LoginBuilder mLoginBuilder;
    private LoginPresentation mPresentation;

    public LoginPresenter(LoginBuilder loginBuilder) {
        mLoginBuilder = loginBuilder;
    }

    public void onCreate(LoginPresentation navigationPresentation) {
        mPresentation = navigationPresentation;

        mPresentation.setDisplayHomeAsUpEnabled(true);
        mPresentation.showAppLogo(mLoginBuilder.getAppLogo());
        mPresentation.setActionBarTitle(mLoginBuilder.getActionBarTitle());

        if (mLoginBuilder.getIsCustomLoginEnabled()) {
            mPresentation.showCustomLoginButton();
        } else {
            mPresentation.hideCustomLoginButton();
        }

        if (mLoginBuilder.getIsFacebookLoginEnabled()) {
            String facebookAppId = mLoginBuilder.getFacebookAppId();
            assert facebookAppId != null;
            mPresentation.initializeFacebookSdk();
            mPresentation.setFacebookAppId(facebookAppId);
            mPresentation.showFacebookLoginButton();
        } else {
            mPresentation.hideFacebookLoginButton();
        }

        if (mLoginBuilder.getIsGoogleLoginEnabled()) {
            mPresentation.showGoogleLoginButton();
        } else {
            mPresentation.hideGoogleLoginButton();
        }
    }

    public void onDestroy() {
        mPresentation = null;
        mLoginBuilder = null;
    }

    public void onCustomSignInClicked(@Nullable String userName, @Nullable String userPassword) {
        if (StringUtils.isNotNullOrEmpty(userName) && StringUtils.isNotNullOrEmpty(userPassword)) {
            mPresentation.showLoginProgress();
            //TODO::Do validations for the email
            //Check if this is user name or user email
            UserProfileInfo userProfileInfo = UserInfoUtil.populateCustomUserWithUserName(UserName.create(userName),
                    UserPassword.create(userPassword));
            updateSessionManager(userProfileInfo);
            mPresentation.hideLoginProgress();
            mPresentation.killCurrentActivity();
            return;
        }

        if (StringUtils.isNullOrEmpty(userName)) {
            mPresentation.showCustomLoginUserNameError(R.string.username_error);
        }

        if (StringUtils.isNotNullOrEmpty(userPassword)) {
            mPresentation.showCustomLoginUserNameError(R.string.password_error);
        }
    }

    public void onCustomSignUpClicked() {
        mPresentation.showLoginProgress();
    }

    public void onFacebookLoginClicked() {
        mPresentation.showLoginProgress();
        mPresentation.performFacebookLoginClick();
    }

    public void onGoogleLoginClicked(GoogleApiClient googleApiClient) {
        mPresentation.showLoginProgress();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        mPresentation.onStartActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onGoogleLoginConnectionFailed(ConnectionResult connectionResult) {
        mPresentation.hideLoginProgress();
    }

    public void onFacebookLoginSuccess(LoginResult loginResult) {
        final AccessToken accessToken = loginResult.getAccessToken();
        GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                processFacebookGraphRequestSuccess(object, accessToken, response);
            }
        }).executeAsync();
    }

    public void onFacebookLoginCancel() {
        mPresentation.hideLoginProgress();
        mPresentation.killCurrentActivity();
    }

    public void onFacebookLoginError(FacebookException error) {
        mPresentation.hideLoginProgress();
        mPresentation.killCurrentActivity();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                if (acct == null) {
                    mPresentation.hideLoginProgress();
                    mPresentation.showGoogleLoginFailed();
                    return;
                }
                GoogleUserInfo googleUserInfo = UserInfoUtil.populateGoogleUser(acct);
                updateSessionManager(googleUserInfo);
                return;
            }
            mPresentation.hideLoginProgress();
            mPresentation.showGoogleLoginFailed();
        }
    }

    private void processFacebookGraphRequestSuccess(JSONObject object, AccessToken accessToken, GraphResponse response) {
        mPresentation.hideLoginProgress();
        String token = accessToken.getToken();
        FacebookUserInfo facebookUserInfo = UserInfoUtil.populateFacebookUser(object, token);
        if (facebookUserInfo != null) {
            updateSessionManager(facebookUserInfo);
            return;
        }
        mPresentation.killCurrentActivity();
    }

    private void updateSessionManager(UserProfileInfo customUserProfileInfo) {
        //TODO::
        hideProgressAndKillCurrentActivity();
    }

    private void updateSessionManager(@Nullable GoogleUserInfo googleUserInfo) {
        //TODO::
        hideProgressAndKillCurrentActivity();
    }

    private void updateSessionManager(@Nullable FacebookUserInfo facebookUserInfo) {
        //TODO::
        hideProgressAndKillCurrentActivity();
    }

    private void hideProgressAndKillCurrentActivity() {
        mPresentation.hideLoginProgress();
        mPresentation.killCurrentActivity();
    }

}
