package com.kenza.cloud.mod

import com.kenza.cloud.makeID
import com.kenza.cloud.mod.effect.PegasusEffect
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.util.registry.Registry

object Effects {

    ///effect give @p cloud_factory:pegasus

    var PEGASUS_EFFECT: StatusEffect? = null
    var DOUBLE_JUMP_EFFECT: StatusEffect? = null



    fun configEffects() {
        PEGASUS_EFFECT = registerStatusEffect("pegasus")
        DOUBLE_JUMP_EFFECT = registerStatusEffect("double_jump")
    }

    private fun registerStatusEffect(name: String?): StatusEffect? {
        return Registry.register<StatusEffect, StatusEffect>(
            Registry.STATUS_EFFECT, makeID(name), PegasusEffect(StatusEffectCategory.BENEFICIAL, 3124687)
        )
    }
}