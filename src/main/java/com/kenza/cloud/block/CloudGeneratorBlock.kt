package com.kenza.cloud.block

import com.kenza.cloud.gui.factory.IRScreenHandlerFactory
import com.kenza.cloud.makeID
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemPlacementContext
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.*
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class CloudGeneratorBlock(
    settings: Settings?,
    val screenHandler: ((Int, PlayerInventory, ScreenHandlerContext) -> ScreenHandler)?,
) : Block(settings), BlockEntityProvider {



    override fun getPlacementState(ctx: ItemPlacementContext?): BlockState? {
        super.getPlacementState(ctx)
        return this.defaultState.with(HORIZONTAL_FACING, ctx?.playerFacing).with(ACTIVE, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
        super.appendProperties(builder)
        builder?.add(HORIZONTAL_FACING)
        builder?.add(ACTIVE)
    }

    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(HORIZONTAL_FACING, getRotated(state[HORIZONTAL_FACING], rotation))
    }


    override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity {
        return CloudGeneratorBlockEntity(pos, state)
    }


//    override fun <T : BlockEntity?> getTicker(
//        world: World,
//        state: BlockState?,
//        type: BlockEntityType<T>?
//    ): BlockEntityTicker<T>? {
//
//        return BlockEntityTicker { _, _, _, blockEntity -> (blockEntity as? DiscHolderBlockEntity)?.tick() }
//
//    }

//    @Suppress("DEPRECATION")
//    override fun onStateReplaced(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moved: Boolean) {
//        val oldBlockEntity = world.getBlockEntity(pos) as? DiscHolderBlockEntity
//        super.onStateReplaced(state, world, pos, newState, moved)
////        if (world.isClient) return
//
//        if (oldBlockEntity?.items?.isNotEmpty() == true) {
//            ItemScatterer.spawn(world, pos, oldBlockEntity.items)
//            world.updateComparators(pos, this)
//        }
//    }


    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hitResult: BlockHitResult
    ): ActionResult {

        screenHandler?.let {
            player.openHandledScreen(IRScreenHandlerFactory(screenHandler, pos))
        }

        return ActionResult.SUCCESS
    }


    companion object {
        val CLOUD_GENERATOR_ID = makeID("cloud_generator")
        val ACTIVE: BooleanProperty = BooleanProperty.of("active");
        val HORIZONTAL_FACING: DirectionProperty = Properties.HORIZONTAL_FACING

        fun getRotated(direction: Direction, rotation: BlockRotation): Direction =
            if (direction.axis.isVertical) direction else when (rotation) {
                BlockRotation.NONE -> direction
                BlockRotation.CLOCKWISE_90 -> direction.rotateYClockwise()
                BlockRotation.CLOCKWISE_180 -> direction.opposite
                BlockRotation.COUNTERCLOCKWISE_90 -> direction.rotateYCounterclockwise()
            }
    }

}