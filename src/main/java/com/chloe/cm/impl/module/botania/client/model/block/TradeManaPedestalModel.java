package com.chloe.cm.impl.module.botania.client.model.block;

import com.chloe.cm.CMConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class TradeManaPedestalModel {
	
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CMConstants.modLoc("trade_mana_pedestal"), "main");
	
	private final ModelPart main;
	private final ModelPart plates;
	private final ModelPart up;
	private final ModelPart down;
	private final ModelPart shards;
	private final ModelPart sw;
	private final ModelPart se;
	private final ModelPart ne;
	private final ModelPart nw;
	private final ModelPart pedestal;
	private final ModelPart core;
	
	public TradeManaPedestalModel(ModelPart root) {
		this.main = root.getChild("main");
		this.plates = this.main.getChild("plates");
		this.up = this.plates.getChild("up");
		this.down = this.plates.getChild("down");
		this.shards = this.main.getChild("shards");
		this.sw = this.shards.getChild("sw");
		this.se = this.shards.getChild("se");
		this.ne = this.shards.getChild("ne");
		this.nw = this.shards.getChild("nw");
		this.pedestal = this.main.getChild("pedestal");
		this.core = this.main.getChild("core");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		
		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 15.625F, -0.0156F));
		
		PartDefinition plates = main.addOrReplaceChild("plates", CubeListBuilder.create(), PartPose.offset(0.0F, 0.375F, -0.0469F));
		
		PartDefinition up = plates.addOrReplaceChild("up", CubeListBuilder.create().texOffs(16, 7).mirror().addBox(-2.0F, -1.5F, -4.375F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
			.texOffs(8, 15).mirror().addBox(-4.0F, -1.5F, -1.875F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
			.texOffs(16, 7).addBox(-2.0F, -1.5F, 3.125F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
			.texOffs(8, 15).addBox(3.0F, -1.5F, -1.875F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, -0.0625F));
		
		PartDefinition down = plates.addOrReplaceChild("down", CubeListBuilder.create().texOffs(16, 7).mirror().addBox(-2.0F, -1.5F, -5.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
			.texOffs(8, 15).mirror().addBox(-5.0F, -1.5F, -2.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
			.texOffs(16, 7).addBox(-2.0F, -1.5F, 4.0F, 4.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
			.texOffs(8, 15).addBox(4.0F, -1.5F, -2.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.5F, 0.0625F));
		
		PartDefinition shards = main.addOrReplaceChild("shards", CubeListBuilder.create(), PartPose.offset(0.0F, 0.375F, 0.0156F));
		
		PartDefinition sw = shards.addOrReplaceChild("sw", CubeListBuilder.create().texOffs(8, 22).addBox(-1.0F, -6.001F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
			.texOffs(0, 15).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(12, 22).addBox(0.0F, 5.001F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, 4.0F));
		
		PartDefinition se = shards.addOrReplaceChild("se", CubeListBuilder.create().texOffs(8, 22).addBox(0.0F, -6.001F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
			.texOffs(0, 15).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(12, 22).addBox(-1.0F, 5.001F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, 4.0F));
		
		PartDefinition ne = shards.addOrReplaceChild("ne", CubeListBuilder.create().texOffs(8, 22).addBox(0.0F, -6.001F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
			.texOffs(0, 15).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(12, 22).addBox(-1.0F, 5.001F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, -4.0F));
		
		PartDefinition nw = shards.addOrReplaceChild("nw", CubeListBuilder.create().texOffs(8, 22).addBox(-1.0F, -6.001F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
			.texOffs(0, 15).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(12, 22).addBox(0.0F, 5.001F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, -4.0F));
		
		PartDefinition pedestal = main.addOrReplaceChild("pedestal", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -0.5F, -3.0F, 6.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.875F, 0.0156F));
		
		PartDefinition core = main.addOrReplaceChild("core", CubeListBuilder.create(), PartPose.offset(0.0F, -8.625F, 0.0156F));
		
		PartDefinition c_r1 = core.addOrReplaceChild("c_r1", CubeListBuilder.create().texOffs(0, 7).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6109F, 0.5236F, 0.9599F));
		
		return LayerDefinition.create(meshdefinition, 32, 32);
	}
	
	public void renderCrystal(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		this.shards.render(ms, buffer, light, overlay);
	}
	
	public void renderUpPlates(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		this.up.render(ms, buffer, light, overlay);
	}
	
	public void renderDownPlates(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		this.down.render(ms, buffer, light, overlay);
	}
	
	public void renderCore(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		this.core.render(ms, buffer, light, overlay);
	}
	
	public void renderPedestal(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		this.pedestal.render(ms, buffer, light, overlay);
	}
}