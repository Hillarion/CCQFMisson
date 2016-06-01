package com.devel.ccqf.ccqfmisson.Utilitairies;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jo on 5/25/16.
 */
public class Verify {

    public Verify() {
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public final static boolean isValidEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

   /* public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }*/

    public final static boolean isValidPhone(CharSequence target){
        if(TextUtils.isEmpty(target)){
            return false;
        }else {
            return Patterns.PHONE.matcher(target).matches();
        }
    }

    public final static boolean isValidName(CharSequence target){
        if(TextUtils.isEmpty(target)){
            return false;
        }else {
            String regex = "[A-Z][a-z]+([ -][A-Z][a-z]+)?";
            return Pattern.matches(regex, target);
        }
    }
}
