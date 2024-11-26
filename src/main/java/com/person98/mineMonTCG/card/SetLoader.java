package com.person98.mineMonTCG.card;

import com.person98.mineMonTCG.MineMonTCG;
import com.person98.mineMonTCG.card.imlp.Set;
import com.person98.mineMonTCG.util.PLogger;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetLoader {
    @Getter
    private static final Map<String, Set> sets = new HashMap<>();
    @Getter
    private static final List<Set> setsList = new ArrayList<>();

    public static void loadSets() {
        File setFile = new File(MineMonTCG.getInstance().getDataFolder(), "sets.yml");
        if (!setFile.exists()) {
            MineMonTCG.getInstance().saveResource("sets.yml", false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(setFile);
        ConfigurationSection setsSection = config.getConfigurationSection("sets");

        if (setsSection == null) {
            PLogger.severe("No sets section found in sets.yml!");
            return;
        }

        sets.clear();
        setsList.clear();

        for (String setId : setsSection.getKeys(false)) {
            ConfigurationSection setSection = setsSection.getConfigurationSection(setId);
            if (setSection == null) continue;

            String displayName = setSection.getString("displayName", setId);
            String releaseDate = setSection.getString("releaseDate");
            String endDate = setSection.getString("endDate");

            if (releaseDate == null || endDate == null) {
                PLogger.warning("Missing date information for set: " + setId);
                continue;
            }

            try {
                Set set = new Set(setId, displayName, releaseDate, endDate);
                sets.put(setId, set);
                setsList.add(set);
            } catch (ParseException e) {
                PLogger.severe("Invalid date format for set " + setId + ": " + e.getMessage());
            }
        }

        PLogger.info("Loaded " + sets.size() + " sets");
    }

    public static Set getSet(String id) {
        return sets.get(id.toLowerCase());
    }

    public static List<Set> getActiveSets() {
        List<Set> activeSets = new ArrayList<>();
        for (Set set : setsList) {
            if (set.isActive()) {
                activeSets.add(set);
            }
        }
        return activeSets;
    }
} 