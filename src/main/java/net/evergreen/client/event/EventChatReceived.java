/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.event;

import net.evergreen.client.event.bus.CancellableEvent;
import net.minecraft.util.IChatComponent;

public class EventChatReceived extends CancellableEvent {

    public IChatComponent message;

    /* 0: Standard Text Message 1: System message displayed as standard 2: Actionbar message */
    public final byte type;

    public EventChatReceived(byte type, IChatComponent message) {
        this.type = type;
        this.message = message;
    }

}