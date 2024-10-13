package net.nukebob.util;

import net.minecraft.util.Identifier;

public class TextureResource {
    public final Identifier texture;
    public final int height;
    public final int width;

    public TextureResource(Identifier texture, int height, int width) {
        this.height = height;
        this.width = width;
        this.texture = texture;
    }
}
