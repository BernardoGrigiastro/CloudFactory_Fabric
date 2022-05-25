package com.kenza.cloud.mixin;

import com.kenza.cloud.provider.LivingEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.ref.WeakReference;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityProvider {


    private boolean levitationEnabled;


    int count = 0;


    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }


//    @Inject(method = "Lnet/minecraft/entity/LivingEntity;tickMovement()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;shouldSwimInFluids()Z"), cancellable = true)
//    public void change_onGround_for_jumping(CallbackInfo ci) {
//
//        double k = this.isInLava() ? this.getFluidHeight(FluidTags.LAVA) : this.getFluidHeight(FluidTags.WATER);
//        int bl = this.isTouchingWater() && k > 0.0 ? 1 : 0;
//        double l = this.getSwimHeight();
//
//
//        if (bl != 0 && (!this.onGround || k > l)) {
//
//        } else if (this.isInLava() && (!this.onGround || k > l)) {
//
//        } else {
//
//            if (!this.onGround && count <= 1) {
//                count += 1;
//                this.onGround = true;
//            }
//        }
//    }


//    @Override
//    public void onLanding() {
//        count = 0;
//        super.onLanding();
//    }

    @Inject(method = "Lnet/minecraft/entity/LivingEntity;applyMovementInput(Lnet/minecraft/util/math/Vec3d;F)Lnet/minecraft/util/math/Vec3d;", at = @At("HEAD"), cancellable = true)
    public void inject_levitation(Vec3d g, float slipperiness, CallbackInfoReturnable<Vec3d> cir) {

        if (!levitationEnabled) {
            return;
        }

        if (this.getVelocity().y < 0) {
            this.setVelocity(this.getVelocity().x, 0, this.getVelocity().z);
        }
        levitationEnabled = false;
    }


    @Override
    public boolean getLevitationEnabled() {
        return levitationEnabled;
    }

    @Override
    public void setLevitationEnabled(boolean levitationEnabled) {
        this.levitationEnabled = levitationEnabled;
    }
}
