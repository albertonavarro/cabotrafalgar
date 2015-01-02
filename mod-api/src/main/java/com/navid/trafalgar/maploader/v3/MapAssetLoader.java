package com.navid.trafalgar.maploader.v3;

import com.google.gson.Gson;
import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * map jme3 custom loader
 *
 */
public final class MapAssetLoader implements AssetLoader {

    /**
     *
     * @param assetInfo
     * @return
     * @throws IOException
     */
    @Override
    public Object load(AssetInfo assetInfo) throws IOException {

        Reader r = new InputStreamReader(assetInfo.openStream());
        MapDefinition game = new Gson().fromJson(r, MapDefinition.class);
        r.close();

        return game;
    }

}
