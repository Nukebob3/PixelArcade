package net.nukebob.tetrismc.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;
import net.nukebob.tetrismc.TetrisMC;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TetrisConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(MinecraftClient.getInstance().runDirectory + "/config/" + TetrisMC.MOD_ID + "/config.json");
    private static TetrisConfig config;

    public boolean mod_enabled = true;
    public int tetris_hard_drop = 1;
    public float tetris_volume = 1.0f;

    public static TetrisConfig loadConfig() {
        if (!CONFIG_FILE.exists()) {
            config = new TetrisConfig();
            saveConfig();
        } else {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                config = GSON.fromJson(reader, TetrisConfig.class);
            } catch (IOException e) {
                TetrisMC.LOGGER.error("Could not load config file", e);
            }
        }
        return config;
    }

    public static void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            TetrisMC.LOGGER.error("Could not save config file", e);
        }
    }
}
