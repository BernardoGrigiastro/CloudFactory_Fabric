package com.kenza.cloud.mixin;


import com.kenza.cloud.block.clouds.CloudAttribute;
import com.kenza.cloud.block.clouds.CloudBlock;
import com.kenza.cloud.block.clouds.CloudStairs;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.kenza.cloud.CloudFactoryMod.MOD_ID;


@Mixin({WorldRenderer.class})
public class WorldRendererMixin {

    @Final
    @Shadow
    private MinecraftClient client;


    private static final Identifier FRAME = new Identifier(MOD_ID, "textures/environment/frame.png");
    private static BufferBuilder bufferBuilder;

    public WorldRendererMixin() {
    }

    @Inject(
            method = {"<init>*"},
            at = {@At("TAIL")}
    )
    private void eden_onRendererInit(MinecraftClient client, BufferBuilderStorage bufferBuilders, CallbackInfo info) {
        bufferBuilder = Tessellator.getInstance().getBuffer();
    }


    @Inject(
            method = {"render"},
            at = {@At("TAIL")}
    )
    public void inject_renderCloudFrame(MatrixStack poseStack, float f, long l, boolean bl, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightTexture, Matrix4f matrix4f, CallbackInfo info) {
        this.renderCloudFrame(poseStack, camera);
    }

    private void renderCloudFrame(MatrixStack poseStack, Camera camera) {
        ItemStack item = this.client.player.getMainHandStack();

        if (this.client.crosshairTarget != null && this.client.crosshairTarget instanceof BlockHitResult) {

            boolean canShow = false;

            try {
                Block block = ((BlockItem) item.getItem()).getBlock();
                canShow = block instanceof CloudAttribute;
            }catch (Throwable e){
//                e.printStackTrace();
            }

            if (canShow){
                poseStack.push();
                BlockHitResult bnr = (BlockHitResult) this.client.crosshairTarget;
                if (this.client.world.getBlockState(bnr.getBlockPos()).isAir()) {
                    double dx = (double) bnr.getBlockPos().getX();
                    double dy = (double) bnr.getBlockPos().getY();
                    double dz = (double) bnr.getBlockPos().getZ();
                    dx -= camera.getPos().x;
                    dy -= camera.getPos().y;
                    dz -= camera.getPos().z;
                    poseStack.translate(dx, dy, dz);
                    Matrix4f matrix = poseStack.peek().getPositionMatrix();
                    RenderSystem.enableTexture();
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderTexture(0, FRAME);
                    bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
                    bufferBuilder.vertex(matrix, -0.001F, -0.001F, -0.001F).texture(0.0F, 0.0F).next();
                    bufferBuilder.vertex(matrix, 1.001F, -0.001F, -0.001F).texture(1.0F, 0.0F).next();
                    bufferBuilder.vertex(matrix, 1.001F, -0.001F, 1.001F).texture(1.0F, 1.0F).next();
                    bufferBuilder.vertex(matrix, -0.001F, -0.001F, 1.001F).texture(0.0F, 1.0F).next();
                    bufferBuilder.vertex(matrix, -0.001F, 1.001F, 1.001F).texture(0.0F, 1.0F).next();
                    bufferBuilder.vertex(matrix, 1.001F, 1.001F, 1.001F).texture(1.0F, 1.0F).next();
                    bufferBuilder.vertex(matrix, 1.001F, 1.001F, -0.001F).texture(1.0F, 0.0F).next();
                    bufferBuilder.vertex(matrix, -0.001F, 1.001F, -0.001F).texture(0.0F, 0.0F).next();
                    bufferBuilder.vertex(matrix, 1.001F, -0.001F, -0.001F).texture(0.0F, 0.0F).next();
                    bufferBuilder.vertex(matrix, 1.001F, 1.001F, -0.001F).texture(1.0F, 0.0F).next();
                    bufferBuilder.vertex(matrix, 1.001F, 1.001F, 1.001F).texture(1.0F, 1.0F).next();
                    bufferBuilder.vertex(matrix, 1.001F, -0.001F, 1.001F).texture(0.0F, 1.0F).next();
                    bufferBuilder.vertex(matrix, -0.001F, -0.001F, 1.001F).texture(0.0F, 1.0F).next();
                    bufferBuilder.vertex(matrix, -0.001F, 1.001F, 1.001F).texture(1.0F, 1.0F).next();
                    bufferBuilder.vertex(matrix, -0.001F, 1.001F, -0.001F).texture(1.0F, 0.0F).next();
                    bufferBuilder.vertex(matrix, -0.001F, -0.001F, -0.001F).texture(0.0F, 0.0F).next();
                    bufferBuilder.vertex(matrix, -0.001F, -0.001F, 1.001F).texture(0.0F, 0.0F).next();
                    bufferBuilder.vertex(matrix, 1.001F, -0.001F, 1.001F).texture(1.0F, 0.0F).next();
                    bufferBuilder.vertex(matrix, 1.001F, 1.001F, 1.001F).texture(1.0F, 1.0F).next();
                    bufferBuilder.vertex(matrix, -0.001F, 1.001F, 1.001F).texture(0.0F, 1.0F).next();
                    bufferBuilder.vertex(matrix, -0.001F, 1.001F, -0.001F).texture(0.0F, 1.0F).next();
                    bufferBuilder.vertex(matrix, 1.001F, 1.001F, -0.001F).texture(1.0F, 1.0F).next();
                    bufferBuilder.vertex(matrix, 1.001F, -0.001F, -0.001F).texture(1.0F, 0.0F).next();
                    bufferBuilder.vertex(matrix, -0.001F, -0.001F, -0.001F).texture(0.0F, 0.0F).next();
                    bufferBuilder.end();
                    BufferRenderer.draw(bufferBuilder);
                    poseStack.pop();
                }
            }

        }

    }
}

