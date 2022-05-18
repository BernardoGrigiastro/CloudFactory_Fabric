package com.kenza.cloud.block.clouds

import net.minecraft.block.BlockState
import net.minecraft.block.FenceBlock
import net.minecraft.block.ShapeContext
import net.minecraft.entity.Entity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class CloudFence(settings: Settings?) : FenceBlock(settings) {

//    override val collidable: Boolean
//        get() = super.collidable
//
//    override fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
//        super<CloudAttribute>.onEntityCollision(state, world, pos, entity)
//    }
//
//    override fun getCollisionShape(
//        state: BlockState,
//        world: BlockView,
//        pos: BlockPos,
//        context: ShapeContext
//    ): VoxelShape? {
//        return super<CloudAttribute>.getCollisionShape(state, world, pos, context)
//    }
}