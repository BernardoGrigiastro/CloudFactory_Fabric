package com.kenza.cloud.block.clouds

import com.kenza.cloud.provider.EntityProvider
import com.kenza.cloud.provider.LivingEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.EntityShapeContext
import net.minecraft.block.PowderSnowBlock
import net.minecraft.block.ShapeContext
import net.minecraft.entity.Entity
import net.minecraft.entity.FallingBlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.GameRules
import net.minecraft.world.World

class CloudBlock(settings: Settings?) : PowderSnowBlock(settings) {


    override fun onEntityCollision(state: BlockState?, world: World, pos: BlockPos, entity: Entity) {
        if (entity !is LivingEntity || entity.getBlockStateAtPos().isOf(this)) {


            if (!canWalkOnPowderSnow(entity)) {
                (entity as? EntityProvider)?.cloudMovementMultiplier = Vec3d(0.6, 0.3, 0.6)
            } else {
                if (entity.isSneaking) {
                    (entity as? EntityProvider)?.cloudMovementMultiplier = Vec3d(0.9, 0.9, 0.9)
                } else {
                    (entity as? LivingEntityProvider)?.levitationEnabled = true
                    (entity as? EntityProvider)?.cloudMovementMultiplier = Vec3d(0.6, 1.0, 0.6)
                }
            }

            if (world.isClient) {
                val bl: Boolean
                val random = world.getRandom()
                bl = entity.lastRenderX != entity.x || entity.lastRenderZ != entity.z
                val bl2 = bl
                if (bl && random.nextBoolean()) {
                    world.addParticle(
                        ParticleTypes.SNOWFLAKE,
                        entity.x,
                        (pos.y + 1).toDouble(),
                        entity.z,
                        (MathHelper.nextBetween(random, -1.0f, 1.0f) * 0.083333336f).toDouble(),
                        0.05,
                        (MathHelper.nextBetween(random, -1.0f, 1.0f) * 0.083333336f).toDouble()
                    )
                }
            }
        }
        if (!world.isClient) {
            if (entity.isOnFire && (world.gameRules.getBoolean(GameRules.DO_MOB_GRIEFING) || entity is PlayerEntity) && entity.canModifyAt(
                    world,
                    pos
                )
            ) {
                world.breakBlock(pos, false)
            }
            entity.isOnFire = false
        }
    }


    override fun getCollisionShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext
    ): VoxelShape? {

        var entity: Entity? = (context as? EntityShapeContext)?.entity

        if (context is EntityShapeContext && entity != null) {
            val bl = entity is FallingBlockEntity
            if (bl || canWalkOnPowderSnow(entity) && context.isAbove(
                    VoxelShapes.fullCube(),
                    pos,
                    false
                ) && !context.isDescending()
            ) {
                return obtainCollisionShape(state, world, pos, context)
            }
        }
        return VoxelShapes.empty()
    }


    private fun obtainCollisionShape(
        state: BlockState?,
        world: BlockView?,
        pos: BlockPos?,
        context: ShapeContext?
    ): VoxelShape? {
        return if (collidable) state?.getOutlineShape(world, pos) else VoxelShapes.empty()
    }

}