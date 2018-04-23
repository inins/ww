package com.frame.component.helper.sound;

public class AudioStateMessage {
    public int what;
    public Object obj;

    public AudioStateMessage() {
    }

    public static AudioStateMessage obtain() {
        return new AudioStateMessage();
    }
}