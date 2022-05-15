package com.kenza.cloud.mixin;

import com.kenza.cloud.attributes.LivingEntityAttributes;
import net.minecraft.block.Blocks;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityAttributes {


    private boolean levitationEnabled;




    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }


    @Inject(method = "Lnet/minecraft/entity/LivingEntity;applyMovementInput(Lnet/minecraft/util/math/Vec3d;F)Lnet/minecraft/util/math/Vec3d;", at = @At("HEAD"), cancellable = true)
    public void inject_levitation(Vec3d g, float slipperiness, CallbackInfoReturnable<Vec3d> cir) {

        if (!levitationEnabled) {
            return;
        }

        if(this.getVelocity().y < 0){
            this.setVelocity(this.getVelocity().x , 0, this.getVelocity().z);
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
