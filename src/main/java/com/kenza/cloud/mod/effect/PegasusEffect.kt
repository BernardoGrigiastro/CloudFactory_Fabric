package com.kenza.cloud.mod.effect

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory

class PegasusEffect(category: StatusEffectCategory?, color: Int) : StatusEffect(category, color){


    override fun applyUpdateEffect(entity: LivingEntity, pAmplifier: Int) {
//        if (!entity.world.isClient()) {
//            val x = entity.x
//            val y = entity.y
//            val z = entity.z
//            entity.teleport(x, y, z)
//            entity.setVelocity(0.0, 0.0, 0.0)
//        }
        super.applyUpdateEffect(entity, pAmplifier)
    }

    override fun canApplyUpdateEffect(pDuration: Int, pAmplifier: Int): Boolean {
        return true
    }
}