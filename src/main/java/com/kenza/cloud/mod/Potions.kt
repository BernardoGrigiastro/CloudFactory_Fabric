package com.kenza.cloud.mod

import com.kenza.cloud.CloudFactoryMod.Companion.MOD_ID
import com.kenza.cloud.mixin.BrewingRecipeRegistryMixin
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.Items
import net.minecraft.potion.Potion
import net.minecraft.potion.Potions
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object Potions {


    private var PEGASUS_POTION: Potion? = null
//    private var JUMP_POTION: Potion? = null

    fun configPotions() {
        PEGASUS_POTION = Registry.register(
            Registry.POTION, Identifier(MOD_ID, "pegasus_potion"), Potion(StatusEffectInstance(Effects.PEGASUS_EFFECT, 60 * 1200, 0))
        )

//        JUMP_POTION = Registry.register(
//            Registry.POTION, Identifier(MOD_ID, "jump_potion"), Potion(StatusEffectInstance(Effects.JUMP_EFFECT, 8 * 1200, 0))
//        )

        registerPotionRecipes()
    }


    private fun registerPotionRecipes() {
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(
            Potions.AWKWARD, Items.FEATHER, PEGASUS_POTION
        )

//        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(
//            Potions.AWKWARD, Items.SLIME_BALL, JUMP_POTION
//        )
    }

}