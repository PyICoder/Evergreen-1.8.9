/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package net.evergreen.client.event;

import net.evergreen.client.event.bus.Event;
import net.evergreen.client.event.bus.Phase;

public class EventClientTick extends Event {

    public final Phase phase;

    public EventClientTick(Phase phase) {
        this.phase = phase;
    }

}
