package com.kenza.cloud.block.clouds

import net.minecraft.block.*
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class CloudWall(settings: Settings?) : WallBlock(settings), CloudAttribute {


    override val collidable: Boolean
        get() = super.collidable


    override fun isSideInvisible(state: BlockState, stateFrom: BlockState, direction: Direction): Boolean {
        val isSideInvisible = super<WallBlock>.isSideInvisible(state, stateFrom, direction)

        return if(!isSideInvisible){
            super<CloudAttribute>.isSideInvisible(state, stateFrom, direction)
        }else{
            isSideInvisible
        }
    }

    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {

        if (!PowderSnowBlock.canWalkOnPowderSnow(entity)) {
            super<CloudAttribute>.onEntityCollision(state, world, pos, entity)
        }else{
            super<WallBlock>.onEntityCollision(state, world, pos, entity)
        }

    }

    override fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape? {

        var entity: Entity? = (context as? EntityShapeContext)?.entity
            ?: return super<WallBlock>.getCollisionShape(state, world, pos, context)

        if (!PowderSnowBlock.canWalkOnPowderSnow(entity)) {
            return super<CloudAttribute>.getCollisionShape(state, world, pos, context)
        }else{
            return super<WallBlock>.getCollisionShape(state, world, pos, context)
        }
    }

}
