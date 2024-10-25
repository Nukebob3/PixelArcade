package net.nukebob.tetrismc.game.tetris.mino;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import net.nukebob.tetrismc.config.TetrisConfig;
import net.nukebob.tetrismc.screen.TetrisScreen;

public class Block {
    public int x, y;
    public static int SIZE = 16;
    public Identifier texture;
    public String mino;
    public int textureWidth;
    public int textureHeight;
    public int destroying;

    public Block (Identifier t, int textureWidth, int textureHeight, String mino) {
        this.texture = t;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.destroying = -1;
        this.mino = mino;
    }

    public void draw(DrawContext context) {
        Identifier texture = this.texture;
        int textureHeight = this.textureHeight;
        int textureWidth = this.textureWidth;
        if (!TetrisConfig.loadConfig().tetris_random_textures) {
            textureHeight = 16;
            textureWidth = 16;
            texture = getDefaultTexture();
        }
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        if (destroying == -1) {
            context.drawTexture(texture, TetrisScreen.left_x + x, TetrisScreen.top_y + y, 0, Block.SIZE * (int) (TetrisScreen.animation / 30f), Block.SIZE, Block.SIZE, Block.SIZE, (int) (Block.SIZE * (float) (textureWidth / textureHeight)));
        } else {
            MinecraftClient.getInstance().getTextureManager().bindTexture(texture);
            RenderSystem.setShaderColor(1, 1, 1, 1 - (destroying * 0.1f));
            context.drawTexture(texture, TetrisScreen.left_x + x, TetrisScreen.top_y + y, 0, Block.SIZE * (int) (TetrisScreen.animation / 30f), Block.SIZE, Block.SIZE, Block.SIZE, (int) (Block.SIZE * (float)(textureWidth / textureHeight)));
            RenderSystem.setShaderColor(1, 1, 1, 1);
            context.drawTexture(Identifier.of("textures/block/destroy_stage_" + destroying + ".png"), TetrisScreen.left_x + x, TetrisScreen.top_y + y, 0, Block.SIZE * (int) (TetrisScreen.animation / 30f), Block.SIZE, Block.SIZE, Block.SIZE, (int) (Block.SIZE * (float)(textureWidth / textureHeight)));
        }
        RenderSystem.disableBlend();
    }

    public void draw(DrawContext context, int yOffset) {
        Identifier texture = this.texture;
        int textureHeight = this.textureHeight;
        int textureWidth = this.textureWidth;
        if (!TetrisConfig.loadConfig().tetris_random_textures) {
            textureHeight = 16;
            textureWidth = 16;
            texture = getDefaultTexture();
        }
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1, 1, 1, 0.3f);
        context.drawTexture(texture, TetrisScreen.left_x + x, TetrisScreen.top_y + y + yOffset, 0, Block.SIZE * (int) (TetrisScreen.animation / 30f), Block.SIZE, Block.SIZE, Block.SIZE, (int) (Block.SIZE * (float) (textureWidth / textureHeight)));
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
    }

    private Identifier getDefaultTexture() {
        Identifier texture = Identifier.of("textures/block/iron_block.png");
        if (mino.equals("square")) {
            texture = Identifier.of("textures/block/gold_block.png");
        } else if (mino.equals("bar")) {
            texture = Identifier.of("textures/block/diamond_block.png");
        } else if (mino.equals("t")) {
            texture = Identifier.of("textures/block/amethyst_block.png");
        } else if (mino.equals("l1")) {
            texture = Identifier.of("textures/block/copper_block.png");
        } else if (mino.equals("l2")) {
            texture = Identifier.of("textures/block/lapis_block.png");
        } else if (mino.equals("z1")) {
            texture = Identifier.of("textures/block/redstone_block.png");
        } else if (mino.equals("z2")) {
            texture = Identifier.of("textures/block/emerald_block.png");
        }
        return texture;
    }
}
