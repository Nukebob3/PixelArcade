package net.nukebob.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.nukebob.PixelArcade;
import net.nukebob.config.ConfigManager;
import net.nukebob.screen.TetrisScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "initWidgetsNormal")
    private void addMinigameButton(int y, int spacingY, CallbackInfo ci) {
        TextIconButtonWidget textIconButtonWidget = TextIconButtonWidget.builder(Text.empty(), (button) -> {
            this.client.setScreen(new TetrisScreen(this));
        }, true).width(20).texture(Identifier.of(PixelArcade.MOD_ID, "icon/button"), 16, 16).build();
        textIconButtonWidget.setPosition(this.width / 2 - 100 + 205, y);
        if (ConfigManager.loadConfig().mod_enabled) this.addDrawableChild(textIconButtonWidget);
    }
}
