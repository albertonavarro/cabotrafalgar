package com.navid.trafalgar.manager;

public interface PausedState extends StateListener {

    /**
     *
     * @param tpf
     */
    void onPaused(float tpf);
}
