package top.nefeli.minecraft.nefeli_gun.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.nefeli.minecraft.nefeli_gun.init.ModItems;

///  蛋壳
public class BulletCasingItem extends Item {
    public BulletCasingItem() {
        super(new Properties().stacksTo(64));
    }

    public static String getMaterial(ItemStack stack) {
        return stack.getOrCreateTag().getString("Material");
    }

    public static Float getCaliber(ItemStack stack) {
        return stack.getOrCreateTag().getFloat("Caliber");
    }

    // 创建带有预设属性的弹壳
    public static ItemStack createStack(float caliber, String material) {
        ItemStack stack = new ItemStack(ModItems.BULLET_CASING.get());
        stack.getOrCreateTag().putFloat("Caliber", caliber);
        stack.getOrCreateTag().putString("Material", material);
        return stack;
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        stack.getOrCreateTag().putString("Material", "铜制");
        stack.getOrCreateTag().putFloat("Caliber", 7.62F);
    }

    @Override
    public Component getName(ItemStack stack) {
        String caliber = getCaliber(stack) + "mm";
        String material = getMaterial(stack);
        return Component.translatable("item.nefeli_gun.bulletcasing", caliber.toUpperCase(), material);
    }
}

