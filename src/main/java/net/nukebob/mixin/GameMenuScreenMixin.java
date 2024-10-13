package net.nukebob.mixin;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.nukebob.PixelArcade;
import net.nukebob.config.ConfigManager;
import net.nukebob.config.ModMenuIntegration;
import net.nukebob.screen.TetrisScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "initWidgets")
    private void addMinigameButton(CallbackInfo ci) {
        TextIconButtonWidget textIconButtonWidget = TextIconButtonWidget.builder(Text.empty(), (button) -> {
            openScreen();
        }, true).width(20).texture(Identifier.of(PixelArcade.MOD_ID, "icon/button"), 16, 16).build();
        textIconButtonWidget.setPosition(this.width / 2 - 100 + 205, 50);

        for (ButtonWidget button : this.children().stream().filter(e -> e instanceof ButtonWidget).map(e -> (ButtonWidget) e).toList()) {
            if (button.getMessage().equals(Text.translatable("menu.returnToGame"))) {
                int buttonX = button.getX();
                int buttonY = button.getY();
                int buttonWidth = button.getWidth();
                textIconButtonWidget.setPosition(buttonX + buttonWidth + 5, buttonY);
                break;
            }
        }

        if (ConfigManager.loadConfig().mod_enabled) this.addDrawableChild(textIconButtonWidget);
    }

    @Unique
    private void openScreen() {
        switch (ConfigManager.loadConfig().game) {
            case 0: this.client.setScreen(new TetrisScreen(this)); break;
        }
    }
}
