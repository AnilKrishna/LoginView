package com.github.metagalactic.loginview.utils;

import android.support.annotation.Nullable;

import com.github.metagalactic.loginview.users.AccessToken;
import com.github.metagalactic.loginview.users.FacebookUserInfo;
import com.github.metagalactic.loginview.users.Gender;
import com.github.metagalactic.loginview.users.GoogleUserInfo;
import com.github.metagalactic.loginview.users.SmartFacebookUser;
import com.github.metagalactic.loginview.users.UserDisplayName;
import com.github.metagalactic.loginview.users.UserEmail;
import com.github.metagalactic.loginview.users.UserName;
import com.github.metagalactic.loginview.users.UserPassword;
import com.github.metagalactic.loginview.users.UserProfileInfo;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.github.metagalactic.loginview.users.UserProfileInfo.create;

@ParametersAreNonnullByDefault
public class UserInfoUtil {

    public interface FacebookFields {
        String EMAIL = "email";
        String ID = "id";
        String BIRTHDAY = "birthday";
        String GENDER = "gender";
        String FIRST_NAME = "first_name";
        String MIDDLE_NAME = "middle_name";
        String LAST_NAME = "last_name";
        String NAME = "name";
        String LINK = "link";
    }

    public static GoogleUserInfo populateGoogleUser(GoogleSignInAccount account) {
        return GoogleUserInfo.create()
                .setUserDisplayName(UserDisplayName.create(account.getDisplayName()))
                .setProfileImage(account.getPhotoUrl())
                .setEmail(UserEmail.create(account.getEmail()))
                .setAccessToken(AccessToken.create(account.getServerAuthCode()))
                .build();
    }

    @Nullable
    public static FacebookUserInfo populateFacebookUser(JSONObject object, String accessToken) {
        SmartFacebookUser facebookUser = new SmartFacebookUser();

        @Gender
        String genderType = Gender.UNKNOWN;
        UserEmail email = null;
        String facebookLink = null;

        try {

            if (object.has(FacebookFields.EMAIL)) {
                email = UserEmail.create(object.getString(FacebookFields.EMAIL));
            }

            if (object.has(FacebookFields.BIRTHDAY)) {
                facebookUser.setBirthday(object.getString(FacebookFields.BIRTHDAY));
            }

            if (object.has(FacebookFields.GENDER)) {
                try {
                    String gender = object.getString(FacebookFields.GENDER);
                    switch (gender) {
                        case Gender.MALE:
                            genderType = Gender.MALE;
                            break;
                        case Gender.FEMALE:
                            genderType = Gender.FEMALE;
                            break;
                    }
                } catch (Exception e) {
                    genderType = Gender.UNKNOWN;
                }
            }

            if (object.has(FacebookFields.LINK)) {
                facebookLink = object.getString(FacebookFields.LINK);
                facebookUser.setProfileLink(facebookLink);
            }
            if (object.has(FacebookFields.ID)) {
                facebookUser.setUserId(object.getString(FacebookFields.ID));
            }
            if (object.has(FacebookFields.NAME)) {
                facebookUser.setProfileName(object.getString(FacebookFields.NAME));
            }
            if (object.has(FacebookFields.FIRST_NAME)) {
                facebookUser.setFirstName(object.getString(FacebookFields.FIRST_NAME));
            }
            if (object.has(FacebookFields.MIDDLE_NAME)) {
                facebookUser.setMiddleName(object.getString(FacebookFields.MIDDLE_NAME));
            }
            if (object.has(FacebookFields.LAST_NAME)) {
                facebookUser.setLastName(object.getString(FacebookFields.LAST_NAME));
            }
        } catch (JSONException e) {
            //TODO :: log here
        }

        return FacebookUserInfo.create()
                .setEmail(email)
                .setGender(genderType)
                .setAccessToken(AccessToken.create(accessToken))
                .build();
    }

    public static UserProfileInfo populateCustomUserWithUserName(UserName userName,
                                                                 UserPassword password) {
        return UserProfileInfo.create(userName)
                .setPassword(password)
                .setGender(Gender.UNKNOWN)
                .build();
    }

    public static UserProfileInfo populateCustomUserWithUserEmail(UserEmail userEmail,
                                                                  UserPassword password) {
        return create(userEmail)
                .setPassword(password)
                .setGender(Gender.UNKNOWN)
                .build();

    }

}
