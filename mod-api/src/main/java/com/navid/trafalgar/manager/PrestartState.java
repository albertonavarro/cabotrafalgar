package com.navid.trafalgar.manager;

/**
 *
 *  
 */
public interface PrestartState extends StateListener{
    /**
     *
     * @param tpf
     */
    void onPrestart(float tpf);
}
