package net.nukebob.game.tetris;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.nukebob.PixelArcade;
import net.nukebob.game.tetris.mino.Block;
import net.nukebob.screen.TetrisScreen;

public class HardDropAnimation extends Animation{
    public HardDropAnimation(int x, int y, int width, int height, int frames) {
        super(x, y, width, height, "hard_drop", frames);
    }

    @Override
    public void draw(DrawContext context) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1, 1, 1, (1 - frame * ((float) 1 / frames)) / 4);
        context.drawTexture(Identifier.of(PixelArcade.MOD_ID, "animation/hard_drop/0.png"), x + TetrisScreen.left_x - width / 2, y + TetrisScreen.top_y + Block.SIZE, 0, 0, width, height, width, height);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }
}
