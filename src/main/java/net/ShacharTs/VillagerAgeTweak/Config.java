package net.ShacharTs.VillagerAgeTweak;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;


@EventBusSubscriber(modid = VillagerAgeCommands.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    // default cooldown 1200 = 20 mins
    public static int defaultCoolDown = 1200;

    // Variable to hold the loaded cooldown time
    public static int playerNewCoolDown;

     //Define the cooldown time in the config
    public static final ModConfigSpec.IntValue COOLDOWN_TIME = BUILDER
            .comment("Cooldown time for baby villagers to grow up (in seconds)")
            .defineInRange("cooldownTime", defaultCoolDown, 1, 1200);  // Default is 100 seconds

    // Build the config
    public static final ModConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        // Load the config
        playerNewCoolDown = COOLDOWN_TIME.get();
    }
}