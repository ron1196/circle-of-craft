package lionking.client;
import lionking.common.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.creativetab.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.*;
import net.minecraft.network.packet.*;
import net.minecraft.pathfinding.*;
import net.minecraft.potion.*;
import net.minecraft.server.*;
import net.minecraft.server.management.*;
import net.minecraft.src.*;
import net.minecraft.stats.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.layer.*;
import net.minecraft.world.storage.*;
import net.minecraft.client.*;
import net.minecraft.client.audio.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.achievement.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.model.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.tileentity.*;


import net.minecraft.src.*;

public class LKRenderFlamingo extends RenderLiving
{
	private ResourceLocation texture = new ResourceLocation("lionking:mob/flamingo.png");
	private ResourceLocation textureChick = new ResourceLocation("lionking:mob/flamingo_chick.png");
	
    public LKRenderFlamingo(ModelBase modelbase)
    {
        super(modelbase, 0.5F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return ((LKEntityFlamingo)entity).isChild() ? textureChick : texture;
    }

	@Override
    protected float handleRotationFloat(EntityLivingBase entityliving, float f)
    {
		LKEntityFlamingo flamingo = (LKEntityFlamingo)entityliving;
        float f1 = flamingo.field_756_e + (flamingo.field_752_b - flamingo.field_756_e) * f;
        float f2 = flamingo.field_757_d + (flamingo.destPos - flamingo.field_757_d) * f;
        return (MathHelper.sin(f1) + 1.0F) * f2;
    }
}
