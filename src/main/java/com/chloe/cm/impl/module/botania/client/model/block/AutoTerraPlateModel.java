package com.chloe.cm.impl.module.botania.client.model.block;

import com.chloe.cm.CMConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class AutoTerraPlateModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CMConstants.modLoc("auto_terra_plate"), "main");
	private final ModelPart main;
	private final ModelPart frame;
	private final ModelPart plate;
	private final ModelPart gem;
	private final ModelPart floating;
	private final ModelPart living;
	private final ModelPart lapis;
	private final ModelPart terra;

	public AutoTerraPlateModel(ModelPart root) {
		this.main = root.getChild("main");
		this.frame = this.main.getChild("frame");
		this.plate = this.main.getChild("plate");
		this.gem = this.main.getChild("gem");
		this.floating = this.main.getChild("floating");
		this.living = this.floating.getChild("living");
		this.lapis = this.floating.getChild("lapis");
		this.terra = this.floating.getChild("terra");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		
		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 2.125F, 0.0F));
		
		PartDefinition frame = main.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(32, 34).addBox(6.0F, -0.5F, -6.0F, 2.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
			.texOffs(0, 19).addBox(-8.0F, -0.5F, -8.0F, 2.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.625F, 0.0F));
		
		PartDefinition f4_r1 = frame.addOrReplaceChild("f4_r1", CubeListBuilder.create().texOffs(0, 34).addBox(-1.0F, 0.0F, -8.0F, 2.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -0.5F, -7.0F, 0.0F, 1.5708F, 0.0F));
		
		PartDefinition f3_r1 = frame.addOrReplaceChild("f3_r1", CubeListBuilder.create().texOffs(32, 19).addBox(-1.0F, 0.0F, -8.0F, 2.0F, 1.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -0.5F, 7.0F, 0.0F, -1.5708F, 0.0F));
		
		PartDefinition plate = main.addOrReplaceChild("plate", CubeListBuilder.create(), PartPose.offset(0.0F, 1.625F, 0.0F));
		
		PartDefinition p_r1 = plate.addOrReplaceChild("p_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-15.0F, 0.0F, -10.0F, 16.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -2.75F, -7.0F, 0.0F, 1.5708F, 0.0F));
		
		PartDefinition floating = main.addOrReplaceChild("floating", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		
		PartDefinition living = floating.addOrReplaceChild("living", CubeListBuilder.create().texOffs(0, 49).addBox(-4.0F, -1.125F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(0, 49).addBox(2.0F, -1.125F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(0, 49).addBox(-1.0F, -1.125F, 2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(0, 49).addBox(-1.0F, -1.125F, -4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 0.0F));
		
		PartDefinition lapis = floating.addOrReplaceChild("lapis", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, 0.0F));
		
		PartDefinition l4_r1 = lapis.addOrReplaceChild("l4_r1", CubeListBuilder.create().texOffs(16, 49).addBox(-1.0F, -0.125F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.25F, -1.0F, -3.25F, 0.0F, -0.7854F, 0.0F));
		
		PartDefinition l3_r1 = lapis.addOrReplaceChild("l3_r1", CubeListBuilder.create().texOffs(16, 49).addBox(-1.0F, -0.125F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.25F, -1.0F, 3.25F, 0.0F, -0.7854F, 0.0F));
		
		PartDefinition l2_r1 = lapis.addOrReplaceChild("l2_r1", CubeListBuilder.create().texOffs(16, 49).addBox(-1.0F, -0.125F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.25F, -1.0F, 3.25F, 0.0F, -0.7854F, 0.0F));
		
		PartDefinition l1_r1 = lapis.addOrReplaceChild("l1_r1", CubeListBuilder.create().texOffs(16, 49).addBox(-1.0F, -0.125F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -1.0F, -3.25F, 0.0F, -0.7854F, 0.0F));
		
		PartDefinition terra = floating.addOrReplaceChild("terra", CubeListBuilder.create().texOffs(8, 49).addBox(-7.0F, 1.875F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(8, 49).addBox(5.0F, 1.875F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(8, 49).addBox(-1.0F, 1.875F, 5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(8, 49).addBox(-1.0F, 1.875F, -7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		
		PartDefinition gem = main.addOrReplaceChild("gem", CubeListBuilder.create().texOffs(8, 53).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.875F, 0.0F));
		
		return LayerDefinition.create(meshdefinition, 64, 64);
	}
	
	public void renderFrame(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		this.frame.render(ms, buffer, light, overlay);
	}
	
	public void renderPlate(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		this.plate.render(ms, buffer, light, overlay);
	}
	
	public void renderGem(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		this.gem.render(ms, buffer, light, overlay);
	}
	
	public void renderLapis(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		this.lapis.render(ms, buffer, light, overlay);
	}
	
	public void renderLiving(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		this.living.render(ms, buffer, light, overlay);
	}
	
	public void renderTerra(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		this.terra.render(ms, buffer, light, overlay);
	}
}