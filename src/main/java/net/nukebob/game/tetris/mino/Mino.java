package net.nukebob.game.tetris.mino;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.nukebob.config.ConfigManager;
import net.nukebob.game.tetris.HardDropAnimation;
import net.nukebob.screen.TetrisScreen;
import net.nukebob.util.TextureResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Mino {
    public Block[] b = new Block[4];
    public Block[] tempB = new Block[4];
    float dropCounter = 0;
    public int direction = 1;
    boolean leftCollision, rightCollision, bottomCollision;
    public boolean active = true;

    public Mino() {
        TextureResource randomBlockTexture = getRandomBlockTexture();
        create(randomBlockTexture.texture, randomBlockTexture.width, randomBlockTexture.height);
    }

    public void create(Identifier t, int textureWidth, int textureHeight) {
        b[0] = new Block(t, textureWidth, textureHeight);
        b[1] = new Block(t, textureWidth, textureHeight);
        b[2] = new Block(t, textureWidth, textureHeight);
        b[3] = new Block(t, textureWidth, textureHeight);
        tempB[0] = new Block(t, textureWidth, textureHeight);
        tempB[1] = new Block(t, textureWidth, textureHeight);
        tempB[2] = new Block(t, textureWidth, textureHeight);
        tempB[3] = new Block(t, textureWidth, textureHeight);
    }

    public void setXY (int x, int y) {}
    public void updateXY (int direction) {
        for (Block b : tempB) {
            if (b.x < 0) return;
            if (b.x > TetrisScreen.WIDTH - Block.SIZE) return;
            if (b.y > TetrisScreen.HEIGHT - Block.SIZE) return;
            for (Block sB : TetrisScreen.staticBlocks) {
                if (sB.x == b.x && sB.y == b.y) {
                    return;
                }
            }
        }
        this.direction = direction;
        b[0].x = tempB[0].x;
        b[0].y = tempB[0].y;
        b[1].x = tempB[1].x;
        b[1].y = tempB[1].y;
        b[2].x = tempB[2].x;
        b[2].y = tempB[2].y;
        b[3].x = tempB[3].x;
        b[3].y = tempB[3].y;
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(Identifier.ofVanilla("block.wooden_button.click_on")), 2.0F, ConfigManager.loadConfig().tetris_volume));
    }
    public void getDirection1() {}
    public void getDirection2() {}
    public void getDirection3() {}
    public void getDirection4() {}
    public void checkMovementCollision() {
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkStaticBlockCollision();

        // check frame collision
        //left wall
        for (Block block : b) {
            if (block.x == 0) {
                leftCollision = true;
                break;
            }
        }
        //right wall
        for (Block value : b) {
            if (value.x + Block.SIZE == TetrisScreen.WIDTH) {
                rightCollision = true;
                break;
            }
        }
        //bottom floor
        for (Block block : b) {
            if (block.y + Block.SIZE == TetrisScreen.HEIGHT) {
                bottomCollision = true;
                break;
            }
        }
    }

    private void checkStaticBlockCollision() {
        for (Block staticBlock : TetrisScreen.staticBlocks) {

            //check down
            for (Block block : b) {
                if (block.y + Block.SIZE == staticBlock.y && block.x == staticBlock.x) {
                    bottomCollision = true;
                    break;
                }
            }
            //check left and right
            for (Block block : b) {
                if (block.x - Block.SIZE == staticBlock.x && block.y == staticBlock.y) {
                    leftCollision = true;
                    break;
                }
            }
            for (Block block : b) {
                if (block.x + Block.SIZE == staticBlock.x && block.y == staticBlock.y) {
                    rightCollision = true;
                    break;
                }
            }

        }
    }
    protected TextureResource getRandomBlockTexture() {
        MinecraftClient client = MinecraftClient.getInstance();
        net.minecraft.block.Block block;
        List<BakedQuad> quads;
        SpriteContents texture;
        BlockState blockState;
        while (true) {
            block = Registries.BLOCK.get(new Random().nextInt(Registries.BLOCK.size()));
            blockState = block.getDefaultState();
            quads = client.getBlockRenderManager().getModel(blockState).getQuads(blockState, Direction.NORTH, MinecraftClient.getInstance().textRenderer.random);
            if (!(quads.isEmpty())) {
                texture = quads.get(new Random().nextInt(quads.size())).getSprite().getContents();
                if (texture.getWidth() == 16 && texture.getHeight() == 16) {
                    Optional<Resource> opR = client.getResourceManager().getResource(Identifier.of("textures/" + texture.getId().getPath() + ".png"));
                    Resource resource = null;
                    if (opR.isPresent()) resource = opR.get();
                    if (resource != null) {
                        BufferedImage image;
                        try {
                            image = ImageIO.read(resource.getInputStream());
                            return new TextureResource(Identifier.of(texture.getId().getNamespace().split(":")[0],"textures/" + texture.getId().getPath() + ".png"), image.getWidth(), image.getHeight());
                        } catch (Exception ignored) {}
                    }
                }
            }
        }
    }
    public void update(float timePassed) {
        checkMovementCollision();

        if (TetrisScreen.upPressed) {
            switch (direction) {
                case 1: getDirection2(); break;
                case 2: getDirection3(); break;
                case 3: getDirection4(); break;
                case 4: getDirection1(); break;
            }
            TetrisScreen.upPressed = false;
        }
        if (TetrisScreen.leftPressed) {
            if (!leftCollision) {
                boolean proceed = true;
                for (Block block : b) {
                    for (Block sBlock : TetrisScreen.staticBlocks) {
                        if (block.x - Block.SIZE == sBlock.x && block.y == sBlock.y) {
                            proceed = false;
                            break;
                        }
                    }
                }
                if (proceed) {
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(Identifier.ofVanilla("block.wooden_button.click_on")), 2.0F,  ConfigManager.loadConfig().tetris_volume));
                    for (Block block : b) {
                        block.x -= Block.SIZE;
                    }
                }
            }
            TetrisScreen.leftPressed = false;
        }
        if (TetrisScreen.rightPressed) {
            if (!rightCollision) {
                boolean proceed = true;
                for (Block block : b) {
                    for (Block sBlock : TetrisScreen.staticBlocks) {
                        if (block.x + Block.SIZE == sBlock.x && block.y == sBlock.y) {
                            proceed = false;
                            break;
                        }
                    }
                }
                if (proceed) {
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(Identifier.ofVanilla("block.wooden_button.click_on")), 2.0F,  ConfigManager.loadConfig().tetris_volume));
                    for (Block block : b) {
                        block.x += Block.SIZE;
                    }
                }
            }
            TetrisScreen.rightPressed = false;
        }
        if (TetrisScreen.downPressed) {
            if (bottomCollision) {
                dropCounter += 30;
            } else {
                boolean proceed = true;
                for (Block block : b) {
                    for (Block b : TetrisScreen.staticBlocks) {
                        if (block.y + Block.SIZE == b.y && block.x == b.x) {
                            proceed = false;
                        }
                    }
                }
                if (proceed) {
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(Identifier.ofVanilla("block.wooden_button.click_on")), 2.0F, ConfigManager.loadConfig().tetris_volume));
                    for (Block block : b) {
                        block.y += Block.SIZE;
                    }
                    TetrisScreen.score++;
                    dropCounter = 0;
                }
            }
            TetrisScreen.downPressed = false;
        }
        if (TetrisScreen.spacePressed && TetrisScreen.hardDrop > 0) {
            MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(Identifier.ofVanilla("entity.wind_charge.wind_burst")), 1.0F,  ConfigManager.loadConfig().tetris_volume));
            int drop = getDropOffset();
            TetrisScreen.animations.add(new HardDropAnimation(b[0].x, b[0].y, 27, drop, 10));
            b[0].y += drop;
            b[1].y += drop;
            b[2].y += drop;
            b[3].y += drop;
            TetrisScreen.spacePressed = false;
            this.active = false;
            TetrisScreen.score += 2 * drop / Block.SIZE;
            return;
        }
        dropCounter = dropCounter + timePassed;
        if (Math.floor(dropCounter) >= TetrisScreen.dropInterval) {
            if (bottomCollision) {
                checkStaticBlockCollision();
                checkMovementCollision();
                if (bottomCollision) active = false;
            } else {
                boolean proceed = true;
                for (Block block : b) {
                    for (Block b : TetrisScreen.staticBlocks) {
                        if (block.y + Block.SIZE == b.y && block.x == b.x) {
                            proceed = false;
                            break;
                        }
                    }
                }
                if (proceed) {
                    for (Block block : b) {
                        block.y += Block.SIZE;
                    }
                    dropCounter = 0;
                }
            }
        }
    }
    public void draw (DrawContext context) {
        for (Block block : b) {
            block.draw(context);
        }
    }
    public void drawHardDrop (DrawContext context) {
        int yOffset = getDropOffset();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1, 1,1, 0.5f);
        for (Block block : b) {
            switch (TetrisScreen.hardDrop) {
                case 2:
                if (getOutline(block)[0]) {
                    context.drawHorizontalLine(TetrisScreen.left_x + block.x, TetrisScreen.left_x + block.x + Block.SIZE - 1, TetrisScreen.top_y + block.y + yOffset, Colors.WHITE);
                }
                if (getOutline(block)[1]) {
                    context.drawHorizontalLine(TetrisScreen.left_x + block.x, TetrisScreen.left_x + block.x + Block.SIZE - 1, TetrisScreen.top_y + block.y + Block.SIZE - 1 + yOffset, Colors.WHITE);
                }
                if (getOutline(block)[2]) {
                    context.drawVerticalLine(TetrisScreen.left_x + block.x, TetrisScreen.top_y + block.y + yOffset, TetrisScreen.top_y + block.y + Block.SIZE - 1 + yOffset, Colors.WHITE);
                    if (!getOutline(block)[1]) {
                        context.drawVerticalLine(TetrisScreen.left_x + block.x, TetrisScreen.top_y + block.y + Block.SIZE + yOffset - 1, TetrisScreen.top_y + block.y + Block.SIZE + yOffset - 1, Colors.WHITE);
                    }
                    if (!getOutline(block)[0]) {
                        context.drawVerticalLine(TetrisScreen.left_x + block.x, TetrisScreen.top_y + block.y + yOffset, TetrisScreen.top_y + block.y + yOffset, Colors.WHITE);
                    }
                }
                if (getOutline(block)[3]) {
                    context.drawVerticalLine(TetrisScreen.left_x + block.x + Block.SIZE - 1, TetrisScreen.top_y + block.y + yOffset, TetrisScreen.top_y + block.y + Block.SIZE - 1 + yOffset, Colors.WHITE);
                    if (!getOutline(block)[1]) {
                        context.drawVerticalLine(TetrisScreen.left_x + block.x + Block.SIZE - 1, TetrisScreen.top_y + block.y + Block.SIZE + yOffset - 1, TetrisScreen.top_y + block.y + Block.SIZE + yOffset - 1, Colors.WHITE);
                    }
                    if (!getOutline(block)[0]) {
                        context.drawVerticalLine(TetrisScreen.left_x + block.x + Block.SIZE - 1, TetrisScreen.top_y + block.y + yOffset, TetrisScreen.top_y + block.y + yOffset, Colors.WHITE);
                    }
                }
                //individual diagonal pixels
                if (!(this instanceof Mino_Square)) {
                    if (!getOutline(block)[0] && !getOutline(block)[2]) {
                        context.drawVerticalLine(TetrisScreen.left_x + block.x, TetrisScreen.top_y + block.y + yOffset, TetrisScreen.top_y + block.y + yOffset, Colors.WHITE);
                    }
                    if (!getOutline(block)[0] && !getOutline(block)[3]) {
                        context.drawVerticalLine(TetrisScreen.left_x + block.x + Block.SIZE - 1, TetrisScreen.top_y + block.y + yOffset, TetrisScreen.top_y + block.y + yOffset, Colors.WHITE);
                    }
                    if (!getOutline(block)[1] && !getOutline(block)[2]) {
                        context.drawVerticalLine(TetrisScreen.left_x + block.x, TetrisScreen.top_y + block.y + Block.SIZE + yOffset - 1, TetrisScreen.top_y + block.y + Block.SIZE + yOffset - 1, Colors.WHITE);
                    }
                    if (!getOutline(block)[1] && !getOutline(block)[3]) {
                        context.drawVerticalLine(TetrisScreen.left_x + block.x + Block.SIZE - 1, TetrisScreen.top_y + block.y + Block.SIZE + yOffset - 1, TetrisScreen.top_y + block.y + Block.SIZE + yOffset - 1, Colors.WHITE);
                    }
                } break;
                case 3: block.draw(context, yOffset); break;
            }
        }
        RenderSystem.setShaderColor(1, 1,1,1);
        RenderSystem.disableBlend();
    }

    private int getDropOffset() {
        int i;
        for (i = 0; i < TetrisScreen.HEIGHT / Block.SIZE; i++) {
            for (Block b : b) {
                for (Block sB : TetrisScreen.staticBlocks) {
                    if (b.x == sB.x && b.y + i * Block.SIZE == sB.y) {
                        return i * Block.SIZE - Block.SIZE;
                    }
                }
                if (b.y + i * Block.SIZE > TetrisScreen.HEIGHT - Block.SIZE) {
                    return i * Block.SIZE - Block.SIZE;
                }
            }
        }
        return i * Block.SIZE - Block.SIZE;
    }

    private boolean[] getOutline(Block b) {
        boolean top = true;
        boolean bottom = true;
        boolean left = true;
        boolean right = true;
        for (Block block : this.b) {
            if (b.y - Block.SIZE == block.y && b.x == block.x) {
                top = false;
            }
            if (b.y + Block.SIZE == block.y && b.x == block.x) {
                bottom = false;
            }
            if (b.x - Block.SIZE == block.x && b.y == block.y) {
                left = false;
            }
            if (b.x + Block.SIZE == block.x && b.y == block.y) {
                right = false;
            }
        }
        return new boolean[]{top, bottom, left, right};
    }
}
