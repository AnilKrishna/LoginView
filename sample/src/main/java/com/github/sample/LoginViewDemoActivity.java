package com.github.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.metagalactic.loginview.SmartCustomLoginListener;
import com.github.metagalactic.loginview.SmartLoginBuilder;
import com.github.metagalactic.loginview.SmartLoginConfig;
import com.github.metagalactic.loginview.users.SmartFacebookUser;
import com.github.metagalactic.loginview.users.SmartGoogleUser;
import com.github.metagalactic.loginview.users.SmartUser;

import java.util.ArrayList;

public class LoginViewDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchview_demo);

        SmartLoginBuilder loginBuilder = new SmartLoginBuilder();

        //Set facebook permissions
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_birthday");
        permissions.add("user_friends");

        Intent intent = loginBuilder.with(getApplicationContext())
                .setAppLogo(R.mipmap.ic_launcher)
                .isFacebookLoginEnabled(true)
                .withFacebookAppId(getString(R.string.facebook_app_id)).withFacebookPermissions(permissions)
                .isGoogleLoginEnabled(true)
                .isCustomLoginEnabled(true, SmartLoginConfig.LoginType.withEmail)
                .setSmartCustomLoginHelper(new SmartCustomLoginListener() {
                    @Override
                    public boolean customSignIn(SmartUser user) {
                        return false;
                    }

                    @Override
                    public boolean customSignUp(SmartUser newUser) {
                        return false;
                    }
                })
                .build();

        startActivityForResult(intent, SmartLoginConfig.LOGIN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String fail = "Login Failed";
        if (resultCode == SmartLoginConfig.FACEBOOK_LOGIN_REQUEST) {
            SmartFacebookUser user;
            try {
                user = data.getParcelableExtra(SmartLoginConfig.USER);
                String userDetails = user.getProfileName() + " " + user.getEmail() + " " + user.getBirthday();

            } catch (Exception e) {

            }
        } else if (resultCode == SmartLoginConfig.GOOGLE_LOGIN_REQUEST) {
            SmartGoogleUser user = data.getParcelableExtra(SmartLoginConfig.USER);
            String userDetails = user.getEmail() + " " + user.getDisplayName();

        } else if (resultCode == SmartLoginConfig.CUSTOM_LOGIN_REQUEST) {
            SmartUser user = data.getParcelableExtra(SmartLoginConfig.USER);
            String userDetails = user.getUsername() + " (Custom User)";

        }
        /*else if(resultCode == SmartLoginConfig.CUSTOM_SIGNUP_REQUEST){
            SmartUser user = data.getParcelableExtra(SmartLoginConfig.USER);
            String userDetails = user.getUsername() + " (Custom User)";
            loginResult.setText(userDetails);
        }*/
        else if (resultCode == RESULT_CANCELED) {

        }

    }
}
