package com.navid.trafalgar.profiles;

import com.google.auto.value.AutoValue;

/**
 *
 * @author casa
 */
public class Pair {

    @AutoValue
    abstract static class TokenSession {

        static TokenSession create(String token, String session) {
            return new AutoValue_Pair_TokenSession(token, session);
        }

        abstract String token();

        abstract String session();
    }

}
