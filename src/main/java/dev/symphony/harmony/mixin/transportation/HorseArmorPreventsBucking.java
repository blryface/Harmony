package dev.symphony.harmony.mixin.transportation;

import dev.symphony.harmony.config.HarmonyConfig;
import it.unimi.dsi.fastutil.objects.Object2FloatArrayMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin (AbstractHorseEntity.class)
public class HorseArmorPreventsBucking {

    // FEATURE:
    // AUTHORS: KikuGie, Flatkat
    @Unique
    private static final Map<Item, Float> preventBuckingChance = new Object2FloatArrayMap<>();

    @Shadow @Final private Inventory inventory;

    static {
        // TODO: Make preventBuckingChance for every type of armor configurable
        // TODO: Make this data-driven or configurable via config screen with a custom map config screen
        preventBuckingChance.put(Items.DIAMOND_HORSE_ARMOR, 0.9F);
        preventBuckingChance.put(Items.IRON_HORSE_ARMOR, 0.75F);
        preventBuckingChance.put(Items.GOLDEN_HORSE_ARMOR, 0.6F);
        preventBuckingChance.put(Items.LEATHER_HORSE_ARMOR, 0.45F);
    }

    @Inject(method = "updateAnger", at = @At("HEAD"), cancellable = true)
    private void rejectAngryWhenDrip(CallbackInfo ci) {
        if(HarmonyConfig.horseArmorPreventsBucking){
            if(FabricLoader.getInstance().isModLoaded("melody") && preventBuckingChance.get(Registries.ITEM.get(Identifier.of("melody:netherite_horse_armor"))) == null) {
                // Temporary solution until we move this to a better, configurable system
                preventBuckingChance.put(Registries.ITEM.get(Identifier.of("melody:netherite_horse_armor")), 1F);
            }
            ItemStack armor = this.inventory.getStack(0);
            float chance = preventBuckingChance.getOrDefault(armor.getItem(), 0F);
            System.out.println(preventBuckingChance);
            if (chance > 0 && chance < 1 || Math.random() <= chance) ci.cancel();
        }
    }
}
