import com.kenza.cloud.block.CloudGeneratorBlock.Companion.ACTIVE
import com.kenza.cloud.block.CloudGeneratorBlockEntity
import com.kenza.cloud.block.base.DefaultSyncableObject
import com.kenza.cloud.item.AlumentumItem
import com.kenza.cloud.recipe.CloudCreatingRecipe
import net.fabricmc.fabric.api.registry.FuelRegistry
import net.minecraft.block.BlockState
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

class CraftingComponent(val entity: CloudGeneratorBlockEntity) :
    DefaultSyncableObject() {


    fun tick(world: World, blockPos: BlockPos, state: BlockState) {
//        progressTime++
//        fuelTime++
//
//        if(progressTime > maxProgressTime){
//            progressTime = 0
//        }
//
//        if(fuelTime > maxFuelTime){
//            fuelTime = 0
//        }


        val oldBlockEntity = world.getBlockEntity(blockPos) as? CloudGeneratorBlockEntity



        if (isConsumingFuel()) {
            fuelTime--
            fuelTime -= 10
        }

        if (hasRecipe()) {
            if (hasFuelInFuelSlot() && !isConsumingFuel()) {
                consumeFuel()
            }
            if (isConsumingFuel()) {

                progressTime ++

                if (progressTime > maxProgressTime) {
                    craftItem()
                }
            }
        } else {
            resetProgress()
        }


        if (state.get(ACTIVE) == !isConsumingFuel()) {
            world.setBlockState(blockPos, state.with(ACTIVE, isConsumingFuel()))
        }

        oldBlockEntity?.sync()
    }

    var progressTime = 0
    var maxProgressTime = 100

    var fuelTime = 0
    var maxFuelTime = 100


    override fun readNbt(nbt: NbtCompound) {
        progressTime = nbt.getInt("progressTime") ?: 0
        fuelTime = nbt.getInt("fuelTime") ?: 0
        maxFuelTime = nbt.getInt("maxFuelTime") ?: 0
//        maxProgressTime = tag?.getInt("maxProgressTime") ?: 0
    }

    override fun writeNbt(nbt: NbtCompound): NbtCompound {
        nbt.putInt("progressTime", progressTime)
        nbt.putInt("fuelTime", fuelTime)
        nbt.putInt("maxFuelTime", maxFuelTime)
//        tag.putInt("maxProgressTime", maxProgressTime)
        return nbt
    }


    override fun toPacket(buf: PacketByteBuf) {
        super.toPacket(buf)
    }

    override fun fromPacket(buf: PacketByteBuf) {
        super.fromPacket(buf)
    }

    private fun craftItem() {
        val world: World = entity.world ?: return
        val inventory1 = SimpleInventory(entity.items.size)

        entity.items.forEachIndexed { i, itemStack ->
            inventory1.setStack(i, entity.items.get(i))
        }

        val match: Optional<CloudCreatingRecipe> = world.recipeManager
            .getFirstMatch<SimpleInventory, CloudCreatingRecipe>(CloudCreatingRecipe.Type.INSTANCE, inventory1, world)

        if (match.isPresent()) {
            entity.removeStack(0, 1)
            entity.setStack(
                1, ItemStack(
                    match.get().getOutput().getItem(),
                    entity.items.get(1).getCount() + 1
                )
            )
            resetProgress()
        }
    }

    private fun resetProgress() {
        progressTime = 0
    }

    private fun consumeFuel() {

        val fuel = entity.getStack(2)
        if (fuel.isEmpty() || fuel.item !is AlumentumItem) {
            return
        }

        fuelTime = FuelRegistry.INSTANCE[entity.removeStack(2, 1).getItem()]
        maxFuelTime = fuelTime

//        fuel.decrement(1)
    }


    private fun hasFuelInFuelSlot(): Boolean {
        return !entity.items.get(0).isEmpty()
    }

    private fun isConsumingFuel(): Boolean = fuelTime > 0


    private fun hasRecipe(): Boolean {
        val world: World = entity.world ?: return false
        val inventory = SimpleInventory(entity.items.size)
        for (i in entity.items.indices) {
            inventory.setStack(i, entity.items.get(i))
        }
        val match: Optional<CloudCreatingRecipe> = world.recipeManager
            .getFirstMatch<SimpleInventory, CloudCreatingRecipe>(CloudCreatingRecipe.Type.INSTANCE, inventory, world)
        return (match.isPresent() && canInsertAmountIntoOutputSlot(
            inventory
        )
                && canInsertItemIntoOutputSlot(
            inventory,
            match.get().getOutput()
        ))
    }

    private fun canInsertItemIntoOutputSlot(inventory: SimpleInventory, output: ItemStack): Boolean {
        return inventory.getStack(3).item === output.item || inventory.getStack(3).isEmpty
    }

    private fun canInsertAmountIntoOutputSlot(inventory: SimpleInventory): Boolean {
        return inventory.getStack(3).maxCount > inventory.getStack(3).count
    }
}