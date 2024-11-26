package com.person98.mineMonTCG.util;

import com.person98.mineMonTCG.MineMonTCG;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Lang {

    private static File langFile;
    @Getter
    private static FileConfiguration langConfig;

    public static void setup() {
        // Get the plugin instance
        PLogger.info("Loading Lang...");
        MineMonTCG plugin = MineMonTCG.getInstance();

        // Create the lang.yml file in the plugin folder if it doesn't exist
        langFile = new File(plugin.getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            plugin.saveResource("lang.yml", false); // Copies lang.yml from the plugin jar to the plugin folder
        }

        // Load the lang.yml configuration
        langConfig = YamlConfiguration.loadConfiguration(langFile);

        PLogger.info("Lang loaded");
    }

    public static void sendMessage(Player player, LANG messageType, String... placeholders) {
        String message = langConfig.getString(messageType.getPath());
        if (message != null) {
            // Replace placeholders if provided
            for (int i = 0; i < placeholders.length; i += 2) {
                message = message.replace(placeholders[i], placeholders[i + 1]);
            }
            player.sendMessage(MiniMessage.miniMessage().deserialize(message));
        }
    }

    public static void saveLang() {
        try {
            langConfig.save(langFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadLang() {
        langConfig = YamlConfiguration.loadConfiguration(langFile);
    }

    @Getter
    public enum LANG {
        PLAYER_NOT_FOUND("messages.playerNotFound"),
        RELOADED("messages.reloaded");

        private final String path;

        LANG(String path) {
            this.path = path;
        }
    }
}
