/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.mod.counterclock.profile;

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
