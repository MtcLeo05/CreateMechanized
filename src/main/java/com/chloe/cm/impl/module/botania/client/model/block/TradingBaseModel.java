package com.chloe.cm.impl.module.botania.client.model.block;

import com.chloe.cm.CMConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class TradingBaseModel {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CMConstants.modLoc("trading_base"), "main");
	
	private final ModelPart main;
	private final ModelPart frame;
	private final ModelPart plate;
	private final ModelPart floating;
	private final ModelPart _1;
	private final ModelPart _2;
	private final ModelPart _3;
	private final ModelPart _4;
	private final ModelPart _5;
	private final ModelPart _6;
	private final ModelPart gem;

	public TradingBaseModel(ModelPart root) {
		this.main = root.getChild("main");
		this.frame = this.main.getChild("frame");
		this.plate = this.main.getChild("plate");
		this.floating = this.main.getChild("floating");
		this._1 = this.floating.getChild("1");
		this._2 = this.floating.getChild("2");
		this._3 = this.floating.getChild("3");
		this._4 = this.floating.getChild("4");
		this._5 = this.floating.getChild("5");
		this._6 = this.floating.getChild("6");
		this.gem = this.main.getChild("gem");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 20.875F, 0.0F));

		PartDefinition frame = main.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 19).addBox(6.0F, -0.5F, -6.0F, 2.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(32, 19).addBox(-8.0F, -0.5F, -8.0F, 2.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 34).addBox(-6.0F, -0.5F, -8.0F, 14.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(32, 34).addBox(-8.0F, -0.5F, 6.0F, 14.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.625F, 0.0F));

		PartDefinition plate = main.addOrReplaceChild("plate", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -1.5F, -8.0F, 16.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.625F, 0.0F));

		PartDefinition floating = main.addOrReplaceChild("floating", CubeListBuilder.create(), PartPose.offset(0.0F, -1.875F, 0.0F));

		PartDefinition _1 = floating.addOrReplaceChild("1", CubeListBuilder.create(), PartPose.offset(-4.5F, 0.0F, -2.75F));

		PartDefinition _1_r1 = _1.addOrReplaceChild("1_r1", CubeListBuilder.create().texOffs(8, 37).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition _2 = floating.addOrReplaceChild("2", CubeListBuilder.create(), PartPose.offset(4.5F, 0.0F, 2.75F));

		PartDefinition _3_r1 = _2.addOrReplaceChild("3_r1", CubeListBuilder.create().texOffs(8, 37).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition _3 = floating.addOrReplaceChild("3", CubeListBuilder.create(), PartPose.offset(-4.5F, 0.0F, 2.75F));

		PartDefinition _3_r2 = _3.addOrReplaceChild("3_r2", CubeListBuilder.create().texOffs(8, 37).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition _4 = floating.addOrReplaceChild("4", CubeListBuilder.create(), PartPose.offset(4.5F, 0.0F, -2.75F));

		PartDefinition _4_r1 = _4.addOrReplaceChild("4_r1", CubeListBuilder.create().texOffs(8, 37).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition _5 = floating.addOrReplaceChild("5", CubeListBuilder.create().texOffs(8, 37).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.5F));

		PartDefinition _6 = floating.addOrReplaceChild("6", CubeListBuilder.create().texOffs(8, 37).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -5.5F));

		PartDefinition gem = main.addOrReplaceChild("gem", CubeListBuilder.create().texOffs(24, 37).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.375F, 0.0F));

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
	
	public void render1(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		this._1.render(ms, buffer, light, overlay);
	}
	
	public void render2(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		this._2.render(ms, buffer, light, overlay);
	}
	
	public void render3(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		this._3.render(ms, buffer, light, overlay);
	}
	
	public void render4(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		this._4.render(ms, buffer, light, overlay);
	}
	
	public void render5(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		this._5.render(ms, buffer, light, overlay);
	}
	
	public void render6(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		this._6.render(ms, buffer, light, overlay);
	}
}