package com.kenza.cloud.provider

import net.minecraft.util.math.Vec3d

interface EntityProvider {
    var cloudMovementMultiplier: Vec3d
    var cloudWalkEffectEnabled: Boolean
    var douleJumpEffectEnabled: Boolean
}