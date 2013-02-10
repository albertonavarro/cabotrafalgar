package com.navid.trafalgar.manager;

/**
 *
 * @author anf
 */
public interface StartedState extends StateListener{
    void onStarted(float tpf);
}
