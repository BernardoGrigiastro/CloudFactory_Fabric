package com.kenza.cloud.utils

import com.minelittlepony.common.event.ScreenInitCallback
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.TitleScreen
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import java.util.*


private var initTitleCounter = 0


fun openLastWorldOnInit() {
    ScreenInitCallback.EVENT.register(ScreenInitCallback { screen: Screen?, buttons: ScreenInitCallback.ButtonList? ->
        onScreenInit(
            screen,
            buttons
        )
    })
}


private fun onScreenInit(screen: Screen?, buttons: ScreenInitCallback.ButtonList?) {
    if (screen is TitleScreen) {
        //open world after start minecraft
        initTitleCounter++
        if (initTitleCounter == 2) {
//            val client = MinecraftClient.getInstance()
//            client.levelStorage.levelList.firstOrNull()?.let { level ->
//                client.soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f))
//                if (client.levelStorage.levelExists(level.rootPath)) {
//                    client.setScreenAndRender(SaveLevelScreen(Text.translatable("selectWorld.data_read")))
//                    client.startIntegratedServer(level.rootPath)
//                }
//            }
        }
    }
}

val <T> Optional<T>.value: T?
    get() = orElse(null)


fun Any.isRenderThread(): Boolean {
    return Thread.currentThread().name.equals("Render thread")
}

