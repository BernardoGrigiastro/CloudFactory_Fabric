package com.kenza.cloud.recipe.rei

import com.kenza.cloud.CloudFactoryMod
import com.kenza.cloud.block.Blocks
import com.kenza.cloud.gui.Textures.RIGHT_PROCESS_FULL
import com.kenza.cloud.recipe.CloudCreatingRecipe
import me.shedaniel.math.Point
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.gui.DrawableConsumer
import me.shedaniel.rei.api.client.gui.Renderer
import me.shedaniel.rei.api.client.gui.widgets.Widget
import me.shedaniel.rei.api.client.gui.widgets.Widgets
import me.shedaniel.rei.api.client.plugins.REIClientPlugin
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry
import me.shedaniel.rei.api.client.registry.display.DisplayCategory
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry
import me.shedaniel.rei.api.common.category.CategoryIdentifier
import me.shedaniel.rei.api.common.display.Display
import me.shedaniel.rei.api.common.entry.EntryIngredient
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes
import me.shedaniel.rei.api.common.util.EntryIngredients
import me.shedaniel.rei.api.common.util.EntryStacks
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.resource.language.I18n
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

@Environment(EnvType.CLIENT)
object REIPlugin : REIClientPlugin {


    val cloud_recipes_category = "cloud_recipes_category"

    val categoryIdentifier = CategoryIdentifier.of<CloudDisplay>(CloudFactoryMod.MOD_ID, cloud_recipes_category)

    override fun registerCategories(registry: CategoryRegistry) {
        registry.add(CloudCategory())
    }


    override fun registerEntries(entryRegistry: EntryRegistry) {

    }

    override fun registerDisplays(registry: DisplayRegistry) {

        registry.recipeManager.recipes.keys.map { type ->
            if (type is CloudCreatingRecipe.Type)
                registry.registerFiller(
                    CloudCreatingRecipe::class.java,
                    { r -> r is CloudCreatingRecipe && r.type == type }) { recipe -> CloudDisplay(recipe) }
        }


    }

    class CloudCategory : DisplayCategory<CloudDisplay> {

        override fun getIcon(): Renderer {
            return EntryStacks.of(Blocks.CLOUD_GENERATOR_ITEM, 1)
        }

        override fun getTitle(): Text {
            return Text.of(I18n.translate("rei.cloud_factory.category", *arrayOfNulls(0)))
        }

        override fun getCategoryIdentifier(): CategoryIdentifier<out CloudDisplay> {
            return REIPlugin.categoryIdentifier
        }

        override fun getDisplayHeight(): Int {
            return 100
        }

        override fun getDisplayWidth(display: CloudDisplay?): Int {
            return 150
        }

        override fun setupDisplay(recipeDisplay: CloudDisplay, bounds: Rectangle): MutableList<Widget?> {
            val recipe = recipeDisplay.recipe
            val startPoint = Point(bounds.centerX - 41, bounds.centerY - 27)
            val widgets = super.setupDisplay(recipeDisplay, bounds).toMutableList()

            widgets.add(

                Widgets.createArrow(Point(startPoint.x + 24, startPoint.y + 18)).apply {
                    animationDuration = 2000.0
                }
//                        Widgets.createTexturedWidget(RIGHT_PROCESS_FULL.image, startPoint.x, startPoint.y, 100, 80)

//                CloudingWidget.create(Point(startPoint.x + 24, startPoint.y + 18)).apply {
//                animationDuration = 1000.0
//            }

            )

            if (recipe.recipeItems.isNotEmpty()) {
                val input = recipeDisplay.inputEntries.filter { it.all { it.type == VanillaEntryTypes.ITEM } }
                widgets.add(Widgets.createSlot(Point(startPoint.x + 1, startPoint.y + 19)).entries(input[0]))
                if (recipe.recipeItems.size > 1)
                    widgets.add(
                        Widgets.createSlot(Point(startPoint.x - 17, startPoint.y + 19)).entries(input[1])
                    )
            }

            if (!recipe.output.isEmpty) {
                widgets.add(Widgets.createResultSlotBackground(Point(startPoint.x + 61, startPoint.y + 19)))
                widgets.add(

                    Widgets.createSlot(Point(startPoint.x + 61, startPoint.y + 19))
                        .entries(recipeDisplay.outputEntries.filter { it.all { it.type == VanillaEntryTypes.ITEM } }[0])
                        .disableBackground().markOutput()
                )
            }

            return widgets
        }
    }

    class CloudDisplay(val recipe: CloudCreatingRecipe) : Display {

        override fun getInputEntries(): MutableList<EntryIngredient> {


            val entryIngredientList: MutableList<EntryIngredient> = ArrayList()

            recipe.recipeItems.forEach { ingredient ->

                val builder = EntryIngredient.builder()
                ingredient.matchingStacks.map { stack -> builder.add(EntryStacks.of(ItemStack(stack.item, 1))) }

                entryIngredientList.add(builder.build())
            }


            return entryIngredientList
        }

        override fun getOutputEntries(): MutableList<EntryIngredient> {

            val list = mutableListOf<EntryIngredient>()

            EntryIngredients.of(recipe.output).let {
                list.add(it)
            }

            return list
        }

        override fun getCategoryIdentifier(): CategoryIdentifier<*> {
            return REIPlugin.categoryIdentifier //CategoryIdentifier.of<CloudDisplay>(CLOUD_CREATING_ID)
        }

    }
}

