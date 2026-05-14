package com.chloe.cm.impl.module.botania.client.render.be;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.client.model.block.TradingBaseModel;
import com.chloe.cm.impl.module.botania.server.behaviour.TradingRiftBehaviour;
import com.chloe.cm.impl.module.botania.server.blockentity.TradingRiftBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.helper.VecHelper;

import java.util.List;

public class TradingRiftBlockEntityRenderer extends KineticBlockEntityRenderer<TradingRiftBlockEntity> {
    private final TradingBaseModel model;
    private final ResourceLocation texture = CMConstants.modLoc("textures/item/trading_base.png");
    
    public TradingRiftBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.model = new TradingBaseModel(context.bakeLayer(TradingBaseModel.LAYER_LOCATION));
    }
    
    @Override
    protected void renderSafe(TradingRiftBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState state = getRenderedBlockState(be);
        RenderType type = getRenderType(be, state);
        
        ms.pushPose();
        ms.translate(0, 0.0f, 0);
        renderRotatingBuffer(be, getRotatedModel(be, state), ms, buffer.getBuffer(type), light);
        ms.popPose();
        
        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(texture));
        
        ms.pushPose();
        ms.mulPose(VecHelper.rotateX(180));
        
        ms.translate(0.5f, -0.1925f, -0.5f);
        
        ms.pushPose();
        model.renderFrame(ms, vc, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        model.renderPlate(ms, vc, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        float angleForBe = KineticBlockEntityRenderer.getAngleForBe(be, be.getBlockPos(), Direction.Axis.Y);
        ms.mulPose(VecHelper.rotateY((float) Math.toDegrees(angleForBe)));
        model.renderGem(ms, vc, light, overlay);
        ms.popPose();
        
        if (be.getTransitional() > 0) {
            renderTransitional(be, partialTicks, ms, buffer, light, overlay);
        } else if (be.active) {
            renderActive(be, partialTicks, ms, buffer, vc, light, overlay);
        } else {
            renderInactive(be, partialTicks, ms, vc, light, overlay);
        }
        
        ms.popPose();
        
        if (be.processingTicks == -1) return;
        
        List<BlockPos> inputPeds = be.inputPeds;
        List<BlockPos> outputPeds = be.tradePeds;
        
        float speedMultiplier = Math.max(1f, Math.abs(be.getSpeed()) / IRotate.SpeedLevel.FAST.getSpeedValue());
        
        float progress = (40.0f - be.processingTicks + partialTicks * (Math.abs(be.getSpeed()) / IRotate.SpeedLevel.FAST.getSpeedValue())) / 40.0f;
        progress = Mth.clamp(progress, 0.0f, 1.0f);
        
        BlockPos bePos = be.getBlockPos();
        double centerX = bePos.getX() + 0.5;
        double centerY = bePos.getY() + 1.25;
        double centerZ = bePos.getZ() + 0.5;
        
        float blueR = 0.0F;
        float blueG = 0.7764706F;
        float blueB = 1.0F;
        
        float greenR = 0.415686F;
        float greenG = 0.909804F;
        float greenB = 0.384314F;
        
        if (progress < 0.35f) {
            float p = progress / 0.35f;
            
            for (BlockPos pedPos : inputPeds) {
                double startX = pedPos.getX() + 0.5;
                double startY = pedPos.getY() + 0.75;
                double startZ = pedPos.getZ() + 0.5;
                
                double currentWorldX, currentWorldY, currentWorldZ;
                
                if (p < 0.5f) {
                    float subP = p / 0.5f;
                    currentWorldX = startX;
                    currentWorldY = Mth.lerp(subP, startY, startY + 1.0);
                    currentWorldZ = startZ;
                } else {
                    float subP = (p - 0.5f) / 0.5f;
                    currentWorldX = Mth.lerp(subP, startX, centerX);
                    currentWorldY = Mth.lerp(subP, startY + 1.0, centerY);
                    currentWorldZ = Mth.lerp(subP, startZ, centerZ);
                }
                
                if (Math.random() < 0.75f * speedMultiplier) {
                    int particleCount = 1 + (int) (Math.random() * 2);
                    for (int i = 0; i < particleCount; i++) {
                        WispParticleData data = WispParticleData.wisp((float) Math.random() / 5.0F, blueR, blueG, blueB, 1F);
                        double pX = currentWorldX + (Math.random() - 0.5) * 0.3;
                        double pY = currentWorldY + (Math.random() - 0.5) * 0.3;
                        double pZ = currentWorldZ + (Math.random() - 0.5) * 0.3;
                        double velY = (float) Math.random() / 25.0F;
                        
                        if (be.getLevel() != null) {
                            be.getLevel().addParticle(data, pX, pY, pZ, 0.0D, velY, 0.0D);
                        }
                    }
                }
            }
        }
        
        if (progress >= 0.35f && progress < 0.45f) {
            int burstCount = (int) (4 * speedMultiplier) + 1;
            for (int i = 0; i < burstCount; i++) {
                WispParticleData burstData = WispParticleData.wisp((float) Math.random() / 3.0F + 0.15F, blueR, blueG, blueB, 1F);
                double vX = (Math.random() - 0.5) * 0.2;
                double vY = (Math.random() * 0.15) + 0.05;
                double vZ = (Math.random() - 0.5) * 0.2;
                
                if (be.getLevel() != null) {
                    be.getLevel().addParticle(burstData, centerX, centerY, centerZ, vX, vY, vZ);
                }
            }
        }
        
        if (progress >= 0.45f && progress < 0.55f) {
            int implodeCount = (int) (4 * speedMultiplier) + 1;
            for (int i = 0; i < implodeCount; i++) {
                WispParticleData implodeData = WispParticleData.wisp((float) Math.random() / 3.0F + 0.15F, greenR, greenG, greenB, 1F);
                
                double angleY = Math.random() * Math.PI * 2;
                double anglePitch = (Math.random() - 0.5) * Math.PI;
                double radius = 1.0 + Math.random() * 0.5;
                
                double dX = Math.cos(angleY) * Math.cos(anglePitch) * radius;
                double dY = Math.sin(anglePitch) * radius;
                double dZ = Math.sin(angleY) * Math.cos(anglePitch) * radius;
                
                double pX = centerX + dX;
                double pY = centerY + dY;
                double pZ = centerZ + dZ;
                
                double speed = 0.1 + Math.random() * 0.05;
                double vX = -dX * speed;
                double vY = -dY * speed;
                double vZ = -dZ * speed;
                
                if (be.getLevel() != null) {
                    be.getLevel().addParticle(implodeData, pX, pY, pZ, vX, vY, vZ);
                }
            }
        }
        
        if (progress >= 0.55f) {
            float p = (progress - 0.55f) / 0.45f;
            
            for (BlockPos pedPos : outputPeds) {
                double targetX = pedPos.getX() + 0.5;
                double targetY = pedPos.getY() + 0.75;
                double targetZ = pedPos.getZ() + 0.5;
                
                double currentWorldX, currentWorldY, currentWorldZ;
                
                if (p < 0.5f) {
                    float subP = p / 0.5f;
                    currentWorldX = targetX;
                    currentWorldY = Mth.lerp(subP, targetY + 1.0, targetY);
                    currentWorldZ = targetZ;
                } else {
                    float subP = (p - 0.5f) / 0.5f;
                    currentWorldX = Mth.lerp(subP, centerX, targetX);
                    currentWorldY = Mth.lerp(subP, centerY, targetY + 1.0);
                    currentWorldZ = Mth.lerp(subP, centerZ, targetZ);
                }
                
                if (Math.random() < 0.75f * speedMultiplier) {
                    int particleCount = 1 + (int) (Math.random() * 2);
                    for (int i = 0; i < particleCount; i++) {
                        WispParticleData data = WispParticleData.wisp((float) Math.random() / 5.0F, greenR, greenG, greenB, 1F);
                        double pX = currentWorldX + (Math.random() - 0.5) * 0.3;
                        double pY = currentWorldY + (Math.random() - 0.5) * 0.3;
                        double pZ = currentWorldZ + (Math.random() - 0.5) * 0.3;
                        double velY = (float) Math.random() / 25.0F;
                        
                        if (be.getLevel() != null) {
                            be.getLevel().addParticle(data, pX, pY, pZ, 0.0D, velY, 0.0D);
                        }
                    }
                }
            }
        }
    }
    
    private void renderInactive(TradingRiftBlockEntity be, float partialTicks, PoseStack ms, VertexConsumer vc, int light, int overlay) {
        ms.pushPose();
        ms.translate(0, -0.115f, 0);
        
        ms.pushPose();
        model.render1(ms, vc, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        model.render2(ms, vc, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        model.render3(ms, vc, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        model.render4(ms, vc, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        model.render5(ms, vc, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        model.render6(ms, vc, light, overlay);
        ms.popPose();
        
        ms.popPose();
    }
    
    private void renderActive(TradingRiftBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, VertexConsumer vc, int light, int overlay) {
        ms.pushPose();
        Vec3 riftCenter = be.getBlockPos().getCenter();
        Vec3 pPos = Minecraft.getInstance().cameraEntity != null ? Minecraft.getInstance().cameraEntity.position() : riftCenter;
        
        ms.mulPose(VecHelper.rotateY((float) Math.toDegrees(Math.atan2(pPos.z - riftCenter.z, pPos.x - riftCenter.x)) - 90f));
        
        ms.pushPose();
        ms.translate(0, -1.125f, 0.5 / 16f);
        ms.mulPose(VecHelper.rotateX(90));
        
        ms.pushPose();
        ms.translate(-2.325 / 16f, 0, -2 / 16f);
        model.render1(ms, vc, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        ms.translate(2.325 / 16f, 0, 2 / 16f);
        model.render2(ms, vc, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        ms.translate(-2.325 / 16f, 0, 2 / 16f);
        model.render3(ms, vc, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        ms.translate(2.325 / 16f, 0, -2 / 16f);
        model.render4(ms, vc, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        ms.translate(-0.125 / 16f, 0, 6 / 16f);
        model.render5(ms, vc, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        ms.translate(-0.125 / 16f, 0, -6 / 16f);
        model.render6(ms, vc, light, overlay);
        ms.popPose();
        
        ms.popPose();
        
        ms.pushPose();
        ms.translate(-0.5f, -1.875, 0);
        
        float time = (float) ClientTickHandler.ticksInGame + partialTicks;
        int activeAlpha = (int) (178.5f + 25.5f * Math.sin(time * 0.05f));
        
        CachedBuffers.partial(block("trading_rift"), be.getBlockState())
            .light(light)
            .color(255, 255, 255, activeAlpha)
            .renderInto(ms, buffer.getBuffer(RenderType.translucent()));
        
        ms.popPose();
        
        ms.popPose();
    }
    
    private void renderTransitional(TradingRiftBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(texture));
        
        float maxTransitional = 40f;
        float transitionTime = be.getTransitional() - partialTicks;
        if (transitionTime < 0) transitionTime = 0;
        
        float progress;
        if (be.active) {
            progress = 1.0f - (transitionTime / maxTransitional);
        } else {
            progress = transitionTime / maxTransitional;
        }
        
        float pLift = Mth.clamp(progress / 0.4f, 0f, 1f);
        
        Vec3 riftCenter = be.getBlockPos().getCenter();
        Vec3 pPos = Minecraft.getInstance().cameraEntity != null ? Minecraft.getInstance().cameraEntity.position() : riftCenter;
        float targetYaw = (float) Math.toDegrees(Math.atan2(pPos.z - riftCenter.z, pPos.x - riftCenter.x)) - 90f;
        
        float currentYaw = Mth.lerp(pLift, 0f, targetYaw);
        
        ms.pushPose();
        ms.mulPose(VecHelper.rotateY(currentYaw));
        
        ms.pushPose();
        float currentY = Mth.lerp(pLift, -0.115f, -1.125f);
        float currentZ = Mth.lerp(pLift, 0f, 0.5f / 16f);
        float currentRotX = Mth.lerp(pLift, 0f, 90f);
        
        ms.translate(0, currentY, currentZ);
        ms.mulPose(VecHelper.rotateX(currentRotX));
        
        float step1 = Mth.clamp((progress - 0.40f) / 0.07f, 0f, 1f);
        ms.pushPose();
        ms.translate(Mth.lerp(step1, 0f, -2.325f / 16f), 0, Mth.lerp(step1, 0f, -2f / 16f));
        model.render1(ms, vc, light, overlay);
        ms.popPose();
        
        float step2 = Mth.clamp((progress - 0.47f) / 0.07f, 0f, 1f);
        ms.pushPose();
        ms.translate(Mth.lerp(step2, 0f, -2.325f / 16f), 0, Mth.lerp(step2, 0f, 2f / 16f));
        model.render3(ms, vc, light, overlay);
        ms.popPose();
        
        float step3 = Mth.clamp((progress - 0.54f) / 0.07f, 0f, 1f);
        ms.pushPose();
        ms.translate(Mth.lerp(step3, 0f, -0.125 / 16f), 0, Mth.lerp(step3, 0f, 6f / 16f));
        model.render5(ms, vc, light, overlay);
        ms.popPose();
        
        float step4 = Mth.clamp((progress - 0.61f) / 0.07f, 0f, 1f);
        ms.pushPose();
        ms.translate(Mth.lerp(step4, 0f, 2.325f / 16f), 0, Mth.lerp(step4, 0f, 2f / 16f));
        model.render2(ms, vc, light, overlay);
        ms.popPose();
        
        float step5 = Mth.clamp((progress - 0.68f) / 0.07f, 0f, 1f);
        ms.pushPose();
        ms.translate(Mth.lerp(step5, 0f, 2.325f / 16f), 0, Mth.lerp(step5, 0f, -2f / 16f));
        model.render4(ms, vc, light, overlay);
        ms.popPose();
        
        float step6 = Mth.clamp((progress - 0.75f) / 0.07f, 0f, 1f);
        ms.pushPose();
        ms.translate(Mth.lerp(step6, 0f, -0.125 / 16f), 0, Mth.lerp(step6, 0f, -6f / 16f));
        model.render6(ms, vc, light, overlay);
        ms.popPose();
        
        ms.popPose();
        
        if (progress > 0.82f) {
            float riftProgress = Mth.clamp((progress - 0.82f) / 0.18f, 0f, 1f);
            
            ms.pushPose();
            ms.translate(-0.5f, -1.875f, 0);
            
            float flicker = (float) Math.abs(Math.sin(riftProgress * Math.PI * 2.5f));
            float combined = riftProgress + (1.0f - riftProgress) * flicker;
            
            int flickerAlpha = (int) (178f * combined);
            
            CachedBuffers.partial(block("trading_rift"), be.getBlockState())
                .light(light)
                .color(255, 255, 255, flickerAlpha)
                .renderInto(ms, buffer.getBuffer(RenderType.translucent()));
            
            ms.popPose();
        }
        
        ms.popPose();
    }
    
    @Override
    protected BlockState getRenderedBlockState(TradingRiftBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }
    
    @Override
    protected SuperByteBuffer getRotatedModel(TradingRiftBlockEntity be, BlockState state) {
        return CachedBuffers.partialFacing(block("shaft_tiny"), state, Direction.DOWN);
    }
    
    private static PartialModel block(String path) {
        return PartialModel.of(CMConstants.modLoc("block/" + path));
    }
}