package com.kenza.cloud.usecase

import com.kenza.cloud.item.PegasusTrinketItem
import com.kenza.cloud.provider.EntityProvider
import com.kenza.cloud.utils.value
import dev.emi.trinkets.api.TrinketsApi
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack

object CloudUseCase {


    fun canWalkOnCloud(entity : Entity): Boolean {
        return  ( (entity as? EntityProvider)?.cloudWalkEffectEnabled ?: false) || ((entity as? LivingEntity)?.hasPegasusTrinket()  ?: false)
    }


    fun LivingEntity.hasPegasusTrinket(): Boolean {
        return try {
            TrinketsApi.getTrinketComponent(this).value?.getEquipped { stack: ItemStack ->
                stack.item is PegasusTrinketItem
            }?.isNotEmpty() ?: false
        } catch (e: NoClassDefFoundError) {
            false
        }
    }

}