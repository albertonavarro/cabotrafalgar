package com.navid.trafalgar.manager;

/**
 *
 *  
 */
public interface StartedState extends StateListener{
    /**
     *
     * @param tpf
     */
    void onStarted(float tpf);
}
