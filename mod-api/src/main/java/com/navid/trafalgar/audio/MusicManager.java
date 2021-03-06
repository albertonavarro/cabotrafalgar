package com.navid.trafalgar.audio;

import com.google.common.collect.EnumMultiset;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by alberto on 9/27/15.
 */
public class MusicManager {

    private Multimap<String, AudioNode> musicPerAmbient = HashMultimap.create();

    private String currentAmbient = null;

    private AudioNode currentMusic = null;

    private float maxGlobalVolume = 1;
    private float currentGlobalVolume = 1;

    @Autowired
    private AssetManager assetManager;

    public void setAmbientMusic(String ambient, String musicFile) {
        AudioNode audioNode = new NamedAudioNode(assetManager, musicFile, AudioData.DataType.Stream);
        audioNode.setPositional(false);
        musicPerAmbient.put(ambient, audioNode);
    }

    public void setCurrentAmbient(String ambient) {
        if(currentAmbient != ambient) {
            currentAmbient = ambient;

            if(currentMusic != null) {
                currentMusic.stop();
            }

            currentMusic = getRandomForAmbient(ambient);

            if (currentMusic!=null){
                currentMusic.setVolume(currentGlobalVolume);
                currentMusic.play();
            }
        }
    }

    private AudioNode getRandomForAmbient(String ambient) {
        List<AudioNode> files = new ArrayList(musicPerAmbient.get(ambient));

        if (!files.isEmpty()) {
            Collections.shuffle(files);
            return files.get(0);
        }

        return null;
    }

    public AudioNode getCurrentMusic() {
        return currentMusic;
    }

    public void toggleMute() {
        currentGlobalVolume = currentGlobalVolume == 0 ? maxGlobalVolume : 0;

        currentMusic.setVolume(currentGlobalVolume);
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
}
