package com.kenza.cloud.block.base

import com.google.common.base.Preconditions
import com.kenza.cloud.block.CloudGeneratorBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.recipe.SmeltingRecipe
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos

abstract class BaseBlockEntity(type: BlockEntityType<CloudGeneratorBlockEntity>?, pos: BlockPos?, state: BlockState?) : BlockEntity(type, pos, state) {

    open val guiSyncableComponent: GuiSyncableComponent? = null


    fun sync() {
        if(world?.isClient == true){
            return
        }
        Preconditions.checkNotNull(world) // Maintain distinct failure case from below
        check(world is ServerWorld) { "Cannot call sync() on the logical client! Did you check world.isClient first?" }
        (world as ServerWorld).chunkManager.markForUpdate(getPos())
    }



    override fun readNbt(nbt: NbtCompound) {
        super.readNbt(nbt)
        guiSyncableComponent?.properties?.forEach {
            it.readNbt(nbt)
        }
    }

    public override fun writeNbt(nbt: NbtCompound) {
        guiSyncableComponent?.properties?.forEach {
            it.writeNbt(nbt)
        }
    }



//    abstract fun readTag(tag: NbtCompound)
//
//    abstract fun writeTag(tag: NbtCompound)
//
//
//    open fun toClientTag(tag: NbtCompound) {
//        readTag(tag)
//    }
//
//    open fun fromClientTag(tag: NbtCompound) {
//        writeTag(tag)
//    }
//
//    override fun toUpdatePacket(): BlockEntityUpdateS2CPacket {
//        return BlockEntityUpdateS2CPacket.create(this)
//    }
//
//    override fun toInitialChunkDataNbt(): NbtCompound {
//        val nbt = super.toInitialChunkDataNbt()
//        toClientTag(nbt)
//        nbt.putBoolean("#c", true)
//        return nbt
//    }
//
//    override fun writeNbt(nbt: NbtCompound) {
//        readTag(nbt)
//    }
//
//    override fun readNbt(nbt: NbtCompound) {
//        if (nbt.contains("#c")) {
//            fromClientTag(nbt)
//        } else {
//            writeTag(nbt)
//        }
//    }



}