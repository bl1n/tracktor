package com.elegion.tracktor.event;

import android.util.Log;

public class ExpandViewEvent {
    private long trackId;


    public ExpandViewEvent(long trackId) {
        this.trackId = trackId;
        Log.d("Debug", "ExpandViewEvent: ");
//        Log.d("Debug", "bind: " + trackId);

    }

    public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }

}
