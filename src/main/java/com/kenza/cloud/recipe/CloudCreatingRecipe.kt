package com.kenza.cloud.recipe

import com.google.gson.JsonObject
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.*
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraft.util.collection.DefaultedList
import net.minecraft.world.World

class CloudCreatingRecipe (val valueId: Identifier,val  valueOutput: ItemStack, val recipeItems: DefaultedList<Ingredient>): Recipe<SimpleInventory> {

    override fun matches(inventory: SimpleInventory, world: World): Boolean {
        return recipeItems.get(0).test(inventory.getStack(0))
    }

    override fun craft(inventory: SimpleInventory?): ItemStack {
        return output
    }

    override fun fits(width: Int, height: Int): Boolean {
        return true
    }

    override fun getOutput(): ItemStack {
        return valueOutput.copy()
    }

    override fun getId(): Identifier {
        return valueId
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return Serializer.INSTANCE
    }

    override fun getType(): RecipeType<*> {
        return Type.INSTANCE
    }


    class Type : RecipeType<CloudCreatingRecipe?> {
        companion object {
            val INSTANCE: Type = Type()
        }

    }



    class Serializer : RecipeSerializer<CloudCreatingRecipe> {
        // this is the name given in the json file
        override fun read(id: Identifier, json: JsonObject): CloudCreatingRecipe {
            val output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"))
            val ingredients = JsonHelper.getArray(json, "ingredients")
            val inputs = DefaultedList.ofSize(1, Ingredient.EMPTY)
            for (i in inputs.indices) {
                inputs[i] = Ingredient.fromJson(ingredients[i])
            }
            return CloudCreatingRecipe(id, output, inputs)
        }

        override fun read(id: Identifier, buf: PacketByteBuf): CloudCreatingRecipe {
            val inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY)
            for (i in inputs.indices) {
                inputs[i] = Ingredient.fromPacket(buf)
            }
            val output = buf.readItemStack()
            return CloudCreatingRecipe(id, output, inputs)
        }

        override fun write(buf: PacketByteBuf, recipe: CloudCreatingRecipe) {
            buf.writeInt(recipe.ingredients.size)
            for (ing in recipe.ingredients) {
                ing.write(buf)
            }
            buf.writeItemStack(recipe.output)
        }

        companion object {
            val INSTANCE = Serializer()
        }
    }


    companion object {
        val CLOUD_CREATING_ID = "cloud_creating"
    }
}