package com.person98.mineMonTCG;

import com.person98.mineMonTCG.card.CardLoader;
import com.person98.mineMonTCG.card.RarityLoader;
import com.person98.mineMonTCG.card.SetLoader;
import com.person98.mineMonTCG.card.TypeLoader;
import com.person98.mineMonTCG.card.pack.PackLoader;
import com.person98.mineMonTCG.settings.Settings;
import com.person98.mineMonTCG.util.Lang;
import com.person98.mineMonTCG.util.PLogger;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineMonTCG extends JavaPlugin {

    @Getter
    private static MineMonTCG instance;

    @Override
    public void onEnable() {
        instance = this;

        PLogger.setup("MineMonTCG", PLogger.ConsoleColor.GREEN);
        PLogger.info("Starting MineMonTCG...");

        Lang.setup();
        Settings.load();
        RarityLoader.loadRarities();
        SetLoader.loadSets();
        TypeLoader.loadTypes();
        CardLoader.loadCards();
        PackLoader.loadPacks();
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
