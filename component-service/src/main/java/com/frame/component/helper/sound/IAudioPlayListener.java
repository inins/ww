package com.frame.component.helper.sound;

import android.net.Uri;

public interface IAudioPlayListener {
    void onStart(Uri var1, String var2);

    void onStop(Uri var1, String var2);

    void onComplete(Uri var1, String var2);
}
