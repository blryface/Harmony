package dev.symphony.harmony.mixin.transportation;

import dev.symphony.harmony.config.HarmonyConfig;
import it.unimi.dsi.fastutil.objects.Object2FloatArrayMap;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
    @Unique
    private static final Map<Item, Float> preventBuckingChance = new Object2FloatArrayMap<>();

    @Shadow @Final private Inventory inventory;

    // FEATURE:
    // AUTHORS: KikuGie, Flatkat
    static {
        // TODO: Make preventBuckingChance for every type of armor configurable
        // TODO: Add netherite horse armor if melody is loaded with a default value of 1F
        preventBuckingChance.put(Items.DIAMOND_HORSE_ARMOR, 0.9F);
        preventBuckingChance.put(Items.IRON_HORSE_ARMOR, 0.75F);
        preventBuckingChance.put(Items.GOLDEN_HORSE_ARMOR, 0.6F);
        preventBuckingChance.put(Items.LEATHER_HORSE_ARMOR, 0.45F);
    }

    @Inject(method = "updateAnger", at = @At("HEAD"), cancellable = true)
    private void rejectAngryWhenDrip(CallbackInfo ci) {
        if(HarmonyConfig.horseArmorPreventsBucking){
            ItemStack armor = this.inventory.getStack(0);
            float chance = preventBuckingChance.getOrDefault(armor.getItem(), 0F);
            if (chance > 0 && chance < 1 || Math.random() <= chance) ci.cancel();
        }
    }
}
