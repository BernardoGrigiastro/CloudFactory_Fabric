package com.kenza.cloud.item

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.BlockStateRaycastContext
import net.minecraft.world.World

class CloudBlockItem(block: Block?, settings: Settings?) : BlockItem(block, settings) {

    override fun use(level: World, player: PlayerEntity, interactionHand: Hand?): TypedActionResult<ItemStack?>? {
        val vec =
            Vec3d(0.0, 0.0, 1.0).rotateX(-player.pitch * 0.017453292f).rotateY(-player.getHeadYaw() * 0.017453292f)
        val hit = level.raycast(BlockStateRaycastContext(
            player.eyePos, player.eyePos.add(vec.multiply(4.9))
        ) { state: BlockState -> state.isAir })
        return if (hit != null) {
            val stack = player.getStackInHand(interactionHand)
            this.place(ItemPlacementContext(player, interactionHand, stack, hit))
            TypedActionResult.success(stack, level.isClient())
        } else {
            TypedActionResult.pass(player.getStackInHand(interactionHand))
        }
    }
}