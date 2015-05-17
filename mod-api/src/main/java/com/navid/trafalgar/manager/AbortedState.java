package com.navid.trafalgar.manager;

public interface AbortedState extends StateListener {

    void onAborted(float tpf);
}
