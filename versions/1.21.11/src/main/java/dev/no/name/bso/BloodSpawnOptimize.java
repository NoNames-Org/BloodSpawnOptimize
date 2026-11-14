package dev.no.name.bso;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class BloodSpawnOptimize implements ClientModInitializer {
    public static final String MOD_ID = "bloodspawnoptimize";

    @Override
    public void onInitializeClient() {
        BsoConfig.init();
        KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"));
        KeyMapping openKey = KeyBindingHelper.registerKeyBinding(
                new KeyMapping("key." + MOD_ID + ".open_menu", InputConstants.KEY_O, category));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openKey.consumeClick()) {
                if (client.screen == null) {
                    client.setScreen(new BsoScreen(BsoConfig.get()));
                }
            }
        });
    }
}
