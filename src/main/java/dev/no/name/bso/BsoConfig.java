package dev.no.name.bso;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class BsoConfig {
    public static final int MIN_DISTANCE = 8;
    public static final int MAX_DISTANCE = 160;
    public static final double MAX_MARGIN = 4;

    private static final Logger LOGGER = LoggerFactory.getLogger("BloodSpawnOptimize");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("bloodspawnoptimize.json");

    private static BsoConfig instance = new BsoConfig();

    public boolean enabled = true;
    public int distance = 48;
    public boolean frustum = true;
    public double margin = 1;
    public boolean textDisplays = false;
    public boolean hideShadows = true;

    public static BsoConfig get() {
        return instance;
    }

    public static void init() {
        instance = load();
    }

    private static BsoConfig load() {
        if (!Files.exists(FILE)) {
            return new BsoConfig();
        }
        try {
            BsoConfig config = GSON.fromJson(Files.readString(FILE), BsoConfig.class);
            return config == null ? new BsoConfig() : config.clamped();
        } catch (IOException | JsonParseException e) {
            LOGGER.error("Не удалось прочитать {}, беру настройки по умолчанию", FILE, e);
            return new BsoConfig();
        }
    }

    public void save() {
        try {
            Files.writeString(FILE, GSON.toJson(clamped()));
        } catch (IOException e) {
            LOGGER.error("Не удалось сохранить {}", FILE, e);
        }
    }

    private BsoConfig clamped() {
        distance = Math.clamp(distance, MIN_DISTANCE, MAX_DISTANCE);
        margin = Math.clamp(margin, 0, MAX_MARGIN);
        return this;
    }
}
