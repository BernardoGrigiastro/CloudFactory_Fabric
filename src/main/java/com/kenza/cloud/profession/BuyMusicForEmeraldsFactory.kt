package com.kenza.cloud.profession

import com.kenza.cloud.utils.intFromRange
import net.minecraft.entity.Entity
import net.minecraft.item.*
import net.minecraft.village.TradeOffers
import net.minecraft.village.TradeOffer
import java.util.*

class BuyMusicForEmeraldsFactory(maxUses: Int, experience: Int) : TradeOffers.Factory {

    private val maxUses: Int
    private val experience: Int
    private val multiplier: Float

    override fun create(entity: Entity, random : Random): TradeOffer {

        val itemStack = ItemStack(com.kenza.cloud.item.ModItems.ALUMENTUM_ITEM, 1)
        return TradeOffer(itemStack, ItemStack(Items.EMERALD).apply {
            count = intFromRange(4, 7)
        }, maxUses, experience, multiplier)
    }

    init {
        this.maxUses = maxUses
        this.experience = experience
        multiplier = 0.05f
    }
}

