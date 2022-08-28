package com.kenza.cloud.mixin;

import com.kenza.cloud.block.clouds.CloudAttribute;
import com.kenza.cloud.provider.EntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;


@Mixin(Entity.class)
public abstract class EntityMixin implements EntityProvider {


    @Shadow
    protected static int FALL_FLYING_FLAG_INDEX;

    private Vec3d cloudMovementMultiplier = Vec3d.ZERO;

    private static final TrackedData<Boolean> CLOUD_WALK_EFFECT =
            DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final TrackedData<Boolean> JUMP_EFFECT =
            DataTracker.registerData(Entity.class, TrackedDataHandlerRegistry.BOOLEAN);


    @Shadow
    private Vec3d velocity = Vec3d.ZERO;

    @Shadow
    private DataTracker dataTracker;

    @Shadow
    private boolean onGround;

    @Shadow
    public abstract Vec3d getEyePos();

    @Shadow
    public Vec3d movementMultiplier;


    @Shadow
    public float fallDistance;

    private int douleJumpCount = 0;


    @Shadow
    protected abstract void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition);


    @Shadow
    protected abstract boolean getFlag(int index);
    @Shadow
    protected abstract void setFlag(int index, boolean value) ;

    @Shadow
    public World world;

    @Shadow
    public abstract BlockPos getBlockPos();

    @Shadow
    public abstract boolean isInsideWall();

    @Shadow
    private EntityDimensions dimensions;

    @Shadow public abstract void setPosition(Vec3d pos);

    @Shadow public abstract double getX();

    @Shadow public abstract double getY();

    @Shadow public abstract double getZ();

    @Inject(method = "Lnet/minecraft/entity/Entity;<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;initDataTracker()V"))
    public void init_data(EntityType type, World world, CallbackInfo ci) {
        this.dataTracker.startTracking(CLOUD_WALK_EFFECT, false);
        this.dataTracker.startTracking(JUMP_EFFECT, false);
    }


    @Inject(method = "Lnet/minecraft/entity/Entity;readNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("HEAD"))
    public void readNbt(NbtCompound nbt, CallbackInfo ci) {

        nbt.putBoolean("cloudWalkEffect", this.dataTracker.get(CLOUD_WALK_EFFECT));
        nbt.putBoolean("jumpEffect", this.dataTracker.get(JUMP_EFFECT));
    }

    //
    @Inject(method = "Lnet/minecraft/entity/Entity;writeNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/nbt/NbtCompound;", at = @At("HEAD"))
    public void writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        this.dataTracker.set(CLOUD_WALK_EFFECT, nbt.getBoolean("cloudWalkEffect"));
        this.dataTracker.set(JUMP_EFFECT, nbt.getBoolean("jumpEffect"));
    }


    @Inject(method = "Lnet/minecraft/entity/Entity;fall(DZLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V", at = @At("HEAD"), cancellable = true)
    public void kenza_fix_fall_for_cloud(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition, CallbackInfo ci) {

        if(state.getBlock() instanceof CloudAttribute){
//            if (this.fallDistance > 0.0f) {
//                state.getBlock().onLandedUpon(this.world, state, landedPosition, this, this.fallDistance);
//                this.world.emitGameEvent(GameEvent.HIT_GROUND, this.pos, GameEvent.Emitter.of(this, this.getSteppingBlockState()));
//            }
            fallDistance = 0;
            ci.cancel();
        }

    }


    @ModifyVariable(method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public Vec3d modify_movement_forCloud(Vec3d movement) {
        if (this.cloudMovementMultiplier.lengthSquared() > 1.0E-7) {
            velocity = velocity.multiply(cloudMovementMultiplier);//0.7f, 1.f, 0.7f);
        }

//        boolean isInsideCloud = isInsideCloud();

        if (this.getFlag(FALL_FLYING_FLAG_INDEX) && isInsideCloud()) {

            BlockPos closeCloudBlockPos = getCloseCloudBlock().get();


            velocity = velocity.multiply(Vec3d.ZERO);//0.7f, 1.f, 0.7f);
            movementMultiplier = Vec3d.ZERO;
            this.setFlag(FALL_FLYING_FLAG_INDEX, false);
            movement =  Vec3d.ZERO;

            double new_x = (this.getX() + closeCloudBlockPos.getX() )/2  + 0.3;
            double new_Y = (this.getY() + closeCloudBlockPos.getY())/2  + 0.3;
            double new_z = (this.getZ() + closeCloudBlockPos.getZ() )/2  + 0.3;
            Vec3d vec3 = new Vec3d( new_x,   new_Y,  + new_z);
            this.setPosition(vec3);
//            System.out.println("kenza " + this.getFlag(FALL_FLYING_FLAG_INDEX) + " isInsideCloud " + isInsideCloud);

        }


        return movement;
    }


    public boolean isInsideCloud() {
//        if (this.noClip) {
//            return false;
//        }
//        float f = this.dimensions.width * 0.8f;
        float f = 1.5f;
        float h = this.dimensions.height * 0.8f;
        Box box = Box.of(this.getEyePos(), f, +1.5 * h, f);
        return BlockPos.stream(box).anyMatch(pos -> {
            BlockState blockState = this.world.getBlockState((BlockPos) pos);

            boolean x = blockState.getBlock() instanceof CloudAttribute;
            return x;
        });
    }

    public Optional<BlockPos> getCloseCloudBlock() {
//        if (this.noClip) {
//            return false;
//        }
        float f = 1.5f;
//        float f = this.dimensions.width * 0.8f;
        float h = this.dimensions.height * 0.8f;
        Box box = Box.of(this.getEyePos(), f, +1.5 * h, f);
        return BlockPos.stream(box).filter( pos -> {
            BlockState blockState = this.world.getBlockState((BlockPos) pos);
            boolean x = blockState.getBlock() instanceof CloudAttribute;
            return x;
        }).findFirst();


    }


    @Inject(method = "Lnet/minecraft/entity/Entity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;fall(DZLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V"))
    public void inject_in_move_after_fall(MovementType movementType, Vec3d movement, CallbackInfo ci) {

    }


    @Inject(method = "Lnet/minecraft/entity/Entity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getLandingPos()Lnet/minecraft/util/math/BlockPos;"))
    public void inject_in_move(MovementType movementType, Vec3d movement, CallbackInfo ci) {
//        convertInstanceOfObject(this, LivingEntityProvider.class).ifPresent(x -> {
//            if (x.getLevitationEnabled()) {
//                this.onGround = true;
//            }
//        });

//        convertInstanceOfObject(this, EntityProvider.class).ifPresent(x -> {
//
//            if (x.getDouleJumpEffectEnabled()) {
//                if(douleJumpCount <= 1){
//                    this.onGround = true;
//                    this.fallDistance = 0;
//                    douleJumpCount++;
//                }
//            }
//        });


        if (this.cloudMovementMultiplier.lengthSquared() > 1.0E-7) {
            this.onGround = true;
            this.cloudMovementMultiplier = Vec3d.ZERO;

        }

    }

    @Inject(method = "Lnet/minecraft/entity/Entity;onLanding()V", at = @At(value = "RETURN"))
    public void inject_OnLanding(CallbackInfo ci) {
        douleJumpCount = 0;
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
    public boolean getCloudWalkEffectEnabled() {
        return this.dataTracker.get(CLOUD_WALK_EFFECT);
    }

    @Override
    public void setCloudWalkEffectEnabled(boolean cloudWalkEnabled) {
        this.dataTracker.set(CLOUD_WALK_EFFECT, cloudWalkEnabled);
    }

    @Override
    public boolean getDouleJumpEffectEnabled() {
        return this.dataTracker.get(JUMP_EFFECT);
    }

    @Override
    public void setDouleJumpEffectEnabled(boolean douleJumpEffectEnabled) {
        this.dataTracker.set(JUMP_EFFECT, douleJumpEffectEnabled);
    }
}
