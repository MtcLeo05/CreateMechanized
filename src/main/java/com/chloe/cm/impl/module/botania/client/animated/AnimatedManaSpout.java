package com.chloe.cm.impl.module.botania.client.animated;

import com.chloe.cm.impl.module.botania.client.render.be.ManaSpoutBlockEntityRenderer;
import com.chloe.cm.impl.module.botania.init.CMBotaniaBlocks;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.gui.UIRenderHelper;
import net.createmod.catnip.render.FluidRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.lib.ResourceLocationHelper;

import java.util.List;

public class AnimatedManaSpout extends AnimatedKinetics {
    private final TextureAtlasSprite waterSprite;
    
    public int mana;
    public int scale = 15;
    public boolean showCatalystUnder = false;
    public StateIngredient catalyst = null;
    
    public double mouseX, mouseY;
    
    public AnimatedManaSpout() {
        this.waterSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ResourceLocationHelper.prefix("block/mana_water"));
    }
    
    
    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack ms = graphics.pose();
        ms.pushPose();
        
        ms.translate(xOffset + 2, yOffset + 50, 100);
        ms.mulPose(Axis.XP.rotationDegrees(-15.5f));
        ms.mulPose(Axis.YP.rotationDegrees(22.5f));
        
        blockElement(CMBotaniaBlocks.MANA_SPOUT.get().defaultBlockState())
            .scale(scale)
            .render(graphics);
        
        float cycle = (AnimationTickHolder.getRenderTime() - offset * 8) % 30;
        float squeeze = cycle < 20 ? Mth.sin((float) (cycle / 20f * Math.PI)) : 0;
        squeeze *= 20;
        
        ms.pushPose();
        blockElement(ManaSpoutBlockEntityRenderer.BITS[0])
            .scale(scale)
            .render(graphics);
        
        ms.translate(0, -3 * squeeze / 32f, 0);
        blockElement(ManaSpoutBlockEntityRenderer.BITS[1])
            .scale(scale)
            .render(graphics);
        
        ms.translate(0, -3 * squeeze / 32f, 0);
        blockElement(ManaSpoutBlockEntityRenderer.BITS[2])
            .scale(scale)
            .render(graphics);
        
        ms.translate(0, -3 * squeeze / 32f, 0);
        
        ms.popPose();
        
        blockElement(AllBlocks.DEPOT.getDefaultState())
            .atLocal(0, 2, 0)
            .scale(scale)
            .render(graphics);
        
        BlockState state = catalyst != null? lerpCatalysts(catalyst.getDisplayed(), cycle): null;
        
        if(showCatalystUnder && state != null) {
            blockElement(state)
                .atLocal(0, 3, 0)
                .scale(scale)
                .render(graphics);
        }
        
        AnimatedKinetics.DEFAULT_LIGHTING.applyLighting();
        
        MultiBufferSource.BufferSource buffer = graphics.bufferSource();
        
        ms.pushPose();
        
        UIRenderHelper.flipForGuiRender(ms);
        ms.scale(16, 16, 16);
        
        ms.pushPose();
        float scaleFactor = scale / 20f;
        
        ms.translate(0.025 * scaleFactor, -0.075 * (1 - scaleFactor), -0.075 * (1 - scaleFactor));
        
        float min = 0.15625f * scaleFactor;
        float max = 1.15f * scaleFactor;
        float manaYOffset = 0.6875f * scaleFactor;
        
        ms.mulPose(Axis.XP.rotationDegrees(180));
        ms.translate(0, -0.3455, -1);
        
        VertexConsumer vc = buffer.getBuffer(RenderHelper.MANA_POOL_WATER);
        RenderSystem.enableBlend();
        
        for (Direction side : Iterate.directions) {
            boolean positive = side.getAxisDirection() == Direction.AxisDirection.POSITIVE;
            
            if (side.getAxis().isHorizontal()) {
                FluidRenderHelper.renderStillTiledFace(side, min, min - manaYOffset, max, min, positive ? max : min, vc, ms, LightTexture.FULL_BRIGHT, 0xaaFFFFFF, this.waterSprite);
                continue;
            }
            
            FluidRenderHelper.renderStillTiledFace(side, min, min, max, max, positive ? min : min - manaYOffset, vc, ms, LightTexture.FULL_BRIGHT, 0xaaFFFFFF, this.waterSprite);
        }
        
        RenderSystem.disableBlend();
        
        ms.popPose();
        Lighting.setupFor3DItems();
        ms.popPose();
        ms.popPose();
        
        if(state == null) return;
        
        if(mouseX > 70 && mouseX < 100 && mouseY > 60 && mouseY < 85) {
            graphics.renderTooltip(Minecraft.getInstance().font, state.getBlock().asItem().getDefaultInstance(), (int) mouseX, (int) mouseY);
        }
    }
    
    public BlockState lerpCatalysts(List<BlockState> states, float cycle) {
        if(states == null) return null;
        
        int amount = states.size();
        float cyclesPerState = 30f / amount;
        
        for (int i = 0; i < states.size(); i++) {
            float stateCycle = cyclesPerState * i;
            
            if(cycle >= stateCycle && cyclesPerState <= cyclesPerState * (i+1)) return states.get(i);
        }
        
        return null;
    }
}
