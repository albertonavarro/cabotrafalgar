package com.navid.trafalgar.manager;

/**
 *
 *
 */
public interface FailedState extends StateListener {

    /**
     *
     * @param tpf
     */
    void onFailed(float tpf);
}
