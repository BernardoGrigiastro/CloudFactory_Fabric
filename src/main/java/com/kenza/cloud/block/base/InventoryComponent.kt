//package com.kenza.cloud.block.base
//
//import com.kenza.cloud.block.CloudGeneratorBlockEntity
//import net.minecraft.inventory.Inventory
//import net.minecraft.inventory.InventoryChangedListener
//import net.minecraft.inventory.SimpleInventory
//import net.minecraft.item.ItemStack
//import net.minecraft.nbt.NbtCompound
//import net.minecraft.nbt.NbtList
//
//class InventoryComponent(val syncable: CloudGeneratorBlockEntity, supplier: InventoryComponent.() -> SimpleInventory) : InventoryChangedListener,
//    DefaultSyncableObject() {
//
//    val inventory: SimpleInventory = supplier()
//
////    init {
////        inventory.addListener(this)
////        inventory.component = this
////    }
//
//
//    override fun onInventoryChanged(sender: Inventory?) {
//        syncable.isMarkedForUpdate = true
//    }
//
//    override fun readNbt(nbt: NbtCompound) {
//        val tagList = nbt.get("Inventory") as NbtList? ?: NbtList()
//        tagList.indices.forEach { i ->
//            val stackTag = tagList.getCompound(i)
//            val slot = stackTag.getInt("Slot")
//            inventory.setStack(slot, ItemStack.fromNbt(stackTag))
//        }
////        itemConfig.readNbt(tag)
//    }
//
//
//    override fun writeNbt(nbt: NbtCompound): NbtCompound {
//        val tagList = NbtList()
//        for (i in 0 until inventory.size()) {
//            val stackTag = NbtCompound()
//            stackTag.putInt("Slot", i)
//            tagList.add(inventory.getStack(i).writeNbt(stackTag))
//        }
//        nbt.put("Inventory", tagList)
////        itemConfig.writeNbt(tag)
//        return nbt
//    }
//
//}