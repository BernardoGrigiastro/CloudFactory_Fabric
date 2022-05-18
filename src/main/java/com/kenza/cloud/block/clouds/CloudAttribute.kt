package com.kenza.cloud.block.clouds

import com.kenza.cloud.provider.EntityProvider
import com.kenza.cloud.provider.LivingEntityProvider
import net.minecraft.block.*
import net.minecraft.block.enums.BlockHalf
import net.minecraft.block.enums.SlabType
import net.minecraft.entity.Entity
import net.minecraft.entity.FallingBlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.state.property.Properties.BLOCK_HALF
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.GameRules
import net.minecraft.world.World

interface CloudAttribute {

    val collidable: Boolean

    fun isSideInvisible(state: BlockState, stateFrom: BlockState, direction: Direction): Boolean {



        (stateFrom.block as? CloudAttribute)?.let {

            if (state.block is CloudFence || state.block is CloudWall || state.block is CloudGate  ) {
                return false
            }

            if (stateFrom.block is CloudFence || stateFrom.block is CloudWall || stateFrom.block is CloudGate  ) {
                return false
            }


            if (state.block is CloudStairs) {

                val facing = state.get(StairsBlock.FACING)
                val half = state.get(StairsBlock.HALF)

                if (stateFrom.block is CloudSlab) {
                    val type = stateFrom.get(SlabBlock.TYPE)


                    if(half == BlockHalf.TOP && type == SlabType.TOP && direction.isHorizontal()){
                        return false
                    }else if(half == BlockHalf.BOTTOM && type == SlabType.BOTTOM  && direction.isHorizontal()){
                        return false
                    }

                    when (direction) {
                        Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST -> {
                            return (type == SlabType.DOUBLE || half.isSameDirection(type))
                        }
                        else -> {}

                    }
                }

                if (stateFrom.block is CloudStairs) {

                    val facing2 = stateFrom.get(StairsBlock.FACING)
                    val half2 = stateFrom.get(StairsBlock.HALF)

                    if(half == BlockHalf.TOP && half2 == BlockHalf.BOTTOM && direction != Direction.UP ) {
                        return false
                    }else if(half == BlockHalf.BOTTOM && half2 == BlockHalf.TOP && direction != Direction.DOWN && facing != facing2){
                        return false
                    }

                }


            }


            if (state.block is CloudSlab && !state.isDoubleSlabBlock()) {

                val type = state.get(SlabBlock.TYPE)

                if (stateFrom.block is CloudStairs) {

                    val facing = stateFrom.get(StairsBlock.FACING)
                    val half = stateFrom.get(StairsBlock.HALF)

                    if(half == BlockHalf.TOP && type == SlabType.BOTTOM){
                        return false
                    }else if(half == BlockHalf.BOTTOM && type == SlabType.TOP && direction != Direction.UP){
                        return false
                    }else if(half == BlockHalf.TOP && type == SlabType.TOP && (direction.isVertical()  || direction == facing.opposite)){
                        return false
                    }

                }

                if (stateFrom.block is CloudSlab) {
                    val type2 = stateFrom.get(SlabBlock.TYPE)

                    if(type != type2 && direction.isHorizontal()){
                        return false
                    }
//                    else if(type == type2  && direction.isHorizontal()){
//                        return
//                    }
                }

            }


            if (state.block is CloudBlock || state.isDoubleSlabBlock()) {

                if (stateFrom.block is CloudStairs) {

                    val facing = stateFrom.get(StairsBlock.FACING)
                    val half = stateFrom.get(StairsBlock.HALF)

                    if (direction == facing) {
                        return false
                    }


                    return when (direction) {
                        Direction.DOWN, Direction.UP -> {
                            return (half != BlockHalf.TOP)
                        }
                        Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST -> {
                            return (direction.opposite == facing)
                        }
                        else -> {
                            return false
                        }
                    }

                }

                if (stateFrom.block is CloudSlab) {

                    val type = stateFrom.get(SlabBlock.TYPE)

                    if (direction == Direction.UP && type == SlabType.TOP) {
                        return false
                    } else if (direction == Direction.DOWN && type == SlabType.BOTTOM) {
                        return false
                    }


                    when (direction) {
                        Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST -> {
                            return (type == SlabType.DOUBLE)
                        }
                        else -> {}
                    }
                }

            }

            return true
        }
        return false
    }

    fun onEntityCollision(state: BlockState, world: World, pos: BlockPos, entity: Entity) {
        if (entity !is LivingEntity || entity.getBlockStateAtPos().isOf(this)) {


            if (!PowderSnowBlock.canWalkOnPowderSnow(entity)) {
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


    fun getCollisionShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape? {

        var entity: Entity? = (context as? EntityShapeContext)?.entity

//        var block = (this as? Block) ?: return VoxelShapes.empty()


        if (context is EntityShapeContext && entity != null) {
            val bl = entity is FallingBlockEntity
            if (bl || PowderSnowBlock.canWalkOnPowderSnow(entity) && context.isAbove(
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



private fun BlockHalf.isSameDirection(type: SlabType): Boolean {

    return (this == BlockHalf.TOP && type == SlabType.TOP) || (this == BlockHalf.BOTTOM && type == SlabType.BOTTOM)

}

private fun Direction.isUp(): Boolean {
    return this == Direction.UP
}

private fun Direction.isDown(): Boolean {
    return this == Direction.DOWN
}

private fun Direction.isVertical(): Boolean {
    return this == Direction.UP || this == Direction.DOWN
}

private fun Direction.isHorizontal(): Boolean {
    return !(this == Direction.UP || this == Direction.DOWN)
}


private fun BlockState.isDoubleSlabBlock(): Boolean {
    return (this.block is SlabBlock) && get(SlabBlock.TYPE) == SlabType.DOUBLE
}

private fun BlockState.isOf(cloudAttribute: CloudAttribute): Boolean {
    return this.isOf(cloudAttribute as? Block)
}
