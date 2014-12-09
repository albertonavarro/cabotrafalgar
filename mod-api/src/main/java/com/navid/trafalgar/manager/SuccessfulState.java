package com.navid.trafalgar.manager;

/**
 *
 *
 */
public interface SuccessfulState extends StateListener {

    /**
     *
     * @param tpf
     */
    void onSuccess(float tpf);
}
