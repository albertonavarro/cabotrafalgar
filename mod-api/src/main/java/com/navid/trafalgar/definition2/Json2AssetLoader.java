package com.navid.trafalgar.definition2;

import com.google.gson.Gson;
import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Json2 jme3 custom loader
 *
 */
public class Json2AssetLoader implements AssetLoader {
    
    /**
     *
     * @param assetInfo
     * @return
     * @throws IOException
     */
    @Override
    public Object load(AssetInfo assetInfo) throws IOException {
        
        Reader r =new InputStreamReader(assetInfo.openStream());
        GameDefinition2 game = new Gson().fromJson(r, GameDefinition2.class);    
        r.close();
        
        return game;
    }
    
}
