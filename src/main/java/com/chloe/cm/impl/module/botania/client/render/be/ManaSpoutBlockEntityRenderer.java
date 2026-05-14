package com.chloe.cm.impl.module.botania.client.render.be;

import com.chloe.cm.CMConstants;
import com.chloe.cm.impl.module.botania.server.blockentity.ManaSpoutBlockEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.FluidRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.lib.ResourceLocationHelper;

public class ManaSpoutBlockEntityRenderer extends SafeBlockEntityRenderer<ManaSpoutBlockEntity> {
    
    private final TextureAtlasSprite waterSprite;
    
    public ManaSpoutBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.waterSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ResourceLocationHelper.prefix("block/mana_water"));
    }
    
    public static final PartialModel[] BITS =
        {
            block("spout/top"),
            block("spout/middle"),
            block("spout/bottom")
        };
    
    @Override
    protected void renderSafe(ManaSpoutBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        
        int currentMana = be.getCurrentMana();
        int maxMana = be.getMaxMana();
        float manaLevel = (float) currentMana / (float) maxMana;
        
        float min = 0.15625f;
        float max = 0.84375f;
        float yOffset = (0.6875f) * manaLevel;
        
        ms.pushPose();
        
        if (manaLevel > 0.0F) {
            ms.mulPose(Axis.XP.rotationDegrees(180));
            ms.translate(0, -0.3455, -1);
            
            VertexConsumer vc = buffer.getBuffer(RenderHelper.MANA_POOL_WATER);
            RenderSystem.enableBlend();
            
            for (Direction side : Iterate.directions) {
                boolean positive = side.getAxisDirection() == Direction.AxisDirection.POSITIVE;
                
                if (side.getAxis().isHorizontal()) {
                    FluidRenderHelper.renderStillTiledFace(side, min, min - yOffset, max, min, positive ? max : min, vc, ms, light, 0xFFFFFFFF, this.waterSprite);
                    continue;
                }
                
                FluidRenderHelper.renderStillTiledFace(side, min, min, max, max, positive ? min : min - yOffset, vc, ms, light, 0xFFFFFFFF, this.waterSprite);
            }
            
            
            RenderSystem.disableBlend();
        }
        
        ms.popPose();
        
        int processingTicks = be.processingTicks;
        float processingPT = processingTicks - partialTicks;
        float processingProgress = 1 - (processingPT - 5) / 10;
        processingProgress = Mth.clamp(processingProgress, 0, 1);
        
        float radius = 0;
        
        if (processingTicks != -1) {
            radius = (float) (Math.pow(((2 * processingProgress) - 1), 2) - 1);
        }
        
        float squeeze = radius;
        if (processingPT < 0)
            squeeze = 0;
        else if (processingPT < 2)
            squeeze = Mth.lerp(processingPT / 2f, 0, -1);
        else if (processingPT < 10)
            squeeze = -1;
        
        ms.pushPose();
        for (PartialModel bit : BITS) {
            CachedBuffers.partial(bit, be.getBlockState())
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.solid()));
            ms.translate(0, -3 * squeeze / 32f, 0);
        }
        ms.popPose();
    }
    
    private static PartialModel block(String path) {
        return PartialModel.of(CMConstants.modLoc("block/" + path));
    }
}
