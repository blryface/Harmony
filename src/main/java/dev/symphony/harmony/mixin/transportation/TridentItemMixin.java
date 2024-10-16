package dev.symphony.harmony.mixin.transportation;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TridentItem.class)
public class TridentItemMixin {

     // Extends the duration of the riptide effect based on the enchantment level, similar to rocket boosts.
    @ModifyArg(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;useRiptide(IFLnet/minecraft/item/ItemStack;)V"), index = 0)
    private int modifyRiptideTicks(int riptideTicks, @Local(argsOnly = true) ItemStack stack) {

        RegistryEntry<Enchantment> entry = stack.getEnchantments().getEnchantments().stream()
                .filter(act -> act.matchesId(Identifier.ofVanilla("riptide")))
                .findFirst()
                .orElse(null);
        int level = EnchantmentHelper.getLevel(entry, stack);

        return 15 + level*5;
    }
}
