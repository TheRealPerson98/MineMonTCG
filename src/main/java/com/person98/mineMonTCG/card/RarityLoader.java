package com.person98.mineMonTCG.card;

import com.person98.mineMonTCG.MineMonTCG;
import com.person98.mineMonTCG.card.imlp.Rarity;
import com.person98.mineMonTCG.util.PLogger;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RarityLoader {
    @Getter
    private static final Map<String, Rarity> rarities = new HashMap<>();
    @Getter
    private static final List<Rarity> raritiesList = new ArrayList<>();

    public static void loadRarities() {
        File rarityFile = new File(MineMonTCG.getInstance().getDataFolder(), "rarity.yml");
        if (!rarityFile.exists()) {
            MineMonTCG.getInstance().saveResource("rarity.yml", false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(rarityFile);
        ConfigurationSection raritiesSection = config.getConfigurationSection("rarities");

        if (raritiesSection == null) {
            PLogger.severe("No rarities section found in rarity.yml!");
            return;
        }

        rarities.clear();
        raritiesList.clear();

        for (String rarityId : raritiesSection.getKeys(false)) {
            ConfigurationSection raritySection = raritiesSection.getConfigurationSection(rarityId);
            if (raritySection == null) continue;

            String displayName = raritySection.getString("displayName", rarityId);
            int weight = raritySection.getInt("weight", 100);

            Rarity rarity = new Rarity(rarityId, displayName, weight);
            rarities.put(rarityId, rarity);
            raritiesList.add(rarity);
        }

        PLogger.info("Loaded " + rarities.size() + " rarities");
    }

    public static Rarity getRarity(String id) {
        return rarities.get(id.toLowerCase());
    }
} 