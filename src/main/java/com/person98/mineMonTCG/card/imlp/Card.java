package com.person98.mineMonTCG.card.imlp;

import com.person98.mineMonTCG.MineMonTCG;
import com.person98.mineMonTCG.settings.Settings;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Card {
    private final String id;
    private final String cardName;
    private final List<Type> types;
    private final Rarity rarity;
    private final Set set;
    private final List<String> description;
    private final boolean canBeShiny;
    
    // Custom item properties (null if using defaults)
    private final Material material;
    private final String displayName;
    private final List<String> lore;
    private final Integer customModelData;
    
    private boolean isShiny = false;

    public Card(String id, String cardName, List<Type> types, Rarity rarity, Set set, List<String> description,
               boolean canBeShiny, Material material, String displayName, List<String> lore, Integer customModelData) {
        this.id = id;
        this.cardName = cardName;
        this.types = types;
        this.rarity = rarity;
        this.set = set;
        this.description = description != null ? description : new ArrayList<>();
        this.canBeShiny = canBeShiny;
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
        this.customModelData = customModelData;
    }

    public void setShiny(boolean shiny) {
        if (!canBeShiny) return;
        this.isShiny = shiny;
    }

    public ItemStack createItemStack() {
        // Use custom material or default from settings
        Material itemMaterial = material != null ? material : Settings.getDefaultCardItem();
        ItemStack item = new ItemStack(itemMaterial);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        // Set display name
        String name = displayName != null ? displayName : Settings.getCardDisplayName();
        name = replacePlaceholders(name);
        meta.setDisplayName(name);

        // Set lore
        List<String> itemLore = new ArrayList<>();
        if (lore != null && !lore.isEmpty()) {
            for (String loreLine : lore) {
                if (loreLine.contains("%description%")) {
                    // Insert description lines where the placeholder is
                    itemLore.addAll(description.stream()
                            .map(line -> "<gray>" + line)
                            .collect(Collectors.toList()));
                } else {
                    itemLore.add(replacePlaceholders(loreLine));
                }
            }
        } else {
            itemLore.addAll(Settings.getCardLore().stream()
                    .map(this::replacePlaceholders)
                    .collect(Collectors.toList()));
        }
        meta.setLore(itemLore);

        // Store card ID in persistent data
        NamespacedKey cardKey = new NamespacedKey(MineMonTCG.getInstance(), "card_id");
        meta.getPersistentDataContainer().set(cardKey, PersistentDataType.STRING, id);

        // Set custom model data using NBT since setCustomModelData is not available
        if (customModelData != null || Settings.getCardCustomModelData() > 0) {
            int modelData = customModelData != null ? customModelData : Settings.getCardCustomModelData();
            NamespacedKey modelKey = new NamespacedKey(MineMonTCG.getInstance(), "CustomModelData");
            meta.getPersistentDataContainer().set(modelKey, PersistentDataType.INTEGER, modelData);
        }

        // Apply glow effect if shiny or configured
        if (isShiny || Settings.isCardGlow()) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        }

        item.setItemMeta(meta);
        return item;
    }

    private String replacePlaceholders(String text) {
        if (text == null) return "";
        
        return text
                .replace("%card_name%", cardName)
                .replace("%types%", types.stream()
                        .map(Type::getDisplayName)
                        .collect(Collectors.joining(", ")))
                .replace("%rarity%", rarity.getDisplayName())
                .replace("%set%", set.getDisplayName())
                .replace("%shiny%", isShiny ? "✦" : "");
    }
} 