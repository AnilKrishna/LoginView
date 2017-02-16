package com.github.metagalactic.loginview;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.github.metagalactic.loginview.common.BaseViews;
import com.github.metagalactic.loginview.manager.UserSessionManager;
import com.github.metagalactic.loginview.users.SmartFacebookUser;
import com.github.metagalactic.loginview.users.SmartGoogleUser;
import com.github.metagalactic.loginview.users.SmartUser;
import com.github.metagalactic.loginview.utils.DialogUtil;
import com.github.metagalactic.loginview.utils.UserUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.util.ArrayList;

import javax.annotation.ParametersAreNonnullByDefault;

import butterknife.BindView;
import zeta.android.utils.lang.StringUtils;
import zeta.android.utils.view.DeviceUtils;
import zeta.android.utils.view.ViewUtils;

@ParametersAreNonnullByDefault
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 105;

    private SmartLoginConfig mConfig;
    private CallbackManager mCallbackManager;

    private Views mViews;
    private ProgressDialog mProgressDialog;

    //Google Sign in related
    private GoogleApiClient mGoogleApiClient;

    static class Views extends BaseViews {

        @BindView(R2.id.applogo_imageView)
        ImageView appLogo;

        @Nullable
        @BindView(R2.id.userNameEditText)
        EditText usernameEditText;

        @Nullable
        @BindView(R2.id.passwordEditText)
        EditText passwordEditText;

        @Nullable
        @BindView(R2.id.userNameSignUp)
        EditText usernameSignup;

        @Nullable
        @BindView(R2.id.passwordSignUp)
        EditText passwordSignup;

        @Nullable
        @BindView(R2.id.repeatPasswordSignUp)
        EditText repeatPasswordSignup;

        @Nullable
        @BindView(R2.id.emailSignUp)
        EditText emailSignup;

        @Nullable
        @BindView(R2.id.signin_container)
        LinearLayout signinContainer;

        @Nullable
        @BindView(R2.id.signup_container)
        LinearLayout signupContainer;

        @Nullable
        @BindView(R2.id.main_container)
        ViewGroup mainContainer;

        Views(AppCompatActivity root) {
            super(root.findViewById(R.id.root_layout));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the mConfig object from the intent and unpack it
        Bundle bundle = getIntent().getExtras();
        mConfig = SmartLoginConfig.unpack(bundle);

        //Set the facebook app id and initialize sdk
        FacebookSdk.setApplicationId(mConfig.getFacebookAppId());

        setContentView(R.layout.activity_smart_login);

        mViews = new Views(this);

        //Set the title and back button on the Action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.smart_login_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Login");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Inject the views in the respective containers
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //include views based on user settings
        if (mConfig.isCustomLoginEnabled()) {
            mViews.signupContainer.addView(layoutInflater.inflate(R.layout.fragment_custom_login, mViews.mainContainer, false));
            if (mConfig.isFacebookEnabled() || mConfig.isGoogleEnabled()) {
                mViews.signupContainer.addView(layoutInflater.inflate(R.layout.fragment_divider, mViews.mainContainer, false));
            }
            mViews.signupContainer.addView(layoutInflater.inflate(R.layout.fragment_signup, mViews.mainContainer, false));

            //listeners
            findViewById(R.id.custom_signin_button).setOnClickListener(this);
            findViewById(R.id.custom_signup_button).setOnClickListener(this);
            findViewById(R.id.user_signup_button).setOnClickListener(this);

            //Hide necessary views
            ViewUtils.setToGone(mViews.signupContainer);
        }

        if (mConfig.isFacebookEnabled()) {
            mViews.signupContainer.addView(layoutInflater.inflate(R.layout.fragment_facebook_login, mViews.mainContainer, false));
            AppCompatButton facebookButton = (AppCompatButton) findViewById(R.id.login_fb_button);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                facebookButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.facebook_vector, 0, 0, 0);
            } else {
                facebookButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_facebook_white_36dp, 0, 0, 0);
            }
            facebookButton.setOnClickListener(this);
        }

        if (mConfig.isGoogleEnabled()) {
            //Decided to remove divider between two social buttons when there is no custom sign in option
            /*if(!mConfig.isCustomLoginEnabled() && mConfig.isFacebookEnabled()){
                signinContainer.addView(layoutInflater.inflate(R.layout.fragment_divider, mainContainer, false));
            }*/
            mViews.signupContainer.addView(layoutInflater.inflate(R.layout.fragment_google_login, mViews.mainContainer, false));
            AppCompatButton googlePlusButton = (AppCompatButton) findViewById(R.id.login_google_button);

            if (DeviceUtils.hasLollipop()) {
                googlePlusButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.google_plus_vector, 0, 0, 0);
            } else {
                googlePlusButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_google_plus_white_36dp, 0, 0, 0);
            }
            googlePlusButton.setOnClickListener(this);
        }

        //Set app logo
        if (mConfig.getAppLogo() != 0) {
            mViews.appLogo.setImageResource(mConfig.getAppLogo());
        } else {
            ViewUtils.setToGone(mViews.appLogo);
        }

        //Facebook login callback
        mCallbackManager = CallbackManager.Factory.create();
    }

    //Required for Facebook and google login
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //for facebook login
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            mProgressDialog = ProgressDialog.show(this, "", getString(R.string.getting_data), true);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("GOOGLE SIGN IN", "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                UserUtil util = new UserUtil();
                SmartGoogleUser googleUser = util.populateGoogleUser(acct);
                mProgressDialog.dismiss();
                finishLogin(googleUser);
            } else {
                Log.d("GOOGLE SIGN IN", "" + requestCode);
                // Signed out, show unauthenticated UI.
                mProgressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Google Login Failed", Toast.LENGTH_SHORT).show();
                finishLogin(null);
            }
        }
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        final String TAG = "GOOGLE LOGIN";
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        mProgressDialog.dismiss();
        Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.login_fb_button) {
            //do facebook login

        } else if (id == R.id.login_google_button) {
            //do google login
            doGoogleLogin();
        } else if (id == R.id.custom_signin_button) {
            //custom signin implementation
            doCustomSignin();
        } else if (id == R.id.custom_signup_button) {
            //custom signup implementation
            //AnimUtil.slideToTop(signinContainer);
            ViewUtils.showFirstAndHideOthers(mViews.signupContainer, mViews.signinContainer);
            findViewById(R.id.userNameSignUp).requestFocus();
        } else if (id == R.id.user_signup_button) {
            doCustomSignup();
        }
    }

    private void doGoogleLogin() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mProgressDialog = ProgressDialog.show(this, "", getString(R.string.logging_holder), true);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* On~ConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        mProgressDialog.dismiss();
    }

    private void doCustomSignup() {

        String username = mViews.usernameSignup.getText().toString();
        String password = mViews.passwordSignup.getText().toString();
        String repeatPassword = mViews.repeatPasswordSignup.getText().toString();
        String email = mViews.emailSignup.getText().toString();

        if (username.equals(StringUtils.EMPTY_STRING)) {
            //DialogUtil.getErrorDialog(R.string.username_error, this).show();
            mViews.usernameSignup.setError(getResources().getText(R.string.username_error));
            mViews.usernameSignup.requestFocus();
        } else if (password.equals("")) {
            //DialogUtil.getErrorDialog(R.string.password_error, this).show();
            mViews.passwordSignup.setError(getResources().getText(R.string.password_error));
            mViews.passwordSignup.requestFocus();
        } else if (email.equals("")) {
            //DialogUtil.getErrorDialog(R.string.no_email_error, this).show();
            mViews.emailSignup.setError(getResources().getText(R.string.no_email_error));
            mViews.emailSignup.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //DialogUtil.getErrorDialog(R.string.invalid_email_error, this).show();
            mViews.emailSignup.setError(getResources().getText(R.string.invalid_email_error));
            mViews.emailSignup.requestFocus();
        } else if (!password.equals(repeatPassword)) {
            //DialogUtil.getErrorDialog(R.string.password_mismatch, this).show();
            mViews.repeatPasswordSignup.setError(getResources().getText(R.string.password_mismatch));
            mViews.repeatPasswordSignup.requestFocus();
        } else {
            if (SmartLoginBuilder.smartCustomLoginListener != null) {
                final ProgressDialog progress = ProgressDialog.show(this, "", getString(R.string.loading_holder), true);
                SmartUser newUser = new UserUtil().populateCustomUserWithUserName(username, email, password);
                if (SmartLoginBuilder.smartCustomLoginListener.customSignUp(newUser)) {
                    progress.dismiss();
                    setResult(SmartLoginConfig.CUSTOM_SIGNUP_REQUEST);
                    finishLogin(newUser);
                } else {
                    progress.dismiss();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        }
    }

    private void doCustomSignin() {
        String username = mViews.usernameEditText.getText().toString();
        String password = mViews.passwordEditText.getText().toString();
        if (username.equals("")) {
            //DialogUtil.getErrorDialog(R.string.username_error, this).show();
            if (mConfig.getLoginType() == SmartLoginConfig.LoginType.withUsername) {
                mViews.usernameEditText.setError(getResources().getText(R.string.username_error));
            } else {
                mViews.usernameEditText.setError(getResources().getText(R.string.email_error));
            }
            mViews.usernameEditText.requestFocus();
        } else if (password.equals("")) {
            //DialogUtil.getErrorDialog(R.string.password_error, this).show();
            mViews.passwordEditText.setError(getResources().getText(R.string.password_error));
            mViews.passwordEditText.requestFocus();
        } else {
            if (SmartLoginBuilder.smartCustomLoginListener != null) {
                final ProgressDialog progress = ProgressDialog.show(this, "", getString(R.string.logging_holder), true);
                SmartUser user;
                if (mConfig.getLoginType() == SmartLoginConfig.LoginType.withUsername) {
                    user = new UserUtil().populateCustomUserWithUserName(username, null, password);
                } else {
                    user = new UserUtil().populateCustomUserWithEmail(null, username, password);
                }
                if (SmartLoginBuilder.smartCustomLoginListener.customSignIn(user)) {
                    progress.dismiss();
                    setResult(SmartLoginConfig.CUSTOM_LOGIN_REQUEST);
                    finishLogin(user);
                } else {
                    progress.dismiss();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        }
    }

    private void doFacebookLogin() {
        if (mConfig.isFacebookEnabled()) {
            Toast.makeText(LoginActivity.this, "Facebook login", Toast.LENGTH_SHORT).show();
            final ProgressDialog progress = ProgressDialog.show(this, "", getString(R.string.logging_holder), true);
            ArrayList<String> permissions = mConfig.getFacebookPermissions();
            if (permissions == null) {
                permissions = SmartLoginConfig.getDefaultFacebookPermissions();
            }
            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, permissions);
            LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    progress.setMessage(getString(R.string.getting_data));
                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    progress.dismiss();
                                    UserUtil util = new UserUtil();
                                    SmartFacebookUser facebookUser = util.populateFacebookUser(object);
                                    if (facebookUser != null) {
                                        finishLogin(facebookUser);
                                    } else {
                                        finish();
                                    }
                                }
                            });
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                    progress.dismiss();
                    finish();
                    Log.d("Facebook Login", "User cancelled the login process");
                }

                @Override
                public void onError(FacebookException e) {
                    progress.dismiss();
                    finish();
                    Toast.makeText(LoginActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void finishLogin(@Nullable SmartUser smartUser) {
        if (smartUser != null) {
            UserSessionManager sessionManager = new UserSessionManager();
            if (sessionManager.setUserSession(this, smartUser)) {
                Intent intent = new Intent();
                intent.putExtra(SmartLoginConfig.USER, smartUser);
                if (smartUser instanceof SmartFacebookUser) {
                    setResult(SmartLoginConfig.FACEBOOK_LOGIN_REQUEST, intent);
                } else if (smartUser instanceof SmartGoogleUser) {
                    setResult(SmartLoginConfig.GOOGLE_LOGIN_REQUEST, intent);
                } else {
                    setResult(SmartLoginConfig.CUSTOM_LOGIN_REQUEST, intent);
                }
                finish();
            } else {
                DialogUtil.getErrorDialog(R.string.network_error, this);
                finish();
            }
        } else {
            DialogUtil.getErrorDialog(R.string.login_failed, this);
            finish();
        }
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (mViews.signupContainer.getVisibility() == View.VISIBLE && mConfig.isCustomLoginEnabled()) {
            mViews.signupContainer.setVisibility(View.GONE);
            mViews.signinContainer.setVisibility(View.VISIBLE);
        } else {
            finish();
        }
    }
}
