package com.kenza.cloud.mixin;

import com.kenza.cloud.CloudFactoryMod;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.ref.WeakReference;

@Mixin(Camera.class)
public class CameraMixin {

    private WeakReference<BlockState> blockState = new WeakReference(null);

    @ModifyVariable(method = "getSubmersionType()Lnet/minecraft/client/render/CameraSubmersionType;", at = @At("STORE"), ordinal = 0)
    public BlockState store_blockState_getSubmersionType(BlockState value) {
        this.blockState = new WeakReference<>(value);
        return value;
    }


    @Inject(method = "getSubmersionType()Lnet/minecraft/client/render/CameraSubmersionType;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/BlockView;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", shift = At.Shift.AFTER), cancellable = true)
    public void kenza_getSubmersionType(CallbackInfoReturnable<CameraSubmersionType> cir) {
        BlockState state = blockState.get();

        if (state == null) {
            return;
        }


        if(state.isOf(CloudFactoryMod.Companion.getCLOUD_BLOCKS().get(0))){
            cir.setReturnValue(CameraSubmersionType.POWDER_SNOW);
            blockState.clear();
        }
    }
}
