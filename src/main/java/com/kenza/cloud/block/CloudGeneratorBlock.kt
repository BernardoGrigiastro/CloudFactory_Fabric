package com.kenza.cloud.block

import com.kenza.cloud.gui.factory.IRScreenHandlerFactory
import com.kenza.cloud.makeID
import com.kenza.cloud.utils.value
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemPlacementContext
import net.minecraft.particle.ParticleTypes
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.*
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.*

class CloudGeneratorBlock(
    settings: Settings?,
    val screenHandler: ((Int, PlayerInventory, ScreenHandlerContext) -> ScreenHandler)?,
) : Block(settings), BlockEntityProvider {


    override fun getPlacementState(ctx: ItemPlacementContext?): BlockState? {
        super.getPlacementState(ctx)
        return this.defaultState.with(FACING, ctx?.playerFacing)
            .with(ACTIVE, false)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>?) {
        super.appendProperties(builder)
        builder?.add(ACTIVE)
        builder?.add(FACING)
    }


    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(FACING, getRotated(state[FACING], rotation))
    }


    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return CloudGeneratorBlockEntity(pos, state)
    }

    override fun <T : BlockEntity?> getTicker(
        world: World,
        state: BlockState?,
        type: BlockEntityType<T>?
    ): BlockEntityTicker<T>? {
        return if (world.isClient) null
        else BlockEntityTicker { world, blockPos, blockState, blockEntity -> (blockEntity as? CloudGeneratorBlockEntity)?.tick(world, blockPos, blockState) }
    }

    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random) {
        if (!state.get(ACTIVE)) {
            return
        }
        val d = pos.x.toDouble() + 0.5
        val e = pos.y.toDouble()
        val f = pos.z.toDouble() + 0.5
        if (random.nextDouble() < 0.1) {
            world.playSound(d, e, f, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0f, 1.0f, false)
        }
        val direction = state.getOrEmpty(FACING).value?.opposite ?: return
        val axis = direction.axis
        val h = random.nextDouble() * 0.6 - 0.3
        val i = if (axis === Direction.Axis.X) direction.offsetX.toDouble() * 0.52 else h
        val j = random.nextDouble() * 6.0 / 16.0
        val k = if (axis === Direction.Axis.Z) direction.offsetZ.toDouble() * 0.52 else h
        world.addParticle(ParticleTypes.SMOKE, d + i, e + j, f + k, 0.0, 0.0, 0.0)
        world.addParticle(ParticleTypes.FLAME, d + i, e + j, f + k, 0.0, 0.0, 0.0)
    }

    @Suppress("DEPRECATION")
    override fun onStateReplaced(state: BlockState, world: World, pos: BlockPos, newState: BlockState, moved: Boolean) {
        super.onStateReplaced(state, world, pos, newState, moved)
//        if (world.isClient) return

        if (state.block == newState.block) {
            return
        }
        val oldBlockEntity = world.getBlockEntity(pos) as? CloudGeneratorBlockEntity

        if (oldBlockEntity?.items?.isNotEmpty() == true) {
            ItemScatterer.spawn(world, pos, oldBlockEntity.items)
            world.updateComparators(pos, this)
        }


    }


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
        val FACING: DirectionProperty = Properties.HORIZONTAL_FACING

        fun getRotated(direction: Direction, rotation: BlockRotation): Direction =
            if (direction.axis.isVertical) direction else when (rotation) {
                BlockRotation.NONE -> direction
                BlockRotation.CLOCKWISE_90 -> direction.rotateYClockwise()
                BlockRotation.CLOCKWISE_180 -> direction.opposite
                BlockRotation.COUNTERCLOCKWISE_90 -> direction.rotateYCounterclockwise()
            }
    }

}