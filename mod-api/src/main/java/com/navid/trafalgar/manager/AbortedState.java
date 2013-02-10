package com.navid.trafalgar.manager;

/**
 *
 * @author alberto
 */
public interface AbortedState extends StateListener{

    void onAborted(float tpf);
}
