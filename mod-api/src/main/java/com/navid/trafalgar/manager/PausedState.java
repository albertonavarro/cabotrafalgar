package com.navid.trafalgar.manager;

/**
 *
 * @author alberto
 */
public interface PausedState extends StateListener {

    void onPaused(float tpf);
}
