/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.event;

import net.evergreen.client.event.bus.CancellableEvent;

public class EventCommandSent extends CancellableEvent {

    public String commandName;
    public String[] parameters;

    public EventCommandSent(String commandName, String[] args) {
        this.commandName = commandName;
        this.parameters = args;
    }

}
