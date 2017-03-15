package com.github.metagalactic.loginview.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.metagalactic.loginview.R;
import com.github.metagalactic.loginview.R2;
import com.github.metagalactic.loginview.activities.presentation.LoginPresentation;
import com.github.metagalactic.loginview.activities.presenter.LoginPresenter;
import com.github.metagalactic.loginview.common.BaseViews;
import com.github.metagalactic.loginview.config.LoginBuilder;
import com.github.metagalactic.loginview.utils.IntentUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import butterknife.BindView;
import zeta.android.utils.view.ViewUtils;

@ParametersAreNonnullByDefault
public class LoginActivity extends AppCompatActivity implements LoginPresentation {

    private Views mViews;
    private CustomLoginViews mCustomLoginViews;
    private GoogleLoginViews mGoogleLoginViews;
    private FacebookLoginViews mFacebookLoginViews;

    private LoginPresenter mPresenter;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;

    static class Views extends BaseViews {

        @BindView(R2.id.metagalactic_coordinator_layout)
        CoordinatorLayout coordinatorLayout;

        @BindView(R2.id.metagalactic_app_bar_layout)
        AppBarLayout appBarLayout;

        @BindView(R2.id.metagalactic_toolbar)
        Toolbar toolbar;

        @BindView(R2.id.metagalactic_scroll_bar)
        ScrollView scrollView;

        @BindView(R2.id.metagalactic_app_logo)
        ImageView appLogo;

        @BindView(R2.id.metagalactic_custom_login_layout)
        ViewStub customLoginViewStub;

        @BindView(R2.id.metagalactic_facebook_login_layout)
        ViewStub facebookLoginViewStub;

        @BindView(R2.id.metagalactic_google_login_layout)
        ViewStub googleLoginViewStub;

        Views(AppCompatActivity root) {
            super(root.findViewById(R.id.metagalactic_coordinator_layout));
        }
    }

    static class CustomLoginViews extends BaseViews {

        @BindView(R2.id.metagalactic_user_name_wrapper)
        TextInputLayout userNameInputLayout;

        @BindView(R2.id.metagalactic_user_name_edit_text)
        EditText userNameEditText;

        @BindView(R2.id.metagalactic_password_wrapper)
        TextInputLayout passwordInputLayout;

        @BindView(R2.id.metagalactic_password_edit_text)
        EditText userPasswordEditText;

        @BindView(R2.id.metagalactic_custom_signin_button)
        Button customSignInButton;

        @BindView(R2.id.metagalactic_custom_signup_button)
        TextView customSignUpButton;

        CustomLoginViews(View root) {
            super(root);
        }
    }

    static class FacebookLoginViews extends BaseViews {

        @BindView(R2.id.facebook_dummy_login_button)
        Button facebookDummyLoginBtn;

        @BindView(R2.id.facebook_login_button)
        LoginButton facebookLoginBtn;

        FacebookLoginViews(View root) {
            super(root);
        }
    }

    static class GoogleLoginViews extends BaseViews {

        @BindView(R2.id.google_login_button)
        Button googleLoginBtn;

