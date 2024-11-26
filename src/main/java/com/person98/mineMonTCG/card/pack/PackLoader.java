package com.person98.mineMonTCG.card.pack;

import com.person98.mineMonTCG.MineMonTCG;
import com.person98.mineMonTCG.card.CardLoader;
import com.person98.mineMonTCG.util.PLogger;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class PackLoader {
    @Getter
    private static final Map<String, Pack> packs = new HashMap<>();
    @Getter
    private static final List<Pack> packsList = new ArrayList<>();

    public static void loadPacks() {
        File packFile = new File(MineMonTCG.getInstance().getDataFolder(), "packs.yml");
        if (!packFile.exists()) {
            MineMonTCG.getInstance().saveResource("packs.yml", false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(packFile);
        ConfigurationSection packsSection = config.getConfigurationSection("packs");

        if (packsSection == null) {
            PLogger.severe("No packs section found in packs.yml!");
            return;
        }

        packs.clear();
        packsList.clear();

        for (String packId : packsSection.getKeys(false)) {
            ConfigurationSection packSection = packsSection.getConfigurationSection(packId);
            if (packSection == null) continue;

            try {
                Pack pack = loadPack(packId, packSection);
                if (pack != null) {
                    packs.put(packId.toLowerCase(), pack);
                    packsList.add(pack);
                }
            } catch (Exception e) {
                PLogger.severe("Error loading pack " + packId + ": " + e.getMessage());
            }
        }

        PLogger.info("Loaded " + packs.size() + " packs");
    }

    private static Pack loadPack(String packId, ConfigurationSection section) {
        String displayName = section.getString("displayName", packId);

        // Load item settings
        ConfigurationSection itemSection = section.getConfigurationSection("item");
        if (itemSection == null) {
            PLogger.warning("No item section found for pack: " + packId);
            return null;
        }

        // Parse item properties
        Material material;
        try {
            material = Material.valueOf(itemSection.getString("material", "CHEST").toUpperCase());
        } catch (IllegalArgumentException e) {
            PLogger.warning("Invalid material for pack: " + packId);
            material = Material.CHEST;
        }

        String itemDisplayName = itemSection.getString("displayName", displayName);
        List<String> lore = itemSection.getStringList("lore");
        boolean glow = itemSection.getBoolean("glow", false);
        Integer customModelData = itemSection.contains("customModelData") ? 
                itemSection.getInt("customModelData") : null;

        // Load card chances
        ConfigurationSection cardsSection = section.getConfigurationSection("cards");
        if (cardsSection == null) {
            PLogger.warning("No cards section found for pack: " + packId);
            return null;
        }

        Map<String, Double> cardChances = new HashMap<>();
        for (String cardId : cardsSection.getKeys(false)) {
            if (CardLoader.getCard(cardId) == null) {
                PLogger.warning("Invalid card ID '" + cardId + "' in pack: " + packId);
                continue;
            }

            double chance = cardsSection.getDouble(cardId);
            cardChances.put(cardId, chance);
        }

        if (cardChances.isEmpty()) {
            PLogger.warning("No valid cards found for pack: " + packId);
            return null;
        }

        return new Pack(packId, displayName, material, itemDisplayName, lore, glow, 
                customModelData, cardChances);
    }

    public static Pack getPack(String id) {
        return packs.get(id.toLowerCase());
    }
} 