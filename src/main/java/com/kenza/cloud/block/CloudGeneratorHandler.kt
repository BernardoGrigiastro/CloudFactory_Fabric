package com.kenza.cloud.block

import WCustomBar
import com.kenza.cloud.CloudFactoryMod.Companion.CLOUD_GENERATOR_HANDLER
import com.kenza.cloud.block.base.BaseBlockEntity
import com.kenza.cloud.gui.IRGuiScreenHandler
import com.kenza.cloud.gui.add
import com.kenza.cloud.gui.configure
import com.kenza.cloud.utils.identifier
import fuelBar
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.ValidatedSlot
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.MusicDiscItem
import net.minecraft.screen.ScreenHandlerContext
import processBar

class CloudGeneratorHandler(syncId: Int, playerInventory: PlayerInventory, ctx: ScreenHandlerContext) :
    IRGuiScreenHandler(
        CLOUD_GENERATOR_HANDLER,
        syncId,
        playerInventory,
        ctx
    ) {
    init {

//        val root = WGridPanel()
//        setRootPanel(root)
//        configure("block.cloud_factory.cloud_generator", ctx, playerInventory, blockInventory)
//
//        val inputSlot = WItemSlot.of(blockInventory, 2)
//        root.add(inputSlot, 3.5, 1.8)
//
//        withBlockEntity<CloudGeneratorBlockEntity> { be ->
//            val processWidget = processBar(be, CRAFTING_COMPONENT_ID)
//            root.add(processWidget, 4.8, 1.8)
//
////            val fluid = fluidTank(be, CloudGeneratorBlockEntity.TANK_ID)
////            root.add(fluid, 6.2, 1.0)
//        }
//
//        root.validate(this)


        val root = WGridPanel()
        setRootPanel(root)
        configure("block.cloud_factory.cloud_generator", ctx, playerInventory, blockInventory)

        val inputSlot = WItemSlot.of(blockInventory, 2)
        root.add(inputSlot, 3.3, 1.8)


        val processWidget = query<CloudGeneratorBlockEntity, WCustomBar> { be -> processBar(be, CRAFTING_COMPONENT_ID) }
        root.add(processWidget, 4.5, 1.7)

        val outputSlot = WItemSlot.outputOf(blockInventory, 3)
        outputSlot.addChangeListener { _, _, _, _ ->
            val player = playerInventory.player
            if (!player.world.isClient) {
                ctx.run { world, pos ->
                    val blockEntity = world.getBlockEntity(pos) as? BaseBlockEntity ?: return@run
//                    blockEntity.dropExperience(player)
                }
            }
        }
        outputSlot.isInsertingAllowed = false
        root.add(outputSlot, 6.34, 1.8)



        val fuelSlot = WItemSlot.of(blockInventory, 4)
        root.add(fuelSlot, .3, 1.8)


        val fuelBar = query<CloudGeneratorBlockEntity, WCustomBar> { be -> fuelBar(be, CRAFTING_COMPONENT_ID) }
        root.add(fuelBar, .35, .9)


        root.validate(this)

    }

    override fun canUse(player: PlayerEntity?): Boolean = true

    companion object {
        const val CRAFTING_COMPONENT_ID = 0
    }
}
