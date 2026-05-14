package com.chloe.cm.impl.module.botania.client.render.be;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.client.model.block.ManaPedestalModel;
import com.chloe.cm.impl.module.botania.server.blockentity.ManaPedestalBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import com.simibubi.create.content.logistics.depot.DepotRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.helper.VecHelper;

import java.util.Random;

public class ManaPedestalBlockEntityRenderer extends SafeBlockEntityRenderer<ManaPedestalBlockEntity> {
    
    private final ManaPedestalModel model;
    private final ResourceLocation texture = CMConstants.modLoc("textures/block/botania/mana_pedestal.png");
    private final ResourceLocation textureAngry = CMConstants.modLoc("textures/block/botania/mana_pedestal_angry.png");
    
    public ManaPedestalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new ManaPedestalModel(context.bakeLayer(ManaPedestalModel.LAYER_LOCATION));
    }
    
    @Override
    protected void renderSafe(ManaPedestalBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        
        VertexConsumer shardVC = buffer.getBuffer(RenderHelper.MANA_PYLON_GLOW);
        VertexConsumer platesVC = buffer.getBuffer(RenderType.entityTranslucent(be.isOverloaded()? textureAngry: texture));
        
        ms.pushPose();
        ms.mulPose(VecHelper.rotateX(180));
        
        ms.pushPose();
        ms.translate(0.5f, -0.65f, -0.5f);
        ms.pushPose();
        
        float worldTime = 0;
        
        if(!be.isOverloaded() && !be.isVirtual()) {
            worldTime = (float) ClientTickHandler.ticksInGame + partialTicks;
            worldTime += (new Random(be.getBlockPos().hashCode())).nextFloat(360);
        }
        
        ms.mulPose(VecHelper.rotateY(worldTime * 1.5F));
        
        ms.pushPose();
        ms.translate(0.0F, (float) Math.sin(-worldTime / (double) 20.0F) * 0.055F, 0.0F);
        model.renderCrystal(ms, shardVC, light, overlay);
        ms.popPose();
        ms.popPose();
        
        ms.pushPose();
        ms.translate(0.0F, (float) Math.sin(worldTime / (double) 20.0F) * 0.05F, 0.0F);
        ms.mulPose(VecHelper.rotateY(worldTime * -1.5F));
        model.renderUpPlates(ms, platesVC, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        ms.translate(0.0F, (float) Math.sin(-worldTime / (double) 20.0F) * 0.05F, 0.0F);
        ms.mulPose(VecHelper.rotateY(worldTime * -2.5F));
        model.renderDownPlates(ms, platesVC, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        ms.mulPose(VecHelper.rotateY(worldTime));
        ms.translate(0.0F, (float) Math.sin(-worldTime / (double) 20.0F) * 0.055F, 0.0F);
        model.renderCore(ms, shardVC, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        ms.translate(0.0F, (float) Math.sin(-worldTime / (double) 20.0F) * 0.055F, 0.0F);
        model.renderPedestal(ms, platesVC, light, overlay);
        ms.popPose();
        
        ms.pushPose();
        ms.mulPose(VecHelper.rotateX(180));
        
        ms.pushPose();
        ms.translate(0.0F, (float) Math.sin(worldTime / (double) 20.0F) * 0.055F, 0.0F);
        ms.translate(-0.5f, -1.2f, -0.5f);
        ms.scale(0.9f, 0.9f, 0.9f);
        DepotRenderer.renderItemsOf(be, partialTicks, ms, buffer, light, overlay, be.getBehaviour(DepotBehaviour.TYPE));
        ms.popPose();
        ms.popPose();
        
        ms.popPose();
        ms.popPose();
    }
}
