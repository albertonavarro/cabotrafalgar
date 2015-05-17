package com.navid.trafalgar.manager;

public interface StartedState extends StateListener {

    void onStarted(float tpf);
}
