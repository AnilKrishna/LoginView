package com.github.metagalactic.loginview.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.metagalactic.loginview.activities.LoginActivity;
import com.github.metagalactic.loginview.config.LoginBuilder;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class IntentUtil {

    private static final String ARG_LOGIN_BUILDER_BUNDLE = "ARG_LOGIN_BUILDER_BUNDLE";

    public static Intent getLoginActivityIntentWithBundle(Context context, LoginBuilder builder) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(ARG_LOGIN_BUILDER_BUNDLE, builder);
        return intent;
    }

    @Nullable
    public static LoginBuilder getLoginBuilderBundle(Bundle bundle) {
        return (LoginBuilder) bundle.get(ARG_LOGIN_BUILDER_BUNDLE);
    }

}
