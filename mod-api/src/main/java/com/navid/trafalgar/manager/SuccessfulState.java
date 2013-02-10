package com.navid.trafalgar.manager;

/**
 *
 * @author anf
 */
public interface SuccessfulState extends StateListener {
    void onSuccess(float tpf);
}
