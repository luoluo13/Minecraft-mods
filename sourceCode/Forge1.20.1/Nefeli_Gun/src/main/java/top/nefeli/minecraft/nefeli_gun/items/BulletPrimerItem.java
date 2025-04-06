package top.nefeli.minecraft.nefeli_gun.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.nefeli.minecraft.nefeli_gun.init.ModItems;

/// 底火
public class BulletPrimerItem extends Item {
    public BulletPrimerItem() {
        super(new Properties());
    }

    public static String getPrimer(ItemStack stack) {
        return stack.getOrCreateTag().getString("Primer");
    }

    // 创建带有预设属性的底火
    public static ItemStack createStack(String primer) {
        ItemStack stack = new ItemStack(ModItems.BULLET_PRIMER.get());
        stack.getOrCreateTag().putString("Primer", primer);
        return stack;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        stack.getOrCreateTag().putString("Primer", "基础");
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.nefeli_gun.bulletprimer", getPrimer(stack));
    }
}

