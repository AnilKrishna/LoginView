package com.github.metagalactic.loginview.users;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({
        Gender.MALE,
        Gender.FEMALE,
        Gender.UNKNOWN,
})
public @interface Gender {
    String MALE = "MALE";
    String FEMALE = "FEMALE";
    String UNKNOWN = "UNKNOWN";
}
