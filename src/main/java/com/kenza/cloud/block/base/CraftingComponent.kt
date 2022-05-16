import com.kenza.cloud.block.base.DefaultSyncableObject
import net.minecraft.nbt.NbtCompound

class CraftingComponent  : DefaultSyncableObject(){


    fun tick() {
        progressTime++
        fuelTime++

        if(progressTime > maxProgressTime){
            progressTime = 0
        }

        if(fuelTime > maxFuelTime){
            fuelTime = 0
        }
    }

    var progressTime = 0
    var maxProgressTime = 720
    var fuelTime = 0
    var maxFuelTime = 720



    override fun readNbt(nbt: NbtCompound) {
        progressTime = nbt.getInt("progressTime") ?: 0
//        maxProgressTime = tag?.getInt("maxProgressTime") ?: 0
    }

    override fun writeNbt(nbt: NbtCompound): NbtCompound {
        nbt.putInt("progressTime", progressTime)
//        tag.putInt("maxProgressTime", maxProgressTime)
        return nbt
    }


}