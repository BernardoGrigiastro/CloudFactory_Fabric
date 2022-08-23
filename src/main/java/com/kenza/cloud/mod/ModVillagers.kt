////package com.kenza.cloud.mod;
////
////import com.google.common.collect.ImmutableSet;
////import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
////import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
////import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
////import net.kaupenjoe.tutorialmod.TutorialMod;
////import net.kaupenjoe.tutorialmod.block.ModBlocks;
////import net.kaupenjoe.tutorialmod.item.ModItems;
////import net.minecraft.block.Block;
////import net.minecraft.item.ItemStack;
////import net.minecraft.item.Items;
////import net.minecraft.sound.SoundEvents;
////import net.minecraft.util.Identifier;
////import net.minecraft.util.registry.Registry;
////import net.minecraft.util.registry.RegistryKey;
////import net.minecraft.village.TradeOffer;
////import net.minecraft.village.VillagerProfession;
////import net.minecraft.world.poi.PointOfInterestType;
////
//object ModVillagers {
//    val JUMPY_POI: PointOfInterestType = registerPOI("jumpy_poi", ModBlocks.JUMPY_BLOCK)
//    val JUMP_MASTER: VillagerProfession = registerProfession(
//        "jumpmaster",
//        RegistryKey.of(Registry.POINT_OF_INTEREST_TYPE_KEY, Identifier(TutorialMod.MOD_ID, "jumpy_poi"))
//    )
//
//    fun registerProfession(name: String?, type: RegistryKey<PointOfInterestType?>?): VillagerProfession {
//        return Registry.register(
//            Registry.VILLAGER_PROFESSION, Identifier(TutorialMod.MOD_ID, name),
//            VillagerProfessionBuilder.create().id(Identifier(TutorialMod.MOD_ID, name)).workstation(type)
//                .workSound(SoundEvents.ENTITY_VILLAGER_WORK_ARMORER).build()
//        )
//    }
//
//    fun registerPOI(name: String?, block: Block): PointOfInterestType {
//        return PointOfInterestHelper.register(
//            Identifier(TutorialMod.MOD_ID, name),
//            1, 1, ImmutableSet.copyOf(block.getStateManager().getStates())
//        )
//    }
//
//    fun registerVillagers() {
//        TutorialMod.LOGGER.debug("Registering Villagers for " + TutorialMod.MOD_ID)
//    }
//
//    fun registerTrades() {
//        TradeOfferHelper.registerVillagerOffers(
//            JUMP_MASTER, 1
//        ) { factories ->
//            factories.add { entity, random ->
//                TradeOffer(
//                    ItemStack(Items.EMERALD, 3),
//                    ItemStack(ModItems.EGGPLANT, 5),
//                    6, 2, 0.02f
//                )
//            }
//        }
//    }
//}