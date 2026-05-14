package com.chloe.cm.impl.module.tconstruct.client.render.be;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.tconstruct.server.blockentity.SearedBurnerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import vazkii.botania.common.helper.VecHelper;

import java.util.ArrayList;
import java.util.List;

public class SearedBurnerBlockEntityRenderer implements BlockEntityRenderer<SearedBurnerBlockEntity> {
    
    private final List<String> modelTextures = new ArrayList<>();
    
    public SearedBurnerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        modelTextures.add("stone");
        modelTextures.add("cracked_bricks");
        modelTextures.add("cobble");
        modelTextures.add("paver");
        modelTextures.add("bricks");
        modelTextures.add("fancy_bricks");
        modelTextures.add("triangle_bricks");
    }
    
    @Override
    public void render(SearedBurnerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        PartialModel frame = PartialModel.of(CMConstants.modLoc("block/tconstruct/seared_burner/" + modelTextures.get(be.getMode())));
        
        ms.pushPose();
        CachedBuffers.partial(frame, be.getBlockState())
            .light(light)
            .renderInto(ms, buffer.getBuffer(RenderType.solid()));
        ms.popPose();
        
        ms.pushPose();
        
        ms.translate(0.5, 0, 0.5);
        
        Vec3 center = be.getBlockPos().getCenter();
        Vec3 pPos = Minecraft.getInstance().cameraEntity != null ? Minecraft.getInstance().cameraEntity.position() : center;
        
        ms.mulPose(VecHelper.rotateY((float) -Math.toDegrees(Math.atan2(pPos.z - center.z, pPos.x - center.x) + 135 - 45 - 12.75 - 12.75 - 6.375)));
        
        ms.translate(-0.5, 0, -0.5);
        
        BlazeBurnerBlock.HeatLevel heatLevel = be.getHeatLevelForRender();
        float animation = be.headAnimation.getValue(partialTicks) * .175f;
        float horizontalAngle = AngleHelper.rad(be.getHeadAngle().getValue(partialTicks));
        boolean canDrawFlame = heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING);
        boolean drawGoggles = be.goggles;
        PartialModel drawHat = be.hat ? AllPartialModels.TRAIN_HAT : be.stockKeeper ? AllPartialModels.LOGISTICS_HAT : null;
        int hashCode = be.hashCode();
        
        BlazeBurnerRenderer.renderShared(ms, null,
            buffer, be.getLevel(), be.getBlockState(),
            be.getHeatLevelForRender(), animation, horizontalAngle,
            canDrawFlame, drawGoggles, drawHat, hashCode);
        
        ms.popPose();
        
    }
}
