package com.kenza.cloud.mod

import com.kenza.cloud.makeID
import com.kenza.cloud.mod.effect.DoubleJumpEffect
import com.kenza.cloud.mod.effect.PegasusEffect
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.util.registry.Registry
import java.awt.Color

object Effects {

    ///effect give @p cloud_factory:pegasus

    var PEGASUS_EFFECT: StatusEffect? = null
    var JUMP_EFFECT: StatusEffect? = null



    fun configEffects() {

        PEGASUS_EFFECT = Registry.register<StatusEffect, StatusEffect>(
            Registry.STATUS_EFFECT, makeID("pegasus"), PegasusEffect(StatusEffectCategory.BENEFICIAL, Color.decode("#FFFF00").rgb)
        )

        JUMP_EFFECT= Registry.register<StatusEffect, StatusEffect>(
            Registry.STATUS_EFFECT, makeID("jump"), DoubleJumpEffect(StatusEffectCategory.BENEFICIAL, Color.decode("#00FFFF").rgb)
        )

    }

}