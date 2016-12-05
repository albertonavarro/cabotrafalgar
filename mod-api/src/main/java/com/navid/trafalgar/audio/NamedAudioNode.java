package com.navid.trafalgar.audio;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;

/**
 * Created by anavarro on 05/12/16.
 */
public class NamedAudioNode extends AudioNode {

    public NamedAudioNode(AssetManager assetManager, String musicFile, AudioData.DataType stream) {
        super(assetManager, musicFile, stream);
    }

    @Override
    public String getName() {
        return this.audioKey.getName();
    }
}
