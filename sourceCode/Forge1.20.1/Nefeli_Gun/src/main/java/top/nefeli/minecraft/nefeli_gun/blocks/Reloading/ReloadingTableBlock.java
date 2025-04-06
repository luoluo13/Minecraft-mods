package top.nefeli.minecraft.nefeli_gun.blocks.Reloading;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class ReloadingTableBlock extends BaseEntityBlock {
    public ReloadingTableBlock() {
        super(Properties.of().mapColor(MapColor.METAL).strength(3.0f));
    }

    //    @Override
//    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
//        if (!level.isClientSide) {
//            MenuProvider menuProvider = state.getMenuProvider(level, pos);
//            if (menuProvider != null) {
//                player.openMenu(menuProvider);
//            }
//        }
//        return InteractionResult.SUCCESS;
//    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            // 使用 NetworkHooks 安全打开菜单（自动写入 BlockPos 到数据包）
            NetworkHooks.openScreen(serverPlayer, (MenuProvider) state.getMenuProvider(level, pos), buf -> {
                buf.writeBlockPos(pos); // 将 BlockPos 写入数据包
            });
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ReloadingTableBlockEntity(pos, state);
    }
}