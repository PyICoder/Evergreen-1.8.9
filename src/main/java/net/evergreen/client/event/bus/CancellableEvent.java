/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.event.bus;

public class CancellableEvent extends Event {

    private boolean cancelled;
    private boolean cancellable = true;

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled && cancellable;
    }

    protected void setCancellable(boolean state) {
        this.cancellable = state;
    }

    @Override
    public boolean post() {
        super.post();
        return isCancelled();
    }
}
