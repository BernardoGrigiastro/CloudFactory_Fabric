package com.kenza.cloud.mixin;

import com.kenza.cloud.attributes.EntityAttributes;
import com.kenza.cloud.attributes.LivingEntityAttributes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.kenza.cloud.utils.Java.convertInstanceOfObject;


@Mixin(Entity.class)
public abstract class EntityMixin implements EntityAttributes {


    private Vec3d cloudMovementMultiplier = Vec3d.ZERO;
    private boolean test1 = false;

    @Shadow
    private Vec3d velocity = Vec3d.ZERO;

    @Shadow
    private boolean onGround;


    @ModifyVariable(method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public Vec3d modify_movement_forCloud(Vec3d movement) {
        if (this.cloudMovementMultiplier.lengthSquared() > 1.0E-7) {
            velocity = velocity.multiply(cloudMovementMultiplier);//0.7f, 1.f, 0.7f);
        }
        return movement;
    }


    @Inject(method = "Lnet/minecraft/entity/Entity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getLandingPos()Lnet/minecraft/util/math/BlockPos;"))
    public void inject_in_move(MovementType movementType, Vec3d movement, CallbackInfo ci) {
//        convertInstanceOfObject(this, LivingEntityAttributes.class).ifPresent(x -> {
//            if (x.getLevitationEnabled()) {
//                this.onGround = true;
//            }
//        });


        if (this.cloudMovementMultiplier.lengthSquared() > 1.0E-7) {
            this.onGround = true;
            this.cloudMovementMultiplier = Vec3d.ZERO;

        }


//        }
    }

    @Inject(method = "Lnet/minecraft/entity/Entity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "RETURN"))
    public void inject_in_end_move(MovementType movementType, Vec3d movement, CallbackInfo ci) {
//        if (this.cloudMovementMultiplier.lengthSquared() > 1.0E-7) {
//            velocity = velocity.multiply(cloudMovementMultiplier);//0.7f, 1.f, 0.7f);
//            this.cloudMovementMultiplier = Vec3d.ZERO;
//        }


    }


    @NotNull
    @Override
    public Vec3d getCloudMovementMultiplier() {
        return cloudMovementMultiplier;
    }

    @Override
    public void setCloudMovementMultiplier(@NotNull Vec3d cloudMovementMultiplier) {
        this.cloudMovementMultiplier = cloudMovementMultiplier;
    }

    @Override
    public boolean getTest1() {
        return false;
    }

    @Override
    public void setTest1(boolean test1) {
        this.test1 = test1;
    }
}
