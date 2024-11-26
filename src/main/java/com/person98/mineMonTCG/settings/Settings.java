package com.person98.mineMonTCG.settings;

import com.person98.mineMonTCG.MineMonTCG;
import com.person98.mineMonTCG.util.PLogger;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Settings {
    private static YamlConfiguration config;

    // Card Settings
    @Getter private static Material defaultCardItem;
    @Getter private static String cardDisplayName;
    @Getter private static List<String> cardLore;
    @Getter private static boolean cardGlow;
    @Getter private static int cardCustomModelData;

    // Feature Settings
    @Getter private static boolean tradingEnabled;
    @Getter private static boolean allowDuplicates;
    @Getter private static boolean playerBattlesEnabled;
    @Getter private static boolean aiBattlesEnabled;

    public static void load() {
        File settingsFile = new File(MineMonTCG.getInstance().getDataFolder(), "settings.yml");
        if (!settingsFile.exists()) {
            MineMonTCG.getInstance().saveResource("settings.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(settingsFile);
        loadCardSettings();
        loadFeatureSettings();
        
        PLogger.info("Settings loaded successfully");
    }

    private static void loadCardSettings() {
        try {
            defaultCardItem = Material.valueOf(config.getString("card.defaultItem", "PAPER").toUpperCase());
        } catch (IllegalArgumentException e) {
            PLogger.warning("Invalid defaultItem in settings.yml, using PAPER");
            defaultCardItem = Material.PAPER;
        }

        cardDisplayName = config.getString("card.displayName", "<gradient:gold:yellow>Trading Card</gradient>");
        cardLore = config.getStringList("card.lore");
        cardGlow = config.getBoolean("card.glow", true);
        cardCustomModelData = config.getInt("card.customModelData", 1001);
    }

    private static void loadFeatureSettings() {
        tradingEnabled = config.getBoolean("features.trading.enabled", true);
        allowDuplicates = config.getBoolean("features.trading.allowDuplicates", true);
        playerBattlesEnabled = config.getBoolean("features.battles.playerBattles", true);
        aiBattlesEnabled = config.getBoolean("features.battles.aiBattles", true);
    }

    public static void reload() {
        load();
    }
} 