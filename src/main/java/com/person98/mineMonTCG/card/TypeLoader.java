package com.person98.mineMonTCG.card;

import com.person98.mineMonTCG.MineMonTCG;
import com.person98.mineMonTCG.card.imlp.Type;
import com.person98.mineMonTCG.util.PLogger;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class TypeLoader {
    @Getter
    private static final Map<String, Type> types = new HashMap<>();
    @Getter
    private static final List<Type> typesList = new ArrayList<>();

    public static void loadTypes() {
        File typeFile = new File(MineMonTCG.getInstance().getDataFolder(), "types.yml");
        if (!typeFile.exists()) {
            MineMonTCG.getInstance().saveResource("types.yml", false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(typeFile);
        ConfigurationSection typesSection = config.getConfigurationSection("types");

        if (typesSection == null) {
            PLogger.severe("No types section found in types.yml!");
            return;
        }

        types.clear();
        typesList.clear();

        for (String typeId : typesSection.getKeys(false)) {
            ConfigurationSection typeSection = typesSection.getConfigurationSection(typeId);
            if (typeSection == null) continue;

            String displayName = typeSection.getString("displayName", typeId);
            List<String> weakAgainst = typeSection.getStringList("weakAgainst");
            List<String> strongAgainst = typeSection.getStringList("strongAgainst");

            Type type = new Type(typeId, displayName, weakAgainst, strongAgainst);
            types.put(typeId.toLowerCase(), type);
            typesList.add(type);
        }

        PLogger.info("Loaded " + types.size() + " types");
    }

    public static Type getType(String id) {
        return types.get(id.toLowerCase());
    }

    public static List<Type> getAllTypes() {
        return Collections.unmodifiableList(typesList);
    }

    public static double calculateDamageMultiplier(Type attackingType, Type defendingType) {
        return attackingType.getDamageMultiplierAgainst(defendingType);
    }
} 