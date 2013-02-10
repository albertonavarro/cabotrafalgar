package com.navid.trafalgar.manager;

/**
 *
 * @author anf
 */
public interface FailedState extends StateListener {

    void onFailed(float tpf);
}
