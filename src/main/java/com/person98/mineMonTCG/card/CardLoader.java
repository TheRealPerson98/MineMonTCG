package com.person98.mineMonTCG.card;

import com.person98.mineMonTCG.MineMonTCG;
import com.person98.mineMonTCG.card.imlp.Card;
import com.person98.mineMonTCG.card.imlp.Rarity;
import com.person98.mineMonTCG.card.imlp.Set;
import com.person98.mineMonTCG.card.imlp.Type;
import com.person98.mineMonTCG.util.PLogger;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class CardLoader {
    @Getter
    private static final Map<String, Card> cards = new HashMap<>();
    @Getter
    private static final List<Card> cardsList = new ArrayList<>();

    public static void loadCards() {
        File cardFile = new File(MineMonTCG.getInstance().getDataFolder(), "cards.yml");
        if (!cardFile.exists()) {
            MineMonTCG.getInstance().saveResource("cards.yml", false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(cardFile);
        ConfigurationSection cardsSection = config.getConfigurationSection("cards");

        if (cardsSection == null) {
            PLogger.severe("No cards section found in cards.yml!");
            return;
        }

        cards.clear();
        cardsList.clear();

        for (String cardId : cardsSection.getKeys(false)) {
            ConfigurationSection cardSection = cardsSection.getConfigurationSection(cardId);
            if (cardSection == null) continue;

            try {
                Card card = loadCard(cardId, cardSection);
                if (card != null) {
                    cards.put(cardId.toLowerCase(), card);
                    cardsList.add(card);
                }
            } catch (Exception e) {
                PLogger.severe("Error loading card " + cardId + ": " + e.getMessage());
            }
        }

        PLogger.info("Loaded " + cards.size() + " cards");
    }

    private static Card loadCard(String cardId, ConfigurationSection section) {
        String cardName = section.getString("cardName");
        List<String> typeIds = section.getStringList("types");
        String rarityId = section.getString("rarity");
        String setId = section.getString("set");
        
        if (cardName == null || typeIds.isEmpty() || rarityId == null || setId == null) {
            PLogger.warning("Missing required fields for card: " + cardId);
            return null;
        }

        // Load types
        List<Type> types = typeIds.stream()
                .map(TypeLoader::getType)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (types.isEmpty()) {
            PLogger.warning("No valid types found for card: " + cardId);
            return null;
        }

        // Load rarity and set
        Rarity rarity = RarityLoader.getRarity(rarityId);
        com.person98.mineMonTCG.card.imlp.Set set = SetLoader.getSet(setId);

        if (rarity == null || set == null) {
            PLogger.warning("Invalid rarity or set for card: " + cardId);
            return null;
        }

        // Optional fields
        List<String> description = section.getStringList("description");
        boolean canBeShiny = section.getBoolean("canBeShiny", true);

        // Custom item properties
        ConfigurationSection itemSection = section.getConfigurationSection("item");
        Material material = null;
        String displayName = null;
        List<String> lore = null;
        Integer customModelData = null;

        if (itemSection != null) {
            try {
                String materialName = itemSection.getString("material");
                if (materialName != null) {
                    material = Material.valueOf(materialName.toUpperCase());
                }
            } catch (IllegalArgumentException e) {
                PLogger.warning("Invalid material for card: " + cardId);
            }

            displayName = itemSection.getString("displayName");
            lore = itemSection.getStringList("lore");
            if (itemSection.contains("customModelData")) {
                customModelData = itemSection.getInt("customModelData");
            }
        }

        return new Card(cardId, cardName, types, rarity, set, description,
                canBeShiny, material, displayName, lore, customModelData);
    }

    public static Card getCard(String id) {
        return cards.get(id.toLowerCase());
    }

    public static List<Card> getCardsByType(Type type) {
        return cardsList.stream()
                .filter(card -> card.getTypes().contains(type))
                .collect(Collectors.toList());
    }

    public static List<Card> getCardsByRarity(Rarity rarity) {
        return cardsList.stream()
                .filter(card -> card.getRarity().equals(rarity))
                .collect(Collectors.toList());
    }

    public static List<Card> getCardsBySet(Set set) {
        return cardsList.stream()
                .filter(card -> card.getSet().equals(set))
                .collect(Collectors.toList());
    }
} 