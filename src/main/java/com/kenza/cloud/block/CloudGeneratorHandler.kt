package com.kenza.cloud.block

import com.kenza.cloud.CloudFactoryMod.Companion.CLOUD_GENERATOR_HANDLER
import com.kenza.cloud.gui.IRGuiScreenHandler
import com.kenza.cloud.gui.add
import com.kenza.cloud.gui.configure
import com.kenza.cloud.utils.identifier
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.ValidatedSlot
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.MusicDiscItem
import net.minecraft.screen.ScreenHandlerContext

class CloudGeneratorHandler(syncId: Int, playerInventory: PlayerInventory, ctx: ScreenHandlerContext) :
    IRGuiScreenHandler(
        CLOUD_GENERATOR_HANDLER,
        syncId,
        playerInventory,
        ctx
    ) {
    init {

        val root = getRootPanel() as WGridPanel

        val slot1 = object : ValidatedSlot(blockInventory, 0, 7, 1) {
            override fun canInsert(stack: ItemStack?): Boolean {
                return super.canInsert(stack)
            }
        }

        val slot = WItemSlot.of(blockInventory, 0, 7, 1).setFilter {
            it.item is MusicDiscItem
        }

        root.add(slot, root.insets.right / 2 + 1 - 3, 1)
//        root.add(x1, root.insets.right / 2 - 1, 3, 5, 1)

//        root.add(WTextField(LiteralText("Type something...")).setMaxLength(64), 0, 7, 5, 1)
//        root.add(WLabel(LiteralText("Large slot:")), 0, 9)
//        root.add(WItemSlot.outputOf(blockInventory, 0), 4, 8)
//        root.add(WItemSlot.of(blockInventory, 7).setIcon(TextureIcon(Identifier("libgui-test", "saddle.png"))), 7, 9)


        root.add(createPlayerInventoryPanel(), 0, 3)

        getRootPanel().validate(this)


//        val root = WGridPanel()
//        setRootPanel(root)
//        configure("block.indrev.smelter", ctx, playerInventory, blockInventory)
//
//        val inputSlot = WItemSlot.of(blockInventory, 2)
//        root.add(inputSlot, 3.5, 1.8)
//
////            withBlockEntity<SmelterBlockEntity> { be ->
////                val processWidget = processBar(be, SmelterBlockEntity.CRAFTING_COMPONENT_ID)
////                root.add(processWidget, 4.8, 1.8)
////
////                val fluid = fluidTank(be, SmelterBlockEntity.TANK_ID)
////                root.add(fluid, 6.2, 1.0)
////            }

    }

    override fun canUse(player: PlayerEntity?): Boolean = true

    companion object {
        val SCREEN_ID = identifier("smelter")
    }
}
