//package com.kenza.cloud.utils
//
import com.google.gson.JsonElement
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.fluid.Fluid
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
import net.minecraft.nbt.NbtCompound
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeManager
import net.minecraft.recipe.RecipeType
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.text.OrderedText
import net.minecraft.util.Identifier
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Direction
import net.minecraft.util.registry.Registry
//
//
//
//
fun <T : ScreenHandler> Identifier.registerScreenHandler(
    f: (Int, PlayerInventory, ScreenHandlerContext) -> T
): ExtendedScreenHandlerType<T> =
    ScreenHandlerRegistry.registerExtended(this) { syncId, inv, buf ->
        f(syncId, inv, ScreenHandlerContext.create(inv.player.world, buf.readBlockPos()))
    } as ExtendedScreenHandlerType<T>

//
//fun ChunkPos.toNbt() = NbtCompound().also {
//    it.putInt("x", x)
//    it.putInt("z", z)
//}
//
//fun getChunkPos(nbt: NbtCompound) = ChunkPos(nbt.getInt("x"), nbt.getInt("z"))
//
//fun getFluidFromJson(json: JsonElement): Array<IRFluidAmount> {
//    if (json.isJsonArray) {
//        return json.asJsonArray.map { getFluidFromJson(it.asJsonObject).toList() }.flatten().toTypedArray()
//    } else {
//        val json = json.asJsonObject
//        val fluidId = json.get("fluid").asString
//        val fluidKey = FluidVariant.of(Registry.FLUID.get(Identifier(fluidId)))
//        val fluidAmount = json.get("amount").asLong
//        return arrayOf(fluidAmount of fluidKey)
//    }
//}
//
//fun createREIFluidWidget(widgets: MutableList<Widget>, startPoint: Point, fluid: IRFluidAmount) {
//    widgets.add(Widgets.createTexturedWidget(TANK_BOTTOM.image, startPoint.x, startPoint.y, 0f, 0f, 16, 52, 16, 52))
//    widgets.add(Widgets.createDrawableWidget { _, matrices, mouseX, mouseY, _ ->
//        fluid.renderGuiRect(startPoint.x + 2.0, startPoint.y.toDouble() + 1.5, startPoint.x.toDouble() + 14, startPoint.y.toDouble() + 50)
//        if (mouseX > startPoint.x && mouseX < startPoint.x + 16 && mouseY > startPoint.y && mouseY < startPoint.y + 52) {
//            val information = mutableListOf<OrderedText>()
//            information.addAll(getTooltip(fluid.resource, fluid.amount, -1))
//            MinecraftClient.getInstance().currentScreen?.renderOrderedTooltip(matrices, information, mouseX, mouseY)
//        }
//    })
//}
//
//fun pack(dirs: Collection<Direction>): Byte {
//    var i = 0
//    dirs.forEach { dir -> i = i or (1 shl dir.id) }
//    return i.toByte()
//}
//
//fun unpack(byte: Byte): List<Direction> {
//    val i = byte.toInt()
//    return DIRECTIONS.filter { dir -> i and (1 shl dir.id) != 0 }
//}
//
//private val DIRECTIONS = Direction.values()
//
//val Fluid?.rawId: Int
//    get() = Registry.FLUID.getRawId(this)
//
//inline fun <T> transaction(block: (Transaction) -> T) = Transaction.openOuter().use(block)
//
//@Suppress("UNCHECKED_CAST")
//fun <T : Recipe<Inventory>> RecipeManager.getRecipes(type: RecipeType<T>) = getAllOfType(type) as Map<Identifier, T>