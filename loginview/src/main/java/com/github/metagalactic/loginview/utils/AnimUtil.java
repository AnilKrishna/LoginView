package com.github.metagalactic.loginview.utils;

import android.view.View;
import android.view.animation.TranslateAnimation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AnimUtil {

    // To animate view slide out from bottom to top
    public static void slideToTop(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, -view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }
}
