package net.nukebob.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.nukebob.PixelArcade;

public class ConfigScreen extends Screen {
    public static boolean texturepackEnabled;

    private final Screen parent;

    Config config = ConfigManager.loadConfig();

    int enableColour = 8781731;
    int disableColour = 16745861;

    public ConfigScreen(Screen parent) {
        super(Text.translatable("pixelarcade:config.title"));
        this.parent = parent;
        texturepackEnabled = MinecraftClient.getInstance().getResourcePackManager().getEnabledIds().contains(PixelArcade.MOD_ID + ":" + "dungeons_bedwars");
    }

    @Override
    protected void init() {
        int buttonWidth = 150;
        int buttonHeight = 20;
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        ButtonWidget toggleModEnabledWidget = ButtonWidget.builder(Text.translatable("pixelarcade:config.mod").append(" ").append(Text.translatable("pixelarcade:config." + (config.mod_enabled ? "enabled" : "disabled"))).withColor(config.mod_enabled ? Colors.GREEN : Colors.RED), this::toggleModEnabled)
                .dimensions(centerX - buttonWidth / 2, centerY - 45, buttonWidth, buttonHeight).build();
        ButtonWidget hardDropSettingsWidget = ButtonWidget.builder(Text.translatable("pixelarcade:tetris.hard_drop").append(": ").append(Text.translatable(config.tetris_hard_drop == 0 ? "pixelarcade:config.disabled" : "pixelarcade:tetris.hard_drop." + (config.tetris_hard_drop == 1 ? "previewless" : (config.tetris_hard_drop == 2 ? "outline" : "hologram")))).withColor((config.tetris_hard_drop != 0) ? enableColour : disableColour), button -> {config.tetris_hard_drop++; if (config.tetris_hard_drop > 3) config.tetris_hard_drop = 0; button.setMessage(Text.translatable("pixelarcade:tetris.hard_drop").append(": ").append(Text.translatable(config.tetris_hard_drop == 0 ? "pixelarcade:config.disabled" : "pixelarcade:tetris.hard_drop." + (config.tetris_hard_drop == 1 ? "previewless" : (config.tetris_hard_drop == 2 ? "outline" : "hologram")))).withColor((config.tetris_hard_drop != 0) ? enableColour : disableColour)); ConfigManager.saveConfig();})
                .dimensions(centerX - buttonWidth / 2, centerY - 20, buttonWidth, buttonHeight).build();
        SliderWidget volumeSlider = new SliderWidget(centerX - buttonWidth / 2, centerY, buttonWidth, buttonHeight,
                Text.translatable("pixelarcade:config.volume"), config.tetris_volume) {
            {
                this.updateMessage();
            }
            @Override
            protected void updateMessage() {
                this.setMessage(Text.translatable("pixelarcade:config.volume").append(": " + (int) (Math.round(this.value * 100)) + "%"));
            }

            @Override
            protected void applyValue() {
                config.tetris_volume = (float) this.value;
                ConfigManager.saveConfig();
            }
        };
        SliderWidget musicSlider = new SliderWidget(centerX - buttonWidth / 2, centerY + 20, buttonWidth, buttonHeight,
                Text.translatable("pixelarcade:config.music"), config.tetris_music) {
            {
                this.updateMessage();
            }
            @Override
            protected void updateMessage() {
                this.setMessage(Text.translatable("pixelarcade:config.music").append(": " + (int) (Math.round(this.value * 100)) + "%"));
            }

            @Override
            protected void applyValue() {
                config.tetris_music = (float) this.value;
                ConfigManager.saveConfig();
            }
        };

        ButtonWidget doneButtonWidget = ButtonWidget.builder(Text.translatable("pixelarcade:config.done").withColor(Colors.WHITE), button -> closeScreen())
                .dimensions(centerX - buttonWidth / 2, centerY + 45, buttonWidth, buttonHeight).build();

        this.addDrawableChild(toggleModEnabledWidget);
        this.addDrawableChild(hardDropSettingsWidget);
        this.addDrawableChild(volumeSlider);
        this.addDrawableChild(musicSlider);
        this.addDrawableChild(doneButtonWidget);

        super.init();
    }

    private void toggleModEnabled(ButtonWidget buttonWidget) {
        config.mod_enabled = !config.mod_enabled;
        buttonWidget.setMessage(Text.translatable("pixelarcade:config.mod").append(" ").append(Text.translatable("pixelarcade:config." + (config.mod_enabled ? "enabled" : "disabled"))).withColor(config.mod_enabled ? Colors.GREEN : Colors.RED));
        ConfigManager.saveConfig();
    }

    private void closeScreen() {
        this.client.setScreen(this.parent);
    }
}
