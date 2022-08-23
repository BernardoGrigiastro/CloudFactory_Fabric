package com.kenza.cloud.mixin;


import com.kenza.cloud.block.Blocks;
import com.kenza.cloud.block.clouds.CloudAttribute;
import com.kenza.cloud.block.clouds.CloudBlock;
import com.kenza.cloud.block.clouds.CloudStairs;
import com.kenza.cloud.item.CloudBlockItem;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.edenring.items.BalloonMushroomBlockItem;
import paulevs.edenring.registries.EdenBlocks;

import static com.kenza.cloud.CloudFactoryMod.MOD_ID;


@Mixin({WorldRenderer.class})
public class WorldRendererMixin {

    @Final
    @Shadow
    private MinecraftClient client;

    @Final
    @Shadow
    private BufferBuilderStorage bufferBuilders;

    @Shadow
    protected static void drawCuboidShapeOutline(MatrixStack poseStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double d, double e, double f, float g, float h, float i, float j) {
    }

    private static final Identifier FRAME = new Identifier(MOD_ID, "textures/environment/frame.png");
    private static BufferBuilder bufferBuilder;

    public WorldRendererMixin() {
    }

    @Inject(
            method = {"<init>*"},
            at = {@At("TAIL")}
    )
    private void eden_onRendererInit(MinecraftClient client, EntityRenderDispatcher entityRenderDispatcher, BlockEntityRenderDispatcher blockEntityRenderDispatcher, BufferBuilderStorage bufferBuilders, CallbackInfo info) {
        bufferBuilder = Tessellator.getInstance().getBuffer();
    }


    @Inject(
            method = {"render"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;getModelViewStack()Lnet/minecraft/client/util/math/MatrixStack;",
                    shift = At.Shift.BEFORE
            )}
    )
    public void inject_renderCloudFrame(MatrixStack poseStack, float f, long l, boolean bl, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightTexture, Matrix4f matrix4f, CallbackInfo info) {
        this.renderCloudFrame(poseStack, camera);
    }

    private void renderCloudFrame(MatrixStack poseStack, Camera camera) {
        ItemStack item = this.client.player.getMainHandStack();

        if (this.client.crosshairTarget != null && this.client.crosshairTarget instanceof BlockHitResult) {
            if (item == null || item.getItem() instanceof CloudBlockItem) {

                CloudBlockItem cloudBlockItem = (CloudBlockItem) item.getItem();


                BlockPos pos = ((BlockHitResult)this.client.crosshairTarget).getBlockPos();
                BlockState state = this.client.world.getBlockState(pos);
                if (state.getMaterial().isReplaceable()) {
//                    state = Blocks.CLOUD_GENERATOR_BLOCK.getDefaultState();
                    state = cloudBlockItem.getBlock().getDefaultState();

                    VertexConsumerProvider.Immediate bufferSource = this.bufferBuilders.getEntityVertexConsumers();
                    VertexConsumer consumer = bufferSource.getBuffer(RenderLayer.getLines());
                    Vec3d camPos = camera.getPos();
                    drawCuboidShapeOutline(poseStack, consumer, state.getOutlineShape(this.client.world, pos, ShapeContext.of(camera.getFocusedEntity())), (double)pos.getX() - camPos.getX(), (double)pos.getY() - camPos.getY(), (double)pos.getZ() - camPos.getZ(), 0.4019F, 0.8749F, 1.F, 0.5F);
                }
            }

        }

    }
}

