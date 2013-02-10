package com.navid.trafalgar.manager;

/**
 *
 * @author anf
 */
public interface PrestartState extends StateListener{
    void onPrestart(float tpf);
}
