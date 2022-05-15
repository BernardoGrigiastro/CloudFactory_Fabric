package com.kenza.cloud

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.BlockStateRaycastContext
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent

class CloudBlockItem(block: Block?, settings: Settings?) : BlockItem(block, settings) {

    override fun use(level: World, player: PlayerEntity, interactionHand: Hand?): TypedActionResult<ItemStack?>? {
        val vec =
            Vec3d(0.0, 0.0, 1.0).rotateX(-player.pitch * 0.017453292f).rotateY(-player.getHeadYaw() * 0.017453292f)
        val hit = level.raycast(BlockStateRaycastContext(
            player.eyePos, player.eyePos.add(vec.multiply(4.9))
        ) { statex: BlockState -> statex.isAir })
        return if (hit != null) {
            val pos = hit.blockPos
            val state = block.defaultState
            val soundType = state.soundGroup
            level.setBlockState(pos, state, 0)
            level.emitGameEvent(player, GameEvent.BLOCK_PLACE, pos)
            level.playSound(
                player,
                pos,
                getPlaceSound(state),
                SoundCategory.BLOCKS,
                (soundType.getVolume() + 1.0f) / 2.0f,
                soundType.getPitch() * 0.8f
            )
            if (!player.abilities.creativeMode && level.getBlockState(pos) == state) {
                player.getStackInHand(interactionHand).decrement(1)
            }
            TypedActionResult.success(player.getStackInHand(interactionHand), level.isClient())
        } else {
            TypedActionResult.pass(player.getStackInHand(interactionHand))
        }
    }
}