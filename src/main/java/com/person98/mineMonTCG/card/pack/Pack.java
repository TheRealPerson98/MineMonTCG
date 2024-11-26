package com.person98.mineMonTCG.card.pack;

import com.person98.mineMonTCG.MineMonTCG;
import com.person98.mineMonTCG.card.CardLoader;
import com.person98.mineMonTCG.card.imlp.Card;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;

import java.util.*;

@Getter
public class Pack {
    private final String id;
    private final String displayName;
    private final Material material;
    private final String itemDisplayName;
    private final List<String> lore;
    private final boolean glow;
    private final Integer customModelData;
    private final Map<String, Double> cardChances;

    public Pack(String id, String displayName, Material material, String itemDisplayName,
                List<String> lore, boolean glow, Integer customModelData, Map<String, Double> cardChances) {
        this.id = id;
        this.displayName = displayName;
        this.material = material;
        this.itemDisplayName = itemDisplayName;
        this.lore = lore;
        this.glow = glow;
        this.customModelData = customModelData;
        this.cardChances = cardChances;
    }

    public ItemStack createItemStack() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.setDisplayName(itemDisplayName);
        meta.setLore(lore);

        // Store pack ID in persistent data
        NamespacedKey packKey = new NamespacedKey(MineMonTCG.getInstance(), "pack_id");
        meta.getPersistentDataContainer().set(packKey, PersistentDataType.STRING, id);

        // Set custom model data
        if (customModelData != null) {
            NamespacedKey modelKey = new NamespacedKey(MineMonTCG.getInstance(), "CustomModelData");
            meta.getPersistentDataContainer().set(modelKey, PersistentDataType.INTEGER, customModelData);
        }

        // Apply glow effect
        if (glow) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }

        item.setItemMeta(meta);
        return item;
    }

    public List<Card> generateCards(int count) {
        List<Card> cards = new ArrayList<>();
        Random random = new Random();

        // Calculate total weight
        double totalWeight = cardChances.values().stream().mapToDouble(Double::doubleValue).sum();

        for (int i = 0; i < count; i++) {
            double roll = random.nextDouble() * totalWeight;
            double currentWeight = 0;
            String selectedCardId = null;

            // Select card based on weights
            for (Map.Entry<String, Double> entry : cardChances.entrySet()) {
                currentWeight += entry.getValue();
                if (roll <= currentWeight) {
                    selectedCardId = entry.getKey();
                    break;
                }
            }

            if (selectedCardId == null) {
                selectedCardId = cardChances.keySet().iterator().next(); // Fallback to first card
            }

            Card selectedCard = CardLoader.getCard(selectedCardId);
            if (selectedCard != null) {
                // Handle shiny chance (5% chance if card can be shiny)
                if (selectedCard.isCanBeShiny() && random.nextDouble() < 0.05) {
                    selectedCard.setShiny(true);
                }
                cards.add(selectedCard);
            }
        }

        return cards;
    }

    public static Pack fromItemStack(ItemStack item) {
        if (item == null || item.getItemMeta() == null) return null;

        NamespacedKey packKey = new NamespacedKey(MineMonTCG.getInstance(), "pack_id");
        String packId = item.getItemMeta().getPersistentDataContainer().get(packKey, PersistentDataType.STRING);

        if (packId == null) return null;
        return PackLoader.getPack(packId);
    }
} 