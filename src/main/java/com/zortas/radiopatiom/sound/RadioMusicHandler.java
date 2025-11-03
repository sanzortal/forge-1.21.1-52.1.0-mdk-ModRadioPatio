package com.zortas.radiopatiom.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;

public class RadioMusicHandler {

    private static SimpleSoundInstance currentSound;

    public static void play(SoundEvent sound, BlockPos pos) {
        stop();
        currentSound = SimpleSoundInstance.forLocalAmbience(sound, 1.0F, 1.0F);
        Minecraft.getInstance().getSoundManager().play(currentSound);
    }

    public static void stop() {
        if (currentSound != null) {
            Minecraft.getInstance().getSoundManager().stop(currentSound);
            currentSound = null;
        }
    }
}