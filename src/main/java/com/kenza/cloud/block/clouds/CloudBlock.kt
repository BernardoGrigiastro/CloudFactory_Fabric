package com.kenza.cloud.block.clouds

import com.kenza.cloud.provider.EntityProvider
import com.kenza.cloud.provider.LivingEntityProvider
import net.minecraft.block.*
import net.minecraft.entity.Entity
import net.minecraft.entity.FallingBlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.FluidState
import net.minecraft.fluid.Fluids
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.GameRules
import net.minecraft.world.World

class CloudBlock(settings: Settings?) : PowderSnowBlock(settings), CloudAttribute {

    override val collidable: Boolean
        get() = super.collidable

    override fun isSideInvisible(state: BlockState, stateFrom: BlockState, direction: Direction): Boolean {
        val isSideInvisible = super<PowderSnowBlock>.isSideInvisible(state, stateFrom, direction)

        return if(!isSideInvisible){
            super<CloudAttribute>.isSideInvisible(state, stateFrom, direction)
        }else{
            isSideInvisible
        }
    }

    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
        super<CloudAttribute>.onEntityCollision(state, world, pos, entity)
    }

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape? {
        return super<CloudAttribute>.getCollisionShape(state, world, pos, context)
    }


//    override fun hasSidedTransparency(state: BlockState?): Boolean {
//        return true
//    }
}