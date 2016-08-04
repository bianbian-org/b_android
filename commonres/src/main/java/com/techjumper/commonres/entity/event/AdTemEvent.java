package com.techjumper.commonres.entity.event;

/**
 * Created by kevin on 16/7/14.
 */

public class AdTemEvent {
    private boolean fromCahce;

    public AdTemEvent(boolean fromCahce) {
        this.fromCahce = fromCahce;
    }

    public boolean isFromCahce() {
        return fromCahce;
    }

    public void setFromCahce(boolean fromCahce) {
        this.fromCahce = fromCahce;
    }
}
