package com.navid.trafalgar.manager;

public interface FailedState extends StateListener {

    void onFailed(float tpf);
}
