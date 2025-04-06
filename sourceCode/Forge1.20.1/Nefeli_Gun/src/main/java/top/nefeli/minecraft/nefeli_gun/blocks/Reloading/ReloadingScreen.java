package top.nefeli.minecraft.nefeli_gun.blocks.Reloading;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ReloadingScreen extends AbstractContainerScreen<ReloadingMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            new ResourceLocation("nefeli_gun", "textures/gui/reloading_table.png");

    public ReloadingScreen(ReloadingMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageWidth = 176; // GUI 宽度
        this.imageHeight = 166; // GUI 高度
    }

    @Override
    protected void renderBg(GuiGraphics gui, float partialTick, int mouseX, int mouseY) {
        gui.fillGradient(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, 0xFFAAAAAA, 0xFF666666);
        // 绘制背景纹理
//        gui.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        renderBackground(gui);
        super.render(gui, mouseX, mouseY, partialTick);
        renderTooltip(gui, mouseX, mouseY); // 绘制物品提示
    }
}