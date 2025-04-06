package top.nefeli.minecraft.nefeli_gun.blocks.Reloading;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import top.nefeli.minecraft.nefeli_gun.init.ModItems;
import top.nefeli.minecraft.nefeli_gun.init.ModMenus;
import top.nefeli.minecraft.nefeli_gun.items.BulletCasingItem;
import top.nefeli.minecraft.nefeli_gun.items.BulletCoreItem;
import top.nefeli.minecraft.nefeli_gun.items.BulletPrimerItem;

import java.util.function.Predicate;

public class ReloadingMenu extends AbstractContainerMenu {
    private final ReloadingTableBlockEntity blockEntity;
    private final Level level;
    private final BlockPos pos;

    public ReloadingMenu(int containerId, Inventory playerInv, ReloadingTableBlockEntity blockEntity) {
        super(ModMenus.RELOADING_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        this.level = playerInv.player.level();
        this.pos = blockEntity.getBlockPos(); // 获取方块的位置
// 添加容器监听器
        addSlotListener(new ContainerListener() {
            @Override
            public void slotChanged(AbstractContainerMenu menu, int slotId, ItemStack stack) {
                // 直接调用容器的 slotsChanged 方法，传递正确的 Container 实例
                if (menu instanceof ReloadingMenu reloadingMenu) {
                    slotsChanged(reloadingMenu.blockEntity); // blockEntity 是 Container 的实现
                }
            }

            @Override
            public void dataChanged(AbstractContainerMenu menu, int slotId, int stack) {

            }
        });
        // 定义槽位 (弹壳、底火、火药、弹头、输出)
        addSlot(new SlotItemFilter((Container) blockEntity, 0, 26, 34, BulletCasingItem.class)); // 弹壳槽
        addSlot(new SlotItemFilter((Container) blockEntity, 1, 62, 34, BulletPrimerItem.class));      // 底火槽
        addSlot(new SlotItemFilter((Container) blockEntity, 2, 98, 34,
                stack -> stack.getItem() == Items.GUNPOWDER ||
                        stack.getItem() == Items.BLAZE_POWDER // 允许火药或烈焰粉
        ));            // 火药槽
        addSlot(new SlotItemFilter((Container) blockEntity, 3, 134, 34, BulletCoreItem.class)); // 弹头槽
        addSlot(new OutputSlot((Container) blockEntity, 4, 170, 34));                                 // 输出槽

        // 玩家背包
        addPlayerInventory(playerInv);
        addPlayerHotbar(playerInv);
//        blockEntity.addListener(container -> {
//            slotsChanged(blockEntity);
//        });
    }

    // 必须实现这个工厂方法（供 MenuType 调用）
    public static ReloadingMenu factory(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
        BlockEntity blockEntity = playerInv.player.level().getBlockEntity(extraData.readBlockPos());
        return new ReloadingMenu(containerId, playerInv, (ReloadingTableBlockEntity) blockEntity);
    }

    private void addPlayerInventory(Inventory playerInv) {
        // 玩家主背包（3行9列）
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int x = 8 + col * 18;
                int y = 84 + row * 18;
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, x, y));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInv) {
        // 玩家快捷栏（1行9列）
        for (int col = 0; col < 9; col++) {
            int x = 8 + col * 18;
            int y = 142;
            this.addSlot(new Slot(playerInv, col, x, y));
        }
    }

    // 检查是否可以合成子弹
    @Override
    public void slotsChanged(Container container) {
        System.out.println("[DEBUG] slotsChanged triggered!");
        ItemStack casing = container.getItem(0);
        ItemStack primer = container.getItem(1);
        ItemStack powder = container.getItem(2);
        ItemStack core = container.getItem(3);

        System.out.println("Debug - Casing: " + casing +
                ", Primer: " + primer +
                ", Powder: " + powder +
                ", Core: " + core);

        if (!casing.isEmpty() && !primer.isEmpty() && !powder.isEmpty() && !core.isEmpty()) {
            // 获取弹壳的口径（NBT）
            Float casingCaliber = casing.getOrCreateTag().getFloat("Caliber");
            Float coreCaliber = core.getOrCreateTag().getFloat("Caliber");
            if (casingCaliber.equals(coreCaliber)) {
                String primerPrimer = primer.getOrCreateTag().getString("Primer");
                String casingMaterial = powder.getOrCreateTag().getString("Material");
                String coreMaterial = core.getOrCreateTag().getString("Material");
                // 创建子弹并设置NBT
                ItemStack bullet = new ItemStack(ModItems.BULLET.get());
                bullet.getOrCreateTag().putString("CoreMaterial", coreMaterial);
                bullet.getOrCreateTag().putString("CasingMaterial", casingMaterial);
                bullet.getOrCreateTag().putString("Primer", primerPrimer);
                bullet.getOrCreateTag().putFloat("Caliber", casingCaliber);
                bullet.getOrCreateTag().putInt("Powder", powder.getCount());
                container.setItem(4, bullet);
            }
        } else {
            container.setItem(4, ItemStack.EMPTY);
        }
        super.slotsChanged(container);
    }

    @Override
    public boolean stillValid(Player player) {
        // 检查两点：
        // 1. 方块是否未被破坏
        // 2. 玩家距离方块是否在 8 格内
        return level.getBlockEntity(pos) == blockEntity &&
                player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        // 仅在普通左键点击输出槽时触发（排除拖拽）
        if (slotId == 4 && clickType == ClickType.PICKUP) {
            // 获取当前输出槽物品
            Slot outputSlot = this.slots.get(4);
            ItemStack outputItem = outputSlot.getItem();

            // 如果输出槽有物品且玩家操作有效
            if (!outputItem.isEmpty()) {
                // 先执行默认的取出逻辑
                super.clicked(slotId, button, clickType, player);

                // 服务器端扣除材料
                if (!player.level().isClientSide) {
                    consumeInputs();
                }
            }
        } else {
            super.clicked(slotId, button, clickType, player);
        }
    }

    private void consumeInputs() {
        if (!this.blockEntity.getLevel().isClientSide) {
            ItemStack casing = this.blockEntity.getItem(0);
            ItemStack primer = this.blockEntity.getItem(1);
            ItemStack powder = this.blockEntity.getItem(2);
            ItemStack core = this.blockEntity.getItem(3);

            // 消耗材料（弹壳、底火、弹头各1，火药全部）
            casing.shrink(1);
            primer.shrink(1);
            core.shrink(1);
            powder.shrink(powder.getCount());

            // 同步数据到客户端
            this.blockEntity.setChanged();
            this.broadcastChanges();
        }
    }

    // 玩家取出成品时消耗材料
    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotId);
        if (slot.hasItem() && slotId == 4) { // 输出槽
            ItemStack output = slot.getItem();
            stack = output.copy();
            if (!this.moveItemStackTo(output, 5, 41, true)) {
                return ItemStack.EMPTY;
            }
            // 消耗输入物品
            if (!player.level().isClientSide) {
                // 获取输入槽的引用
                ItemStack casing = this.blockEntity.getItem(0);
                ItemStack primer = this.blockEntity.getItem(1);
                ItemStack powder = this.blockEntity.getItem(2);
                ItemStack core = this.blockEntity.getItem(3);

                // 弹壳、底火、弹头各消耗1个
                casing.shrink(1);
                primer.shrink(1);
                core.shrink(1);

                // 火药消耗全部放入的数量
                int powderUsed = powder.getCount();
                powder.shrink(powderUsed);

                // 标记数据已修改（同步到客户端）
                this.blockEntity.setChanged();
            }
        }
        return stack;
    }

    // 自定义槽位过滤
    private static class SlotItemFilter extends Slot {
        private final Class<? extends Item> itemClass; // 接受一类物品（如 BulletCasingItem）
        private final Predicate<ItemStack> extraCondition; // 额外条件（如口径）

        // 构造方法：仅检查物品类型
        public SlotItemFilter(Container container, int slotId, int x, int y,
                              Class<? extends Item> itemClass) {
            this(container, slotId, x, y, itemClass, stack -> true); // 无条件

        }

        // 构造方法：检查类型 + 额外条件（如NBT中的口径）
        public SlotItemFilter(Container container, int slotId, int x, int y,
                              Class<? extends Item> itemClass, Predicate<ItemStack> extraCondition) {
            super(container, slotId, x, y);
            this.itemClass = itemClass;
            this.extraCondition = extraCondition;
        }

        public SlotItemFilter(Container container, int slotId, int x, int y, Predicate<ItemStack> filter) {
            super(container, slotId, x, y);
            this.itemClass = null;
            this.extraCondition = filter;
        }

        @Override
        public void set(ItemStack stack) {
            super.set(stack);
            if (container instanceof ReloadingTableBlockEntity) {
                ((ReloadingTableBlockEntity) container).setChanged(); // 强制触发更新
            }
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            if (itemClass != null) {
                return itemClass.isInstance(stack.getItem()) && extraCondition.test(stack);
            } else {
                return extraCondition.test(stack);
            }
        }
    }

    // 输出槽（不可放入物品）
    private static class OutputSlot extends Slot {
        public OutputSlot(Container container, int slotId, int x, int y) {
            super(container, slotId, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}