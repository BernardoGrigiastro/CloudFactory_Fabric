package com.kenza.cloud.block.clouds

import net.minecraft.block.BlockState
import net.minecraft.block.PowderSnowBlock
import net.minecraft.block.ShapeContext
import net.minecraft.block.SlabBlock
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class CloudSlab(settings: Settings?) : SlabBlock(settings) , CloudAttribute{

    override val collidable: Boolean
        get() = super.collidable

    override fun isSideInvisible(state: BlockState, stateFrom: BlockState, direction: Direction): Boolean {

        val isSideInvisible = super<SlabBlock>.isSideInvisible(state, stateFrom, direction)

       return if(!isSideInvisible){
            super<CloudAttribute>.isSideInvisible(state, stateFrom, direction)
        }else{
            isSideInvisible
        }
//        return if(super<CloudAttribute>.isSideInvisible(state, stateFrom, direction)){
//            true
//        }else{
//
//        }
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


}