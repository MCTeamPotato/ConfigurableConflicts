package com.teampotato.configurable_conflicts;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@Mod(ConfigurableConflicts.MOD_ID)
public class ConfigurableConflicts {
    public static final String MOD_ID = "configurable_conflicts";
    public static final ForgeConfigSpec config;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> enchantmentsToBeAlwaysCompatible, enchantmentsToBeAlwaysIncompatible;

    public ConfigurableConflicts() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, config);
    }

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("ConfigurableConflicts").comment("Format: minecraft:infinity;minecraft:mending. That will make this two enchantments compatible");
        enchantmentsToBeAlwaysIncompatible = builder.defineList("enchantmentsToBeAlwaysIncompatible", new ObjectArrayList<>(), o -> o instanceof String);
        enchantmentsToBeAlwaysCompatible = builder.defineList("enchantmentsToBeAlwaysCompatible", new ObjectArrayList<>(), o -> o instanceof String);
        builder.pop();
        config = builder.build();
    }

    public static boolean isCompat(Enchantment instance, Enchantment arg, boolean originalResult) {
        boolean checkCompat = originalResult;
        if (!checkCompat) {
            for (String enchantments : ConfigurableConflicts.enchantmentsToBeAlwaysCompatible.get()) {
                Enchantment enchantment1 = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(enchantments.split(";")[0]));
                Enchantment enchantment2 = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(enchantments.split(";")[1]));
                if (enchantment1 == null || enchantment2 == null) continue;
                if ((enchantment1.equals(instance) && arg.equals(enchantment2)) || (enchantment1.equals(arg) && instance.equals(enchantment2))) {
                    checkCompat = true;
                    break;
                }
            }
        } else {
            for (String enchantments : ConfigurableConflicts.enchantmentsToBeAlwaysIncompatible.get()) {
                Enchantment enchantment1 = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(enchantments.split(";")[0]));
                Enchantment enchantment2 = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(enchantments.split(";")[1]));
                if (enchantment1 == null || enchantment2 == null) continue;
                if ((enchantment1.equals(instance) && arg.equals(enchantment2)) || (enchantment1.equals(arg)) && instance.equals(enchantment2)) {
                    checkCompat = false;
                    break;
                }
            }
        }
        return checkCompat;
    }
}
