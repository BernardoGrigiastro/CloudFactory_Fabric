package com.kenza.cloud.recipe

import com.google.gson.JsonObject
import com.kenza.cloud.makeID
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.recipe.ShapedRecipe
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.registry.Registry

object Recipes {

    fun configRecipes() {
        Registry.register<RecipeSerializer<*>, RecipeSerializer<*>>(
            Registry.RECIPE_SERIALIZER, makeID(CloudCreatingRecipe.CLOUD_CREATING_ID),
            CloudCreatingRecipe.Serializer.INSTANCE
        )
        Registry.register<RecipeType<*>, RecipeType<*>>(
            Registry.RECIPE_TYPE, makeID(CloudCreatingRecipe.CLOUD_CREATING_ID),
            CloudCreatingRecipe.Type.INSTANCE
        )
    }



}