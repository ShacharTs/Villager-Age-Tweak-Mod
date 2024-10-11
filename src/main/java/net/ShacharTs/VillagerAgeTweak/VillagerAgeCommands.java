package net.ShacharTs.VillagerAgeTweak;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import org.jetbrains.annotations.NotNull;

import static net.ShacharTs.VillagerAgeTweak.Config.defaultCoolDown;


@Mod(VillagerAgeCommands.MOD_ID)
public class VillagerAgeCommands {
    public static final String MOD_ID = "villageragetweakshacharts";

    public VillagerAgeCommands(ModContainer modContainer) {
        // Register event bus
        NeoForge.EVENT_BUS.register(this);
        // Register the configuration for the COMMON type (ensure it registers early in the mod lifecycle)
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    // Chaning function for baby villager
    @SubscribeEvent
    public void onEntityJoinLevelEvent(@NotNull EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Villager villager) {
            if (villager.isBaby()) {
                villager.setAge(-defaultCoolDown * 20);  // Apply the config cooldown time
            }
        }
    }

    // new commands for player
    @SubscribeEvent
    public void onRegisterCommandsEvent(@NotNull RegisterCommandsEvent event) {
        // command for changing villager grow cooldown
        event.getDispatcher().register(Commands.literal("changevillagercooldown").then(Commands.argument("seconds", IntegerArgumentType.integer(1, 1200)).executes(context -> {
            int newCooldown = IntegerArgumentType.getInteger(context, "seconds");
            defaultCoolDown = newCooldown;
            Config.COOLDOWN_TIME.set(newCooldown); // Save new value if needed
            context.getSource().sendSystemMessage(Component.literal("Cooldown changed to " + newCooldown + " second").withStyle(ChatFormatting.GREEN));
            return Command.SINGLE_SUCCESS;
        })));
        // command for check current villager cooldown
        event.getDispatcher().register(Commands.literal("checkvillagercooldown").executes(context -> {
            context.getSource().sendSystemMessage(Component.literal("Current grow time is " + defaultCoolDown + " second").withStyle(ChatFormatting.DARK_GREEN));
            return Command.SINGLE_SUCCESS;
        }));
        // command for reseting villager cooldown to default minecraft setting (20 mins)
        event.getDispatcher().register((Commands.literal("resetvillagercooldown")).executes(context -> {
            defaultCoolDown = 1200; // 1200 = 20 mins in seconds
            context.getSource().sendSystemMessage(Component.literal("Villager cooldown has been reset to default (20 mins)").withStyle(ChatFormatting.DARK_RED));
            return Command.SINGLE_SUCCESS;
        }));
        event.getDispatcher().register(Commands.literal("villageragetweaklist").executes(context -> {
            context.getSource().sendSystemMessage(Component.literal("List of the commands").withStyle(ChatFormatting.GOLD));
            context.getSource().sendSystemMessage(Component.literal("/changevillagercooldown -> Change villager grow time").withStyle(ChatFormatting.YELLOW));
            context.getSource().sendSystemMessage(Component.literal("/checkvillagercooldown -> Check villager grow time ").withStyle(ChatFormatting.YELLOW));
            context.getSource().sendSystemMessage(Component.literal("/resetvillagercooldown -> Reset villager grow time ").withStyle(ChatFormatting.YELLOW));
            return Command.SINGLE_SUCCESS;
        }));

    }
}