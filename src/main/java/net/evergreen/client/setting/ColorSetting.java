/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.setting;

import java.awt.*;

public class ColorSetting extends Setting {

    private Color color;
    private boolean allowAlpha;

    public ColorSetting(Color def, String displayName, String description, boolean allowAlpha) {
        super(displayName, description);
        this.color = def;
        this.allowAlpha = allowAlpha;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean allowAlpha() {
        return allowAlpha;
    }

    public void setAllowAlpha(boolean allowAlpha) {
        this.allowAlpha = allowAlpha;
    }
}
