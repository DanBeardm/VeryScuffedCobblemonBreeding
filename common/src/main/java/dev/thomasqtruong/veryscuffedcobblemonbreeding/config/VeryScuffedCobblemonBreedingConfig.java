package dev.thomasqtruong.veryscuffedcobblemonbreeding.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class VeryScuffedCobblemonBreedingConfig {
    Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
    public static int COMMAND_POKEBREED_PERMISSION_LEVEL = 0;      // Default for MC is 2.
    public static int VIP_COMMAND_POKEBREED_PERMISSION_LEVEL = 0;  // VIP permission level.
    public static int COOLDOWN_IN_MINUTES = 22;      // Default: 5 minutes cooldown.
    public static int VIP_COOLDOWN_IN_MINUTES = 5;  // VIP breeding cooldown, default: 3.
    public static int MASTER_COMMAND_POKEBREED_PERMISSION_LEVEL = 0;     // or whatever level
    public static int LEGEND_COMMAND_POKEBREED_PERMISSION_LEVEL = 0;     // if you want Legend above that
    public static int MASTER_COOLDOWN_IN_MINUTES = 17;                  // example
    public static int LEGEND_COOLDOWN_IN_MINUTES = 14;                   // example
    public static int DITTO_BREEDING = 1;  // Whether breeding with a ditto is allowed or not, default: 1 (true).
    public static int HIDDEN_ABILITY = 1;  // Whether passing down hidden abilities is enabled: 1 (true).

    public VeryScuffedCobblemonBreedingConfig() {
        init();
    }

    // Extracts data from the config file.
    public void init() {
        File configFolder = new File(System.getProperty("user.dir") + "/config/veryscuffedcobblemonbreeding");
        File configFile = new File(configFolder, "config.json");
        System.out.println("VeryScuffedCobblemonBreeding config -> " + configFolder.getAbsolutePath());
        if (!configFolder.exists()) {
            configFolder.mkdirs();
            createConfig(configFolder);
        } else if (!configFile.exists()) {
            createConfig(configFolder);
        }

        try {
            Type type = new TypeToken<HashMap<String, Integer>>(){}.getType();
            JsonObject obj = GSON.fromJson(new FileReader(configFile), JsonObject.class);

            JsonObject permLevels = obj.get("permissionlevels").getAsJsonObject();
            HashMap<String, Integer> permissionMap = GSON.fromJson(permLevels, type);
            COMMAND_POKEBREED_PERMISSION_LEVEL = permissionMap.getOrDefault("command.pokebreed", 0);
            VIP_COMMAND_POKEBREED_PERMISSION_LEVEL = permissionMap.getOrDefault("command.vippokebreed", 0);
            MASTER_COMMAND_POKEBREED_PERMISSION_LEVEL = permissionMap.getOrDefault("command.masterpokebreed", 0);
            LEGEND_COMMAND_POKEBREED_PERMISSION_LEVEL = permissionMap.getOrDefault("command.legendpokebreed", 0);

            JsonObject cooldowns = obj.get("cooldowns").getAsJsonObject();
            HashMap<String, Integer> cooldownsMap = GSON.fromJson(cooldowns, type);
            COOLDOWN_IN_MINUTES = cooldownsMap.getOrDefault("command.pokebreed.cooldown", 22);
            VIP_COOLDOWN_IN_MINUTES = cooldownsMap.getOrDefault("command.pokebreed.vipcooldown", 5);
            MASTER_COOLDOWN_IN_MINUTES = cooldownsMap.getOrDefault("command.pokebreed.mastercooldown", 17);
            LEGEND_COOLDOWN_IN_MINUTES = cooldownsMap.getOrDefault("command.pokebreed.legendcooldown", 14);

            JsonObject otherFeatures = obj.get("otherFeatures").getAsJsonObject();
            HashMap<String, Integer> otherFeaturesMap = GSON.fromJson(otherFeatures, type);
            DITTO_BREEDING = otherFeaturesMap.getOrDefault("ditto.breeding", 1);
            HIDDEN_ABILITY = otherFeaturesMap.getOrDefault("hidden.ability", 1);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createConfig(File configFolder) {
        File file = new File(configFolder, "config.json");
        try {
            file.createNewFile();
            JsonWriter writer = GSON.newJsonWriter(new FileWriter(file));
            writer.beginObject()
                    .name("permissionlevels")
                        .beginObject()
                            .name("command.pokebreed")
                            .value(COMMAND_POKEBREED_PERMISSION_LEVEL)
                            .name("command.vippokebreed")
                            .value(VIP_COMMAND_POKEBREED_PERMISSION_LEVEL)
                            .name("command.masterpokebreed")
                            .value(MASTER_COMMAND_POKEBREED_PERMISSION_LEVEL)
                            .name("command.legendpokebreed")
                            .value(LEGEND_COMMAND_POKEBREED_PERMISSION_LEVEL)
                        .endObject()
                    .name("cooldowns")
                        .beginObject()
                            .name("command.pokebreed.cooldown")
                            .value(COOLDOWN_IN_MINUTES)
                            .name("command.pokebreed.vipcooldown")
                            .value(VIP_COOLDOWN_IN_MINUTES)
                            .name("command.pokebreed.mastercooldown")
                            .value(MASTER_COOLDOWN_IN_MINUTES)
                            .name("command.pokebreed.legendcooldown")
                            .value(LEGEND_COOLDOWN_IN_MINUTES)
                        .endObject()
                    .name("otherFeatures")
                        .beginObject()
                            .name("ditto.breeding")
                            .value(DITTO_BREEDING)
                            .name("hidden.ability")
                            .value(HIDDEN_ABILITY)
                        .endObject()
                    .endObject()
                    .flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
