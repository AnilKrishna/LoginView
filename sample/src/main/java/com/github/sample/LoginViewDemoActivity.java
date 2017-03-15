package com.github.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.metagalactic.loginview.config.LoginBuilder;
import com.github.metagalactic.loginview.utils.IntentUtil;

public class LoginViewDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivityForResult(IntentUtil.getLoginActivityIntentWithBundle(getApplicationContext(),
                LoginBuilder.create()
                        .setActionBarTitle(R.string.app_name)
                        .setAppLogo(R.mipmap.ic_launcher)
                        .setIsCustomLoginEnabled(true)
                        .setIsGoogleLoginEnabled(true)
                        .setFacebookAppId(getString(R.string.app_id))
                        .setIsFacebookLoginEnabled(true)
                        .build()), 100);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO::
    }
}
