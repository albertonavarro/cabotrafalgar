package com.navid.trafalgar.manager;

/**
 *
 *   
 */
public interface AbortedState extends StateListener{

    /**
     *
     * @param tpf
     */
    void onAborted(float tpf);
}
