package net.nukebob.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.nukebob.TetrisMC;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/" + TetrisMC.MOD_ID + ".json");
    private static Config config;

    public static Config loadConfig() {
        if (!CONFIG_FILE.exists()) {
            config = new Config();
            saveConfig();
        } else {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                config = GSON.fromJson(reader, Config.class);
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