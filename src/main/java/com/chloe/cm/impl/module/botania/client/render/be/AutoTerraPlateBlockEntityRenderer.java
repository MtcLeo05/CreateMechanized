package com.chloe.cm.impl.module.botania.client.render.be;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.client.model.block.AutoTerraPlateModel;
import com.chloe.cm.impl.module.botania.server.behaviour.AutoTerraPlateBehaviour;
import com.chloe.cm.impl.module.botania.server.blockentity.AutoTerraPlateBlockEntity;
import com.chloe.cm.util.RotationHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.FluidRenderHelper;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.helper.VecHelper;
import vazkii.botania.common.lib.ResourceLocationHelper;

import java.util.List;
import java.util.Random;

public class AutoTerraPlateBlockEntityRenderer extends KineticBlockEntityRenderer<AutoTerraPlateBlockEntity> {
    private final AutoTerraPlateModel model;
    private final ResourceLocation texture = CMConstants.modLoc("textures/item/auto_terra_plate.png");
    private final TextureAtlasSprite waterSprite;
    
    public AutoTerraPlateBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.model = new AutoTerraPlateModel(context.bakeLayer(AutoTerraPlateModel.LAYER_LOCATION));
        this.waterSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ResourceLocationHelper.prefix("block/mana_water"));
    }
    
    @Override
    protected void renderSafe(AutoTerraPlateBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState state = getRenderedBlockState(be);
        RenderType type = getRenderType(be, state);
        renderRotatingBuffer(be, getRotatedModel(be, state), ms, buffer.getBuffer(type), light);
        
        VertexConsumer platesVC = buffer.getBuffer(RenderType.entityTranslucent(texture));
        
        ms.pushPose();
        ms.mulPose(VecHelper.rotateX(180));
        
        if (be.getTransitional() > 0) {
            renderTransitional(be, partialTicks, ms, buffer, light, overlay);
        } else if (be.active) {
            renderActive(be, partialTicks, ms, buffer, light, overlay);
        } else {
            renderInactive(be, partialTicks, ms, buffer, light, overlay);
        }
        
        ms.translate(0.5, 0.072f, -0.5);
        
        ms.pushPose();
        model.renderFrame(ms, platesVC, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        ms.translate(0, -0.25, 0);
        model.renderPlate(ms, platesVC, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        ms.translate(0, -0.495, 0);
        float angleForBe = KineticBlockEntityRenderer.getAngleForBe(be, be.getBlockPos(), Direction.Axis.Y);
        ms.mulPose(VecHelper.rotateY((float) Math.toDegrees(-angleForBe)));
        model.renderGem(ms, platesVC, light, overlay);
        ms.popPose();
        
        ms.popPose();
        
        renderManaBubble(be, partialTicks, ms, buffer, light);
        
        if (be.processingTicks == -1) return;
        
        AutoTerraPlateBehaviour behaviour = (AutoTerraPlateBehaviour) be.getBehaviour(AutoTerraPlateBehaviour.TYPE);
        List<BlockPos> pos = behaviour.getUsedPeds();
        
        float speedMultiplier = Math.abs(be.getSpeed()) / IRotate.SpeedLevel.FAST.getSpeedValue();
        if (speedMultiplier <= 0) speedMultiplier = 1;
        float progress = (40.0f - be.processingTicks + partialTicks * speedMultiplier) / 40.0f;
        progress = Mth.clamp(progress, 0.0f, 1.0f);
        
        BlockPos bePos = be.getBlockPos();
        
        Level level = be.getLevel();
        
        for (BlockPos pedPos : pos) {
            double startX = pedPos.getX() + 0.5;
            double startY = pedPos.getY() + 0.75;
            double startZ = pedPos.getZ() + 0.5;
            
            double targetX = bePos.getX() + 0.5;
            double targetY = bePos.getY() + 1.25; // Adjusted destination target to 1 block above
            double targetZ = bePos.getZ() + 0.5;
            
            double currentWorldX, currentWorldY, currentWorldZ;
            
            if (progress < 0.5f) { // Simplified animation steps
                float p = progress / 0.5f;
                currentWorldX = startX;
                currentWorldY = Mth.lerp(p, startY, startY + 1.0);
                currentWorldZ = startZ;
            } else {
                float p = (progress - 0.5f) / 0.5f;
                currentWorldX = Mth.lerp(p, startX, targetX);
                currentWorldY = Mth.lerp(p, startY + 1.0, targetY);
                currentWorldZ = Mth.lerp(p, startZ, targetZ);
            }
            
            float startR = 0.0F;
            float startG = 0.7764706F;
            float startB = 1.0F;
            
            float endR = 0.415686F;
            float endG = 0.909804F;
            float endB = 0.384314F;
            
            float currentR = Mth.lerp(progress, startR, endR);
            float currentG = Mth.lerp(progress, startG, endG);
            float currentB = Mth.lerp(progress, startB, endB);
            
            int particleCount = 3 + (int) (Math.random() * 3);
            
            if (Math.random() < 0.75 * speedMultiplier) {
                for (int i = 0; i < particleCount; i++) {
                    WispParticleData data = WispParticleData.wisp((float) Math.random() / 5.0F, currentR, currentG, currentB, 1F);
                    
                    double pX = currentWorldX + (Math.random() - 0.5) * 0.3;
                    double pY = currentWorldY + (Math.random() - 0.5) * 0.3;
                    double pZ = currentWorldZ + (Math.random() - 0.5) * 0.3;
                    
                    double velY = (float) Math.random() / 25.0F;
                    
                    if (level != null) {
                        level.addParticle(data, pX, pY, pZ, 0.0D, velY, 0.0D);
                    }
                }
            }
        }
        
        if (!be.active || be.getTransitional() != 0) return;
        
        if (progress >= 0.85f) {
            if (Math.random() < 0.75 * speedMultiplier) {
                int burstCount = 15 + (int) (Math.random() * 6);
                
                for (int i = 0; i < burstCount; i++) {
                    float colorMix = (float) Math.random();
                    
                    float r = Mth.lerp(colorMix, 0.0F, 0.415686F);
                    float g = Mth.lerp(colorMix, 0.7764706F, 0.909804F);
                    float b = Mth.lerp(colorMix, 1.0F, 0.384314F);
                    
                    WispParticleData burstData = WispParticleData.wisp((float) Math.random() / 3.0F, r, g, b, 0.5F);
                    
                    double pX = bePos.getX() + 0.5;
                    double pY = bePos.getY() + 1.25; 
                    double pZ = bePos.getZ() + 0.5;
                    
                    double vX = (Math.random() - 0.5) * 0.2;
                    double vY = (Math.random() * 0.15) + 0.05;
                    double vZ = (Math.random() - 0.5) * 0.2;
                    
                    if (level != null) {
                        level.addParticle(burstData, pX, pY, pZ, vX, vY, vZ);
                    }
                }
            }
        }
    }
    
    private void renderManaBubble(AutoTerraPlateBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light) {
        if (be.getCurrentMana() == 0) return;
        
        float scale = be.getCurrentMana() / (float) be.getMaxMana();
        
        VertexConsumer manaVC = buffer.getBuffer(RenderHelper.MANA_POOL_WATER);
        
        ms.pushPose();
        
        float manaTime = (float) ClientTickHandler.ticksInGame + partialTicks;
        manaTime += (new Random(be.getBlockPos().hashCode())).nextFloat(360);
        manaTime %= 360;
        
        ms.translate(0.5f, 0.75f, 0.5f);
        ms.scale(scale, scale, scale);
        ms.mulPose(RotationHelper.rotateAxis(new Vec3(0, 0, 1), 45));
        ms.mulPose(RotationHelper.rotateAxis(new Vec3(1, 0, 0), 45));
        ms.mulPose(RotationHelper.rotateAxis(new Vec3(0, 1, 0), manaTime));
        
        float minX = -0.0625f;
        float minY = -0.0625f;
        float minZ = -0.0625f;
        float maxX = 0.0625f;
        float maxY = 0.0625f;
        float maxZ = 0.0625f;
        
        RenderSystem.enableBlend();
        
        for (Direction side : Iterate.directions) {
            boolean positive = side.getAxisDirection() == Direction.AxisDirection.POSITIVE;
            
            switch (side.getAxis()) {
                case X:
                    FluidRenderHelper.renderStillTiledFace(side, minZ, minY, maxZ, maxY, positive ? maxX : minX, manaVC, ms, light, 0xFFFFFFFF, this.waterSprite);
                    break;
                
                case Z:
                    FluidRenderHelper.renderStillTiledFace(side, minX, minY, maxX, maxY, positive ? maxZ : minZ, manaVC, ms, light, 0xFFFFFFFF, this.waterSprite);
                    break;
                
                case Y:
                    FluidRenderHelper.renderStillTiledFace(side, minX, minZ, maxX, maxZ, positive ? maxY : minY, manaVC, ms, light, 0xFFFFFFFF, this.waterSprite);
                    break;
            }
        }
        
        RenderSystem.disableBlend();
        ms.popPose();
    }
    
    private void renderActive(AutoTerraPlateBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        VertexConsumer platesVC = buffer.getBuffer(RenderType.entityTranslucent(texture));
        
        float worldTime;
        
        if (be.isVirtual()) worldTime = 0;
        else {
            worldTime = (float) ClientTickHandler.ticksInGame + partialTicks;
            worldTime += (new Random(be.getBlockPos().hashCode())).nextFloat(360);
            worldTime %= 360;
            worldTime *= (Math.abs(be.getSpeed()) / IRotate.SpeedLevel.FAST.getSpeedValue()) * 8;
        }
        
        renderRotatingLapis(worldTime, ms, platesVC, light, overlay);
        renderRotatingLiving(worldTime, ms, platesVC, light, overlay);
        renderRotatingTerra(worldTime, ms, platesVC, light, overlay);
    }
    
    private void renderInactive(AutoTerraPlateBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        VertexConsumer platesVC = buffer.getBuffer(RenderType.entityTranslucent(texture));
        
        ms.pushPose();
        ms.translate(0.5f, -0.5f, -0.5f);
        model.renderLiving(ms, platesVC, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        ms.translate(0.5f, -0.5f, -0.5f);
        model.renderLapis(ms, platesVC, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        ms.translate(0.5f, -0.5f, -0.5f);
        model.renderTerra(ms, platesVC, light, overlay);
        ms.popPose();
    }
    
    private void renderTransitional(AutoTerraPlateBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        VertexConsumer platesVC = buffer.getBuffer(RenderType.entityTranslucent(texture));
        
        float maxTransitional = 20f;
        
        float transitionTime = be.getTransitional() - partialTicks;
        if (transitionTime < 0) transitionTime = 0;
        
        float progress;
        if (be.active) {
            progress = 1.0f - (transitionTime / maxTransitional);
        } else {
            progress = transitionTime / maxTransitional;
        }
        
        float worldTime = (float) ClientTickHandler.ticksInGame + partialTicks;
        worldTime += (new Random(be.getBlockPos().hashCode())).nextFloat(360);
        worldTime %= 360;
        worldTime *= (Math.abs(be.getSpeed()) / IRotate.SpeedLevel.FAST.getSpeedValue()) * 8;
        
        float baseSpeedDegrees = 8.0f;
        float vT = baseSpeedDegrees * maxTransitional;
        
        float offset = vT * (progress - 0.5f * progress * progress - 0.5f);
        
        ms.pushPose();
        float lapisX = Mth.lerp(progress, 0.5f, 0.63f);
        float lapisY = Mth.lerp(progress, -0.5f, -0.75f);
        float lapisZ = Mth.lerp(progress, -0.5f, -0.63f);
        ms.translate(lapisX, lapisY, lapisZ);
        
        if (progress > 0) {
            float lapisRotX = Mth.lerp(progress, 0f, 90f);
            float lapisRotZ = Mth.lerp(progress, 0f, 45f);
            float lapisRotY = worldTime - offset;
            
            ms.mulPose(RotationHelper.rotateAxis(new Vec3(1, 0, 0), lapisRotX));
            ms.mulPose(RotationHelper.rotateAxis(new Vec3(0, 0, 1), lapisRotZ));
            ms.mulPose(RotationHelper.rotateAxis(new Vec3(0, 1, 0), lapisRotY));
        }
        ms.pushPose();
        model.renderLapis(ms, platesVC, light, overlay);
        ms.popPose();
        ms.popPose();
        
        ms.pushPose();
        float livingX = Mth.lerp(progress, 0.5f, 0.3675f);
        float livingY = Mth.lerp(progress, -0.5f, -0.75f);
        float livingZ = Mth.lerp(progress, -0.5f, -0.63f);
        ms.translate(livingX, livingY, livingZ);
        
        if (progress > 0) {
            float livingRotX = Mth.lerp(progress, 0f, 90f);
            float livingRotZ = Mth.lerp(progress, 0f, -45f);
            float livingRotY = worldTime - offset;
            
            ms.mulPose(RotationHelper.rotateAxis(new Vec3(1, 0, 0), livingRotX));
            ms.mulPose(RotationHelper.rotateAxis(new Vec3(0, 0, 1), livingRotZ));
            ms.mulPose(RotationHelper.rotateAxis(new Vec3(0, 1, 0), livingRotY));
        }
        ms.pushPose();
        model.renderLiving(ms, platesVC, light, overlay);
        ms.popPose();
        ms.popPose();
        
        ms.pushPose();
        float terraX = 0.5f;
        float terraY = Mth.lerp(progress, -0.5f, -0.85f);
        float terraZ = Mth.lerp(progress, -0.5f, -0.5f);
        ms.translate(terraX, terraY, terraZ);
        
        if (progress > 0) {
            float terraRotY = worldTime - offset;
            
            ms.mulPose(RotationHelper.rotateAxis(new Vec3(0, 1, 0), terraRotY));
        }
        
        ms.pushPose();
        model.renderTerra(ms, platesVC, light, overlay);
        ms.popPose();
        ms.popPose();
    }
    
    private void renderRotatingTerra(float worldTime, PoseStack ms, VertexConsumer vc, int light, int overlay) {
        ms.pushPose();
        ms.translate(0.5f, -0.85f, -0.5);
        
        ms.mulPose(RotationHelper.rotateAxis(new Vec3(0, 1, 0), worldTime));
        
        ms.pushPose();
        model.renderTerra(ms, vc, light, overlay);
        
        ms.popPose();
        ms.popPose();
    }
    
    private void renderRotatingLapis(float worldTime, PoseStack ms, VertexConsumer vc, int light, int overlay) {
        ms.pushPose();
        ms.translate(0.63, -0.75f, -0.63);
        ms.mulPose(RotationHelper.rotateAxis(new Vec3(1, 0, 0), 90));
        ms.mulPose(RotationHelper.rotateAxis(new Vec3(0, 0, 1), 45));
        ms.mulPose(RotationHelper.rotateAxis(new Vec3(0, 1, 0), worldTime));
        
        ms.pushPose();
        model.renderLapis(ms, vc, light, overlay);
        
        ms.popPose();
        ms.popPose();
    }
    
    private void renderRotatingLiving(float worldTime, PoseStack ms, VertexConsumer vc, int light, int overlay) {
        ms.pushPose();
        ms.translate(0.3675, -0.75f, -0.63);
        ms.mulPose(RotationHelper.rotateAxis(new Vec3(1, 0, 0), 90));
        ms.mulPose(RotationHelper.rotateAxis(new Vec3(0, 0, 1), -45));
        ms.mulPose(RotationHelper.rotateAxis(new Vec3(0, 1, 0), worldTime));
        
        ms.pushPose();
        model.renderLiving(ms, vc, light, overlay);
        
        ms.popPose();
        ms.popPose();
    }
    
    @Override
    protected BlockState getRenderedBlockState(AutoTerraPlateBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }
    
    @Override
    protected SuperByteBuffer getRotatedModel(AutoTerraPlateBlockEntity be, BlockState state) {
        return CachedBuffers.partialFacing(block("shaft_tiny"), state, Direction.DOWN);
    }
    
    private static PartialModel block(String path) {
        return PartialModel.of(CMConstants.modLoc("block/" + path));
    }
}
