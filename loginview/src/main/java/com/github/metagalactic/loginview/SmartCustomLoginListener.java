package com.github.metagalactic.loginview;

import com.github.metagalactic.loginview.users.SmartUser;

public interface SmartCustomLoginListener {

    boolean customSignIn(SmartUser user);

    boolean customSignUp(SmartUser newUser);
}