        GoogleLoginViews(View root) {
            super(root);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginBuilder loginBuilderBundle = IntentUtil.getLoginBuilderBundle(getIntent().getExtras());
        assert loginBuilderBundle != null;
        mPresenter = new LoginPresenter(loginBuilderBundle);
        mCallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        mViews = new Views(this);

        initGoogleSdk();
        setSupportActionBar(mViews.toolbar);
        mPresenter.onCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterClickListeners();
        mPresenter.onDestroy();

        mPresenter = null;
        mGoogleApiClient = null;

        mViews = null;
        mCustomLoginViews = null;
        mGoogleLoginViews = null;
        mFacebookLoginViews = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void setActionBarTitle(@StringRes int actionBarTitle) {
        getSupportActionBar().setTitle(actionBarTitle);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void setDisplayHomeAsUpEnabled(boolean enable) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
    }

    @Override
    public void showAppLogo(@DrawableRes int appLogo) {
        mViews.appLogo.setImageResource(appLogo);
        ViewUtils.setToVisible(mViews.appLogo);
    }

    @Override
    public void hideAppLogo() {
        ViewUtils.setToGone(mViews.appLogo);
    }

    @Override
    public void showCustomLoginButton() {
        if (mCustomLoginViews == null) {
            final View view = ViewUtils.ensureInflated(mViews.customLoginViewStub);
            mCustomLoginViews = new CustomLoginViews(view);
            registerCustomLoginClickListeners();
        }
    }

    @Override
    public void hideCustomLoginButton() {
        ViewUtils.setToGone(mViews.customLoginViewStub);
    }

    @Override
    public void initializeFacebookSdk() {
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    @Override
    public void setFacebookAppId(String facebookAppId) {
        FacebookSdk.setApplicationId(facebookAppId);
    }

    @Override
    public void setFacebookLogInWithReadPermissions(List<String> permissions) {
        LoginManager.getInstance().logInWithReadPermissions(this, permissions);
    }

    @Override
    public void performFacebookLoginClick() {
        mFacebookLoginViews.facebookLoginBtn.performClick();
    }

    @Override
    public void showFacebookLoginButton() {
        if (mFacebookLoginViews == null) {
            final View view = ViewUtils.ensureInflated(mViews.facebookLoginViewStub);
            mFacebookLoginViews = new FacebookLoginViews(view);
            registerFacebookLoginClickListeners();
        }
    }

    @Override
    public void hideFacebookLoginButton() {
        ViewUtils.setToGone(mViews.facebookLoginViewStub);
    }

    @Override
    public void showGoogleLoginButton() {
        if (mGoogleLoginViews == null) {
            final View view = ViewUtils.ensureInflated(mViews.googleLoginViewStub);
            mGoogleLoginViews = new GoogleLoginViews(view);
            registerGoogleLoginClickListeners();
        }
    }

    @Override
    public void showGoogleLoginFailed() {
        Snackbar.make(mViews.getRootView(), "Google login failed", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void hideGoogleLoginButton() {
        ViewUtils.setToGone(mViews.googleLoginViewStub);
    }

    @Override
    public void showLoginProgress() {
        Snackbar.make(mViews.getRootView(), "Show login progress", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void hideLoginProgress() {
        Snackbar.make(mViews.getRootView(), "Hide login progress", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onStartActivityForResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void killCurrentActivity() {
        finish();
    }

    @Override
    public void showCustomLoginUserNameError(@StringRes int error) {
        Snackbar.make(mViews.getRootView(), "Custom login username error", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showCustomLoginUserPasswordError(@StringRes int error) {
        Snackbar.make(mViews.getRootView(), "Custom login password error", Snackbar.LENGTH_SHORT).show();
    }

    private void initGoogleSdk() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        mPresenter.onGoogleLoginConnectionFailed(connectionResult);
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void registerCustomLoginClickListeners() {
        if (mCustomLoginViews == null) {
            return;
        }
        mCustomLoginViews.customSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mCustomLoginViews.userNameEditText.getText().toString();
                String userPassword = mCustomLoginViews.userPasswordEditText.getText().toString();
                mPresenter.onCustomSignInClicked(userName, userPassword);
            }
        });

        mCustomLoginViews.customSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onCustomSignUpClicked();
            }
        });

    }

    private void registerGoogleLoginClickListeners() {
        mGoogleLoginViews.googleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onGoogleLoginClicked(mGoogleApiClient);
            }
        });
    }

    private void registerFacebookLoginClickListeners() {
        mFacebookLoginViews.facebookDummyLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onFacebookLoginClicked();
            }
        });
        mFacebookLoginViews.facebookLoginBtn.registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        mPresenter.onFacebookLoginSuccess(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        mPresenter.onFacebookLoginCancel();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        mPresenter.onFacebookLoginError(error);
                    }
                });
    }

    private void unRegisterClickListeners() {
        unRegisterCustomLoginClickListeners();
        unRegisterGoogleLoginClickListeners();
        unRegisterFacebookLoginClickListeners();
    }

    private void unRegisterCustomLoginClickListeners() {
        if (mCustomLoginViews == null) {
            return;
        }
        mCustomLoginViews.customSignInButton.setOnClickListener(null);
        mCustomLoginViews.customSignUpButton.setOnClickListener(null);
    }

    private void unRegisterGoogleLoginClickListeners() {
        if (mGoogleLoginViews == null) {
            return;
        }
        mGoogleLoginViews.googleLoginBtn.setOnClickListener(null);
    }

    private void unRegisterFacebookLoginClickListeners() {
        if (mFacebookLoginViews == null) {
            return;
        }
        mFacebookLoginViews.facebookLoginBtn.setOnClickListener(null);
    }

}
