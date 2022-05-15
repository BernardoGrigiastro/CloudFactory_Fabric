package com.kenza.cloud.block

import com.kenza.cloud.CloudFactoryMod
import com.kenza.cloud.utils.ImplementedInventory
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos

class CloudGeneratorBlockEntity (pos: BlockPos?, state: BlockState?) :
    BlockEntity(CloudFactoryMod.CLOUD_GENERATOR_TYPE, pos, state),
    ImplementedInventory,
    NamedScreenHandlerFactory {

    private val INVENTORY_SIZE = 8;

    private var inventoriesItems: DefaultedList<ItemStack> = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY)
    private var inventoryTouched = true


    override fun markDirty() {
        super<BlockEntity>.markDirty()
    }



    override fun getItems(): DefaultedList<ItemStack> {
        inventoryTouched = true
        return inventoriesItems
    }

    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity?): ScreenHandler? {
        return CloudGeneratorHandler(
            syncId,
            inv,
            ScreenHandlerContext.create(world, pos)
        )
    }

    override fun getDisplayName(): Text {
        return LiteralText("CloudGenerator")
    }

}